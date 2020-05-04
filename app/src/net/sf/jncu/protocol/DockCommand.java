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
