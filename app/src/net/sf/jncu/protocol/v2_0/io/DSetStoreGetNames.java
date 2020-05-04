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

import net.sf.jncu.protocol.v1_0.io.DSetCurrentStore;


/**
 * This command is the same as <tt>kDSetCurrentStore</tt> except that it returns
 * the names of the soups on the stores as if you'd send a
 * <tt>kDGetSoupNames</tt> command. It sets the current store on the Newton. A
 * store frame is sent to uniquely identify the store to be set: <br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "foo",<br>
 * &nbsp;&nbsp;kind: "bar",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;info: {&lt;info frame&gt;}		// This one is optional<br>
 * }</code>
 * <br>
 * A <tt>kDSoupNames</tt> is sent by the Newton in response.
 *
 * <pre>
 * 'ssgn'
 * length
 * store frame
 * </pre>
 *
 * @author moshew
 */
public class DSetStoreGetNames extends DSetCurrentStore {

    /**
     * <tt>kDSetStoreGetNames</tt>
     */
    public static final String COMMAND = "ssgn";

    /**
     * Creates a new command.
     */
    public DSetStoreGetNames() {
        super(COMMAND);
    }

}
