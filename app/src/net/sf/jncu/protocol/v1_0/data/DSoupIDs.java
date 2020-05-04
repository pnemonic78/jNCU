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

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This command is sent in response to a <tt>kDGetSoupIDs</tt> command. It
 * returns all the IDs from the current soup.
 *
 * <pre>
 * 'sids'
 * length
 * count
 * array of ids for the soup
 * </pre>
 */
public class DSoupIDs extends BaseDockCommandFromNewton {

    /**
     * <tt>kDSoupIDs</tt>
     */
    public static final String COMMAND = "sids";

    private final List<Integer> ids = new ArrayList<Integer>();

    /**
     * Creates a new command.
     */
    public DSoupIDs() {
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
     * Get the entry IDs.
     *
     * @return the IDs.
     */
    public List<Integer> getIDs() {
        return ids;
    }

    /**
     * Set the entry IDs.
     *
     * @param changedIDs the IDs.
     */
    protected void setIDs(List<Integer> changedIDs) {
        this.ids.clear();
        if (changedIDs != null)
            this.ids.addAll(changedIDs);
    }

    /**
     * Add an entry ID.
     *
     * @param id the ID.
     */
    protected void addID(Integer id) {
        this.ids.add(id);
    }

}
