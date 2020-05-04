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
