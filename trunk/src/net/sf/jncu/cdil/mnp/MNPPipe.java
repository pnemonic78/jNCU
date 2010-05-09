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
import net.sf.jncu.protocol.v2_0.session.DockingState;
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

	protected final byte CREDIT = 7;

	protected final CommPortIdentifier portId;
	protected final int baud;
	protected final MNPSerialPort port;
	protected final MNPPacketLayer packetLayer = new MNPPacketLayer();
	/** Outgoing sequence. */
	protected byte sequence;
	/** Internal state. */
	private DockingState state = DockingState.HANDSHAKE_LR_LISTEN;

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
				if (state != DockingState.DISCONNECTED) {
					ioe.printStackTrace();
				}
			}
		} while (state != DockingState.DISCONNECTED);
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
			setState(DockingState.DISCONNECTED);
		} catch (PipeDisconnectedException pde) {
			// ignore - we are already disconnected
		}
	}

	public void packetReceived(MNPPacket packet) {
		byte packetType = packet.getType();
		DockingState newState = state;

		try {
			if (packetType == MNPPacket.LR) {
				newState = DockingState.HANDSHAKE_LR_LISTEN;
			} else if (packetType == MNPPacket.LT) {
				MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;
				MNPLinkAcknowledgementPacket ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
				ack.setSequence(lt.getSequence());
				ack.setCredit(CREDIT);
				sendAndAcknowledge(ack);
			} else if (packetType == MNPPacket.LD) {
				newState = DockingState.DISCONNECTING;
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
		if (state == DockingState.HANDSHAKE_DONE) {
			setState(DockingState.ACCEPTED);
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
			resend &= (state != DockingState.DISCONNECTED);
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

	protected void setState(DockingState state) throws PipeDisconnectedException {
		if (this.state == DockingState.DISCONNECTED) {
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
	 * @see #commandReceived(DockCommandFromNewton, DockingState)
	 */
	protected void commandReceived(byte[] cmdData, DockingState state) throws PipeDisconnectedException, TimeoutException {
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
	protected void commandReceived(DockCommandFromNewton cmd, DockingState state) throws PipeDisconnectedException, TimeoutException {
		if (cmd == null) {
			return;
		}

		switch (state) {
		case HANDSHAKE_RTDK_RECEIVED:
			DCmdInitiateDocking cmdInitiateDocking = (DCmdInitiateDocking) DockCommandFactory.getInstance().create(DCmdInitiateDocking.COMMAND);
			cmdInitiateDocking.setSession(DCmdInitiateDocking.SESSION_SETTING_UP);
			setState(state, DockingState.HANDSHAKE_DOCK_SENDING, null, cmd);
			sendCommand(cmdInitiateDocking);
			break;
		case HANDSHAKE_NAME_RECEIVED:
			this.info = ((DCmdNewtonName) cmd).getInformation();
			DCmdDesktopInfo cmdDesktopInfo = (DCmdDesktopInfo) DockCommandFactory.getInstance().create(DCmdDesktopInfo.COMMAND);
			this.challengeDesktop = cmdDesktopInfo.getEncryptedKey();
			setState(state, DockingState.HANDSHAKE_DINFO_SENDING, null, cmd);
			sendCommand(cmdDesktopInfo);
			break;
		case HANDSHAKE_NINFO_RECEIVED:
			DCmdNewtonInfo cmdNewtonInfo = (DCmdNewtonInfo) cmd;
			this.protocolVersion = cmdNewtonInfo.getProtocolVersion();
			this.challengeNewton = cmdNewtonInfo.getEncryptedKey();

			DCmdWhichIcons cmdWhichIcons = (DCmdWhichIcons) DockCommandFactory.getInstance().create(DCmdWhichIcons.COMMAND);
			cmdWhichIcons.setIcons(DCmdWhichIcons.kBackupIcon | DCmdWhichIcons.kInstallIcon | DCmdWhichIcons.kKeyboardIcon | DCmdWhichIcons.kRestoreIcon);
			setState(state, DockingState.HANDSHAKE_ICONS_SENDING, null, cmd);
			sendCommand(cmdWhichIcons);
			break;
		case HANDSHAKE_ICONS_RESULT_RECEIVED:
			DCmdResult cmdResult = (DCmdResult) cmd;
			if (cmdResult.getErrorCode() == 0) {
				DCmdSetTimeout cmdSetTimeout = (DCmdSetTimeout) DockCommandFactory.getInstance().create(DCmdSetTimeout.COMMAND);
				cmdSetTimeout.setTimeout(getTimeout());
				setState(state, DockingState.HANDSHAKE_TIMEOUT_SENDING, null, cmd);
				sendCommand(cmdSetTimeout);
			} else {
				// Was problem, so try send again with less icons?
				cmdWhichIcons = (DCmdWhichIcons) DockCommandFactory.getInstance().create(DCmdWhichIcons.COMMAND);
				cmdWhichIcons.setIcons(DCmdWhichIcons.kInstallIcon | DCmdWhichIcons.kKeyboardIcon);
				setState(state, DockingState.HANDSHAKE_ICONS_SENDING, null, cmd);
				sendCommand(cmdWhichIcons);
			}
			break;
		case HANDSHAKE_PASS_RECEIVED:
			DCmdPassword cmdPassword = (DCmdPassword) cmd;
			setState(state, DockingState.HANDSHAKE_PASS_SENDING, null, cmdPassword);
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
	protected void setState(DockingState oldState, DockingState state, MNPPacket packet, DockCommandFromNewton cmd) throws PipeDisconnectedException,
			TimeoutException {
		byte packetType = (packet == null) ? 0 : packet.getType();
		MNPLinkTransferPacket lt;
		byte[] data;

		setState(state);

		switch (oldState) {
		case DISCONNECTED:
			if (state != DockingState.DISCONNECTED) {
				throw new PipeDisconnectedException();
			}
		}

		switch (state) {
		case HANDSHAKE_LR_LISTEN:
			if (packetType == MNPPacket.LR) {
				// Can start connecting again as long we are not busy
				// handshaking.
				if (state.compareTo(DockingState.HANDSHAKE_LR_RECEIVED) < 0) {
					setState(state, DockingState.HANDSHAKE_LR_RECEIVED, packet, null);
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
				setState(state, DockingState.HANDSHAKE_LR_SENDING, reply, null);
				sendAndAcknowledge(reply);
			}
			break;
		case HANDSHAKE_LR_SENDING:
			if (packetType == MNPPacket.LA) {
				setState(state, DockingState.HANDSHAKE_LR_SENT, packet, null);
			}
			break;
		case HANDSHAKE_LR_SENT:
			setState(state, DockingState.HANDSHAKE_RTDK_LISTEN, packet, null);
			break;
		case HANDSHAKE_RTDK_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdRequestToDock.COMMAND.equals(cmd.getCommand())) {
						setState(state, DockingState.HANDSHAKE_RTDK_RECEIVED, packet, cmd);
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
				setState(state, DockingState.HANDSHAKE_DOCK_SENT, packet, null);
			}
			break;
		case HANDSHAKE_DOCK_SENT:
			setState(state, DockingState.HANDSHAKE_NAME_LISTEN, packet, null);
			break;
		case HANDSHAKE_NAME_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdNewtonName.COMMAND.equals(cmd.getCommand())) {
						setState(state, DockingState.HANDSHAKE_NAME_RECEIVED, packet, cmd);
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
				setState(state, DockingState.HANDSHAKE_DINFO_SENT, packet, null);
			}
			break;
		case HANDSHAKE_DINFO_SENT:
			setState(state, DockingState.HANDSHAKE_NINFO_LISTEN, packet, null);
			break;
		case HANDSHAKE_NINFO_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdNewtonInfo.COMMAND.equals(cmd.getCommand())) {
						setState(state, DockingState.HANDSHAKE_NINFO_RECEIVED, packet, cmd);
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
				setState(state, DockingState.HANDSHAKE_ICONS_SENT, packet, null);
			}
			break;
		case HANDSHAKE_ICONS_SENT:
			setState(state, DockingState.HANDSHAKE_ICONS_RESULT_LISTEN, packet, null);
			break;
		case HANDSHAKE_ICONS_RESULT_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdResult.COMMAND.equals(cmd.getCommand())) {
						setState(state, DockingState.HANDSHAKE_ICONS_RESULT_RECEIVED, packet, cmd);
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
				setState(state, DockingState.HANDSHAKE_TIMEOUT_SENT, packet, null);
			}
			break;
		case HANDSHAKE_TIMEOUT_SENT:
			setState(state, DockingState.HANDSHAKE_PASS_LISTEN, packet, null);
			break;
		case HANDSHAKE_PASS_LISTEN:
			if (packetType == MNPPacket.LT) {
				lt = (MNPLinkTransferPacket) packet;
				data = lt.getData();
				if (DockCommandFromNewton.isCommand(data)) {
					cmd = DockCommandFromNewton.deserialize(data);
					if (DCmdPassword.COMMAND.equals(cmd.getCommand())) {
						setState(state, DockingState.HANDSHAKE_PASS_RECEIVED, packet, cmd);
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
				setState(state, DockingState.HANDSHAKE_PASS_SENT, packet, null);
			}
			break;
		case HANDSHAKE_PASS_SENT:
			setState(state, DockingState.HANDSHAKE_DONE, packet, null);
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
