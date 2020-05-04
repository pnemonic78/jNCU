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

import java.io.IOException;
import java.io.OutputStream;

/**
 * The entry at the specified key location is returned. <tt>Nil</tt> is returned
 * if there is no entry with the specified key.
 *
 * <pre>
 * 'goto'
 * length
 * cursor id
 * key
 * </pre>
 *
 * @author moshew
 */
public class DCursorGotoKey extends DCursor {

    /**
     * <tt>kDCursorGotoKey</tt>
     */
    public static final String COMMAND = "goto";

    private int key;

    /**
     * Creates a new command.
     */
    public DCursorGotoKey() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getKey(), data);
    }

    /**
     * Get the key location.
     *
     * @return the key.
     */
    public int getKey() {
        return key;
    }

    /**
     * Set the key location.
     *
     * @param key the key.
     */
    public void setKey(int key) {
        this.key = key;
    }

}
