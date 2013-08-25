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

import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPacketLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.io.NoSuchPortException;
import net.sf.jncu.io.PortInUseException;
import net.sf.jncu.protocol.IDockCommandFromNewton;
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
public class MNPPipe extends CDPipe<MNPPacket> {

	/** State for MNP pipe. */
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

	protected final String portName;
	protected final int baud;
	protected final MNPSerialPort port;
	/** MNP handshaking state. */
	protected MNPState stateMNP = MNPState.MNP_HANDSHAKE_LR_LISTEN;

	/**
	 * Creates a new MNP pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @param portName
	 *            the port name.
	 * @param baud
	 *            the baud rate to communicate at in bytes per second.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	public MNPPipe(CDLayer layer, String portName, int baud) throws PlatformException, ServiceNotSupportedException {
		super(layer);
		setName("MNPPipe-" + getId());
		this.portName = portName;
		this.baud = baud;
		try {
			this.port = (portName == null) ? null : new MNPSerialPort(portName, baud);
		} catch (NoSuchPortException nspe) {
			throw new PlatformException(nspe);
		} catch (PortInUseException piue) {
			throw new PlatformException(piue);
		} catch (TooManyListenersException tmle) {
			throw new PlatformException(tmle);
		} catch (IOException ioe) {
			throw new ServiceNotSupportedException(ioe);
		}
	}

	@Override
	public void write(byte[] b, int offset, int count) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
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
	public void setTimeout(int timeoutInSecs) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		if (getMNPState() != MNPState.MNP_HANDSHAKE_LR_LISTEN) {
			throw new BadPipeStateException("Only able set the port timeout at port creation.");
		}
		super.setTimeout(timeoutInSecs);
	}

	@Override
	protected void disconnectImpl() {
		MNPLinkDisconnectPacket packet = (MNPLinkDisconnectPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LD);
		try {
			sendAndAcknowledge(packet);
		} catch (TimeoutException te) {
			te.printStackTrace();
		}
		super.disconnectImpl();
	}

	@Override
	public void packetReceived(MNPPacket packet) {
		super.packetReceived(packet);

		try {
			switch (packet.getType()) {
			case MNPPacket.LA:
				packetReceivedLA((MNPLinkAcknowledgementPacket) packet);
				break;
			case MNPPacket.LD:
				packetReceivedLD((MNPLinkDisconnectPacket) packet);
				break;
			case MNPPacket.LR:
				packetReceivedLR((MNPLinkRequestPacket) packet);
				break;
			case MNPPacket.LT:
				packetReceivedLT((MNPLinkTransferPacket) packet);
				break;
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

	@Override
	public void packetSent(MNPPacket packet) {
		super.packetSent(packet);

		try {
			switch (packet.getType()) {
			case MNPPacket.LD:
				docking.setState(DockingState.DISCONNECTED);
				setState(MNPState.MNP_DISCONNECTED);
				break;
			case MNPPacket.LR:
				setState(MNPState.MNP_HANDSHAKE_LR_SENT);
				break;
			}
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		} catch (PipeDisconnectedException pde) {
			pde.printStackTrace();
		}
	}

	@Override
	public void packetAcknowledged(MNPPacket packet) {
		super.packetAcknowledged(packet);

		final byte packetType = packet.getType();
		final MNPState state = getMNPState();

		try {
			switch (state) {
			case MNP_HANDSHAKE_LR_SENT:
				if (packetType == MNPPacket.LR) {
					docking.setState(DockingState.HANDSHAKE_RTDK);
					setState(MNPState.MNP_HANDSHAKE_DOCK);
				}
				break;
			}
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		} catch (PipeDisconnectedException pde) {
			pde.printStackTrace();
		}
	}

	@Override
	protected void acceptImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
		super.acceptImpl();
		if ((stateMNP == MNPState.MNP_HANDSHAKE_DOCK) && (getDockingState() == DockingState.HANDSHAKE_DONE)) {
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
		if (allowSend())
			getSerialPacketLayer().sendQueued(packet);
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
			break;
		case MNP_ACCEPTED:
		case MNP_IDLE:
			processCommand(command);
			break;
		case MNP_DISCONNECTED:
			// TODO throw new PipeDisconnectedException();
		default:
			throw new BadPipeStateException("state " + stateMNP);
		}
	}

	/**
	 * Check that the state transition is valid.
	 * 
	 * @param oldState
	 *            the old state.
	 * @param newState
	 *            the new state.
	 * @throws BadPipeStateException
	 *             if the transition is invalid.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 */
	protected void validateState(MNPState oldState, MNPState newState) throws BadPipeStateException, PipeDisconnectedException {
		// Only move the previous state to the next state, or to its own state.
		if (oldState == MNPState.MNP_DISCONNECTED) {
			throw new PipeDisconnectedException();
		}
		if (newState == MNPState.MNP_HANDSHAKE_LR_RECEIVED) {
			return;
		}
		int compare = newState.compareTo(oldState);
		if ((compare != 0) && (compare != 1) && (newState != MNPState.MNP_DISCONNECTING)) {
			throw new BadPipeStateException("bad state from " + oldState + " to " + newState);
		}
	}

