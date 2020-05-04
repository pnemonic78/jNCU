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
 * Docking Command interface.
 *
 * @author moshew
 */
public interface DockCommand {

    /**
     * Get the command.
     *
     * @return the command.
     */
    String getCommand();

    /**
     * Get the length.
     *
     * @return the length. Default value is {@code 0}.
     */
    int getLength();

    /**
     * Number of bytes for a word.
     */
    int LENGTH_WORD = 4;

    /**
     * False.
     */
    int FALSE = 0;
    /**
     * True.
     */
    int TRUE = 1;

    /**
     * Command prefix.<br>
     * <tt>kDNewtonDock</tt>
     */
    String COMMAND_PREFIX = "newtdock";
    /**
     * Command prefix length.<br>
     * <tt>kDNewtonDockLength</tt>
     */
    int COMMAND_PREFIX_LENGTH = COMMAND_PREFIX.length();
    /**
     * Number of characters for command name.
     */
    int COMMAND_NAME_LENGTH = LENGTH_WORD;

    /**
     * Length of a docking command header.<br>
     * <ol>
     * <li>'newtdock' = 8
     * <li>command name = 4
     * <li>data length = 4
     * </ol>
     */
    int COMMAND_HEADER_LENGTH = COMMAND_PREFIX_LENGTH + COMMAND_NAME_LENGTH + 4;
}
