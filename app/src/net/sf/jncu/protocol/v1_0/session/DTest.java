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
package net.sf.jncu.protocol.v1_0.session;

import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

import java.io.IOException;
import java.io.InputStream;

/**
 * Test.
 *
 * <pre>
 * 'test'
 * length
 * data
 * </pre>
 */
public class DTest extends DockCommandToNewtonScript<NSOFObject> implements DockCommandBidi {

    /**
     * <tt>kDTest</tt>
     */
    public static final String COMMAND = "test";

    /**
     * Creates a new command.
     */
    public DTest() {
        super(COMMAND);
    }

    /**
     * Creates a new command.
     *
     * @param cmd the command.
     */
    protected DTest(String cmd) {
        super(cmd);
    }

    @Override
    public void decode(InputStream data) throws IOException {
        DockCommandFromNewtonScript<NSOFObject> cmd = new DockCommandFromNewtonScript<NSOFObject>(COMMAND) {
        };
        cmd.decode(data);
        setLength(cmd.getLength());
        setObject(cmd.getResult());
    }
}
