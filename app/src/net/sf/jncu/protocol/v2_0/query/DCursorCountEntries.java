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
