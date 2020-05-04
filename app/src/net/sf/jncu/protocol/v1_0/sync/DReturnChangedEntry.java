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
package net.sf.jncu.protocol.v1_0.sync;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command is sent when the desktop wants to retrieve a changed entry from
 * the current soup.
 *
 * <pre>
 * 'rcen'
 * length
 * id
 * </pre>
 *
 * @author moshew
 */
public class DReturnChangedEntry extends DockCommandToNewtonLong {

    /**
     * <tt>kDReturnChangedEntry</tt>
     */
    public static final String COMMAND = "rcen";

    /**
     * Creates a new command.
     */
    public DReturnChangedEntry() {
        super(COMMAND);
    }

    /**
     * Get the entry id.
     *
     * @return the id.
     */
    public int getId() {
        return getValue();
    }

    /**
     * Set the entry id.
     *
     * @param id the id.
     */
    public void setId(int id) {
        setValue(id);
    }
}
