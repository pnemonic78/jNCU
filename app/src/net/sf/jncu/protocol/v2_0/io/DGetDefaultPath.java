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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandFromNewtonBlank;

/**
 * This commands requests the desktop system to return the default path. This is
 * the list that goes in the folder pop-up for the Macintosh and in the
 * directories list for Windows.
 *
 * <pre>
 * 'dpth'
 * length = 0
 * </pre>
 *
 * @author moshew
 */
public class DGetDefaultPath extends DockCommandFromNewtonBlank {

    /**
     * <tt>kDGetDefaultPath</tt>
     */
    public static final String COMMAND = "dpth";

    public DGetDefaultPath() {
        super(COMMAND);
    }

}
