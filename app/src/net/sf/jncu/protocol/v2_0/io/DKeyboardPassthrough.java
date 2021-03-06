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

import net.sf.jncu.protocol.BaseDockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewtonBlank;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is sent to enter keyboard pass-through mode. It can be followed
 * by <tt>kDKeyboardChar</tt>, <tt>kDKeyboardString</tt>, <tt>kDHello</tt> and
 * <tt>kDOperationDone</tt> commands.
 *
 * <pre>
 * 'kybd'
 * length
 * </pre>
 *
 * @author moshew
 */
public class DKeyboardPassthrough extends DockCommandToNewtonBlank implements DockCommandFromNewton {

    /**
     * <tt>kDStartKeyboardPassthrough</tt>
     */
    public static final String COMMAND = "kybd";

    /**
     * Creates a new command.
     */
    public DKeyboardPassthrough() {
        super(COMMAND);
    }

    @Override
    public void decode(InputStream data) throws IOException {
        // nothing to decode.
        setLength(BaseDockCommandFromNewton.ntohl(data));
    }

}
