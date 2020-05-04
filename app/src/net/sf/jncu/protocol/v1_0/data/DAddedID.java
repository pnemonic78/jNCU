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

/**
 * This command is sent in response to a <tt>kDAddEntry</tt> command from the
 * desktop. It returns the ID that the entry was given when it was added to the
 * current soup.
 *
 * <pre>
 * 'adid'
 * length
 * id
 * </pre>
 */
public class DAddedID extends BaseDockCommandFromNewton {

    /**
     * <tt>kDAddedID</tt>
     */
    public static final String COMMAND = "adid";

    private int id;

    /**
     * Creates a new command.
     */
    public DAddedID() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setId(ntohl(data));
    }

    /**
     * Get the entry id.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the entry id.
     *
     * @param id the id.
     */
    protected void setId(int id) {
        this.id = id;
    }

}
