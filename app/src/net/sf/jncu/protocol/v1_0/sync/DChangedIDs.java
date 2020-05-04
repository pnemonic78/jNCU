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
package net.sf.jncu.protocol.v1_0.sync;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

/**
 * This command is sent in response to a <tt>kDGetChangedIDs</tt> command. It
 * returns all the ids with <tt>mod</tt> time &gt; the <tt>last sync</tt> time.
 * If the last sync time is {@code 0}, no changed entries are returned (this
 * would happen on the first sync).
 *
 * <pre>
 * 'cids'
 * length
 * count
 * array of ids for the soup
 * </pre>
 */
public class DChangedIDs extends BaseDockCommandFromNewton {

    /**
     * <tt>kDChangedIDs</tt>
     */
    public static final String COMMAND = "cids";

    private final Set<Integer> ids = new TreeSet<Integer>();

    /**
     * Creates a new command.
     */
    public DChangedIDs() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        ids.clear();
        int count = ntohl(data);
        for (int i = 0; i < count; i++) {
            addID(ntohl(data));
        }
    }

    /**
     * Get the changed IDs.
     *
     * @return the IDs.
     */
    public Set<Integer> getIDs() {
        return ids;
    }

    /**
     * Set the changed IDs.
     *
     * @param ids the IDs.
     */
    protected void setIDs(Set<Integer> ids) {
        this.ids.clear();
        this.ids.addAll(ids);
    }

    /**
     * Add a changed ID.
     *
     * @param id the ID.
     */
    protected void addID(Integer id) {
        this.ids.add(id);
    }
}
