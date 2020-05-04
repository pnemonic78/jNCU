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
package net.sf.jncu.protocol.v2_0;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;
import net.sf.jncu.protocol.BaseDockCommandToNewton;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.DockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This command is sent when a message is received that is unknown. When the
 * desktop receives this command it can either install a protocol extension and
 * try again or return an error to the Newton. If the built-in Newton code
 * receives this command it always signals an error. The bad command parameter
 * is the 4 char command that wasn't recognised. The data is not returned.
 *
 * <pre>
 * 'unkn'
 * length
 * bad command
 * </pre>
 *
 * @author moshew
 */
public class DUnknownCommand extends BaseDockCommandToNewton implements DockCommandBidi {

    /**
     * <tt>kDUnknownCommand</tt>
     */
    public static final String COMMAND = "unkn";

    private String badCommand;

    /**
     * Creates a new command.
     */
    public DUnknownCommand() {
        super(COMMAND);
    }

    /**
     * Get the bad command.
     *
     * @return the bad command.
     */
    public String getBadCommand() {
        return badCommand;
    }

    /**
     * Set the bad command.
     *
     * @param badCommand the bad command.
     */
    public void setBadCommand(String badCommand) {
        this.badCommand = badCommand;
    }

    /**
     * Set the bad command.
     *
     * @param badCommand the bad command.
     */
    public void setBadCommand(byte[] badCommand) {
        setBadCommand(new String(badCommand));
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        char[] cmdName = getBadCommand().toCharArray();
        data.write(cmdName[0] & 0xFF);
        data.write(cmdName[1] & 0xFF);
        data.write(cmdName[2] & 0xFF);
        data.write(cmdName[3] & 0xFF);
    }

    @Override
    public void decode(InputStream data) throws IOException {
        DockCommandFromNewton cmd = new BaseDockCommandFromNewton(COMMAND) {

            @Override
            protected void decodeCommandData(InputStream data) throws IOException {
                char[] cmdName = new char[getLength()];
                cmdName[0] = (char) (data.read() & 0xFF);
                cmdName[1] = (char) (data.read() & 0xFF);
                cmdName[2] = (char) (data.read() & 0xFF);
                cmdName[3] = (char) (data.read() & 0xFF);

                setBadCommand(new String(cmdName));
            }
        };
        cmd.decode(data);
    }

    @Override
    public String toString() {
        return "Unknown command: '" + getBadCommand() + "'";
    }
}
