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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This command tells the desktop that the user has changed the path. The
 * desktop responds with a new list of files and folders. The path is sent as an
 * array of strings rather than an array of frames as all of the other commands
 * are for performance reasons. For the Macintosh, the array would be something
 * like: <code>["Desktop",{Name:"My hard disk", whichVol:0}, "Business"]</code>
 * to set the path to "<tt>My hard disk:business:</tt>". " <tt>Desktop</tt>"
 * will always be at the start of the list, since that's the way Standard File
 * works. So if the user wanted to set the path to somewhere in the Desktop
 * Folder he would send something like
 * <code>["Desktop",{Name:"Business", whichVol:-1}]</code> to set the path to "
 * <tt>My hard disk:Desktop Folder:business:</tt>"
 * <p>
 * The second item in the array, will always be a frame instead of a string and
 * will contain an additional slot "<tt>whichvol</tt>" to indicate to the
 * desktop whether that item is a name of a volume or a folder in the Desktop
 * Folder and if so it's <tt>volRefNum</tt>.
 * <p>
 * For Windows the array would be something like:
 * <code>["c:\", "business"]</code> to set the path to "<tt>c:\business</tt>".
 *
 * <pre>
 * 'spth'
 * length
 * array of strings
 * </pre>
 *
 * @author moshew
 */
public class DSetPath extends BaseDockCommandFromNewton {

    /**
     * <tt>kDSetPath</tt>
     */
    public static final String COMMAND = "spth";

    private File path;

    public DSetPath() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setPath(null);
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFArray arr = (NSOFArray) decoder.inflate(data);
        NSOFObject[] entries = arr.getValue();
        NSOFString path = (NSOFString) entries[0];
        File file = new File(path.getValue() + File.separatorChar);
        for (int i = 1; i < entries.length; i++) {
            path = (NSOFString) entries[i];
            file = new File(file, path.getValue() + File.separatorChar);
        }
        setPath(file);
    }

    /**
     * Get the file path.
     *
     * @return the path.
     */
    public File getPath() {
        return path;
    }

    /**
     * Set the file path. This is supposed to be a folder.
     *
     * @param path the path.
     */
    protected void setPath(File path) {
        this.path = path;
    }

}
