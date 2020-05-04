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

import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.mnp.MNPPipe.MNPState;
import net.sf.jncu.io.NullOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EmptyPacketLayer extends MNPSerialPacketLayer {

    private final InputStream nullIn;
    private final OutputStream nullOut;

    public EmptyPacketLayer(MNPPipe pipe) {
        super(pipe, null);
        setName("EmptyPacketLayer-" + getId());
        setTimeout(Integer.MAX_VALUE);
        this.nullIn = new ByteArrayInputStream(new byte[]{});
        this.nullOut = new NullOutputStream();
    }

    @Override
    protected InputStream getInput() throws IOException {
        return nullIn;
    }

    @Override
    protected OutputStream getOutput() throws IOException {
        return nullOut;
    }

    @Override
    protected boolean allowAcknowledge(MNPPacket packet) {
        return false;
    }

    @Override
    public void run() {
        try {
            ((MNPPipe) pipe).setState(MNPState.MNP_HANDSHAKE_LR_LISTEN);
        } catch (PipeDisconnectedException pde) {
            pde.printStackTrace();
        }
        super.run();
    }
}
