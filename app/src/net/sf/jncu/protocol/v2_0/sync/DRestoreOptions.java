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

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command is sent to the Newton to specify which applications and packages
 * can be restored. It is sent in response to a <tt>kDRestoreFile</tt> command
 * from the Newton. If the user elects to do a selective restore the Newton
 * returns a similar command to the desktop indicating what should be restored.
 * <p>
 * Example: <tt>restoreWhich</tt> = <code>{storeType: kRestoreToNewton,
 * packages: ["pkg1", ...],
 * applications: ["app1", ...]}</code> <br>
 * <tt>storeType</tt> slot indicates whether the data will be restored to a card
 * (<tt>kRestoreToCard = 1</tt>) or the Newton (<tt>kRestoreToNewton = 0</tt>).
 *
 * <pre>
 * 'ropt'
 * length
 * restoreWhich
 * </pre>
 *
 * @author moshew
 */
public class DRestoreOptions extends DockCommandToNewtonScript<NSOFFrame> {

    /**
     * <tt>kDRestoreOptions</tt>
     */
    public static final String COMMAND = "ropt";

    /**
     * <tt>kRestoreToNewton</tt>
     */
    public static final int RESTORE_TO_NEWTON = 0;
    /**
     * <tt>kRestoreToCard</tt>
     */
    public static final int RESTORE_TO_CARD = 1;

    /**
     * Creates a new command.
     */
    public DRestoreOptions() {
        super(COMMAND);
    }

}
