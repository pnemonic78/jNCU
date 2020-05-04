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
package net.sf.jncu.cdil.mnp;

import net.sf.jncu.cdil.CDPacket;

/**
 * MNP packet.
 *
 * @author moshew
 */
public abstract class MNPPacket extends CDPacket {

    /**
     * Link Request packet.
     */
    public static final byte LR = 0x01;
    /**
     * Link Disconnect packet.
     */
    public static final byte LD = 0x02;
    /**
     * Link Transfer packet.
     */
    public static final byte LT = 0x04;
    /**
     * Link Acknowledgement packet.
     */
    public static final byte LA = 0x05;
    /**
     * Link N packet.
     */
    public static final byte LN = 0x06;
    /**
     * Link N Acknowledgement packet.
     */
    public static final byte LNA = 0x07;

    private byte type;
    private int length;
    private int transmitted;

    /**
     * Creates a new MNP packet.
     *
     * @param type   the type.
     * @param length the default data length.
     */
    public MNPPacket(byte type, int length) {
        super();
        this.type = type;
        this.length = length;
    }

    /**
     * Parse the packet.
     *
     * @param payload the payload.
     * @return the array offset.
     */
    public int deserialize(byte[] payload) {
        int offset = 0;
        if (payload[offset] == 255) {
            offset++;
            length = ((payload[offset++] & 0xFF) << 8) + (payload[offset++] & 0xFF);
        } else {
            length = payload[offset++] & 0xFF;
        }
        if (length > payload.length)
            throw new ArrayIndexOutOfBoundsException(length);
        type = payload[offset++];
        return offset;
    }

    /**
     * Get the type.
     *
     * @return the type.
     */
    public byte getType() {
        return type;
    }

    /**
     * Get the data length.
     *
     * @return the length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Get the transmitted.
     *
     * @return the transmitted.
     */
    public int getTransmitted() {
        return transmitted;
    }

    /**
     * Set the transmitted.
     *
     * @param transmitted the transmitted.
     */
    public void setTransmitted(int transmitted) {
        this.transmitted = transmitted;
    }

}
