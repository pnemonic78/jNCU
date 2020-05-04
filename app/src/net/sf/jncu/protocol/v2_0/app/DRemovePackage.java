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

import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command tells the Newton to delete a package. It can be used during
 * selective restore or any other time.
 *
 * <pre>
 * 'rmvp'
 * length
 * name ref
 * </pre>
 *
 * @author Moshe
 */
public class DRemovePackage extends DockCommandToNewtonScript<NSOFString> {

    /**
     * <tt>kDRemovePackage</tt>
     */
    public static final String COMMAND = "rmvp";

    /**
     * Constructs a new command.
     */
    public DRemovePackage() {
        super(COMMAND);
    }
}
