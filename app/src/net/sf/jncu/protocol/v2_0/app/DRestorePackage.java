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
package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command sends all the entries associated with a package to the Newton in
 * a single array. Packages are made up of at least 2 entries: one for the
 * package info and one for each part in the package. All of these entries must
 * be restored at the same time to restore a working package. A
 * <tt>kDResult</tt> is returned after the package has been successfully
 * restored.
 *
 * <pre>
 * 'rpkg'
 * length
 * package array
 * </pre>
 *
 * @author moshew
 */
public class DRestorePackage extends DockCommandToNewtonScript<NSOFArray> {

    /**
     * <tt>kDRestorePackage</tt>
     */
    public static final String COMMAND = "rpkg";

    /**
     * Creates a new command.
     */
    public DRestorePackage() {
        super(COMMAND);
    }

}
