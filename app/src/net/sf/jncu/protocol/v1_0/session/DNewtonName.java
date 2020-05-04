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
package net.sf.jncu.protocol.v1_0.session;

import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * The name of the Newton.<br>
 * This command is sent in response to a correct <tt>kDInitiateDocking</tt>
 * command from the docker. The Newton's name is used to locate the proper
 * synchronise file. The version info includes things like machine type (e.g.
 * J1), ROM version, etc.
 *
 * <pre>
 * 'name'
 * length
 * version info
 * name
 * </pre>
 */
public class DNewtonName extends BaseDockCommandFromNewton {

    /**
     * <tt>kDNewtonName</tt>
     */
    public static final String COMMAND = "name";

    private NewtonInfo info;
    protected int versionInfoLength;

    /**
     * Creates a new command.
     */
    public DNewtonName() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setInformation(null);
        setName(null);
        versionInfoLength = ntohl(data);
        // Clone the information buffer in case not all fields are read and we
        // still need to reach the name.
        byte[] infoBuf = new byte[versionInfoLength];
        readAll(data, infoBuf);
        setInformation(decodeInfo(new ByteArrayInputStream(infoBuf)));
        setName(decodeName(data));
    }

    /**
     * Decode the version information.
     *
     * @param data the data.
     * @throws IOException if an I/O error occurs.
     */
    protected NewtonInfo decodeInfo(InputStream data) throws IOException {
        NewtonInfo info = new NewtonInfo();
        info.setNewtonId(ntohl(data)); // #1
        info.setManufacturerId(ntohl(data)); // #2
        info.setMachineType(ntohl(data)); // #3
        info.setROMVersion(ntohl(data)); // #4
        info.setROMStage(ntohl(data)); // #5
        info.setRAMSize(ntohl(data)); // #6
        info.setScreenHeight(ntohl(data)); // #7
        info.setScreenWidth(ntohl(data)); // #8
        info.setPatchVersion(ntohl(data)); // #9
        info.setObjectSystemVersion(ntohl(data)); // #10
        info.setInternalStoreSignature(ntohl(data)); // #11
        info.setScreenResolutionVertical(ntohl(data)); // #12
        info.setScreenResolutionHorizontal(ntohl(data)); // #13
        info.setScreenDepth(ntohl(data)); // #14

        return info;
    }

    /**
     * Decode the owner name.
     *
     * @param data the data.
     * @throws IOException if an I/O error occurs.
     */
    protected String decodeName(InputStream data) throws IOException {
        int nameLength = getLength();
        // 4 bytes for version info length.
        nameLength -= LENGTH_WORD;
        // already read the version info.
        nameLength -= versionInfoLength;
        byte[] nameBytes = new byte[nameLength];
        data.read(nameBytes);
        // remove 2 bytes for null-terminated string.
        return new String(nameBytes, 0, nameLength - 2, NSOFString.CHARSET_UTF16);
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return getInformation().getName();
    }

    /**
     * Set the name.
     *
     * @param name the name.
     */
    protected void setName(String name) {
        getInformation().setName(name);
    }

    /**
     * Get the Newton information.
     *
     * @return the information.
     */
    public NewtonInfo getInformation() {
        if (info == null)
            info = new NewtonInfo();
        return info;
    }

    /**
     * Set the Newton information.
     *
     * @param info the information.
     */
    protected void setInformation(NewtonInfo info) {
        this.info = info;
    }
}
