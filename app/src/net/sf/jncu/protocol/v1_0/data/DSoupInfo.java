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
package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * This command is used to send a soup info frame. When received the info for
 * the current soup is set to the specified frame.
 *
 * <pre>
 * 'sinf'
 * length
 * soup info frame
 * </pre>
 */
public class DSoupInfo extends DockCommandFromNewtonScript<NSOFFrame> implements DockCommandBidi {

    /**
     * <tt>kDSoupInfo</tt><br>
     * <tt>kDSetSoupInfo</tt>
     */
    public static final String COMMAND = "sinf";

    private Soup soup;
    private DockCommandToNewtonScript<NSOFFrame> to;

    /**
     * Creates a new command.
     */
    public DSoupInfo() {
        super(COMMAND);
    }

    /**
     * Get the soup.
     *
     * @return the soup.
     */
    public Soup getSoup() {
        return soup;
    }

    /**
     * Set the soup.
     *
     * @param soup the soup.
     */
    protected void setSoup(Soup soup) {
        this.soup = soup;
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        super.decodeCommandData(data);
        Soup soup = null;
        NSOFFrame frame = getResult();
        if (frame != null) {
            soup = getSoup();
            if (soup == null)
                soup = new Soup("");
            soup.fromFrame(frame);
        }
        setSoup(soup);
    }

    @Override
    public InputStream getCommandPayload() throws IOException {
        if (to == null) {
            to = new DockCommandToNewtonScript<NSOFFrame>(COMMAND) {
            };
        }
        return to.getCommandPayload();
    }

    @Override
    public int getCommandPayloadLength() throws IOException {
        return to.getCommandPayloadLength();
    }

}
