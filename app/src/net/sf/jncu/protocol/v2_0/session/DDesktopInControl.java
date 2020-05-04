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

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * Desktop is in control while connected.
 *
 * <pre>
 * 'dsnc'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DDesktopInControl extends DockCommandToNewtonBlank {

    /**
     * <tt>kDDesktopInControl</tt>
     */
    public static final String COMMAND = "dsnc";

    /**
     * Creates a new command.
     */
    public DDesktopInControl() {
        super(COMMAND);
    }
}
