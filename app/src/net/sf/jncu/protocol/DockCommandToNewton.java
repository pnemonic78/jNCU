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
import java.io.InputStream;

import net.sf.jncu.protocol.v1_0.app.DLoadPackage;

/**
 * Docking command interface from Newton to desktop.
 *
 * @author moshew
 */
public interface DockCommandToNewton extends DockCommand {

    /**
     * Get the command payload to send.
     * <p>
     * Useful primarily for extra-long commands, for example
     * {@link DLoadPackage}, where we write much data.
     *
     * @return the payload.
     * @throws IOException if an I/O error occurs.
     */
    public InputStream getCommandPayload() throws IOException;

    /**
     * Get the command payload length.
     *
     * @return the payload length.
     * @throws IOException if an I/O error occurs.
     */
    public int getCommandPayloadLength() throws IOException;
}
