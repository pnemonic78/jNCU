/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.cdil.mnp;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.EOFException;
import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v1_0.session.DHello;
import net.sf.jncu.protocol.v2_0.session.DockingState;

/**
 * MNP Serial pipe.
 * <p>
 * Handshake protocol:
 * <ol>
 * <li>Listen for LR from Newton
 * <li>Newton sends LR
 * <li>Send LR to Newton
 * <li>Newton sends LA (for LR in previous step)
 * <li>Newton sends LT
 * <li>Send LA to Newton (for LT in previous step)
 * <li>Send LT to Newton
 * <li>Newton sends LA (for LT in previous step)
 * </ol>
 * 
 * @author moshew
 */
public class MNPPipe extends CDPipe implements MNPPacketListener {

	/** State for MNP. */
	enum MNPState {
		/** Listen for LR from Newton. */
		MNP_HANDSHAKE_LR_LISTEN,
		/** Newton sends LR. */
		MNP_HANDSHAKE_LR_RECEIVED,
		/** Send LR to Newton. */
		MNP_HANDSHAKE_LR_SENDING,
		/** Newton sends LA (for LR in previous step). */
		MNP_HANDSHAKE_LR_SENT,
		/** Let the docking protocol continue handshaking. */
		MNP_HANDSHAKE_DOCK,
		/** Connect request accepted. */
		MNP_ACCEPTED,
		/** Idle. */
		MNP_IDLE,
		/** Disconnecting. */
		MNP_DISCONNECTING,
		/** Disconnected. */
		MNP_DISCONNECTED
	}

	protected final byte CREDIT = 7;

	protected final CommPortIdentifier portId;
	protected final int baud;
	protected final MNPSerialPort port;
	protected final MNPPacketLayer packetLayer = new MNPPacketLayer();
	/** Outgoing sequence. */
	private byte sequence;
	/** MNP handshaking state. */
	protected MNPState stateMNP = MNPState.MNP_HANDSHAKE_LR_LISTEN;
	protected MNPPacketSender sender;

