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

/**
 * Docking command listener interface.
 *
 * @author moshew
 */
public interface DockCommandListener {

    /**
     * Notification that a command was received.
     *
     * @param command the command.
     */
    void commandReceived(DockCommandFromNewton command);

    /**
     * Notification that a command is being received.<br>
     * Used mainly to show a progress bar.
     * <p>
     * <em>If the command is small then this method might never be called.</em>
     *
     * @param command  the command.
     * @param progress the number of bytes received.
     * @param total    the total number of bytes to receive.
     */
    void commandReceiving(DockCommandFromNewton command, int progress, int total);

    /**
     * Notification that a command was sent.
     *
     * @param command the command.
     */
    void commandSent(DockCommandToNewton command);

    /**
     * Notification that a command is being received.<br>
     * Used mainly to show a progress bar.
     * <p>
     * <em>If the command is small then this method might never be called.</em>
     *
     * @param command  the command.
     * @param progress the number of bytes sent.
     * @param total    the total number of bytes to send.
     */
    void commandSending(DockCommandToNewton command, int progress, int total);

    /**
     * Notification that no more commands will be available.
     */
    void commandEOF();

}
