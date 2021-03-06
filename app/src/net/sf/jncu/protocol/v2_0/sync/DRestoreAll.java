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

/**
 * This command is sent to the desktop if the user elects to restore all
 * information. <tt>Merge</tt> is {@code 0} to not merge, {@code 1} to merge.
 *
 * <pre>
 * 'rall'
 * length
 * merge
 * </pre>
 *
 * @author moshew
 */
public class DRestoreAll extends BaseDockCommandFromNewton {

    /**
     * <tt>kDRestoreAll</tt>
     */
    public static final String COMMAND = "rall";

    private boolean merge;

    /**
     * Creates a new command.
     */
    public DRestoreAll() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setMerge(ntohl(data) != FALSE);
    }

    /**
     * Is merge?
     *
     * @return merge?
     */
    public boolean isMerge() {
        return merge;
    }

    /**
     * Set merge.
     *
     * @param merge merge?
     */
    public void setMerge(boolean merge) {
        this.merge = merge;
    }

}
