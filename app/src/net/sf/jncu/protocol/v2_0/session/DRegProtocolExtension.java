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
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This command installs a protocol extension into the Newton. The extension
 * lasts for the length of the current connection (in other words, you have to
 * install the extension every time you connect). The function is a Newton
 * script closure that would have to be compiled on the desktop. See the Dante
 * Connection (ROM) API IU document for details. A <tt>kDResult</tt> with value
 * {@code 0} (or the error value if an error occurred) is sent to the desktop in
 * response.
 *
 * <pre>
 * 'pext'
 * length
 * command
 * function
 * </pre>
 *
 * @author moshew
 */
public class DRegProtocolExtension extends BaseDockCommandToNewton {

    /**
     * <tt>kDRegProtocolExtension</tt>
     */
    public static final String COMMAND = "pext";

    private int extension;
    private NSOFObject function;

    /**
     * Creates a new command.
     */
    public DRegProtocolExtension() {
        super(COMMAND);
    }

    /**
     * Get the extension id.
     *
     * @return the command.
     */
    public int getExtension() {
        return extension;
    }

    /**
     * Set the extension id.
     *
     * @param extension the command.
     */
    public void setExtension(int extension) {
        this.extension = extension;
    }

    /**
     * Get the function.
     *
     * @return the function.
     */
    public NSOFObject getFunction() {
        return function;
    }

    /**
     * Set the function.
     *
     * @param function the function.
     */
    public void setFunction(NSOFObject function) {
        this.function = function;
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getExtension(), data);
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(getFunction(), data);
    }
}
