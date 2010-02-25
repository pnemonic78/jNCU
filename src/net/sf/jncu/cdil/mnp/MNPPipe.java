package net.sf.jncu.cdil.mnp;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;

/**
 * MNP Serial pipe.
 * 
 * @author moshew
 */
public class MNPPipe extends CDPipe {

	private MNPPacketLayer packetLayer = new MNPPacketLayer();
	private final CommPortIdentifier portId;
	private final int baud;
	private SerialPort port;

	/**
	 * Creates a new MNP pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @param portId
	 *            the port identifier.
	 * @param baud
	 *            the baud rate to communicate at in bytes per second.
	 */
	public MNPPipe(CDLayer layer, CommPortIdentifier portId, int baud) {
		super(layer);
		this.portId = portId;
		this.baud = baud;
	}

	@Override
	public void run() {
		InputStream in;
		MNPPacket packet;

		try {
			port = (SerialPort) portId.open(portId.getName(), baud);

			in = this.getInput();
			packet = packetLayer.receive(in);
		} catch (BadPipeStateException bpse) {
			bpse.printStackTrace();
		} catch (CDILNotInitializedException ce) {
			// TODO Auto-generated catch block
			ce.printStackTrace();
		} catch (PlatformException pe) {
			// TODO Auto-generated catch block
			pe.printStackTrace();
		} catch (PipeDisconnectedException pde) {
			// TODO Auto-generated catch block
			pde.printStackTrace();
		} catch (TimeoutException te) {
			// TODO Auto-generated catch block
			te.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} catch (PortInUseException piue) {
			// TODO Auto-generated catch block
			piue.printStackTrace();
		}
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
			packetLayer.send(getOutput(), packet);
		} catch (IOException ioe) {
			throw new PipeDisconnectedException(ioe);
		}
	}

	@Override
	protected OutputStream getOutput() throws IOException {
		return port.getOutputStream();
	}
}
