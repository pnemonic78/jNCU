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
package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * Delete package dirty.
 *
 * <pre>
 * 'dpkd'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DDeletePackageDirty extends DockCommandToNewtonBlank {

    /**
     * <tt>kDDeletePkgDir</tt>
     */
    public static final String COMMAND = "dpkd";

    /**
     * Creates a new command.
     */
    public DDeletePackageDirty() {
        super(COMMAND);
    }

}
