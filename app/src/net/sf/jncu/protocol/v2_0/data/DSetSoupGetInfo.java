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
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.v1_0.data.DSetCurrentSoup;

/**
 * This command is like a combination of <tt>kDSetCurrentSoup</tt> and
 * <tt>kDGetChangedInfo</tt>. It sets the current soup -- see
 * <tt>kDSetCurrentSoup</tt> for details. A <tt>kDSoupInfo</tt> or
 * <tt>kDRes</tt> command is sent by the Newton in response.
 *
 * <pre>
 * 'ssgi'
 * length
 * soup name
 * </pre>
 *
 * @author moshew
 */
public class DSetSoupGetInfo extends DSetCurrentSoup {

    /**
     * <tt>kDSetSoupGetInfo</tt>
     */
    public static final String COMMAND = "ssgi";

    /**
     * Creates a new command.
     */
    public DSetSoupGetInfo() {
        super(COMMAND);
    }

}
