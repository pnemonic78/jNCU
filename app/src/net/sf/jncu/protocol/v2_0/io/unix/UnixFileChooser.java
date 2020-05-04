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
package net.sf.jncu.protocol.v2_0.io.unix;

import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.v2_0.io.FileChooser;

import java.awt.Window;
import java.util.Collection;

/**
 * Data source for interacting with the Newton file browser for Unix and Linux
 * operating systems.
 *
 * @author Moshe
 */
public class UnixFileChooser extends FileChooser {

    /**
     * Constructs a new file chooser.
     *
     * @param pipe  the pipe.
     * @param types the chooser types.
     * @param owner the owner window.
     */
    public UnixFileChooser(CDPipe pipe, Collection<NSOFString> types, Window owner) {
        super(pipe, types, owner);
        init();
    }

    /**
     * Constructs a new file chooser.
     *
     * @param pipe  the pipe.
     * @param type  the chooser type.
     * @param owner the owner window.
     */
    public UnixFileChooser(CDPipe pipe, NSOFString type, Window owner) {
        super(pipe, type, owner);
        init();
    }

    /**
     * Initialise.
     */
    private void init() {
    }
}
