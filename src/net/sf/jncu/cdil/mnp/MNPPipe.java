package net.sf.jncu.cdil.mnp;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
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
import net.sf.jncu.protocol.v2_0.session.DockCommandSession;

/**
 * MNP Serial pipe.
 * 
 * @author moshew
 */
public class MNPPipe extends CDPipe implements MNPPacketListener {

	private final CommPortIdentifier portId;
	private final int baud;
	private MNPSerialPort port;
	private MNPPacketLayer packetLayer = new MNPPacketLayer();

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
	public MNPPipe(CDLayer layer, CommPortIdentifier portId, int baud) throws ServiceNotSupportedException {
		super(layer);
		this.portId = portId;
		this.baud = baud;
		packetLayer.addPacketListener(this);
	}

	@Override
	public void run() {
		try {
			port = new MNPSerialPort(portId, baud, getTimeout());
		} catch (PortInUseException piue) {
			// TODO Auto-generated catch block
			piue.printStackTrace();
		} catch (TooManyListenersException tmle) {
			// TODO Auto-generated catch block
			tmle.printStackTrace();
		} catch (UnsupportedCommOperationException ucoe) {
			// TODO Auto-generated catch block
			ucoe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
		do {
			try {
				do {
					MNPPacket packet = packetLayer.receive(port.getInputStream());
					if (packet.getType() == MNPPacket.LR) {
						layer.setState(this, CDState.CONNECT_PENDING);
					}

				} while (getCDState() != CDState.CONNECT_PENDING);
			} catch (BadPipeStateException bpse) {
				bpse.printStackTrace();
			} catch (PipeDisconnectedException pde) {
				// TODO Auto-generated catch block
				pde.printStackTrace();
			} catch (IOException ioe) {
				// TODO Auto-generated catch block
				ioe.printStackTrace();
			}
		} while (getCDState() != CDState.DISCONNECTED);
	}

	@Override
	public void write(byte[] b, int offset, int count) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		super.write(b, offset, count);
		MNPLinkTransferPacket packet = (MNPLinkTransferPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LT);
		byte[] data = new byte[count];
		System.arraycopy(b, offset, data, 0, count);
		packet.setData(data);
		try {
			packetLayer.send(port.getOutputStream(), packet);
		} catch (IOException ioe) {
			throw new PipeDisconnectedException(ioe);
		}
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
		super.disconnectImpl();
		port.close();
		packetLayer = null;
	}

	public void packetReceived(MNPPacket packet) {
		switch (getCDState()) {
		case LISTENING:
			if (packet.getType() == MNPPacket.LR) {
				layer.setState(this, CDState.CONNECT_PENDING);
			}
			break;
		}
	}

	@Override
	protected void acceptImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
		super.acceptImpl();
		MNPLinkAcknowledgementPacket ack;
		try {
			ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
			packetLayer.send(port.getOutputStream(), ack);
			MNPPacket packet;
			do {
				packet = packetLayer.receive(port.getInputStream());
				if (packet.getType() == MNPPacket.LT) {
					MNPLinkTransferPacket lt = (MNPLinkTransferPacket) packet;
					byte[] data = lt.getData();
					if (!DockCommandFromNewton.isCommand(data)) {
						throw new PipeDisconnectedException();
					}
					DockCommandFromNewton cmd = DockCommandFromNewton.deserialize(data);
					if (cmd == null) {
						throw new PipeDisconnectedException();
					}
					if (DockCommandSession.NewtonToDesktop.kDRequestToDock.equals(cmd.getCommand())) {
						layer.setState(this, CDState.CONNECTED);
					}
					ack = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
					packetLayer.send(getOutput(), ack);
				}
			} while (getCDState() == CDState.CONNECT_PENDING);
		} catch (IOException ioe) {
			throw new PipeDisconnectedException(ioe);
		}
	}

	/**
	 * Poll the Newton for debugging.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void poll() throws IOException {
		InputStream in = port.getInputStream();
		int i = 0;
		int b;
		do {
			b = in.read();
			if (b == -1) {
				throw new EOFException();
			}
			if ((i & 15) == 0) {
				System.out.println();
			} else {
				System.out.print(' ');
			}
			System.out.print("0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
			i++;
		} while ((getCDState() == CDState.CONNECTED) && (port != null));
	}

}
