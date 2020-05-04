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
package net.sf.jncu.protocol.v1_0.session;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * Ask desktop to start docking process.<br>
 * This command is sent to a docker that the junior wishes to connect with (on
 * the network, serial, etc.). The Newt expects a <tt>kDInitiateDocking</tt>
 * command in response. The protocol version is the version of the messaging
 * protocol that's being used.
 *
 * <pre>
 * 'rtdk'
 * length
 * protocol version
 * </pre>
 */
public class DRequestToDock extends BaseDockCommandFromNewton {

    /**
     * <tt>kDRequestToDock</tt>
     */
    public static final String COMMAND = "rtdk";

    private int protocol;

    /**
     * Creates a new command.
     */
    public DRequestToDock() {
        super(COMMAND);
    }

    /**
     * Creates a new command.
     *
     * @param cmd the command.
     */
    protected DRequestToDock(String cmd) {
        super(cmd);
    }

    /**
     * Get the protocol version.
     *
     * @return the protocol version.
     */
    public int getProtocol() {
        return protocol;
    }

    /**
     * Set the protocol version.
     *
     * @param protocol the protocol version.
     */
    protected void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setProtocol(ntohl(data));
    }

}
