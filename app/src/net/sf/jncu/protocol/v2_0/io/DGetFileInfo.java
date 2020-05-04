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

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

/**
 * This command asks the desktop to return info about the specified file. See
 * <tt>kDFileInfo</tt> for info about what's returned.
 * <p>
 * If the selected item is at the Desktop level, a frame
 * <code>{Name:"Business", whichVol:-1}</code> will be sent instead of the
 * string to indicate the <tt>volRefNum</tt> for the file.
 *
 * <pre>
 * 'gfin'
 * length
 * filename string
 * </pre>
 *
 * @author moshew
 */
public class DGetFileInfo extends BaseDockCommandFromNewton {

    /**
     * <tt>kDGetFileInfo</tt>
     */
    public static final String COMMAND = "gfin";

    private String filename;

    public DGetFileInfo() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setFilename((String) null);
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFString name = (NSOFString) decoder.inflate(data);
        setFilename(name);
    }

    /**
     * Get the file name.
     *
     * @return the file name.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the file name.
     *
     * @param filename the file name.
     */
    protected void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Set the file name.
     *
     * @param filename the file name.
     */
    protected void setFilename(NSOFString filename) {
        if (filename == null)
            setFilename((String) null);
        else
            setFilename(filename.getValue());
    }
}
