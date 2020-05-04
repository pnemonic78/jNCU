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
