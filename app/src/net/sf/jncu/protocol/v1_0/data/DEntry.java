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

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is sent in response to a <tt>kDReturnEntry</tt> command. The
 * entry in the current soup specified by the ID in the <tt>kDReturnEntry</tt>
 * command is returned.
 *
 * <pre>
 * 'entr'
 * length
 * entry  // binary data
 * </pre>
 */
public class DEntry extends DockCommandFromNewtonScript<NSOFFrame> {

    /**
     * <tt>kDEntry</tt>
     */
    public static final String COMMAND = "entr";

    private SoupEntry entry;

    /**
     * Creates a new command.
     */
    public DEntry() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        super.decodeCommandData(data);

        NSOFFrame frame = getResult();
        this.entry = null;
        if (frame != null) {
            this.entry = new SoupEntry(frame);
        }
    }

    /**
     * Get the soup entry.
     *
     * @return the entry.
     */
    public SoupEntry getEntry() {
        return entry;
    }
}
