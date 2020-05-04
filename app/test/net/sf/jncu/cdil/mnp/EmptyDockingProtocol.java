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

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;
import net.sf.jncu.protocol.v2_0.session.DockingState;

public class EmptyDockingProtocol extends DockingProtocol<MNPPacket, MNPPacketLayer> {

    public EmptyDockingProtocol(EmptyPipe pipe) {
        super(pipe);
    }

    @Override
    public void commandReceived(DockCommandFromNewton command) {
        // ignore
    }

    @Override
    public void commandSent(DockCommandToNewton command) {
        // ignore
    }

    @Override
    protected void validateState(DockingState oldState, DockingState newState) throws BadPipeStateException, PipeDisconnectedException {
        // Pretend that all states are valid.
    }
}
