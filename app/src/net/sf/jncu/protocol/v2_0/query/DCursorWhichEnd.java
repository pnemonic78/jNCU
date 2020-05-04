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
 * Returns <tt>kDLongData</tt> with a {@code 0} for unknown, {@code 1} for start
 * and {@code 2} for end.
 *
 * <pre>
 * 'whch'
 * length
 * cursor id
 * </pre>
 *
 * @author moshew
 */
public class DCursorWhichEnd extends DCursor {

    /**
     * <tt>kDCursorWhichEnd</tt>
     */
    public static final String COMMAND = "whch";

    /**
     * <tt>eCursorWhichEnd</tt>
     */
    public enum CursorWhichEnd {
        /**
         * Cursor is at unknown position.
         */
        UNKNOWN,
        /**
         * Cursor is at start position.
         */
        START,
        /**
         * Cursor is at end position.
         */
        END
    }

    /**
     * Creates a new command.
     */
    public DCursorWhichEnd() {
        super(COMMAND);
    }

}
