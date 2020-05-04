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

import net.sf.jncu.protocol.v1_0.session.DTest;

/**
 * This command is first sent from the desktop to the Newton. The Newton
 * immediately echos the object back to the desktop. The object can be any
 * NewtonScript object (anything that can be sent through the object
 * read/write).
 * <p>
 * This command can also be sent with no ref attached. If the length is 0 the
 * command is echoed back to the desktop with no ref included.
 *
 * <pre>
 * 'rtst'
 * length
 * object
 * </pre>
 *
 * @author moshew
 */
public class DRefTest extends DTest {

    /**
     * <tt>kDRefTest</tt>
     */
    public static final String COMMAND = "rtst";

    /**
     * Creates a new command.
     */
    public DRefTest() {
        super(COMMAND);
    }
}
