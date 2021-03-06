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
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command asks the desktop to import the file specified by the last path
 * command and the filename string. The response to this can be either a list of
 * translators (if there is more than one applicable translator) or an
 * indication that importing is in progress. If the selected item is at the
 * Desktop level, a frame <code>{Name: "Business", whichVol: -1}</code> is sent.
 * Otherwise, a string is sent.
 *
 * <pre>
 * 'impt'
 * length
 * filename string
 * </pre>
 *
 * @author moshew
 */
public class DImportFile extends BaseDockCommandFromNewton {

    /**
     * <tt>kDImportFile</tt>
     */
    public static final String COMMAND = "impt";

    private String filename;

    public DImportFile() {
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
