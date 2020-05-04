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
package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;
import net.sf.jncu.protocol.BaseDockCommandToNewton;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * This command returns the key received in the <tt>kDInitiateDocking</tt>
 * message encrypted using the password.
 *
 * <pre>
 * 'pass'
 * length
 * encrypted key
 * </pre>
 *
 * @author moshew
 */
public class DPassword extends BaseDockCommandFromNewton implements DockCommandToNewton {

    /**
     * <tt>kDPassword</tt>
     */
    public static final String COMMAND = "pass";

    /**
     * "Bad password error".
     */
    public static final int ERROR_BAD_PASSWORD = -28022;
    /**
     * "Password retry".
     */
    public static final int ERROR_RETRY_PASSWORD = -28023;

    private long encryptedKey;
    private DockCommandToNewton to;

    /**
     * Creates a new command.
     */
    public DPassword() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        long keyHi = ntohl(data) & 0xFFFFFFFFL;
        long keyLo = ntohl(data) & 0xFFFFFFFFL;
        setEncryptedKey((keyHi << 32) | keyLo);
    }

    /**
     * Get the encrypted key.
     *
     * @return the encrypted key.
     */
    public long getEncryptedKey() {
        return encryptedKey;
    }

    /**
     * Set the encrypted key.
     *
     * @param encryptedKey the encrypted key.
     */
    protected void setEncryptedKey(long encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    @Override
    public InputStream getCommandPayload() throws IOException {
        if (to == null) {
            to = new BaseDockCommandToNewton(COMMAND) {

                @Override
                public int getLength() {
                    return 8;
                }

                @Override
                protected void writeCommandData(OutputStream data) throws IOException {
                    htonl(getEncryptedKey(), data);
                }
            };
        }
        return to.getCommandPayload();
    }

    @Override
    public int getCommandPayloadLength() throws IOException {
        return to.getCommandPayloadLength();
    }
}
