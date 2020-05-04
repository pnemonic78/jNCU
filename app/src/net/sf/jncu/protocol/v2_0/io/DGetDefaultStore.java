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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command returns info about the default store. This info is the same as
 * the info returned by the <tt>kDGetStoreNames</tt> command (see
 * <tt>kDStoreNames</tt> for details). The default store is the one used by
 * LoadPackage.
 * <p>
 * Returns a <tt>kDDefaultStore</tt> command.
 *
 * <pre>
 * 'gdfs'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DGetDefaultStore extends DockCommandToNewtonBlank {

    /**
     * <tt>kDGetDefaultStore</tt>
     */
    public static final String COMMAND = "gdfs";

    /**
     * Creates a new command.
     */
    public DGetDefaultStore() {
        super(COMMAND);
    }

}
