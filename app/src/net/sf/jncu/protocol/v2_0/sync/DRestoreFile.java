/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jncu.protocol.v2_0.sync;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command asks the desktop to restore the file specified by the last path
 * command and the filename. If the selected item is at the Desktop level, a
 * frame <code>{name: "Business", whichVol:-1}</code> is sent. Otherwise, a
 * string is sent.
 *
 * <pre>
 * 'rsfl'
 * length
 * filename
 * </pre>
 *
 * @author moshew
 */
public class DRestoreFile extends BaseDockCommandFromNewton {

    /**
     * <tt>kDRestoreFile</tt>
     */
    public static final String COMMAND = "rsfl";

    private String filename;

    /**
     * Creates a new command.
     */
    public DRestoreFile() {
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
