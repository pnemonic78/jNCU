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
import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
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
public class MNPPipe extends CDPipe<MNPPacket> implements MNPPacketListener {

	/** State for MNP. */
	protected enum MNPState {
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

	/** Port can timeout after 1 minute. */
	private static final int PORT_TIMEOUT = 60;

	protected final CommPortIdentifier portId;
	protected final int baud;
	protected final MNPSerialPort port;
	private MNPSerialPacketLayer packetLayer;
	/** MNP handshaking state. */
	protected MNPState stateMNP = MNPState.MNP_HANDSHAKE_LR_LISTEN;

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
		try {
			this.port = new MNPSerialPort(portId, baud, PORT_TIMEOUT);
		} catch (PortInUseException piue) {
			throw new PlatformException(piue);
		} catch (TooManyListenersException tmle) {
			throw new PlatformException(tmle);
		} catch (UnsupportedCommOperationException ucoe) {
			throw new ServiceNotSupportedException(ucoe);
		} catch (IOException ioe) {
			throw new ServiceNotSupportedException(ioe);
		}
	}

	@Override
	public void run() {
		do {
			try {
				getPacketLayer().listen();
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
			sendAndAcknowledge(packet);
		}
	}

	@Override
	public void setTimeout(int timeoutInSecs) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		if (portId != null) {
			throw new BadPipeStateException("Only able set the port timeout at port creation.");
		}
		super.setTimeout(timeoutInSecs);
	}

	@Override
	protected void disconnectImpl() throws PlatformException, TimeoutException {
		MNPLinkDisconnectPacket packet = (MNPLinkDisconnectPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LD);
		sendAndAcknowledge(packet);
		getPacketLayer().removePacketListener(this);
		super.disconnectImpl();
	}

