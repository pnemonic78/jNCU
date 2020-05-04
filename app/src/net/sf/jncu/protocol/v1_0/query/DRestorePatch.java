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
package net.sf.jncu.protocol.v1_0.query;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command is used to restore the patch backed up with
 * <tt>kDGetPatches</tt>. The Newton returns a <tt>kDResult</tt> of 0 (or an
 * error if appropriate) if the patch wasn't installed. If the patch was
 * installed the Newton restarts.
 *
 * <pre>
 * 'rpat'
 * length
 * patch
 * </pre>
 *
 * @author moshew
 */
public class DRestorePatch extends DockCommandToNewtonLong {

    /**
     * <tt>kDRestorePatch</tt>
     */
    public static final String COMMAND = "rpat";

    /**
     * Creates a new command.
     */
    public DRestorePatch() {
        super(COMMAND);
    }

    /**
     * Get the patch.
     *
     * @return the patch.
     */
    public int getPatch() {
        return getValue();
    }

    /**
     * Set the patch.
     *
     * @param patch the patch.
     */
    public void setPatch(int patch) {
        setValue(patch);
    }
}
