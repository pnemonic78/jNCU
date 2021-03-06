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
package net.sf.jncu.protocol.v2_0.sync;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

/**
 * This command is sent in response to a <tt>kDBackupSoup</tt> command (see that
 * command for command sequence details). The length for this command is always
 * set to {@code -1} indicating that the length is unknown. The ids are
 * specified as a compressed array of 16 bit numbers. Each id should be offset
 * by any value specified by a previous <tt>kDSetBaseID</tt> command (this is
 * how we can specify a 32 bit value in 15 bits). Each id is a number between
 * {@code 0} and {@code 0x7FFF (32767}. Negative numbers specify a count of the
 * number of entries above the previous number before the next break
 * (non-contiguous id). The sequence is ended by a {@code 0x8000} word. So, if
 * the Newton contains ids {@code 0, 1, 2, 3, 4, 10, 20, 21, 30, 31, 32} the
 * array would look like {@code 0, -4, 10, 20, -1, 30, -2, 0x8000}<br>
 * Thus we send 8 words instead of 11 longs. Is it worth it? If there are a lot
 * of entries it should be.
 *
 * <pre>
 * 'bids'
 * length = -1
 * ids
 * </pre>
 *
 * @author moshew
 */
public class DBackupIDs extends BaseDockCommandFromNewton {

    /**
     * <tt>kDBackupIDs</tt>
     */
    public static final String COMMAND = "bids";

    private Set<Short> ids;

    /**
     * Creates a new command.
     */
    public DBackupIDs() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        Set<Short> ids = new TreeSet<Short>();
        short id = ntohs(data);
        short idPrev = -1;

        while (id != 0x8000) {
            if (id >= 0) {
                ids.add(id);
            } else if (idPrev >= 0) {
                int contiguous = -id;
                id = idPrev;
                for (int i = 0; i < contiguous; i++) {
                    id++;
                    ids.add(id);
                }
            }

            idPrev = id;
            id = ntohs(data);
        }
        setIds(ids);
        setLength(ids.size() << 2);
    }

    /**
     * Get the offset IDs.
     *
     * @return the IDs.
     */
    public Set<Short> getIds() {
        return ids;
    }

    /**
     * Set the offset IDs.
     *
     * @param ids the IDs.
     */
    protected void setIds(Set<Short> ids) {
        this.ids = ids;
    }

}
