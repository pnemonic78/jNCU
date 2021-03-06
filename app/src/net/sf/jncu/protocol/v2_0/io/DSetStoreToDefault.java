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
 * This command can be used instead of <tt>kDSetCurrentStore</tt>. It sets the
 * current store to the one the user has picked as the default store (internal
 * or card).
 *
 * <pre>
 * 'sdef'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DSetStoreToDefault extends DockCommandToNewtonBlank {

    /**
     * <tt>kDSetStoreToDefault</tt>
     */
    public static final String COMMAND = "sdef";

    /**
     * Constructs a new command.
     */
    public DSetStoreToDefault() {
        super(COMMAND);
    }

}
