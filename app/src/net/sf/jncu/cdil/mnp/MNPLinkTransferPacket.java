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

/**
 * MNP Link Transfer packet.
 *
 * @author moshew
 */
public class MNPLinkTransferPacket extends MNPPacket {

    /**
     * Maximum data length per packet before having to split into multiple
     * packets.
     */
    public static final short MAX_DATA_LENGTH = 256;

    private byte[] data;
    private int sequence;

    /**
     * Creates a new MNP LT packet.
     */
    public MNPLinkTransferPacket() {
        super(LT, 2);
    }

    @Override
    public int deserialize(byte[] payload) {
        int offset = super.deserialize(payload);

        setSequence(payload[offset++]);
        byte[] data = new byte[payload.length - offset];
        System.arraycopy(payload, offset, data, 0, data.length);
        offset += data.length;
        setData(data);

        return offset;
    }

    @Override
    public byte[] serialize() {
        byte[] payload;
        if (data == null) {
            payload = new byte[3];
        } else {
            payload = new byte[3 + data.length];
            System.arraycopy(data, 0, payload, 3, data.length);
        }
        payload[0] = 2;
        payload[1] = LT;
        payload[2] = (byte) sequence;
        return payload;
    }

    /**
     * Get the data.
     *
     * @return the data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Set the data.
     *
     * @param data the data.
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * Set the data.
     *
     * @param data   the data.
     * @param offset the offset.
     * @param length the number of bytes.
     */
    public void setData(byte[] data, int offset, int length) {
        if ((offset == 0) && (data.length == length))
            this.data = data;
        else {
            this.data = new byte[length];
            System.arraycopy(data, offset, this.data, 0, length);
        }
    }

    /**
     * Get the sequence number.
     *
     * @return the sequence.
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * Set the sequence number.
     *
     * @param sequence the sequence.
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * Set the sequence number.
     *
     * @param sequence the sequence.
     */
    public void setSequence(byte sequence) {
        setSequence(sequence & 0xFF);
    }
}
