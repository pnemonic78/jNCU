package net.sf.jncu.cdil.mnp;

import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;

public class TraceDecodeDockingProtocol extends DockingProtocol<MNPPacket> {

	public TraceDecodeDockingProtocol(TraceDecodePipe pipe) {
		super(pipe);
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		// ignore
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		// ignore
	}

}
