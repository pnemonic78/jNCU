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

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command is used to customise the set of icons shown on the Newton. The
 * <tt>iconMask</tt> is a long that indicates which icons should be shown. For
 * example, to show all icons you would use this:
 * <code>kBackupIcon + kSyncIcon + kInstallIcon + kRestoreIcon + kImportIcon + kKeyboardIcon</code>
 * Where:
 * <ul>
 * <li>kBackupIcon = 1
 * <li>kRestoreIcon = 2
 * <li>kInstallIcon = 4
 * <li>kImportIcon = 8
 * <li>kSyncIcon = 16
 * <li>kKeyboardIcon = 32
 * </ul>
 *
 * <pre>
 * 'wicn'
 * length
 * iconMask
 * </pre>
 *
 * @author moshew
 */
public class DWhichIcons extends DockCommandToNewtonLong {

    /**
     * <tt>kDWhichIcons</tt>
     */
    public static final String COMMAND = "wicn";

    /**
     * No icons.
     */
    public static final int NONE = 0;
    /**
     * "Backup" icon.<br>
     * <tt>kBackupIcon</tt>
     */
    public static final int BACKUP = 1;
    /**
     * "Restore" icon.<br>
     * <tt>kRestoreIcon</tt>
     */
    public static final int RESTORE = 2;
    /**
     * "Install package" icon.<br>
     * <tt>kInstallIcon</tt>
     */
    public static final int INSTALL = 4;
    /**
     * "Import" icon.<br>
     * <tt>kImportIcon</tt>
     */
    public static final int IMPORT = 8;
    /**
     * "Synchronise / Synchronize" icon.<br>
     * <tt>kSyncIcon</tt>
     */
    public static final int SYNC = 16;
    /**
     * "Keyboard" icon.<br>
     * <tt>kKeyboardIcon</tt>
     */
    public static final int KEYBOARD = 32;
    /**
     * All icons.
     */
    public static final int ALL = BACKUP | INSTALL | KEYBOARD;

    /**
     * Creates a new command.
     */
    public DWhichIcons() {
        super(COMMAND);
    }

    /**
     * Get the icons.
     *
     * @return the icons.
     */
    public int getIcons() {
        return getValue();
    }

    /**
     * Set the icons.
     *
     * @param icons the icons.
     */
    public void setIcons(int icons) {
        setValue(icons);
    }

    /**
     * Add an icon.
     *
     * @param icon the icon.
     */
    public void addIcon(int icon) {
        switch (icon) {
            case BACKUP:
            case RESTORE:
            case INSTALL:
            case IMPORT:
            case SYNC:
            case KEYBOARD:
                setIcons(getIcons() | icon);
                break;
            default:
                throw new IllegalArgumentException("unknown icon");
        }
    }
}
