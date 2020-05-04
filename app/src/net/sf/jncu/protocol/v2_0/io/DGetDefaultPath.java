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

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * This commands requests the desktop system to return the default path. This is
 * the list that goes in the folder pop-up for the Macintosh and in the
 * directories list for Windows.
 *
 * <pre>
 * 'dpth'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DGetDefaultPath extends DockCommandFromNewtonBlank {

    /**
     * <tt>kDGetDefaultPath</tt>
     */
    public static final String COMMAND = "dpth";

    public DGetDefaultPath() {
        super(COMMAND);
    }

}
