/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
