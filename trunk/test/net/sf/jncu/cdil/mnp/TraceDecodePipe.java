package net.sf.jncu.cdil.mnp;

import java.io.InputStream;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPacketLayer;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v2_0.session.DRequestToDock;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;
import net.sf.jncu.protocol.v2_0.session.DockingState;

public class TraceDecodePipe extends MNPPipe {

	private final InputStream receivedFromNewton;
	private final InputStream sentToNewton;

	public TraceDecodePipe(CDLayer layer, InputStream receivedFromNewton, InputStream sentToNewton) throws PlatformException, ServiceNotSupportedException {
		super(layer, null, 0);
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
	public void write(IDockCommandToNewton cmd) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		// ignore - we only write packets.
	}

	@Override
	public boolean allowSend() {
		return false;
	}

	@Override
	protected void acceptImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
		docking.setState(DockingState.HANDSHAKE_DONE);
		super.acceptImpl();
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		String cmd = command.getCommand();

		switch (stateMNP) {
		case MNP_DISCONNECTED:
			if (DRequestToDock.COMMAND.equals(cmd)) {
				stateMNP = MNPState.MNP_HANDSHAKE_DOCK;
			}
			break;
		}

		super.commandReceived(command);
	}
}
