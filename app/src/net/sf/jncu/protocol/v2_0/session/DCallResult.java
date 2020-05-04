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

import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * This command is sent in response to a <tt>kDCallGlobalfunction</tt> or
 * <tt>kDCallRootMethod</tt> command. The ref is the return value from the
 * function or method called.
 *
 * <pre>
 * 'cres'
 * length
 * ref
 * </pre>
 *
 * @author moshew
 */
public class DCallResult extends DockCommandFromNewtonScript<NSOFObject> {

    /**
     * <tt>kDCallResult</tt>
     */
    public static final String COMMAND = "cres";

    /**
     * Creates a new command.
     */
    public DCallResult() {
        super(COMMAND);
    }

}
