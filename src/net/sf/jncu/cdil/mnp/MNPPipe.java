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
import net.sf.jncu.protocol.v2_0.DockCommandFactory;
import net.sf.jncu.protocol.v2_0.session.DCmdInitiateDocking;
import net.sf.jncu.protocol.v2_0.session.DockCommandSession;

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
		/**
		 * Listen for LR from Newton.<br>
		 * Newton sends LR.<br>
		 * Send LR to Newton.
		 */
		HANDSHAKE_LISTEN_LR,
		/** Newton sends LA (for LR in previous step). */
		HANDSHAKE_LR_RECEIVED,
		/**
		 * Newton sends LT (kDRequestToDock).<br>
		 * Send LA to Newton (for LT in previous step).<br>
		 * Send LT (kDInitiateDocking) to Newton.
		 */
		HANDSHAKE_LISTEN_RTDK,
		/** Newton sends LA (for LT in previous step). */
		HANDSHAKE_RTDK_RECEIVED,
		/**
		 * Newton sends LT (kDNewtonName).<br>
		 * Send LA to Newton (for LT in previous step).<br>
		 * Send LT (kDDesktopInfo) to Newton.
		 */
		HANDSHAKE_LISTEN_NAME,
		/** Newton sends LA (for LT in previous step). */
		HANDSHAKE_NAME_RECEIVED,
		/**
		 * Newton sends LT (kDNewtonInfo).<br>
		 * Send LA to Newton (for LT in previous step).<br>
		 * Send LT (kDWhichIcons) to Newton (optional).
		 */
		HANDSHAKE_LISTEN_INFO,
		/** Newton sends LA (for LT in previous step). */
		HANDSHAKE_INFO_RECEIVED,
		/** Finished handshaking. */
		HANDSHAKE_DONE,
		/** Connect request accepted. */
		ACCEPTED,
		/** Idle. */
		IDLE,
		/** Disconnected. */
		DISCONNECTED
	}

	protected final CommPortIdentifier portId;
	protected final int baud;
	protected final MNPSerialPort port;
	protected final MNPPacketLayer packetLayer = new MNPPacketLayer();
	/** Outgoing sequence. */
	protected byte sequence;
	/** Internal state. */
	private State state = State.HANDSHAKE_LISTEN_LR;

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
				ioe.printStackTrace();
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
		packetSendAndAcknowledge(packet);
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
		packetSendAndAcknowledge(packet);

		super.disconnectImpl();
		port.close();
		try {
			setState(State.DISCONNECTED);
		} catch (PipeDisconnectedException pde) {
			// ignore - we are already disconnected
		}
	}

	public void packetReceived(MNPPacket packet) {
		try {
			if (packet instanceof MNPLinkRequestPacket) {
				setState(State.HANDSHAKE_LISTEN_LR);
			}

			switch (state) {
			case HANDSHAKE_LISTEN_LR:
				if (packet instanceof MNPLinkRequestPacket) {
					MNPLinkRequestPacket lr = (MNPLinkRequestPacket) packet;
					sequence = 0;
					MNPLinkRequestPacket reply = (MNPLinkRequestPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LR);
					reply.setDataPhaseOpt(lr.getDataPhaseOpt());
					reply.setFramingMode(lr.getFramingMode());
					reply.setMaxInfoLength(lr.getMaxInfoLength());
					reply.setMaxOutstanding(lr.getMaxOutstanding());
					reply.setTransmitted(lr.getTransmitted());
					packetSendAndAcknowledge(reply);
					setState(State.HANDSHAKE_LR_RECEIVED);
				}
				break;
			case HANDSHAKE_LR_RECEIVED:
				if (packet instanceof MNPLinkAcknowledgementPacket) {
					setState(State.HANDSHAKE_LISTEN_RTDK);
				}
				break;
			case HANDSHAKE_LISTEN_RTDK:
				if (packet instanceof MNPLinkTransferPacket) {
					MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;

					MNPLinkAcknowledgementPacket ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
					ack.setSequence(lt.getSequence());
					ack.setCredit((byte) 7);
					packetSendAndAcknowledge(ack);

					byte[] data = lt.getData();
					if (!DockCommandFromNewton.isCommand(data)) {
						throw new BadPipeStateException();
					}
					DockCommandFromNewton cmd = DockCommandFromNewton.deserialize(data);
					if (!DockCommandSession.NewtonToDesktop.kDRequestToDock.equals(cmd.getCommand())) {
						// Ignore erroneous command
						return;
					}

					DCmdInitiateDocking cmdReply = (DCmdInitiateDocking) DockCommandFactory.getInstance().create(
							DockCommandSession.DesktopToNewton.kDInitiateDocking);
					cmdReply.setSession(DCmdInitiateDocking.SESSION_SETTING_UP);
					MNPLinkTransferPacket reply = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
					reply.setSequence(++sequence);
					reply.setData(cmdReply.getPayload());
					packetSendAndAcknowledge(reply);
					setState(State.HANDSHAKE_RTDK_RECEIVED);
				}
				break;
			case HANDSHAKE_RTDK_RECEIVED:
				if (packet instanceof MNPLinkAcknowledgementPacket) {
					setState(State.HANDSHAKE_LISTEN_NAME);
				}
				break;
			case HANDSHAKE_LISTEN_NAME:
				if (packet instanceof MNPLinkTransferPacket) {
					MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;

					MNPLinkAcknowledgementPacket ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
					ack.setSequence(lt.getSequence());
					ack.setCredit((byte) 7);
					packetSendAndAcknowledge(ack);

					byte[] data = lt.getData();
					if (!DockCommandFromNewton.isCommand(data)) {
						throw new BadPipeStateException();
					}
					DockCommandFromNewton cmd = DockCommandFromNewton.deserialize(data);
					if (!DockCommandSession.NewtonToDesktop.kDNewtonName.equals(cmd.getCommand())) {
						// Ignore erroneous command
						return;
					}
					// TODO keep the Newton Name for getter.

					DockCommandToNewton cmdReply = (DockCommandToNewton) DockCommandFactory.getInstance().create(
							DockCommandSession.DesktopToNewton.kDDesktopInfo);
					MNPLinkTransferPacket reply = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
					reply.setSequence(++sequence);
					reply.setData(cmdReply.getPayload());
					packetSendAndAcknowledge(reply);
					setState(State.HANDSHAKE_NAME_RECEIVED);
				}
				break;
			case HANDSHAKE_NAME_RECEIVED:
				if (packet instanceof MNPLinkAcknowledgementPacket) {
					setState(State.HANDSHAKE_LISTEN_INFO);
				}
				break;
			case HANDSHAKE_LISTEN_INFO:
				if (packet instanceof MNPLinkTransferPacket) {
					MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;

					MNPLinkAcknowledgementPacket ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
					ack.setSequence(lt.getSequence());
					ack.setCredit((byte) 7);
					packetSendAndAcknowledge(ack);

					byte[] data = lt.getData();
					if (!DockCommandFromNewton.isCommand(data)) {
						throw new BadPipeStateException();
					}
					DockCommandFromNewton cmd = DockCommandFromNewton.deserialize(data);
					if (!DockCommandSession.NewtonToDesktop.kDNewtonInfo.equals(cmd.getCommand())) {
						// Ignore erroneous command
						return;
					}
					// TODO keep the Newton Info for getter.

					DockCommandToNewton cmdReply = (DockCommandToNewton) DockCommandFactory.getInstance().create(
							DockCommandSession.DesktopToNewton.kDWhichIcons);
					MNPLinkTransferPacket reply = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
					reply.setSequence(++sequence);
					reply.setData(cmdReply.getPayload());
					packetSendAndAcknowledge(reply);
					setState(State.HANDSHAKE_NAME_RECEIVED);
				}
				break;
			case HANDSHAKE_INFO_RECEIVED:
				if (packet instanceof MNPLinkAcknowledgementPacket) {
					setState(State.HANDSHAKE_DONE);
					layer.setState(this, CDState.CONNECT_PENDING);
				}
				break;
			case HANDSHAKE_DONE:
			case ACCEPTED:
			case IDLE:
				if (packet instanceof MNPLinkTransferPacket) {
					MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;

					MNPLinkAcknowledgementPacket ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
					ack.setSequence(lt.getSequence());
					ack.setCredit((byte) 7);
					packetSendAndAcknowledge(ack);

					byte[] data = lt.getData();
					if (!DockCommandFromNewton.isCommand(data)) {
						throw new BadPipeStateException();
					}
					// DockCommandFromNewton cmd =
					// DockCommandFromNewton.deserialize(data);
					// TODO wait for concatenated command (commands split over
					// multiple packets).
					// TODO process the command.
				} else if (packet instanceof MNPLinkDisconnectPacket) {
					disconnectQuiet();
				}
				break;
			case DISCONNECTED:
				throw new PipeDisconnectedException();
			default:
				throw new BadPipeStateException();
			}
			// } catch (IOException ioe) {
			// // Consume so that Newton will send packet again.
			// ioe.printStackTrace();
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
	private void packetSendAndAcknowledge(MNPPacket packet) throws TimeoutException {
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

	private void setState(State state) throws PipeDisconnectedException {
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

}
