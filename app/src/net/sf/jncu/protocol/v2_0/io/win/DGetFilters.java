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
package net.sf.jncu.protocol.v2_0.io.win;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * This command asks the desktop to send a list of filters to display in the
 * Windows file browser. A <tt>kDFilters</tt> command is expected in response.
 *
 * <pre>
 * 'gflt'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DGetFilters extends DockCommandFromNewtonBlank {

    /**
     * <tt>kDGetFilters</tt>
     */
    public static final String COMMAND = "gflt";

    public DGetFilters() {
        super(COMMAND);
    }

}