	/**
	 * Creates a new MNP pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @param portId
	 *            the port identifier.
	 * @param baud
	 *            the baud rate to communicate at in bytes per second.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	public MNPPipe(CDLayer layer, CommPortIdentifier portId, int baud) throws PlatformException, ServiceNotSupportedException {
		super(layer);
		this.portId = portId;
		this.baud = baud;
		this.packetLayer.addPacketListener(this);
		MNPSerialPort port = null;
		try {
			port = new MNPSerialPort(portId, baud, getTimeout());
		} catch (PortInUseException piue) {
			throw new PlatformException(piue);
		} catch (TooManyListenersException tmle) {
			throw new PlatformException(tmle);
		} catch (UnsupportedCommOperationException ucoe) {
			throw new ServiceNotSupportedException(ucoe);
		} catch (IOException ioe) {
			throw new ServiceNotSupportedException(ioe);
		}
		this.port = port;
		try {
			this.sender = new MNPPacketSender(this, packetLayer, port.getOutputStream());
		} catch (IOException ioe) {
			throw new PlatformException(ioe);
		}
		this.sender.start();
	}

	@Override
	public void run() {
		do {
			try {
				packetLayer.listen(port.getInputStream());
			} catch (EOFException eofe) {
				disconnectQuiet();
			} catch (IOException ioe) {
				if (docking.getState() != DockingState.DISCONNECTED) {
					ioe.printStackTrace();
				}
			}
		} while (docking.getState() != DockingState.DISCONNECTED);
	}

	@Override
	public void write(byte[] b, int offset, int count) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		if ((stateMNP == MNPState.MNP_ACCEPTED) || (stateMNP == MNPState.MNP_IDLE)) {
			super.write(b, offset, count);
		}
		if ((stateMNP != MNPState.MNP_DISCONNECTING) && (stateMNP != MNPState.MNP_DISCONNECTED)) {
			MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
			byte[] data = new byte[count];
			System.arraycopy(b, offset, data, 0, count);
			packet.setData(data);
			packet.setSequence(++sequence);
			sendAndAcknowledge(packet);
		}
	}

	@Override
	public void setTimeout(int timeoutInSecs) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		if (port != null) {
			throw new BadPipeStateException("only able set the port timeout at port creation.");
		}
		super.setTimeout(timeoutInSecs);
	}

	@Override
	protected void disconnectImpl() throws PlatformException, TimeoutException {
		MNPLinkDisconnectPacket packet = (MNPLinkDisconnectPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LD);
		sendAndAcknowledge(packet);
		sender.cancel();

		super.disconnectImpl();
		packetLayer.removePacketListener(this);
		port.close();
		try {
			docking.setState(DockingState.DISCONNECTED);
			setState(MNPState.MNP_DISCONNECTED);
		} catch (PipeDisconnectedException pde) {
			// ignore - we are already disconnected
		}
	}

	public void packetReceived(MNPPacket packet) {
		byte packetType = packet.getType();
		MNPState oldState = stateMNP;
		MNPState newState = oldState;

		try {
			if (packetType == MNPPacket.LR) {
				newState = MNPState.MNP_HANDSHAKE_LR_LISTEN;
			} else if (packetType == MNPPacket.LT) {
				MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;
				MNPLinkAcknowledgementPacket ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
				ack.setSequence(lt.getSequence());
				ack.setCredit(CREDIT);
				sendAndAcknowledge(ack);
			} else if (packetType == MNPPacket.LD) {
				newState = MNPState.MNP_DISCONNECTING;
			}
			setState(oldState, newState, packet, null);
		} catch (PipeDisconnectedException pde) {
			disconnectQuiet();
		} catch (TimeoutException te) {
			// Probably connection was disconnected.
			disconnectQuiet();
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		} catch (CDILNotInitializedException cnie) {
			cnie.printStackTrace();
		} catch (PlatformException pe) {
			pe.printStackTrace();
		}
	}

	@Override
	protected void acceptImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
		super.acceptImpl();
		if ((stateMNP == MNPState.MNP_HANDSHAKE_DOCK) && (docking.getState() == DockingState.HANDSHAKE_DONE)) {
			layer.setState(this, CDState.CONNECTED);
			setState(MNPState.MNP_ACCEPTED);
		}
	}

	@Override
	protected void idleImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
		super.idleImpl();
		// Nothing to do because we are already listening for packets.
	}

	/**
	 * Send a packet and wait for acknowledgement.<br>
	 * Do not wait for acknowledgement if the packet is itself and
	 * acknowledgement.
	 * 
	 * @param packet
	 *            the packet to send.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	protected void sendAndAcknowledge(MNPPacket packet) throws TimeoutException {
		sender.send(packet);
	}

	/**
	 * Command has been received, and now process it.
	 * 
	 * @param cmd
	 *            the command.
	 * @param state
	 *            the pipe state.
	 * @throws PipeDisconnectedException
	 *             if pipe disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 * @throws PlatformException
	 * @throws CDILNotInitializedException
	 * @throws BadPipeStateException
	 */
	protected void commandReceived(IDockCommandFromNewton cmd, MNPState state) throws PipeDisconnectedException, TimeoutException, BadPipeStateException,
			CDILNotInitializedException, PlatformException {
		if (cmd == null) {
			return;
		}
		super.commandReceived(cmd);

		switch (state) {
		case MNP_ACCEPTED:
		case MNP_IDLE:
			// Process the command.
			String cmdName = cmd.getCommand();
			if (DHello.COMMAND.equals(cmdName)) {
				queueCommandFromNewton.remove(cmd);
			} else if (DDisconnect.COMMAND.equals(cmdName)) {
				queueCommandFromNewton.remove(cmd);
				disconnect();
			}
			break;
		case MNP_DISCONNECTED:
			throw new PipeDisconnectedException();
		default:
			throw new BadPipeStateException("bad state " + state);
		}
	}

