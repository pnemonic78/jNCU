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
package net.sf.jncu.protocol;

import net.sf.jncu.protocol.v1_0.app.DLoadPackage;

import java.io.IOException;
import java.io.InputStream;

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
    InputStream getCommandPayload() throws IOException;

    /**
     * Get the command payload length.
     *
     * @return the payload length.
     * @throws IOException if an I/O error occurs.
     */
    int getCommandPayloadLength() throws IOException;
}
