/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * http://sourceforge.net/projects/jncu
 *
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
 */
package net.sf.jncu.protocol.v1_0.io;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.swing.SwingUtils;

/**
 * Device information.
 *
 * @author moshew
 */
public class Device implements Comparable<Device> {

    /**
     * <tt>kDesktop</tt><br>
     * Desktop path type.
     */
    public static final int DESKTOP = 0;
    /**
     * <tt>kFile</tt><br>
     * <tt>kDesktopFile</tt><br>
     * File path type type.
     */
    public static final int FILE = 1;
    /**
     * <tt>kFolder</tt><br>
     * <tt>kDesktopFolder</tt><br>
     * Folder path type.
     */
    public static final int FOLDER = 2;
    /**
     * <tt>kDisk</tt><br>
     * <tt>kDesktopDisk</tt><br>
     * Disk path type.
     */
    public static final int DISK = 3;

    /**
     * <tt>kFloppyDisk</tt><br>
     * <tt>kFloppyDrive</tt><br>
     * Floppy disk device.
     */
    public static final int FLOPPY_DISK = 0;
    /**
     * <tt>kHardDisk</tt><br>
     * <tt>kHardDrive</tt><br>
     * Hard disk drive device.
     */
    public static final int HARD_DISK = 1;
    /**
     * <tt>kCdRomDisk</tt><br>
     * <tt>kCDROMDrive</tt><br>
     * CD-ROM disc device.
     */
    public static final int CDROM_DISK = 2;
    /**
     * <tt>kNetDrive</tt><br>
     * Network drive device.
     */
    public static final int NET_DRIVE = 3;

    protected static final NSOFSymbol SLOT_ALIAS = new NSOFSymbol("alias");
    protected static final NSOFSymbol SLOT_DISK = new NSOFSymbol("disktype");
    protected static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
    protected static final NSOFSymbol SLOT_TYPE = new NSOFSymbol("type");
    protected static final NSOFSymbol SLOT_VOL = new NSOFSymbol("whichVol");
    protected static final NSOFSymbol SLOT_VOLREF = new NSOFSymbol("volRefNum");

    private String name;
    private int type = -1;
    private int diskType = -1;
    private Integer vol;
    private Integer volRefNum;
    private String alias;

    protected static final char driveCharWindows = ':';

    /**
     * Creates a new device.
     */
    public Device() {
        super();
    }

    /**
     * Creates a new device.
     *
     * @param file the file.
     */
    public Device(File file) {
        super();
        setFile(file);
    }

    /**
     * Set the device file.
     *
     * @param file the file.
     */
    public void setFile(File file) {
        String path = file.getPath();

        String name = file.getName();
        if (name.length() == 0) {
            name = path;
        }
        setName(name);
        setType(FILE);

        FileSystemView fileSystemView = SwingUtils.getFileSystemView();
        if (fileSystemView.isDrive(file)) {
            // Special case for Windows drives:
            // Prepend the drive name to the device name for DSetDrive
            // - {"C:\" + "Local Disk (C:)"} to {"C: Local Disk"}
            if (File.separatorChar == '\\') {
                if (path.charAt(1) == driveCharWindows) {
                    path = path.substring(0, 2);
                    name = fileSystemView.getSystemDisplayName(file);
                    int indexBracket = name.lastIndexOf('(');
                    if (indexBracket > 0) {
                        name = name.substring(0, indexBracket);
                        name = name.trim();
                    }
                    if ((name.length() > 0) && !name.startsWith(path))
                        setName(path + ' ' + name);
                }
            }

            setType(DISK);
            setDiskType(HARD_DISK);
            if (fileSystemView.isFloppyDrive(file)) {
                setDiskType(FLOPPY_DISK);
            } else if (file.getFreeSpace() == 0L) {
                setDiskType(Device.CDROM_DISK);
            }
        } else if (file.isDirectory()) {
            setType(FOLDER);
            // File home = getHome();
            // if (home.equals(file)) {
            // setType(DESKTOP);
            // setVolume(0);
            // }
        }
    }

