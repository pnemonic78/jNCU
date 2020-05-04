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
package net.sf.jncu.protocol.v2_1.session;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.BaseDockCommandToNewton;

/**
 * This command is sent to set the status text.
 *
 * <pre>
 * 'stxt'
 * length
 * status text
 * </pre>
 *
 * @author moshew
 */
public class DSetStatusText extends BaseDockCommandToNewton {

    /**
     * <tt>kDSetStatusText</tt>
     */
    public static final String COMMAND = "stxt";

    private String status;

    /**
     * Creates a new command.
     */
    public DSetStatusText() {
        super(COMMAND);
    }

    /**
     * Get the bad command.
     *
     * @return the bad command.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the bad command.
     *
     * @param status the status text.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        BaseDockCommandToNewton.writeString(getStatus(), data);
    }

    @Override
    public String toString() {
        return "Status: '" + getStatus() + "'";
    }
}
