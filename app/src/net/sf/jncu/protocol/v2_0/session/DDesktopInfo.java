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

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFBoolean;
import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * This command is used to negotiate the real protocol version. The protocol
 * version sent with the <tt>kDRequestToDock</tt> command is now fixed at
 * version 9 (the version used by the 1.0 ROMs) so we can support package
 * loading with NPI 1.0, Connection 2.0 and NTK 1.0. Connection 3.0 will send
 * this command with the real protocol version it wants to use to talk to the
 * Newton. The Newton will respond with a number equal to or lower than the
 * number sent to it by the desktop. The desktop can then decide whether it can
 * talk the specified protocol or not.
 * <p>
 * The desktop type is a long that identifies the sender- {@code 0} for the
 * Macintosh and {@code 1} for Windows.
 * <p>
 * The password key is used as part of the password verification.
 * <p>
 * Session type will be the real session type and should override what was sent
 * in <tt>kDInitiateDocking</tt>. In fact, it will either be the same as was
 * sent in <tt>kDInitiateDocking</tt> or "settingUp" to indicate that although
 * the desktop has accepted a connection, the user has not yet specified an
 * operation.
 * <p>
 * <tt>AllowSelectiveSync</tt> is a boolean. The desktop should say no when the
 * user hasn't yet done a full sync and, therefore, can't do a selective sync.
 * <p>
 * <tt>desktopApps</tt> is an array of frames that describes who the Newton is
 * talking with. Each frame in the array looks like this:
 * <code>{name: "Newton Backup Utility", id: 1, version: 1}</code> There might
 * be more than one item in the array if the Newton is connecting with a DIL
 * application. The built-in Connection application expects 1 item in the array
 * that has id:
 * <ul>
 * <li>1: NBU
 * <li>2: NCU
 * </ul>
 * It won't allow connection with any other id. NCK 2.0, NTK and NPI use old
 * revisions of the protocol and aren't considered here.
 *
 * <pre>
 * 'dinf'
 * length
 * protocol version
 * desktopType		// 0 for Mac, 1 for Windows
 * encrypted key	// 2 longs
 * session type
 * allowSelectiveSync // 0 = no, 1 = yes
 * desktopApps ref
 * </pre>
 *
 * @see #NBU
 * @see #NCU
 * @see #MACINTOSH
 * @see #WINDOWS
 */
public class DDesktopInfo extends BaseDockCommandToNewton {

    /**
     * <tt>kDDesktopInfo</tt>
     */
    public static final String COMMAND = "dinf";

    /**
     * The protocol version. Must be at least 10.
     */
    public static final int PROTOCOL_VERSION = 10;

    /**
     * Newton Backup Utility.<br>
     * <tt>kNBU</tt>
     */
    public static final int NBU = 1;
    /**
     * Newton Connection Utilities.<br>
     * <tt>kNCU</tt>
     */
    public static final int NCU = 2;

    /**
     * Apple Macintosh desktop type.<br>
     * <tt>kMacintosh</tt> <br>
     * <tt>kMacDesktop</tt>
     */
    public static final int MACINTOSH = 0;
    /**
     * Microsoft Windows desktop type.<br>
     * <tt>kWindows</tt> <br>
     * <tt>kWindowsDesktop</tt>
     */
    public static final int WINDOWS = 1;

    private int sessionType;
    private int desktopType;
    private boolean selectiveSync;
    private long encryptedKey;
    private NSOFArray desktopApps;
    private static final Random rand = new Random();

    /**
     * Creates a new command.
     */
    public DDesktopInfo() {
        super(COMMAND);
        setSessionType(DInitiateDocking.SESSION_SETTING_UP);
        // FIXME Windows type doesn't support many commands.
        setDesktopType((File.separatorChar == '\\') ? WINDOWS : MACINTOSH);
        setSelectiveSync(true);
        setEncryptedKey(rand.nextLong());
        setDesktopApps(null);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(PROTOCOL_VERSION, data);
        htonl(getDesktopType(), data);
        htonl(getEncryptedKey(), data);
        htonl(getSessionType(), data);
        htonl(isSelectiveSync() ? TRUE : FALSE, data);
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(getDesktopApps(), data);
    }

    /**
     * Get the session type.
     *
     * @return the session type.
     */
    public int getSessionType() {
        return sessionType;
    }

    /**
     * Set the session type.
     *
     * @param sessionType the session type.
     * @see DInitiateDocking
     */
    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }

    /**
     * Get the desktop type.
     *
     * @return the desktop type.
     */
    public int getDesktopType() {
        return desktopType;
    }

    /**
     * Set the desktop type.
     *
     * @param desktopType the desktop type.
     */
    private void setDesktopType(int desktopType) {
        this.desktopType = desktopType;
    }

    /**
     * Allow selective sync?
     *
     * @return allow?
     */
    public boolean isSelectiveSync() {
        return selectiveSync;
    }

    /**
     * Set selective sync.
     *
     * @param selectiveSync allow?
     */
    public void setSelectiveSync(boolean selectiveSync) {
        this.selectiveSync = selectiveSync;
    }

    /**
     * Get the desktop applications.
     *
     * @return the array of desktop applications.
     */
    public NSOFArray getDesktopApps() {
        if (desktopApps == null) {
            NSOFFrame app = new NSOFFrame();
            app.put("id", new NSOFInteger(NCU));
            app.put("name", new NSOFString("jNewton Connection Utilities"));
            app.put("version", new NSOFInteger(1));
            app.put("doesAuto", NSOFBoolean.TRUE);

            this.desktopApps = new NSOFPlainArray(1);
            desktopApps.set(0, app);
        }
        return desktopApps;
    }

    /**
     * Set the desktop applications.
     *
     * @param desktopApps the array of desktop applications.
     */
    public void setDesktopApps(NSOFArray desktopApps) {
        this.desktopApps = desktopApps;
    }

    /**
     * Get the encrypted key.
     *
     * @return the encrypted key.
     */
    public long getEncryptedKey() {
        return encryptedKey;
    }

    /**
     * Set the encrypted key.
     *
     * @param encryptedKey the encrypted key.
     */
    public void setEncryptedKey(long encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

}
