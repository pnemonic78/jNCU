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
import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.protocol.BaseDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.io.Device;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This command returns the initial strings for the folder pop-up in the
 * Macintosh version of the window and for the directories list in the Windows
 * version. It is also returned after the user taps on a folder alias. In this
 * case the path must be changed to reflect the new location. Each element of
 * the array is a frame that takes this form:<br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "my hard disk",<br>
 * &nbsp;&nbsp;type: disk,<br>
 * &nbsp;&nbsp;disktype: harddrive,<br>
 * &nbsp;&nbsp;whichVol: 0,			// Optional - see below<br>
 * }</code><br>
 * <p>
 * The possible values for type are (desktop = 0, file = 1, folder = 2, disk =
 * 3). If the type is disk, there is an additional slot <tt>disktype</tt> with
 * the values (floppy = 0, hardDrive = 1, cdRom = 2, netDrive = 3). Finally, for
 * the second frame in the array i.e. the one after Desktop, there will be an
 * additional slot <tt>whichvol</tt> , which will be a {@code 0} if the item is
 * disk or a <tt>volRefNum</tt> if the item is a folder on the desktop.
 * <p>
 * For example, the Macintosh might send:<br>
 * <code>[{name: "desktop", type: desktop}, {name: "my hard disk", type: disk, disktype: harddrive, whichvol: 0}, {name: "business", type: folder}]</code>
 * <br>
 * or for some folder on the desktop it might send:<br>
 * <code>[{name: "desktop", type: desktop}, {name: "business", type: folder, whichvol: -1}, {name: "my folder", type: folder}]</code>
 * <p>
 * For Windows it might be: [{name: "c:\", type: 'folder}, {name: "business",
 * type: 'folder}]
 *
 * <pre>
 * 'path'
 * length
 * folder array
 * </pre>
 *
 * @author moshew
 */
public class DPath extends BaseDockCommandToNewton {

    /**
     * <tt>kDPath</tt>
     */
    public static final String COMMAND = "path";

    private File file;

    /**
     * Creates a new command.
     */
    public DPath() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        File file = getPath();
        if (file != null) {
            final List<Device> devices = new ArrayList<Device>();
            Device device;
            String path = file.getPath();
            while (path != null) {
                file = new File(path);
                device = new Device(file);
                devices.add(0, device);
                path = file.getParent();
            }

            NSOFObject[] paths = new NSOFObject[devices.size()];
            int i = 0;
            for (Device dev : devices) {
                paths[i++] = dev.toFrame();
            }
            NSOFArray arr = new NSOFPlainArray(paths);
            NSOFEncoder encoder = new NSOFEncoder();
            encoder.flatten(arr, data);
        }
    }

    /**
     * Get the initial path.
     *
     * @return the folder.
     */
    public File getPath() {
        return file;
    }

    /**
     * Set the initial path.
     *
     * @param file the folder.
     */
    public void setPath(File file) {
        this.file = file;
    }

}
