package net.sf.jncu.cdil.mnp;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

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
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v1_0.DCmdResult;
import net.sf.jncu.protocol.v2_0.DockCommandFactory;
import net.sf.jncu.protocol.v2_0.session.DCmdDesktopInfo;
import net.sf.jncu.protocol.v2_0.session.DCmdInitiateDocking;
import net.sf.jncu.protocol.v2_0.session.DCmdNewtonInfo;
import net.sf.jncu.protocol.v2_0.session.DCmdNewtonName;
import net.sf.jncu.protocol.v2_0.session.DCmdPassword;
import net.sf.jncu.protocol.v2_0.session.DCmdRequestToDock;
import net.sf.jncu.protocol.v2_0.session.DCmdSetTimeout;
import net.sf.jncu.protocol.v2_0.session.DCmdWhichIcons;
import net.sf.jncu.protocol.v2_0.session.NewtonInfo;

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

	/** State for handshaking protocol. */
	private static enum State {
		/** Listen for LR from Newton. */
		HANDSHAKE_LR_LISTEN,
		/** Newton sends LR. */
		HANDSHAKE_LR_RECEIVED,
		/** Send LR to Newton. */
		HANDSHAKE_LR_SENDING,
		/** Newton sends LA (for LR in previous step). */
		HANDSHAKE_LR_SENT,
		/** Listen for LT (kDRequestToDock) from Newton. */
		HANDSHAKE_RTDK_LISTEN,
		/**
		 * Newton sends LT (kDRequestToDock).<br>
		 * Send LA to Newton.
		 */
		HANDSHAKE_RTDK_RECEIVED,
		/** Send LT (kDInitiateDocking) to Newton. */
		HANDSHAKE_DOCK_SENDING,
		/** Newton sends LA (for LT in previous step). */
		HANDSHAKE_DOCK_SENT,
		/** List for LT (DNewtonName) from Newton. */
		HANDSHAKE_NAME_LISTEN,
		/**
		 * Newton sends LT (kDNewtonName).<br>
		 * Send LA to Newton.
		 */
		HANDSHAKE_NAME_RECEIVED,
		/** Send LT (kDDesktopInfo) to Newton. */
		HANDSHAKE_DINFO_SENDING,
		/** Newton sends LA (for LT in previous step). */
		HANDSHAKE_DINFO_SENT,
		/** List for LT (kDNewtonInfo) from Newton. */
		HANDSHAKE_NINFO_LISTEN,
		/**
		 * Newton sends LT (kDNewtonInfo).<br>
		 * Send LA to Newton.
		 */
		HANDSHAKE_NINFO_RECEIVED,
		/** Send LT (kDWhichIcons) to Newton (optional). */
		HANDSHAKE_ICONS_SENDING,
		/** Newton sends LA (for LT in previous step). */
		HANDSHAKE_ICONS_SENT,
		/** List for LT (kDResult) from Newton (for kDWhichIcons). */
		HANDSHAKE_ICONS_RESULT_LISTEN,
		/**
		 * Newton sends LT (kDResult) (for kDWhichIcons in previous step).<br>
		 * Send LA to Newton.
		 */
		HANDSHAKE_ICONS_RESULT_RECEIVED,
		/** Send LT (kDSetTimeout) to Newton. */
		HANDSHAKE_TIMEOUT_SENDING,
		/** Newton sends LA (for LT in previous step). */
		HANDSHAKE_TIMEOUT_SENT,
		/** List for LT (kDPassword) from Newton. */
		HANDSHAKE_PASS_LISTEN,
		/**
		 * Newton sends LT (kDPassword).<br>
		 * Send LA to Newton.
		 */
		HANDSHAKE_PASS_RECEIVED,
		/** Send LT (kDPassword) to Newton. */
		HANDSHAKE_PASS_SENDING,
		/** Newton sends LA (for LT in previous step). */
		HANDSHAKE_PASS_SENT,
		/** Finished handshaking. */
		HANDSHAKE_DONE,
		/** Connect request accepted. */
		ACCEPTED,
		/** Idle. */
		IDLE,
		/** Disconnecting. */
		DISCONNECTING,
		/** Disconnected. */
		DISCONNECTED
	}

	protected final byte CREDIT = 7;

	protected final CommPortIdentifier portId;
	protected final int baud;
	protected final MNPSerialPort port;
	protected final MNPPacketLayer packetLayer = new MNPPacketLayer();
	/** Outgoing sequence. */
	protected byte sequence;
	/** Internal state. */
	private State state = State.HANDSHAKE_LR_LISTEN;

	private NewtonInfo info;
	private int protocolVersion = DCmdDesktopInfo.PROTOCOL_VERSION;
	/** The password sent by the Desktop. */
	private transient long challengeDesktop;
	/** The password sent by the Newton. */
	private transient long challengeNewton;
	/** The ciphered password sent by the Newton. */
	private transient long challengeNewtonCiphered;

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
	}

	@Override
	public void run() {
		do {
			try {
				packetLayer.listen(port.getInputStream());
			} catch (IOException ioe) {
				if (state != State.DISCONNECTED) {
					ioe.printStackTrace();
				}
			}
		} while (state != State.DISCONNECTED);
	}

	@Override
	public void write(byte[] b, int offset, int count) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		super.write(b, offset, count);
		MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
		byte[] data = new byte[count];
		System.arraycopy(b, offset, data, 0, count);
		packet.setData(data);
		packet.setSequence(++sequence);
		sendAndAcknowledge(packet);
	}

	@Override
	public void setTimeout(int timeoutInSecs) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		// Can only set the port timeout at port creation.
		if (port != null) {
			throw new BadPipeStateException();
		}
		super.setTimeout(timeoutInSecs);
	}

	@Override
	protected void disconnectImpl() throws PlatformException, TimeoutException {
		MNPLinkDisconnectPacket packet = (MNPLinkDisconnectPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LD);
		sendAndAcknowledge(packet);

		super.disconnectImpl();
		port.close();
		try {
			setState(State.DISCONNECTED);
		} catch (PipeDisconnectedException pde) {
			// ignore - we are already disconnected
		}
	}

	public void packetReceived(MNPPacket packet) {
		byte packetType = packet.getType();
		State newState = state;

		try {
			if (packetType == MNPPacket.LR) {
				newState = State.HANDSHAKE_LR_LISTEN;
			} else if (packetType == MNPPacket.LT) {
				MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;
				MNPLinkAcknowledgementPacket ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
				ack.setSequence(lt.getSequence());
				ack.setCredit(CREDIT);
				sendAndAcknowledge(ack);
			} else if (packetType == MNPPacket.LD) {
				newState = State.DISCONNECTING;
			}
			setState(state, newState, packet, null);
		} catch (PipeDisconnectedException pde) {
			disconnectQuiet();
		} catch (TimeoutException te) {
			// Probably connection was disconnected.
			disconnectQuiet();
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		}
	}

	@Override
	protected void acceptImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
		super.acceptImpl();
		if (state == State.HANDSHAKE_DONE) {
			setState(State.ACCEPTED);
			layer.setState(this, CDState.CONNECTED);
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
		byte acknowledge = sequence;
		long timeout = 2000L;
		long retry = timeout * 5;
		boolean resend = true;
		boolean needAck = true;
		CDState stateCD;
		MNPPacket response = null;
		MNPLinkAcknowledgementPacket ack;

		if ((packet.getType() == MNPPacket.LA) || (packet.getType() == MNPPacket.LD)) {
			needAck = false;
		} else if (packet instanceof MNPLinkTransferPacket) {
			MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;
			acknowledge = lt.getSequence();
		}

		do {
			try {
				packetLayer.send(port.getOutputStream(), packet);
				if (needAck) {
					response = packetLayer.receive(port.getInputStream());
					if ((response != null) && (response.getType() == MNPPacket.LA)) {
						ack = (MNPLinkAcknowledgementPacket) response;
						resend &= (ack.getSequence() < acknowledge);
					}
				} else {
					resend = false;
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			stateCD = getCDState();
			resend &= (stateCD == CDState.CONNECT_PENDING) || (stateCD == CDState.CONNECTED) || (stateCD == CDState.LISTENING);
			resend &= (state != State.DISCONNECTED);
			if (resend) {
				retry -= timeout;
				if (retry < 0) {
					throw new TimeoutException();
				}
			}
		} while (resend);

		if (response != null) {
			packetReceived(response);
		}
	}

	protected void setState(State state) throws PipeDisconnectedException {
		if (this.state == State.DISCONNECTED) {
			throw new PipeDisconnectedException();
		}
		this.state = state;
	}

	protected void disconnectQuiet() {
		try {
			disconnect();
		} catch (BadPipeStateException bpse) {
			// ignore
		} catch (CDILNotInitializedException cnie) {
			// ignore
		} catch (PlatformException pe) {
			// ignore
		} catch (PipeDisconnectedException pde) {
			// ignore
		} catch (TimeoutException te) {
			// ignore
		}
	}

	protected void sendCommand(DockCommandToNewton cmd) throws TimeoutException {
		MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
		packet.setSequence(++sequence);
		packet.setData(cmd.getPayload());
		sendAndAcknowledge(packet);
	}

	/**
	 * Command has been received, and now process it.
	 * 
	 * @param cmdData
	 *            the raw command data.
	 * @param state
	 *            the pipe state.
	 * @throws PipeDisconnectedException
	 *             if pipe disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 * @see #commandReceived(DockCommandFromNewton, State)
	 */
	protected void commandReceived(byte[] cmdData, State state) throws PipeDisconnectedException, TimeoutException {
		DockCommandFromNewton cmd = DockCommandFromNewton.deserialize(cmdData);
		commandReceived(cmd, state);
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
	 */
	protected void commandReceived(DockCommandFromNewton cmd, State state) throws PipeDisconnectedException, TimeoutException {
		if (cmd == null) {
			return;
		}

		switch (state) {
		case HANDSHAKE_RTDK_RECEIVED:
			DCmdInitiateDocking cmdInitiateDocking = (DCmdInitiateDocking) DockCommandFactory.getInstance().create(DCmdInitiateDocking.COMMAND);
			cmdInitiateDocking.setSession(DCmdInitiateDocking.SESSION_SETTING_UP);
			setState(state, State.HANDSHAKE_DOCK_SENDING, null, cmd);
			sendCommand(cmdInitiateDocking);
			break;
		case HANDSHAKE_NAME_RECEIVED:
			this.info = ((DCmdNewtonName) cmd).getInformation();
			DCmdDesktopInfo cmdDesktopInfo = (DCmdDesktopInfo) DockCommandFactory.getInstance().create(DCmdDesktopInfo.COMMAND);
			this.challengeDesktop = cmdDesktopInfo.getEncryptedKey();
			setState(state, State.HANDSHAKE_DINFO_SENDING, null, cmd);
			sendCommand(cmdDesktopInfo);
			break;
		case HANDSHAKE_NINFO_RECEIVED:
			DCmdNewtonInfo cmdNewtonInfo = (DCmdNewtonInfo) cmd;
			this.protocolVersion = cmdNewtonInfo.getProtocolVersion();
			this.challengeNewton = cmdNewtonInfo.getEncryptedKey();

			DCmdWhichIcons cmdWhichIcons = (DCmdWhichIcons) DockCommandFactory.getInstance().create(DCmdWhichIcons.COMMAND);
			cmdWhichIcons.setIcons(DCmdWhichIcons.kBackupIcon | DCmdWhichIcons.kInstallIcon | DCmdWhichIcons.kKeyboardIcon | DCmdWhichIcons.kRestoreIcon);
			setState(state, State.HANDSHAKE_ICONS_SENDING, null, cmd);
			sendCommand(cmdWhichIcons);
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			DCmdResult cmdResult = (DCmdResult) cmd;
			if (cmdResult.getErrorCode() == 0) {
				DCmdSetTimeout cmdSetTimeout = (DCmdSetTimeout) DockCommandFactory.getInstance().create(DCmdSetTimeout.COMMAND);
				cmdSetTimeout.setTimeout(getTimeout());
				setState(state, State.HANDSHAKE_TIMEOUT_SENDING, null, cmd);
				sendCommand(cmdSetTimeout);
			} else {
				// Was problem, so try send again with less icons?
				cmdWhichIcons = (DCmdWhichIcons) DockCommandFactory.getInstance().create(DCmdWhichIcons.COMMAND);
				cmdWhichIcons.setIcons(DCmdWhichIcons.kInstallIcon | DCmdWhichIcons.kKeyboardIcon);
				setState(state, State.HANDSHAKE_ICONS_SENDING, null, cmd);
				sendCommand(cmdWhichIcons);
			}
			break;
		case HANDSHAKE_PASS_RECEIVED:
			DCmdPassword cmdPassword = (DCmdPassword) cmd;
			setState(state, State.HANDSHAKE_PASS_SENDING, null, cmdPassword);
			break;
		case HANDSHAKE_DONE:
		case ACCEPTED:
		case IDLE:
			// TODO process the command.
			break;
		case DISCONNECTED:
			throw new PipeDisconnectedException();
		default:
			throw new BadPipeStateException();
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
	 */
	protected void setState(State oldState, State state, MNPPacket packet, DockCommandFromNewton cmd) throws PipeDisconnectedException, TimeoutException {
		byte packetType = (packet == null) ? 0 : packet.getType();
		MNPLinkTransferPacket lt;
		byte[] data;

		setState(state);

		switch (oldState) {
		case DISCONNECTED:
			if (state != State.DISCONNECTED) {
				throw new PipeDisconnectedException();
			}
		}

		switch (state) {
		case HANDSHAKE_LR_LISTEN:
			if (packetType == MNPPacket.LR) {
				// Can start connecting again as long we are not busy
				// handshaking.
				if (state.compareTo(State.HANDSHAKE_LR_RECEIVED) < 0) {
					setState(state, State.HANDSHAKE_LR_RECEIVED, packet, null);
				}
			}
			break;
		case HANDSHAKE_LR_RECEIVED:
			if (packetType == MNPPacket.LR) {
				MNPLinkRequestPacket lr = (MNPLinkRequestPacket) packet;
				this.sequence = 0;
				MNPLinkRequestPacket reply = (MNPLinkRequestPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LR);
				reply.setDataPhaseOpt(lr.getDataPhaseOpt());
				reply.setFramingMode(lr.getFramingMode());
				reply.setMaxInfoLength(lr.getMaxInfoLength());
				reply.setMaxOutstanding(lr.getMaxOutstanding());
				reply.setTransmitted(lr.getTransmitted());
				setState(state, State.HANDSHAKE_LR_SENDING, reply, null);
				sendAndAcknowledge(reply);
			}
			break;
		case HANDSHAKE_LR_SENDING:
			if (packetType == MNPPacket.LA) {
				setState(state, State.HANDSHAKE_LR_SENT, packet, null);
			}
			break;
		case HANDSHAKE_LR_SENT:
			setState(state, State.HANDSHAKE_RTDK_LISTEN, packet, null);
			break;
		case HANDSHAKE_RTDK_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdRequestToDock.COMMAND.equals(cmd.getCommand())) {
						setState(state, State.HANDSHAKE_RTDK_RECEIVED, packet, cmd);
					}
				}
			}
			break;
		case HANDSHAKE_RTDK_RECEIVED:
			if (DCmdRequestToDock.COMMAND.equals(cmd.getCommand())) {
				commandReceived(cmd, state);
			}
			break;
		case HANDSHAKE_DOCK_SENDING:
			if (packetType == MNPPacket.LA) {
				setState(state, State.HANDSHAKE_DOCK_SENT, packet, null);
			}
			break;
		case HANDSHAKE_DOCK_SENT:
			setState(state, State.HANDSHAKE_NAME_LISTEN, packet, null);
			break;
		case HANDSHAKE_NAME_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdNewtonName.COMMAND.equals(cmd.getCommand())) {
						setState(state, State.HANDSHAKE_NAME_RECEIVED, packet, cmd);
					}
				}
			}
			break;
		case HANDSHAKE_NAME_RECEIVED:
			if (DCmdNewtonName.COMMAND.equals(cmd.getCommand())) {
				commandReceived(cmd, state);
			}
			break;
		case HANDSHAKE_DINFO_SENDING:
			if (packetType == MNPPacket.LA) {
				setState(state, State.HANDSHAKE_DINFO_SENT, packet, null);
			}
			break;
		case HANDSHAKE_DINFO_SENT:
			setState(state, State.HANDSHAKE_NINFO_LISTEN, packet, null);
			break;
		case HANDSHAKE_NINFO_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdNewtonInfo.COMMAND.equals(cmd.getCommand())) {
						setState(state, State.HANDSHAKE_NINFO_RECEIVED, packet, cmd);
					}
				}
			}
			break;
		case HANDSHAKE_NINFO_RECEIVED:
			if (DCmdNewtonInfo.COMMAND.equals(cmd.getCommand())) {
				commandReceived(cmd, state);
			}
			break;
		case HANDSHAKE_ICONS_SENDING:
			if (packetType == MNPPacket.LA) {
				setState(state, State.HANDSHAKE_ICONS_SENT, packet, null);
			}
			break;
		case HANDSHAKE_ICONS_SENT:
			setState(state, State.HANDSHAKE_ICONS_RESULT_LISTEN, packet, null);
			break;
		case HANDSHAKE_ICONS_RESULT_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdResult.COMMAND.equals(cmd.getCommand())) {
						setState(state, State.HANDSHAKE_ICONS_RESULT_RECEIVED, packet, cmd);
					}
				}
			}
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			if (DCmdResult.COMMAND.equals(cmd.getCommand())) {
				commandReceived(cmd, state);
			}
			break;
		case HANDSHAKE_TIMEOUT_SENDING:
			if (packetType == MNPPacket.LA) {
				setState(state, State.HANDSHAKE_TIMEOUT_SENT, packet, null);
			}
			break;
		case HANDSHAKE_TIMEOUT_SENT:
			setState(state, State.HANDSHAKE_PASS_LISTEN, packet, null);
			break;
		case HANDSHAKE_PASS_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdPassword.COMMAND.equals(cmd.getCommand())) {
						setState(state, State.HANDSHAKE_PASS_RECEIVED, packet, cmd);
					}
				}
			}
			break;
		case HANDSHAKE_PASS_RECEIVED:
			if (DCmdPassword.COMMAND.equals(cmd.getCommand())) {
				commandReceived(cmd, state);
			}
			break;
		case HANDSHAKE_PASS_SENDING:
			if (packetType == MNPPacket.LA) {
				setState(state, State.HANDSHAKE_PASS_SENT, packet, null);
			}
			break;
		case HANDSHAKE_PASS_SENT:
			setState(state, State.HANDSHAKE_DONE, packet, null);
			break;
		case HANDSHAKE_DONE:
		case ACCEPTED:
		case IDLE:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					commandReceived(cmd, state);
				}
			}
			break;
		case DISCONNECTING:
			disconnectQuiet();
			break;
		case DISCONNECTED:
			break;
		default:
			throw new BadPipeStateException();
		}
	}
}
