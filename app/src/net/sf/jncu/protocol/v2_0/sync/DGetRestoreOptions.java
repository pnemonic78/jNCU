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
package net.sf.jncu.protocol.v2_0.sync;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is sent to the desktop if the user wants to do a selective
 * restore. The desktop should return a <tt>kDRestoreOptions</tt> command.
 *
 * <pre>
 * 'grop'
 * length
 * </pre>
 *
 * @author moshew
 */
public class DGetRestoreOptions extends BaseDockCommandFromNewton {

    /**
     * <tt>kDGetRestoreOptions</tt>
     */
    public static final String COMMAND = "grop";

    /**
     * Creates a new command.
     */
    public DGetRestoreOptions() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
    }

}
