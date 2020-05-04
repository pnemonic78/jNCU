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
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command requests that all of the entries in a soup be returned to the
 * desktop. The Newton responds with a series of <tt>kDEntry</tt> commands for
 * all the entries in the current soup followed by a <tt>kDBackupSoupDone</tt>
 * command. All of the entries are sent without any request from the desktop (in
 * other words, a series of commands is sent). The process can be interrupted by
 * the desktop by sending a <tt>kDOperationCanceled</tt> command. The cancel
 * will be detected between entries. The <tt>kDEntry</tt> commands are sent
 * exactly as if they had been requested by a <tt>kDReturnEntry</tt> command
 * (they are long padded).
 *
 * <pre>
 * 'snds'
 * length
 * </pre>
 *
 * @author moshew
 */
public class DSendSoup extends DockCommandToNewtonBlank {

    /**
     * <tt>kDSendSoup</tt>
     */
    public static final String COMMAND = "snds";

    /**
     * Creates a new command.
     */
    public DSendSoup() {
        super(COMMAND);
    }

}
