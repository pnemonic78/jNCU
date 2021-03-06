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
package net.sf.jncu;

import net.sf.jncu.cdil.mnp.CommPorts;
import net.sf.jncu.cdil.mnp.MNPSerialPort;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Settings.
 *
 * @author moshew
 */
public class Settings {

    /**
     * Backup files folder.
     */
    private static final String FOLDER = "jNCU";

    private File mainFolder;
    private Communications comm;
    private General general;
    private Security sec;
    private AutoDock dock;

    /**
     * Constructs new settings.
     */
    public Settings() {
        super();

        File userFolder = new File(System.getProperty("user.home"));
        this.mainFolder = new File(userFolder, FOLDER);
        this.comm = new Communications();
        this.general = new General();
        this.sec = new Security();
        this.dock = new AutoDock();
        load();
    }

    /**
     * Load the settings from the preferences.
     */
    protected void load() {
        Preferences prefs = Preferences.getInstance();
        comm.read(prefs);
        general.read(prefs);
        sec.read(prefs);
        dock.read(prefs);
    }

    /**
     * Persist the settings to the preferences.
     */
    public void save() {
        Preferences prefs = Preferences.getInstance();
        comm.write(prefs);
        general.write(prefs);
        sec.write(prefs);
        dock.write(prefs);
        prefs.save();
    }

    /**
     * Settings category.
     *
     * @author Moshe
     */
    protected interface SettingsCategory {
        /**
         * Read the settings from the preferences.
         *
         * @param prefs the preferences.
         */
        void read(Preferences prefs);

        /**
         * Write the settings to the preferences.
         *
         * @param prefs the preferences.
         */
        void write(Preferences prefs);
    }

    /**
     * Communications settings.
     *
     * @author Moshe
     */
    public class Communications implements SettingsCategory {
        /**
         * Property key for port id.
         */
        private static final String KEY_PORT_ID = "jncu.port.id";
        /**
         * Property key for port speed.
         */
        private static final String KEY_PORT_SPEED = "jncu.port.baud";

        private final Collection<String> portIds = new ArrayList<String>();
        private String portId;
        private int baud;

        /**
         * Constructs a new category.<br>
         * Scan for the list of ports.
         */
        public Communications() {
            rescanPorts();
        }

        /**
         * Get the list of port names.
         *
         * @return the list of ports.
         */
        public Collection<String> getPorts() {
            return portIds;
        }

        /**
         * Rescan for the list of ports.
         */
        public void rescanPorts() {
            portIds.clear();

            CommPorts discoverer = new CommPorts();
            portIds.addAll(discoverer.getPortNames());
        }

        /**
         * Get the port identifier name.
         *
         * @return the port id.
         */
        public String getPortIdentifier() {
            return portId;
        }

        /**
         * Set the port identifier name.
         *
         * @param portName the port id.
         */
        public void setPortIdentifier(String portName) {
            this.portId = portName;
        }

        /**
         * Get the port speed (baud).
         *
         * @return the speed.
         */
        public int getPortSpeed() {
            return baud;
        }

        /**
         * Set the port speed (baud).
         *
         * @param portSpeed the speed.
         */
        public void setPortSpeed(int portSpeed) {
            this.baud = portSpeed;
        }

        @Override
        public void read(Preferences prefs) {
            setPortIdentifier(prefs.get(KEY_PORT_ID));
            setPortSpeed(Integer.parseInt(prefs.get(KEY_PORT_SPEED, Integer.toString(MNPSerialPort.BAUD_38400))));
        }

        @Override
        public void write(Preferences prefs) {
            prefs.set(KEY_PORT_ID, getPortIdentifier());
            prefs.set(KEY_PORT_SPEED, Integer.toString(getPortSpeed()));
        }
    }

    /**
     * Get the communications category.
     *
     * @return the category.
     */
    public Communications getCommunications() {
        return comm;
    }

    /**
     * Type of name for the backup file.
     *
     * @author Moshe
     */
    public enum BackupName {
        /**
         * Date and time.
         */
        DATE("backup.date"),
        /**
         * Newton name.
         */
        NEWTON_NAME("device.name"),
        /**
         * Newton id.
         */
        NEWTON_ID("device.id");

        private final String labelKey;

        /**
         * Constructs a new backup name.
         *
         * @param labelKey the label key.
         */
        BackupName(String labelKey) {
            this.labelKey = labelKey;
        }

        /**
         * Get the resource id.
         *
         * @return the label key.
         */
        public String getLabelKey() {
            return labelKey;
        }

        /**
         * Get the user-friendly label.
         *
         * @return the label text.
         */
        public String getLabel() {
            return JNCUResources.getString(labelKey);
        }

        @Override
        public String toString() {
            final String label = getLabel();
            return (label == null) ? super.toString() : label;
        }
    }

    /**
     * General settings.
     *
     * @author Moshe
     */
    public class General implements SettingsCategory {
        /**
         * Property key for backup path.
         */
        private static final String KEY_BACKUP_PATH = "jncu.backup.path";
        /**
         * Property key for backup name.
         */
        private static final String KEY_BACKUP_NAME = "jncu.backup.name";

        private File backupFolder;
        private BackupName backupName;

        /**
         * Constructs a new category.
         */
        public General() {
            this.backupFolder = new File(mainFolder, "Backups");
            backupFolder.mkdirs();
            this.backupName = BackupName.DATE;
        }

        /**
         * Get the backup folder.
         *
         * @return the folder.
         */
        public File getBackupFolder() {
            return backupFolder;
        }

