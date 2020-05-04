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
 * This command is sent to backup any packages that are installed on the Newton.
 * It expects a <tt>kDPackage</tt> command or a <tt>kDResponse</tt> with an error of
 * {@code 0} (to indicate that there are no more packages) in response.
 *
 * <pre>
 * 'bpkg'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DBackupPackages extends DockCommandToNewtonBlank {

    /**
     * <tt>kDBackupPackages</tt>
     */
    public static final String COMMAND = "bpkg";

    /**
     * Creates a new command.
     */
    public DBackupPackages() {
        super(COMMAND);
    }

}
