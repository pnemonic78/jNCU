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
 * This command is sent to let the Newton know that an import operation is
 * starting. The Newton will display an appropriate message after it gets this
 * message.
 *
 * <pre>
 * 'dimp'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DImporting extends DockCommandToNewtonBlank {

    /**
     * <tt>kDImporting</tt>
     */
    public static final String COMMAND = "dimp";

    /**
     * Constructs a new command.
     */
    public DImporting() {
        super(COMMAND);
    }

}
