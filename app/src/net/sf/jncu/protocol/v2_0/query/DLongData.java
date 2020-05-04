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
import java.io.InputStream;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

/**
 * Newton returns a long value. The interpretation of the data depends on the
 * command which prompted the return of the long value.
 *
 * <pre>
 * 'ldta'
 * length
 * data
 * </pre>
 *
 * @author moshew
 */
public class DLongData extends BaseDockCommandFromNewton {

    /**
     * <tt>kDLongData</tt>
     */
    public static final String COMMAND = "ldta";

    private int data;

    /**
     * Creates a new command.
     */
    public DLongData() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setData(ntohl(data));
    }

    /**
     * Get the data.
     *
     * @return the data.
     */
    public int getData() {
        return data;
    }

    /**
     * Set the data.
     *
     * @param data the data.
     */
    public void setData(int data) {
        this.data = data;
    }

}