	/**
	 * Set the MNP state.
	 * 
	 * @param state
	 *            the state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 */
	protected void setState(MNPState state) throws PipeDisconnectedException {
		validateState(this.stateMNP, state);
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

	@Override
	protected CDPacketLayer<MNPPacket> createPacketLayer() {
		return new MNPSerialPacketLayer(this, port);
	}

	@Override
	protected CDCommandLayer<MNPPacket> createCommandLayer(CDPacketLayer<MNPPacket> packetLayer) {
		return new MNPSerialCommandLayer((MNPSerialPacketLayer) packetLayer);
	}

	@Override
	protected void processCommand(IDockCommandFromNewton command) {
		super.processCommand(command);

		String cmdName = command.getCommand();

		if (DDisconnect.COMMAND.equals(cmdName)) {
			disconnectQuiet();
		}
	}

	@Override
	public boolean allowSend() {
		return super.allowSend() && (getMNPState() != MNPState.MNP_DISCONNECTED);
	}

	/**
	 * Get the packet layer. Creates layer if {@code null}.
	 * 
	 * @return the packet layer.
	 */
	protected MNPSerialPacketLayer getSerialPacketLayer() {
		return (MNPSerialPacketLayer) getPacketLayer();
	}

	/**
	 * Received a LA packet.
	 */
	protected void packetReceivedLA(MNPLinkAcknowledgementPacket packet) {
	}

	/**
	 * Received a LD packet.
	 */
	protected void packetReceivedLD(MNPLinkDisconnectPacket packet) {
		try {
			setState(MNPState.MNP_DISCONNECTING);
			disconnectQuiet();
		} catch (PipeDisconnectedException pde) {
			// Already disconnected.
		}
	}

	/**
	 * Received a LR packet.
	 * 
	 * @throws PipeDisconnectedException
	 * @throws TimeoutException
	 * @throws PlatformException
	 * @throws CDILNotInitializedException
	 * @throws BadPipeStateException
	 */
	protected void packetReceivedLR(MNPLinkRequestPacket packet) throws PipeDisconnectedException, BadPipeStateException, CDILNotInitializedException, PlatformException,
			TimeoutException {
		final MNPState oldState = getMNPState();
		MNPState state = MNPState.MNP_HANDSHAKE_LR_RECEIVED;

		validateState(oldState, state);
		setState(state);

		// Can start connecting again as long we are not busy handshaking.
		if (state.compareTo(MNPState.MNP_HANDSHAKE_LR_SENT) < 0) {
			docking.setState(DockingState.HANDSHAKE_LR);

			MNPPacketFactory.getInstance().resetSequence();
			MNPLinkRequestPacket reply = (MNPLinkRequestPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LR);
			reply.setDataPhaseOpt(packet.getDataPhaseOpt());
			reply.setFramingMode(packet.getFramingMode());
			reply.setTransmitted(packet.getTransmitted());

			setState(MNPState.MNP_HANDSHAKE_LR_SENDING);
			sendAndAcknowledge(reply);
		}
	}

	/**
	 * Received a LT packet.
	 */
	protected void packetReceivedLT(MNPLinkTransferPacket packet) {
	}
}
