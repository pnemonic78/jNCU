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

/**
 * This command is sent to the desktop after the connection is established using
 * AppleTalk, serial, etc. (when the user taps the "connect" button). The
 * protocol version is the version of the messaging protocol that's being used
 * and should always be set to the number 9 for the version of the protocol
 * defined here.
 *
 * <pre>
 * 'auto'
 * length = 0
 * </pre>
 */
public class DRequestToAutoDock extends DRequestToDock {

    /**
     * <tt>kDRequestToAutoDock</tt>
     */
    public static final String COMMAND = "auto";

    /**
     * Creates a new command.
     */
    public DRequestToAutoDock() {
        super(COMMAND);
    }

}