        /**
         * Set the backup folder.
         *
         * @param backupFolder the folder.
         */
        public void setBackupFolder(File backupFolder) {
            this.backupFolder = backupFolder;
        }

        /**
         * Set the backup folder.
         *
         * @param backupFolder the folder path.
         */
        public void setBackupFolder(String backupFolder) {
            setBackupFolder(new File(backupFolder));
        }

        /**
         * Get the backup name type.
         *
         * @return the name type.
         */
        public BackupName getBackupName() {
            return backupName;
        }

        /**
         * Set the backup name type.
         *
         * @param backupName the name type.
         */
        public void setBackupName(BackupName backupName) {
            this.backupName = backupName;
        }

        @Override
        public void read(Preferences prefs) {
            setBackupFolder(prefs.get(KEY_BACKUP_PATH, getBackupFolder().getPath()));
            String backupNameValue = prefs.get(KEY_BACKUP_NAME, getBackupName().name());
            if (backupNameValue != null)
                setBackupName(BackupName.valueOf(backupNameValue));
        }

        @Override
        public void write(Preferences prefs) {
            prefs.set(KEY_BACKUP_PATH, getBackupFolder().getPath());
            prefs.set(KEY_BACKUP_NAME, getBackupName().name());
        }
    }

    /**
     * Get the general category.
     *
     * @return the category.
     */
    public General getGeneral() {
        return general;
    }

    /**
     * Security settings.
     *
     * @author Moshe
     */
    public class Security implements SettingsCategory {
        /**
         * Property key for the password.
         */
        private static final String KEY_PASSWORD = "jncu.password";

        private transient String password;

        /**
         * Constructs a new category.
         */
        public Security() {
        }

        /**
         * Get the password.
         *
         * @return the password.
         */
        public String getPassword() {
            return password;
        }

        /**
         * Set the password.
         *
         * @param password the password.
         */
        public void setPassword(String password) {
            if (password == null)
                password = "";
            this.password = password;
        }

        @Override
        public void read(Preferences prefs) {
            // Decrypt the password.
            String passwordBigInt = prefs.get(KEY_PASSWORD, null);
            if ((passwordBigInt == null) || passwordBigInt.length() == 0) {
                setPassword(null);
            } else {
                try {
                    BigInteger big = new BigInteger(passwordBigInt);
                    byte[] bytes = big.toByteArray();
                    String password = new String(bytes, StandardCharsets.UTF_16);
                    setPassword(password);
                } catch (NumberFormatException nfe) {
                    JNCUApp.showError(null, "Read password", nfe);
                }
            }
        }

        @Override
        public void write(Preferences prefs) {
            // Encrypt the password.
            String password = getPassword();
            if ((password == null) || (password.length() == 0)) {
                prefs.set(KEY_PASSWORD, "");
            } else {
                byte[] bytes = password.getBytes(StandardCharsets.UTF_16);
                BigInteger big = new BigInteger(bytes);
                String passwordBigInt = big.toString();
                prefs.set(KEY_PASSWORD, passwordBigInt);
            }
        }
    }

    /**
     * Get the security category.
     *
     * @return the category.
     */
    public Security getSecurity() {
        return sec;
    }

    /**
     * Automatic docking (Auto Dock) settings.
     *
     * @author Moshe
     */
    public class AutoDock implements SettingsCategory {
        /**
         * Property key for automatic backup.
         */
        private static final String KEY_DOCK_BACKUP = "jncu.dock.backup";
        /**
         * Property key for automatic selective backup.
         */
        private static final String KEY_DOCK_BACKUP_SELECTIVE = "jncu.dock.backup.selective";
        /**
         * Property key for automatic synchronize.
         */
        private static final String KEY_DOCK_SYNC = "jncu.dock.sync";

        private boolean backup;
        private boolean backupSelective;
        private boolean sync;

        /**
         * Constructs a new category.
         */
        public AutoDock() {
        }

        /**
         * Should backup the Newton device when auto-docked?
         *
         * @return whether to backup.
         */
        public boolean isBackup() {
            return backup;
        }

        /**
         * Set to backup the Newton device when auto-docked.
         *
         * @param backup backup?
         */
        public void setBackup(boolean backup) {
            this.backup = backup;
        }

        /**
         * Is the auto-dock backup selective?
         *
         * @return is selective?
         */
        public boolean isBackupSelective() {
            return backupSelective;
        }

        /**
         * Set the auto-dock backup as selective.
         *
         * @param backupSelective is selective?
         */
        public void setBackupSelective(boolean backupSelective) {
            this.backupSelective = backupSelective;
        }

        /**
         * Should synchronise the Newton device when auto-docked?
         *
         * @return synchronise?
         */
        public boolean isSync() {
            return sync;
        }

        /**
         * Set to synchronise the Newton device when auto-docked.
         *
         * @param sync synchronise?
         */
        public void setSync(boolean sync) {
            this.sync = sync;
        }

        @Override
        public void read(Preferences prefs) {
            setBackup(prefs.getBoolean(KEY_DOCK_BACKUP, false));
            setBackupSelective(prefs.getBoolean(KEY_DOCK_BACKUP_SELECTIVE, false));
            setSync(prefs.getBoolean(KEY_DOCK_SYNC, false));
        }

        @Override
        public void write(Preferences prefs) {
            prefs.set(KEY_DOCK_BACKUP, isBackup());
            prefs.set(KEY_DOCK_BACKUP_SELECTIVE, isBackup());
            prefs.set(KEY_DOCK_SYNC, isBackup());
        }
    }

    /**
     * Get the auto-dock category.
     *
     * @return the category.
     */
    public AutoDock getAutoDock() {
        return dock;
    }
}
