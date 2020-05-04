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

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command is sent by the desktop in response to the
 * <tt>kDResolveAlias</tt> command. If the value is {@code 0}, the alias can't
 * be resolved. If the data is {@code 1} (or non-zero) the alias can be
 * resolved.
 *
 * <pre>
 * 'alir'
 * length
 * resolved
 * </pre>
 *
 * @author moshew
 */
public class DAliasResolved extends DockCommandToNewtonLong {

    /**
     * <tt>kDAliasResolved</tt>
     */
    public static final String COMMAND = "alir";

    /**
     * Creates a new command.
     */
    public DAliasResolved() {
        super(COMMAND);
    }

    /**
     * Is alias resolved?
     *
     * @return true if resolved?
     */
    public boolean isResolved() {
        return getValue() != FALSE;
    }

    /**
     * Set alias resolved.
     *
     * @param resolved resolved?
     */
    public void setResolved(boolean resolved) {
        setValue(resolved ? TRUE : FALSE);
    }
}
