package net.sf.jncu.cdil.mnp;

import java.io.InputStream;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPacketLayer;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;

public class TraceDecodePipe extends EmptyPipe {

	private final InputStream receivedFromNewton;
	private final InputStream sentToNewton;

	public TraceDecodePipe(CDLayer layer, InputStream receivedFromNewton, InputStream sentToNewton) throws PlatformException, ServiceNotSupportedException {
		super(layer);
		setName("TraceDecodePipe-" + getId());
		this.receivedFromNewton = receivedFromNewton;
		this.sentToNewton = sentToNewton;
	}

	@Override
	protected CDPacketLayer<MNPPacket> createPacketLayer() {
		return new TraceDecodePacketLayer(this, receivedFromNewton, sentToNewton);
	}

	@Override
	protected DockingProtocol<MNPPacket> createDockingProtocol() {
		return new TraceDecodeDockingProtocol(this);
	}

	@Override
	protected void disconnectImpl() {
		try {
			// Wait for commands to finish.
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		if (!Boolean.getBoolean("debug"))
			super.disconnectImpl();
	}
}
