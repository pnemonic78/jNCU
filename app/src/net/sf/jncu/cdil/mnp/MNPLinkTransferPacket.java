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
