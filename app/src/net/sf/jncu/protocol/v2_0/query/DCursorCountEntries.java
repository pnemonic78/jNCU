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
package net.sf.jncu.protocol.v2_0.query;

/**
 * Returns the count of the entries matching the query specification. A
 * <tt>kDLongData</tt> is returned.
 *
 * <pre>
 * 'cnt '
 * length
 * cursor id
 * </pre>
 *
 * @author moshew
 */
public class DCursorCountEntries extends DCursor {

    /**
     * <tt>kDCursorCountEntries</tt>
     */
    public static final String COMMAND = "cnt ";

    /**
     * Creates a new command.
     */
    public DCursorCountEntries() {
        super(COMMAND);
    }

}