	/**
	 * Flow to the next state.
	 * 
	 * @param oldState
	 *            the old state.
	 * @param state
	 *            the new state.
	 * @param packet
	 *            the packet.
	 * @param cmd
	 *            the command.
	 * @throws PipeDisconnectedException
	 *             if pipe is disconnected.
	 * @throws TimeoutException
	 *             if a timeout occurs.
	 * @throws PlatformException
	 * @throws CDILNotInitializedException
	 * @throws BadPipeStateException
	 */
	protected void setState(MNPState oldState, MNPState state, MNPPacket packet, IDockCommandFromNewton cmd) throws PipeDisconnectedException,
			TimeoutException, BadPipeStateException, CDILNotInitializedException, PlatformException {
		byte packetType = (packet == null) ? 0 : packet.getType();
		byte[] data = null;

		// Only move the previous state to the next state, or to its own state.
		int compare = state.compareTo(oldState);
		if ((compare != 0) && (compare != 1) && (state != MNPState.MNP_DISCONNECTING)) {
			throw new BadPipeStateException("bad state from " + oldState + " to " + state);
		}

		setState(state);

		if (packetType == MNPPacket.LT) {
			MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;
			data = lt.getData();
			if (DockCommandFromNewton.isCommand(data)) {
				cmd = DockCommandFromNewton.deserialize(data);
			}
		}

		switch (state) {
		case MNP_HANDSHAKE_LR_LISTEN:
			if (packetType == MNPPacket.LR) {
				// Can start connecting again as long we are not busy
				// handshaking.
				if (state.compareTo(MNPState.MNP_HANDSHAKE_LR_SENT) < 0) {
					docking.setState(docking.getState(), DockingState.HANDSHAKE_LR_RECEIVED, data, cmd);
					setState(state, MNPState.MNP_HANDSHAKE_LR_RECEIVED, packet, null);
				}
			}
			break;
		case MNP_HANDSHAKE_LR_RECEIVED:
			if (packetType == MNPPacket.LR) {
				MNPLinkRequestPacket lr = (MNPLinkRequestPacket) packet;
				this.sequence = 0;
				MNPLinkRequestPacket reply = (MNPLinkRequestPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LR);
				reply.setDataPhaseOpt(lr.getDataPhaseOpt());
				reply.setFramingMode(lr.getFramingMode());
				reply.setMaxInfoLength(lr.getMaxInfoLength());
				reply.setMaxOutstanding(lr.getMaxOutstanding());
				reply.setTransmitted(lr.getTransmitted());
				docking.setState(docking.getState(), DockingState.HANDSHAKE_LR_SENDING, data, cmd);
				setState(state, MNPState.MNP_HANDSHAKE_LR_SENDING, reply, null);
				sendAndAcknowledge(reply);
			}
			break;
		case MNP_HANDSHAKE_LR_SENDING:
			if (packetType == MNPPacket.LA) {
				docking.setState(docking.getState(), DockingState.HANDSHAKE_LR_SENT, data, cmd);
				setState(state, MNPState.MNP_HANDSHAKE_LR_SENT, packet, null);
			}
			break;
		case MNP_HANDSHAKE_LR_SENT:
			setState(state, MNPState.MNP_HANDSHAKE_DOCK, packet, null);
			break;
		case MNP_HANDSHAKE_DOCK:
			DockingState stateDocking = docking.getState();

			switch (stateDocking) {
			case HANDSHAKE_DOCK_SENDING:
				if (packetType == MNPPacket.LA) {
					docking.setState(stateDocking, DockingState.HANDSHAKE_DOCK_SENT, data, cmd);
				}
				break;
			case HANDSHAKE_DINFO_SENDING:
				if (packetType == MNPPacket.LA) {
					docking.setState(stateDocking, DockingState.HANDSHAKE_DINFO_SENT, data, cmd);
				}
				break;
			case HANDSHAKE_ICONS_SENDING:
				if (packetType == MNPPacket.LA) {
					docking.setState(stateDocking, DockingState.HANDSHAKE_ICONS_SENT, data, cmd);
				}
				break;
			case HANDSHAKE_TIMEOUT_SENDING:
				if (packetType == MNPPacket.LA) {
					docking.setState(stateDocking, DockingState.HANDSHAKE_TIMEOUT_SENT, data, cmd);
				}
				break;
			case HANDSHAKE_PASS_SENDING:
				if (packetType == MNPPacket.LA) {
					docking.setState(stateDocking, DockingState.HANDSHAKE_PASS_SENT, data, cmd);
					notifyConnected();
				}
				break;
			}

			if (packetType != MNPPacket.LA) {
				docking.commandReceived(cmd);
			}
			break;
		case MNP_ACCEPTED:
		case MNP_IDLE:
			if (packetType == MNPPacket.LT) {
				if (!DockCommandFromNewton.isCommand(data)) {
					throw new BadPipeStateException("expected command");
				}
				if (cmd == null) {
					System.err.println(data);
				}
				commandReceived(cmd, state);
			}
			break;
		case MNP_DISCONNECTING:
			disconnectQuiet();
			break;
		case MNP_DISCONNECTED:
			break;
		default:
			throw new BadPipeStateException("bad state from " + oldState + " to " + state);
		}
	}

	private void setState(MNPState state) throws PipeDisconnectedException {
		if (this.stateMNP == MNPState.MNP_DISCONNECTED) {
			throw new PipeDisconnectedException();
		}
		this.stateMNP = state;
	}

	/**
	 * Get the MNP state.
	 * 
	 * @return the state.
	 */
	MNPState getMNPState() {
		return stateMNP;
	}
}
