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
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is used to negotiate the real protocol version. See
 * <tt>kDDesktopInfo</tt> for more info. The password key is used as part of the
 * password verification.
 *
 * <pre>
 * 'ninf'
 * length
 * protocol version
 * encrypted key
 * </pre>
 */
public class DNewtonInfo extends BaseDockCommandFromNewton {

    /**
     * <tt>kDNewtonInfo</tt>
     */
    public static final String COMMAND = "ninf";

    private int protocolVersion;
    private long encryptedKey;

    /**
     * Creates a new command.
     */
    public DNewtonInfo() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setProtocolVersion(ntohl(data));
        long keyHi = ntohl(data) & 0xFFFFFFFFL;
        long keyLo = ntohl(data) & 0xFFFFFFFFL;
        setEncryptedKey((keyHi << 32) | keyLo);
    }

    /**
     * Get the protocol version.
     *
     * @return the protocol version.
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Set the protocol version.
     *
     * @param protocolVersion the protocol version.
     */
    protected void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
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

}