	/**
	 * Disconnect request was sent, so now we can actually disconnect.
	 */
	protected void disconnectSent() {
		getPacketLayer().close();
		try {
			docking.setState(DockingState.DISCONNECTED);
			setState(MNPState.MNP_DISCONNECTED);
		} catch (PipeDisconnectedException pde) {
			// ignore - we are already disconnected
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.cdil.mnp.MNPPacketListener#packetReceived(net.sf.jncu.cdil
	 * .mnp.MNPPacket)
	 */
	@Override
	public void packetReceived(MNPPacket packet) {
		byte packetType = packet.getType();
		MNPState oldState = stateMNP;
		MNPState newState = oldState;

		try {
			if (packetType == MNPPacket.LR) {
				newState = MNPState.MNP_HANDSHAKE_LR_RECEIVED;
			} else if (packetType == MNPPacket.LD) {
				newState = MNPState.MNP_DISCONNECTING;
			}
			setState(oldState, newState, packet);
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		} catch (CDILNotInitializedException cnie) {
			cnie.printStackTrace();
		} catch (PipeDisconnectedException pde) {
			disconnectQuiet();
		} catch (PlatformException pe) {
			pe.printStackTrace();
		} catch (TimeoutException te) {
			// Probably connection was disconnected by Newton.
			disconnectQuiet();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.cdil.mnp.MNPPacketListener#packetSent(net.sf.jncu.cdil.mnp
	 * .MNPPacket)
	 */
	@Override
	public void packetSent(MNPPacket packet) {
		byte packetType = packet.getType();

		try {
			if (packetType == MNPPacket.LD) {
				disconnectSent();
			} else if (packetType == MNPPacket.LA) {
				setState(stateMNP, stateMNP, packet);
			}
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		} catch (CDILNotInitializedException cnie) {
			cnie.printStackTrace();
		} catch (PipeDisconnectedException pde) {
			pde.printStackTrace();
		} catch (PlatformException pe) {
			pe.printStackTrace();
		} catch (TimeoutException te) {
			te.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.mnp.MNPPacketListener#packetEOF()
	 */
	@Override
	public void packetEOF() {
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
		getPacketLayer().sendAndAcknowledge(packet);
	}

	/**
	 * Command has been received, and now process it.
	 * 
	 * @param command
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
	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		super.commandReceived(command);

		switch (stateMNP) {
		case MNP_HANDSHAKE_DOCK:
			docking.commandReceived(command);
			break;
		case MNP_ACCEPTED:
		case MNP_IDLE:
			processCommand(command);
			break;
		case MNP_DISCONNECTED:
			// TODO throw new PipeDisconnectedException();
		default:
			throw new BadPipeStateException("bad state " + stateMNP);
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		super.commandSent(command);

		try {
			if (stateMNP == MNPState.MNP_HANDSHAKE_DOCK) {
				docking.commandSent(command);
			}
		} catch (BadPipeStateException bpe) {
			bpe.printStackTrace();
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
	protected void setState(MNPState oldState, MNPState state, MNPPacket packet) throws PipeDisconnectedException, TimeoutException, BadPipeStateException,
			CDILNotInitializedException, PlatformException {
		byte packetType = (packet == null) ? 0 : packet.getType();

		// Only move the previous state to the next state, or to its own state.
		int compare = state.compareTo(oldState);
		if ((compare != 0) && (compare != 1) && (state != MNPState.MNP_DISCONNECTING)) {
			throw new BadPipeStateException("bad state from " + oldState + " to " + state);
		}

		setState(state);

		switch (state) {
		case MNP_HANDSHAKE_LR_LISTEN:
			break;
		case MNP_HANDSHAKE_LR_RECEIVED:
			// Can start connecting again as long we are not busy handshaking.
			if ((packetType == MNPPacket.LR) && (state.compareTo(MNPState.MNP_HANDSHAKE_LR_SENT) < 0)) {
				docking.setState(DockingState.HANDSHAKE_LR_RECEIVED, null);

				MNPLinkRequestPacket lr = (MNPLinkRequestPacket) packet;
				MNPPacketFactory.getInstance().resetSequence();
				MNPLinkRequestPacket reply = (MNPLinkRequestPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LR);
				reply.setDataPhaseOpt(lr.getDataPhaseOpt());
				reply.setFramingMode(lr.getFramingMode());
				reply.setMaxInfoLength(lr.getMaxInfoLength());
				reply.setMaxOutstanding(lr.getMaxOutstanding());
				reply.setTransmitted(lr.getTransmitted());
				docking.setState(DockingState.HANDSHAKE_LR_SENDING, null);
				setState(state, MNPState.MNP_HANDSHAKE_LR_SENDING, reply);
				sendAndAcknowledge(reply);
			}
			break;
		case MNP_HANDSHAKE_LR_SENDING:
			if (packetType == MNPPacket.LA) {
				docking.setState(DockingState.HANDSHAKE_LR_SENT, null);
				setState(state, MNPState.MNP_HANDSHAKE_LR_SENT, packet);
			}
			break;
		case MNP_HANDSHAKE_LR_SENT:
			docking.setState(DockingState.HANDSHAKE_RTDK_LISTEN, null);
			setState(state, MNPState.MNP_HANDSHAKE_DOCK, packet);
			break;
		case MNP_HANDSHAKE_DOCK:
		case MNP_ACCEPTED:
		case MNP_IDLE:
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
	protected MNPState getMNPState() {
		return stateMNP;
	}

	/**
	 * Create a packet layer.
	 * 
	 * @param port
	 *            the serial port.
	 * @return the packet layer.
	 */
	protected MNPSerialPacketLayer createPacketLayer(MNPSerialPort port) {
		return new MNPSerialPacketLayer(port, this);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.CDPipe#createCommandLayer()
	 */
	@Override
	protected CDCommandLayer<MNPPacket> createCommandLayer() {
		return new MNPSerialCommandLayer(getPacketLayer());
	}

	/**
	 * Process the command.
	 * 
	 * @param command
	 *            the received command.
	 */
	protected void processCommand(IDockCommandFromNewton command) {
		String cmdName = command.getCommand();
		if (DDisconnect.COMMAND.equals(cmdName)) {
			disconnectQuiet();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.CDPipe#canSend()
	 */
	@Override
	public boolean canSend() {
		return (getMNPState() != MNPState.MNP_DISCONNECTED) && super.canSend();
	}

	/**
	 * Get the packet layer. Creates layer if {@code null}.
	 * 
	 * @return the packet layer.
	 */
	protected MNPSerialPacketLayer getPacketLayer() {
		if (packetLayer == null) {
			packetLayer = createPacketLayer(port);
			packetLayer.addPacketListener(this);
		}
		return packetLayer;
	}
}
