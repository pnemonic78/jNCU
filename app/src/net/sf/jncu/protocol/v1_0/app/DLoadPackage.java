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
package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This command will load a package into the Newton's RAM. The package data
 * should be padded to an even multiple of 4 by adding zero bytes to the end of
 * the package data.
 *
 * <pre>
 * 'lpkg'
 * length
 * package data
 * </pre>
 *
 * @author Moshe
 */
public class DLoadPackage extends BaseDockCommandToNewton {

    /**
     * <tt>kDLoadPackage</tt>
     */
    public static final String COMMAND = "lpkg";

    private File file;

    /**
     * Creates a new command.
     */
    public DLoadPackage() {
        super(COMMAND);
    }

    @Override
    protected InputStream getCommandData() throws IOException {
        File file = getFile();
        if (file == null)
            return null;

        if (file.length() < 8L)
            throw new PackageException("package size too small");
        InputStream in = null;

        // Check that the file header starts with "package"
        byte[] buf = new byte[7];
        try {
            in = new FileInputStream(file);
            in.read(buf);
            if ((buf[0] != 'p') || (buf[1] != 'a') || (buf[2] != 'c') || (buf[3] != 'k') || (buf[4] != 'a') || (buf[5] != 'g') || (buf[6] != 'e'))
                throw new PackageException("package header must start with 'package'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        in = new FileInputStream(file);

        return in;
    }

    /**
     * Get the package file.
     *
     * @return the file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Set the package file.
     *
     * @param file the file.
     */
    public void setFile(File file) {
        this.file = file;
        if (file == null)
            setLength(0);
        else
            setLength((int) file.length());
    }

}
