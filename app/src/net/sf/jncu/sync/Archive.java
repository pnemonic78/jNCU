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
package net.sf.jncu.sync;

import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Archive file for backup from the Newton device, and restore to the Newton
 * device.
 * <p>
 * The contents of a jNCU archive typically have the following structure:
 * <ul>
 * <li><tt>modified</tt></li>
 * <li><tt>device</tt></li>
 * <li><tt>stores</tt>
 * <ul>
 * <li>store #1
 * <ul>
 * <li><tt>store</tt></li>
 * <li><tt>packages</tt>
 * <ul>
 * <li>package1<tt>.pkg</tt></li>
 * <li>...</li>
 * </ul>
 * </li>
 * <li><tt>soups</tt>
 * <ul>
 * <li>soup name #1
 * <ul>
 * <li><tt>soup</tt></li>
 * <li><tt>entries</tt></li>
 * </ul>
 * </li>
 * <li>...</li>
 * <li>soup name #n</li>
 * </ul>
 * </ul>
 * </li>
 * <li>...</li>
 * <li>store #n</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author mwaisberg
 */
public class Archive {

    /**
     * Character to mark entry as directory or folder.
     */
    protected static final String DIRECTORY = "/";

    /**
     * Entry name for the time stamp.
     */
    public static final String ENTRY_MODIFIED = "modified";
    /**
     * Entry name for the device information.
     */
    public static final String ENTRY_DEVICE = "device";
    /**
     * Entry name for the stores folder.
     */
    public static final String ENTRY_STORES = "stores";
    /**
     * Entry name for the store information.
     */
    public static final String ENTRY_STORE = "store";
    /**
     * Entry name for the packages folder.
     */
    public static final String ENTRY_PACKAGES = "packages";
    /**
     * Entry name for the soups folder.
     */
    public static final String ENTRY_SOUPS = "soups";
    /**
     * Entry name for the soup information.
     */
    public static final String ENTRY_SOUP = "soup";
    /**
     * Entry name for the entries database.
     */
    public static final String ENTRY_ENTRIES = "entries";
    /**
     * Entry name for an entry database.
     */
    public static final String ENTRY_ENTRY = "entry.";

    private long modified = System.currentTimeMillis();
    private NewtonInfo deviceInfo;
    private final Map<String, Store> stores = new HashMap<String, Store>();

    /**
     * Creates a new archive.
     */
    public Archive() {
    }

    /**
     * Get the modified time stamp.<br>
     * Equivalent to "last sync time".
     *
     * @return the time in milliseconds.
     */
    public long getModified() {
        return modified;
    }

    /**
     * Set the modified time stamp.<br>
     * Equivalent to "last sync time".
     *
     * @param modified the time in milliseconds.
     */
    public void setModified(long modified) {
        this.modified = modified;
    }

    /**
     * Get the device information.
     *
     * @return the information.
     */
    public NewtonInfo getDeviceInfo() {
        return deviceInfo;
    }

    /**
     * Set the device information.
     *
     * @param info the information.
     */
    public void setDeviceInfo(NewtonInfo info) {
        this.deviceInfo = info;
    }

    /**
     * Get the stores.
     *
     * @return the list of stores.
     */
    public Collection<Store> getStores() {
        return stores.values();
    }

    /**
     * Set the stores.
     *
     * @param stores the list of stores.
     */
    public void setStores(Collection<Store> stores) {
        this.stores.clear();
        if (stores != null) {
            for (Store store : stores)
                addStore(store);
        }
    }

    /**
     * Add the store.
     *
     * @param store the store.
     */
    public void addStore(Store store) {
        this.stores.put(store.getName(), store);
    }

    /**
     * Find the store.
     *
     * @param name the store name.
     * @return the store - {@code null} otherwise.
     */
    public Store findStore(String name) {
        return stores.get(name);
    }
}
