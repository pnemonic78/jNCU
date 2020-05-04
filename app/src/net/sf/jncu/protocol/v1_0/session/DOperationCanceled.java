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
package net.sf.jncu.protocol.v1_0.session;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.DockCommandToNewtonBlank;

/**
 * This command is sent when the user cancels an operation. Usually no action is
 * required on the receivers part except to return to the "ready" state.
 *
 * <pre>
 * 'opca'
 * length = 0
 * </pre>
 */
public class DOperationCanceled extends DockCommandToNewtonBlank implements DockCommandBidi {

    /**
     * <tt>kDOperationCanceled</tt>
     */
    public static final String COMMAND = "opca";

    /**
     * Creates a new command.
     */
    public DOperationCanceled() {
        this(COMMAND);
    }

    /**
     * Creates a new command.
     *
     * @param cmd the command.
     */
    protected DOperationCanceled(String cmd) {
        super(cmd);
    }

    @Override
    public void decode(InputStream data) throws IOException {
        // nothing to decode.
        setLength(BaseDockCommandFromNewton.ntohl(data));
    }

}
