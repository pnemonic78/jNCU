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
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command sets the timeout for the connection (the time the Newton will
 * wait to receive data for it disconnects). This time is usually set to 30
 * seconds.
 *
 * <pre>
 * 'stim'
 * length
 * timeout in seconds
 * </pre>
 *
 * @author moshew
 */
public class DSetTimeout extends DockCommandToNewtonLong {

    /**
     * <tt>kDSetTimeout</tt>
     */
    public static final String COMMAND = "stim";

    /**
     * Creates a new command.
     */
    public DSetTimeout() {
        super(COMMAND);
        setTimeout(30);
    }

    /**
     * Get the timeout.
     *
     * @return the timeout in seconds.
     */
    public int getTimeout() {
        return getValue();
    }

    /**
     * Set the timeout.
     *
     * @param timeout the timeout in seconds.
     */
    public void setTimeout(int timeout) {
        setValue(timeout);
    }
}
