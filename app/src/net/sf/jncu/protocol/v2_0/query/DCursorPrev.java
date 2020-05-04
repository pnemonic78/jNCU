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
 * Moves the cursor to the previous entry in the set of entries referenced by
 * the cursor and returns the entry. If the cursor is moved before the first
 * entry nil is returned.
 *
 * <pre>
 * 'prev'
 * length
 * cursor id
 * </pre>
 *
 * @author moshew
 */
public class DCursorPrev extends DCursor {

    /**
     * <tt>kDCursorPrev</tt>
     */
    public static final String COMMAND = "prev";

    /**
     * Creates a new command.
     */
    public DCursorPrev() {
        super(COMMAND);
    }

}
