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
package net.sf.jncu.protocol.v1_0.io;

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This command sets the current store on the Newton. A store frame is sent to
 * uniquely identify the store to be set: <br>
 * <code>
 * {<br>&nbsp;&nbsp;name: "foo",<br>
 * &nbsp;&nbsp;kind: "bar",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;info: {&lt;info frame&gt;}		// This one is optional<br>
 * }</code>
 *
 * <pre>
 * 'ssto'
 * length
 * store frame
 * </pre>
 *
 * @author moshew
 */
public class DSetCurrentStore extends BaseDockCommandToNewton {

    /**
     * <tt>kDSetCurrentStore</tt>
     */
    public static final String COMMAND = "ssto";

    private Store store;

    /**
     * Creates a new command.
     */
    public DSetCurrentStore() {
        super(COMMAND);
    }

    /**
     * Creates a new command.
     *
     * @param cmd the command.
     */
    protected DSetCurrentStore(String cmd) {
        super(cmd);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        NSOFFrame storeFrame = store.toFrame();
        NSOFFrame frame = new NSOFFrame();
        frame.put(Store.SLOT_NAME, storeFrame.get(Store.SLOT_NAME));
        if (storeFrame.hasSlot(Store.SLOT_KIND))
            frame.put(Store.SLOT_KIND, storeFrame.get(Store.SLOT_KIND));
        if (storeFrame.hasSlot(Store.SLOT_SIGNATURE))
            frame.put(Store.SLOT_SIGNATURE, storeFrame.get(Store.SLOT_SIGNATURE));

        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(frame, data);
    }

    /**
     * Get the store.
     *
     * @return the store.
     */
    public Store getStore() {
        return store;
    }

    /**
     * Set the store.
     *
     * @param store the store.
     */
    public void setStore(Store store) {
        this.store = store;
    }

}
