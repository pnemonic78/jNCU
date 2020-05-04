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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command returns a store info frame describing the default store. This
 * frame contains the same info returned for all stores by the
 * <tt>kDStoreNames</tt> command except that it doesn't include the store info.
 * It contains the name, signature, total size, used size and kind.
 *
 * <pre>
 * 'dfst'
 * length
 * store frame
 * </pre>
 *
 * @author moshew
 */
public class DDefaultStore extends BaseDockCommandFromNewton {

    /**
     * <tt>kDDefaultStore</tt>
     */
    public static final String COMMAND = "dfst";

    private Store store;

    /**
     * Creates a new command.
     */
    public DDefaultStore() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFFrame frame = (NSOFFrame) decoder.inflate(data);
        setStore(frame);
    }

    /**
     * Get the store information.
     *
     * @return the store.
     */
    public Store getStore() {
        return store;
    }

    /**
     * Set the store information.
     *
     * @param store the store.
     */
    protected void setStore(Store store) {
        this.store = store;
    }

    /**
     * Set the store information.
     *
     * @param frame the store frame.
     */
    protected void setStore(NSOFFrame frame) {
        Store store = new Store("");
        store.fromFrame(frame);
        setStore(store);
    }

}
