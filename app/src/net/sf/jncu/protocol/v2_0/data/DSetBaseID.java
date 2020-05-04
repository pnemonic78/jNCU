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

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command sets a new base id for the ids sent with subsequent
 * <tt>kDBackupIDs</tt> commands. The new base is a long which should be added
 * to every id in all <tt>kDBackupIDs</tt> commands until a
 * <tt>kDBackupSoupDone</tt> command is received.
 *
 * <pre>
 * 'base'
 * length
 * new base
 * </pre>
 *
 * @author moshew
 */
public class DSetBaseID extends BaseDockCommandFromNewton {

    /**
     * <tt>kDSetBaseID</tt>
     */
    public static final String COMMAND = "base";

    private int id;

    /**
     * Creates a new command.
     */
    public DSetBaseID() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setId(ntohl(data));
    }

    /**
     * Get the base id.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the base id.
     *
     * @param id the id.
     */
    protected void setId(int id) {
        this.id = id;
    }

}
