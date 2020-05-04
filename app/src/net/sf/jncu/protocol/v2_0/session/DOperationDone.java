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

import net.sf.jncu.protocol.BaseDockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command is sent when an operation is completed. It's only sent in
 * situations where there might be some ambiguity. Currently, there are two
 * situations where this is sent. When the desktop finishes a restore it sends
 * this command. When a sync is finished and there are no sync results
 * (conflicts) to send to the Newton the desktop sends this command.
 *
 * <pre>
 * 'opdn'
 * length = 0
 * </pre>
 */
public class DOperationDone extends DockCommandToNewtonBlank implements DockCommandBidi {

    /**
     * <tt>kDOperationDone</tt>
     */
    public static final String COMMAND = "opdn";

    /**
     * Creates a new command.
     */
    public DOperationDone() {
        super(COMMAND);
    }

    @Override
    public void decode(InputStream data) throws IOException {
        // nothing to decode.
        setLength(BaseDockCommandFromNewton.ntohl(data));
    }

}
