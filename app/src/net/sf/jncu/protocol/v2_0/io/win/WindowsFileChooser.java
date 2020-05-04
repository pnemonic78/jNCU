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
package net.sf.jncu.protocol.v2_0.io.win;

import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.v2_0.io.DPath;
import net.sf.jncu.protocol.v2_0.io.FileChooser;

import java.awt.Window;
import java.io.File;
import java.util.Collection;

/**
 * Data source for interacting with the Newton file browser for Windows
 * operating systems.
 *
 * @author Moshe
 */
public class WindowsFileChooser extends FileChooser {

    /**
     * Constructs a new file chooser.
     *
     * @param pipe  the pipe.
     * @param types the chooser types.
     * @param owner the owner window.
     */
    public WindowsFileChooser(CDPipe pipe, Collection<NSOFString> types, Window owner) {
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
    public WindowsFileChooser(CDPipe pipe, NSOFString type, Window owner) {
        super(pipe, type, owner);
        init();
    }

    /**
     * Initialise.
     */
    private void init() {
    }

    @Override
    public void commandReceived(DockCommandFromNewton command) {
        if (!isEnabled())
            return;

        super.commandReceived(command);

        final String cmd = command.getCommand();

        if (DGetDevices.COMMAND.equals(cmd)) {
            DDevices cmdDevices = new DDevices();
            write(cmdDevices);
        } else if (DGetFilters.COMMAND.equals(cmd)) {
            DFilters cmdFilters = new DFilters();
            cmdFilters.addFilters(getFilters());
            write(cmdFilters);
        } else if (DSetDrive.COMMAND.equals(cmd)) {
            DSetDrive cmdSet = (DSetDrive) command;
            setPath(new File(cmdSet.getDrive()));
            DPath cmdPath = new DPath();
            cmdPath.setPath(getPath());
            write(cmdPath);
        } else if (DSetFilter.COMMAND.equals(cmd)) {
            DSetFilter cmdSet = (DSetFilter) command;
            int index = cmdSet.getIndex();
            setFilter(getFilters().get(index));
            sendFiles();
        }
    }

}
