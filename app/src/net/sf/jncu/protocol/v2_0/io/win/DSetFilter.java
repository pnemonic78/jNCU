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
package net.sf.jncu.protocol.v2_0.io.win;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command changes the current filter being used. A
 * <tt>kDFilesAndFolders</tt> command is expected in return. The index is a long
 * indicating which item in the filters array sent from the desktop should be
 * used as the current filter. Index is 0-based. Windows only.
 *
 * <pre>
 * 'sflt'
 * length
 * index
 * </pre>
 *
 * @author moshew
 */
public class DSetFilter extends BaseDockCommandFromNewton {

    /**
     * <tt>kDSetFilter</tt>
     */
    public static final String COMMAND = "sflt";

    private int index;

    public DSetFilter() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setIndex(ntohl(data));
    }

    /**
     * Get the filter index.
     *
     * @return the index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the filter index.
     *
     * @param index the index.
     */
    protected void setIndex(int index) {
        this.index = index;
    }

}
