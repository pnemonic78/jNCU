/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * http://sourceforge.net/projects/jncu
 *
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
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
