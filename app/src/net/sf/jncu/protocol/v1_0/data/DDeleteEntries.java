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
package net.sf.jncu.protocol.v1_0.data;

import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;

/**
 * This command is sent to delete one or more entries from the current soup.
 *
 * <pre>
 * 'dele'
 * length
 * count
 * array of ids
 * </pre>
 *
 * @author moshew
 */
public class DDeleteEntries extends BaseDockCommandToNewton {

    /**
     * <tt>kDDeleteEntries</tt>
     */
    public static final String COMMAND = "dele";

    private final Set<Integer> ids = new TreeSet<Integer>();

    /**
     * Creates a new command.
     */
    public DDeleteEntries() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getIDs().size(), data);
        for (Integer id : getIDs()) {
            htonl(id, data);
        }
    }

    /**
     * Get the IDs to delete.
     *
     * @return the IDs.
     */
    public Set<Integer> getIDs() {
        return ids;
    }

    /**
     * Set the IDs to delete.
     *
     * @param ids the IDs.
     */
    protected void setIDs(Set<Integer> ids) {
        this.ids.clear();
        if (ids != null)
            this.ids.addAll(ids);
        setLength(4 + (this.ids.size() * 4));
    }

    /**
     * Add an ID to delete.
     *
     * @param id the ID.
     */
    protected void addID(Integer id) {
        this.ids.add(id);
        setLength(4 + (this.ids.size() * 4));
    }
}
