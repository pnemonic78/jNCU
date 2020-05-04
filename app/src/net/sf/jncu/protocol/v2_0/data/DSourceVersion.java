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
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Tells the Newton the version that the subsequent data is from. For example,
 * if a 1.x data file is being restored the desktop would tell the Newton that
 * version {@code 1} data was coming. Same for importing a 1.x NTF file.
 * Otherwise, it should indicate that 2.x data is coming. When the connection is
 * first started the version defaults to 2.x. This information is necessary for
 * the Newton to know whether or not it should run the conversion scripts. A
 * <tt>kDRes</tt> command with value {@code 0} is sent by the Newton in response
 * to this command. This commands affects only data added to the Newton with
 * <tt>kDAddEntry</tt> and <tt>kDAddEntryWithUniqueID</tt> commands. In
 * particular, note that data returned by <tt>kDReturnEntry</tt> isn't converted
 * if it's a different version than was set by this command.
 * <p>
 * <tt>manufacturer</tt> and <tt>machinetype</tt> tell the Newton the type of
 * Newton that's the source of the data being restored. These are sent at the
 * beginning of a connection as part of the Newton name command.
 *
 * <pre>
 * 'sver'
 * length
 * vers
 * manufacturer
 * machineType
 * </pre>
 *
 * @author moshew
 */
public class DSourceVersion extends BaseDockCommandToNewton {

    /**
     * <tt>kDSourceVersion</tt>
     */
    public static final String COMMAND = "sver";

    /**
     * Source versions.<br>
     * <tt>eSourceVersion</tt>
     */
    public enum SourceVersion {
        /**
         * Unknown version.<br>
         * <tt>eNone</tt>
         */
        NONE,
        /**
         * <tt>1.x</tt> data file.<br>
         * <tt>eOnePointXData</tt><br>
         * <tt>kOnePointXData</tt>
         */
        ONE_POINT_X_DATA,
        /**
         * <tt>2.x</tt> data file.<br>
         * <tt>eTwoPointXData</tt><br>
         * <tt>kTwoPointXData</tt>
         */
        TWO_POINT_X_DATA
    }

    private SourceVersion version;
    private int manufacturerId;
    private int machineType;

    /**
     * Creates a new command.
     */
    public DSourceVersion() {
        super(COMMAND);
        setLength(12);
    }

    /**
     * Get the version.
     *
     * @return the version.
     */
    public SourceVersion getVersion() {
        return version;
    }

    /**
     * Set the version.
     *
     * @param version the version.
     */
    public void setVersion(SourceVersion version) {
        this.version = version;
    }

    /**
     * Get the manufacturer id.
     * <p>
     * An integer indicating the manufacturer of the Newton device.
     *
     * @return the manufacturer id.
     */
    public int getManufacturerId() {
        return manufacturerId;
    }

    /**
     * Set the manufacturer id.
     *
     * @param manufacturerId the manufacturer id.
     */
    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    /**
     * Get the machine type.
     * <p>
     * An integer indicating the hardware type this ROM was built for.
     *
     * @return the machine type.
     */
    public int getMachineType() {
        return machineType;
    }

    /**
     * Set the machine type.
     *
     * @param machineType the machine type.
     */
    public void setMachineType(int machineType) {
        this.machineType = machineType;
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        htonl(getVersion().ordinal(), data);
        htonl(getManufacturerId(), data);
        htonl(getMachineType(), data);
    }
}
