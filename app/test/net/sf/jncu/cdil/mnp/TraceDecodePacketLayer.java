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

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class TraceDecodePacketLayer extends EmptyPacketLayer {

    private final InputStream receivedFromNewton;
    private final InputStream sentToNewton;

    public TraceDecodePacketLayer(MNPPipe pipe, InputStream receivedFromNewton, InputStream sentToNewton) {
        super(pipe);
        setName("TraceDecodePacketLayer-" + getId());
        this.receivedFromNewton = receivedFromNewton;
        this.sentToNewton = new BufferedInputStream(sentToNewton, 1024);
    }

    @Override
    protected InputStream getInput() throws IOException {
        return receivedFromNewton;
    }

    public byte[] readSent() throws EOFException, IOException {
        return read(sentToNewton);
    }

    @Override
    protected boolean allowAcknowledge(MNPPacket packet) {
        return true;
    }
}
