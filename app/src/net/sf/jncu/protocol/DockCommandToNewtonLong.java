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
package net.sf.jncu.protocol;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Docking command to the Newton with a single 4-byte long number.
 *
 * @author moshe
 */
public abstract class DockCommandToNewtonLong extends BaseDockCommandToNewton {

    private int value;

    /**
     * Constructs a new command.
     *
     * @param cmd the command.
     */
    public DockCommandToNewtonLong(String cmd) {
        super(cmd);
        setLength(4);
    }

    /**
     * Constructs a new command.
     *
     * @param cmd the command.
     */
    public DockCommandToNewtonLong(byte[] cmd) {
        super(cmd);
        setLength(4);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getValue(), data);
    }

    /**
     * Get the value.
     *
     * @return the value.
     */
    protected int getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value the value.
     */
    protected void setValue(int value) {
        this.value = value;
    }
}
