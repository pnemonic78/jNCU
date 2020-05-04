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

import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command displays the password slip to let the user enter a password. The
 * string is displayed as the title of the slip. A <tt>kDPassword</tt> command
 * is returned.
 *
 * <pre>
 * 'gpwd'
 * length
 * string ref
 * </pre>
 *
 * @author moshew
 */
public class DGetPassword extends DockCommandToNewtonScript<NSOFString> {

    /**
     * <tt>kDGetPassword</tt>
     */
    public static final String COMMAND = "gpwd";

    /**
     * Creates a new command.
     */
    public DGetPassword() {
        super(COMMAND);
    }

}
