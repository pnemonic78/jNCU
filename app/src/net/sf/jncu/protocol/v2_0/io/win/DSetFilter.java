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
package net.sf.jncu.protocol.v2_0.io.win;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

/**
 * This command changes the current filter being used. A
 * <tt>kDFilesAndFolders</tt> command is expected in return. The index is a long
 * indicating which item in the filters array sent from the desktop should be
 * used as the current filter. Index is 0-based. Windows only.
 *
 * <pre>
 * 'sflt'
 * length
 * index
 * </pre>
 *
 * @author moshew
 */
public class DSetFilter extends BaseDockCommandFromNewton {

    /**
     * <tt>kDSetFilter</tt>
     */
    public static final String COMMAND = "sflt";

    private int index;

    public DSetFilter() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setIndex(ntohl(data));
    }

    /**
     * Get the filter index.
     *
     * @return the index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the filter index.
     *
     * @param index the index.
     */
    protected void setIndex(int index) {
        this.index = index;
    }

}
