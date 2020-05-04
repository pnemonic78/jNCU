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

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This command asks the desktop to change the drive on the desktop and set the
 * directory to the current directory for that drive. The string contains the
 * drive letter followed by a colon e.g. "<tt>C:</tt>". Windows only.
 *
 * <pre>
 * 'sdrv'
 * length
 * drive string
 * </pre>
 *
 * @author moshew
 */
public class DSetDrive extends BaseDockCommandFromNewton {

    /**
     * <tt>kDSetDrive</tt>
     */
    public static final String COMMAND = "sdrv";

    private String drive;

    public DSetDrive() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setDrive((String) null);
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFString drive = (NSOFString) decoder.inflate(data);
        setDrive(drive);
    }

    /**
     * Get the drive.
     *
     * @return the drive.
     */
    public String getDrive() {
        return drive;
    }

    /**
     * Set the drive.
     *
     * @param drive the drive.
     */
    protected void setDrive(String drive) {
        if (drive != null) {
            if (drive.endsWith(":")) {
                drive += File.separatorChar;
            }
        }
        this.drive = drive;
    }

    /**
     * Set the drive.
     *
     * @param drive the drive.
     */
    protected void setDrive(NSOFString drive) {
        if (drive == null) {
            setDrive((String) null);
        } else {
            setDrive(drive.getValue());
        }
    }
}
