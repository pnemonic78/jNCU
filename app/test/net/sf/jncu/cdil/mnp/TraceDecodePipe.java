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

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;

import java.io.InputStream;

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
    protected MNPPacketLayer createPacketLayer() {
        return new TraceDecodePacketLayer(this, receivedFromNewton, sentToNewton);
    }

    @Override
    protected DockingProtocol<MNPPacket, MNPPacketLayer> createDockingProtocol() {
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
