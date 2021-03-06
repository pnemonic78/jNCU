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

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This command is sent in response to a <tt>kDGetStoreNames</tt> command. It
 * returns information about all the stores on the Newton. Each array slot
 * contains the following information about a store:<br>
 * <code>
 * {<br>
 * &nbsp;&nbsp;name: "",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;totalsize: 1234,<br>
 * &nbsp;&nbsp;usedsize: 1234,<br>
 * &nbsp;&nbsp;kind: "",<br>
 * &nbsp;&nbsp;info: {store info frame},<br>
 * &nbsp;&nbsp;readOnly: true,<br>
 * &nbsp;&nbsp;defaultStore: true,		// only for the default store<br>
 * &nbsp;&nbsp;storePassword: password  // only if a store password has been set<br>
 * }</code>
 *
 * <pre>
 * 'stor'
 * length
 * array of frames
 * </pre>
 */
public class DStoreNames extends BaseDockCommandFromNewton {

    /**
     * <tt>kDStoreNames</tt>
     */
    public static final String COMMAND = "stor";

    private final List<Store> stores = new ArrayList<Store>();

    /**
     * Creates a new command.
     */
    public DStoreNames() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFArray arr = (NSOFArray) decoder.inflate(data);
        Store store;
        setStores(null);
        for (NSOFObject o : arr.getValue()) {
            store = new Store("");
            store.fromFrame((NSOFFrame) o);
            addStore(store);
        }
    }

    /**
     * Get the stores.
     *
     * @return the stores.
     */
    public List<Store> getStores() {
        return stores;
    }

    /**
     * Set the stores.
     *
     * @param stores the stores.
     */
    protected void setStores(List<Store> stores) {
        this.stores.clear();
        if (stores != null)
            this.stores.addAll(stores);
    }

    /**
     * Add a store.
     *
     * @param store the store.
     */
    protected void addStore(Store store) {
        this.stores.add(store);
    }

    /**
     * Get the default store.
     *
     * @return the store.
     */
    public Store getDefaultStore() {
        for (Store store : stores) {
            if (store.isDefaultStore())
                return store;
        }
        if (stores.size() > 0)
            return stores.get(0);
        return null;
    }
}
