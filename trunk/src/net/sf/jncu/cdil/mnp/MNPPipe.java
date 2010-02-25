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
		do {
			try {
				port = new MNPSerialPort(portId, baud, getTimeout());
				do {
					packetLayer.listen(port.getInputStream());
				} while (getCDState() != CDState.CONNECT_PENDING);
			} catch (BadPipeStateException bpse) {
				bpse.printStackTrace();
			} catch (PipeDisconnectedException pde) {
				// TODO Auto-generated catch block
				pde.printStackTrace();
			} catch (IOException ioe) {
				// TODO Auto-generated catch block
				ioe.printStackTrace();
			} catch (PortInUseException piue) {
				// TODO Auto-generated catch block
				piue.printStackTrace();
			} catch (TooManyListenersException tmle) {
				// TODO Auto-generated catch block
				tmle.printStackTrace();
			} catch (UnsupportedCommOperationException ucoe) {
				// TODO Auto-generated catch block
				ucoe.printStackTrace();
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
		if (getCDState() == CDState.LISTENING) {
			if (packet.getType() == MNPPacket.LR) {
				layer.setState(this, CDState.CONNECT_PENDING);
			}
		}
	}

	@Override
	protected void acceptImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
		super.acceptImpl();
		MNPLinkAcknowledgementPacket packet = (MNPLinkAcknowledgementPacket) MNPPacketFactory.getInstance().createLinkPacket(MNPPacket.LA);
		try {
			packetLayer.send(port.getOutputStream(), packet);
		} catch (IOException ioe) {
			throw new PipeDisconnectedException();
		}
	}

}
