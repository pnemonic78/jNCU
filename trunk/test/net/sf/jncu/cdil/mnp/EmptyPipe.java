/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.cdil.mnp;

import java.util.concurrent.TimeoutException;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v2_0.session.DRequestToDock;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;
import net.sf.jncu.protocol.v2_0.session.DockingState;

public class EmptyPipe extends MNPPipe {

	public EmptyPipe(CDLayer layer) throws PlatformException, ServiceNotSupportedException {
		super(layer, (String) null, 0);
		setName("EmptyPipe-" + getId());
	}

	@Override
	protected MNPPacketLayer createPacketLayer() {
		return new EmptyPacketLayer(this);
	}

	@Override
	protected DockingProtocol<MNPPacket, MNPPacketLayer> createDockingProtocol() {
		return new EmptyDockingProtocol(this);
	}

	@Override
	public void write(DockCommandToNewton cmd) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		// ignore - we only write packets.
	}

	@Override
	public boolean allowSend() {
		return false;
	}

	@Override
	protected void acceptImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
		docking.setState(DockingState.HANDSHAKE_DONE);
		setState(MNPState.MNP_HANDSHAKE_DOCK);
		super.acceptImpl();
	}

	@Override
	public void commandReceived(DockCommandFromNewton command) {
		final String cmd = command.getCommand();

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

	@Override
	public void startListening() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		super.startListening();
		layer.setState(this, CDState.CONNECT_PENDING);
	}
}