    /**
     * Get the frame.
     *
     * @return the frame.
     */
    public NSOFFrame toFrame() {
        NSOFFrame frame = new NSOFFrame();
        frame.put(SLOT_NAME, new NSOFString(getName()));
        frame.put(SLOT_TYPE, new NSOFInteger(geType()));
        if (getDiskType() == DISK) {
            frame.put(SLOT_DISK, new NSOFInteger(getDiskType()));
        }
        if (getVolume() != null) {
            frame.put(SLOT_VOL, new NSOFInteger(getVolume()));
        }
        if (getVolumeRef() != null) {
            frame.put(SLOT_VOLREF, new NSOFInteger(getVolumeRef()));
        }
        String alias = getAlias();
        if ((alias != null) && (alias.length() > 0)) {
            frame.put(SLOT_ALIAS, new NSOFString(alias));
        }
        return frame;
    }

    /**
     * Decode the frame.
     *
     * @param frame the frame.
     */
    public void decode(NSOFFrame frame) {
        NSOFObject value;

        value = frame.get(SLOT_ALIAS);
        setAlias(null);
        if (value != null) {
            NSOFString s = (NSOFString) value;
            setAlias(s.getValue());
        }

        value = frame.get(SLOT_DISK);
        setDiskType(-1);
        if (value != null) {
            NSOFImmediate imm = (NSOFImmediate) value;
            setDiskType(imm.getValue());
        }

        value = frame.get(SLOT_NAME);
        setName(null);
        if (value != null) {
            NSOFString s = (NSOFString) value;
            setName(s.getValue());
        }

        value = frame.get(SLOT_TYPE);
        setType(-1);
        if (value != null) {
            NSOFImmediate imm = (NSOFImmediate) value;
            setType(imm.getValue());
        }

        value = frame.get(SLOT_VOL);
        setVolume(null);
        if (value != null) {
            NSOFImmediate imm = (NSOFImmediate) value;
            setVolume(imm.getValue());
        }

        value = frame.get(SLOT_VOLREF);
        setVolumeRef(null);
        if (value != null) {
            NSOFImmediate imm = (NSOFImmediate) value;
            setVolumeRef(imm.getValue());
        }
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the device type.
     *
     * @return the type.
     */
    public int geType() {
        return type;
    }

    /**
     * Set the device type.
     *
     * @param type the type.
     * @see #DESKTOP
     * @see #DISK
     * @see #FILE
     * @see #FOLDER
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Get the disk type.
     *
     * @return the disk type.
     */
    public int getDiskType() {
        return diskType;
    }

    /**
     * Set the disk type.
     *
     * @param diskType the disk type.
     * @see #CDROM_DISK
     * @see #FLOPPY_DISK
     * @see #HARD_DISK
     * @see #NET_DRIVE
     */
    public void setDiskType(int diskType) {
        this.diskType = diskType;
    }

    /**
     * Get the volume.
     *
     * @return the volume.
     */
    public Integer getVolume() {
        return vol;
    }

    /**
     * Set the volume.
     *
     * @param vol the volume.
     */
    public void setVolume(Integer vol) {
        this.vol = vol;
    }

    /**
     * Get the volume reference number.
     *
     * @return the reference.
     */
    public Integer getVolumeRef() {
        return volRefNum;
    }

    /**
     * Set the volume reference number.
     *
     * @param volRefNum the reference.
     */
    public void setVolumeRef(Integer volRefNum) {
        this.volRefNum = volRefNum;
    }

    /**
     * Get the alias.
     *
     * @return the alias.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Set the alias.
     *
     * @param alias the alias.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Get the user's home folder.
     *
     * @return the home folder.
     */
    protected File getHome() {
        return SwingUtils.getFileSystemView().getHomeDirectory();
    }

    @Override
    public String toString() {
        return toFrame().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Device) {
            Device that = (Device) obj;
            return this.compareTo(that) == 0;
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(Device that) {
        int c = this.getName().compareTo(that.getName());
        if (c != 0)
            return c;
        c = this.type - that.type;
        if (c != 0)
            return c;
        c = this.diskType - that.diskType;
        if (c != 0)
            return c;
        c = compare(this.vol, that.vol);
        if (c != 0)
            return c;
        c = compare(this.volRefNum, that.volRefNum);
        if (c != 0)
            return c;
        c = compare(this.alias, that.alias);
        if (c != 0)
            return c;
        return 0;
    }

    protected <T extends Comparable<T>> int compare(T o1, T o2) {
        if (o1 == null) {
            if (o2 == null)
                return 0;
            return -1;
        }
        if (o2 == null)
            return 1;
        return o1.compareTo(o2);
    }
}
