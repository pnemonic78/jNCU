package net.sf.jncu.cdil.mnp;

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

public class EmptyPipe extends MNPPipe {

	public EmptyPipe(CDLayer layer) throws PlatformException, ServiceNotSupportedException {
		super(layer, (String) null, 0);
		setName("EmptyPipe-" + getId());
	}

	@Override
	protected CDPacketLayer<MNPPacket> createPacketLayer() {
		return new EmptyPacketLayer(this);
	}

	@Override
	protected DockingProtocol<MNPPacket> createDockingProtocol() {
		return new EmptyDockingProtocol(this);
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
	}

	@Override
	protected void validateState(MNPState oldState, MNPState newState) throws BadPipeStateException, PipeDisconnectedException {
		// Pretend that all states are valid.
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
