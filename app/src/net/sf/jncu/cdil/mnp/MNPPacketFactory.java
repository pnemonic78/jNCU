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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * MNP packet factory.
 *
 * @author moshew
 */
public class MNPPacketFactory {

    private static MNPPacketFactory instance;

    /**
     * Outgoing sequence.
     */
    private byte sequence;

    /**
     * Creates a new factory.
     */
    protected MNPPacketFactory() {
        super();
    }

    /**
     * Get the factory instance.
     *
     * @return the factory.
     */
    public static MNPPacketFactory getInstance() {
        if (instance == null) {
            instance = new MNPPacketFactory();
        }
        return instance;
    }

    /**
     * Create a MNP link packet.
     *
     * @param type the link type.
     * @return the packet.
     * @see MNPPacket#LA
     * @see MNPPacket#LD
     * @see MNPPacket#LR
     * @see MNPPacket#LT
     */
    public MNPPacket createLinkPacket(byte type) {
        switch (type) {
            case MNPPacket.LA:
                return createLA();
            case MNPPacket.LD:
                return createLD();
            case MNPPacket.LR:
                return createLR();
            case MNPPacket.LT:
                return createLTSend();
        }
        throw new IllegalArgumentException("invalid type " + type);
    }

    /**
     * Create a MNP link packet, and decode.
     *
     * @param payload the payload.
     * @return the packet.
     */
    public MNPPacket createLinkPacket(byte[] payload) {
        if ((payload == null) || (payload.length < 2)) {
            return null;
        }
        byte type = payload[1];
        MNPPacket packet = null;
        if (type == MNPPacket.LT) {
            packet = createLT();
        } else {
            packet = createLinkPacket(type);
        }
        packet.deserialize(payload);
        return packet;
    }

    /**
     * Create a Link Transfer packet.
     *
     * @param data   the payload data.
     * @param length the data length.
     * @return the packet.
     * @throws IOException if an I/O error occurs.
     */
    protected MNPLinkTransferPacket createLT(InputStream data, int length) throws IOException {
        if (data == null)
            return null;
        MNPLinkTransferPacket packet = createLTSend();
        length = Math.min(length, MNPLinkTransferPacket.MAX_DATA_LENGTH);
        if (length > 0) {
            byte[] buf = new byte[length];
            int count = 0;
            int offset = 0;
            do {
                count = data.read(buf, offset, length);
                if (count == -1)
                    break;
                offset += count;
                length -= count;
            } while (length > 0);
            packet.setData(buf, 0, offset);
        }
        return packet;
    }

    /**
     * Reset the sequence.
     */
    public void resetSequence() {
        this.sequence = 0;
    }

    /**
     * Set the sequence.
     *
     * @param sequence the sequence.
     */
    void setSequence(byte sequence) {
        this.sequence = sequence;
    }

    /**
     * Create a Link Acknowledgement packet.
     *
     * @return the packet.
     */
    protected MNPLinkAcknowledgementPacket createLA() {
        return new MNPLinkAcknowledgementPacket();
    }

    /**
     * Create a Link Disconnect packet.
     *
     * @return the packet.
     */
    protected MNPLinkDisconnectPacket createLD() {
        return new MNPLinkDisconnectPacket();
    }

    /**
     * Create a Link Request packet.
     *
     * @return the packet.
     */
    protected MNPLinkRequestPacket createLR() {
        return new MNPLinkRequestPacket();
    }

    /**
     * Create a Link Transfer packet.
     *
     * @return the packet.
     */
    protected MNPLinkTransferPacket createLT() {
        return new MNPLinkTransferPacket();
    }

    /**
     * Create a Link Transfer packet for sending.
     *
     * @return the packet.
     */
    protected MNPLinkTransferPacket createLTSend() {
        MNPLinkTransferPacket packet = createLT();
        packet.setSequence(++sequence);
        return packet;
    }

    /**
     * Create MNP link transfer packets to be the target of the "foreach"
     * statement.
     *
     * @param data the payload data.
     * @return the list of packets.
     * @throws IOException if an I/O error occurs.
     */
    public Iterable<MNPLinkTransferPacket> createTransferPackets(byte[] data) throws IOException {
        if (data == null)
            return null;
        return createTransferPackets(new ByteArrayInputStream(data), data.length);
    }

    /**
     * Create MNP link transfer packets to be the target of the "foreach"
     * statement.
     *
     * @param data the payload data. The stream must return the correct number of
     *             bytes available.
     * @return the list of packets.
     * @throws IOException if an I/O error occurs.
     * @see #createTransferPackets(InputStream, int)
     */
    public Iterable<MNPLinkTransferPacket> createTransferPackets(InputStream data) throws IOException {
        if (data == null)
            return null;
        return createTransferPackets(data, data.available());
    }

    /**
     * Create MNP link transfer packets to be the target of the "foreach"
     * statement.
     *
     * @param data   the payload data.
     * @param length the payload length.
     * @return the list of packets.
     */
    public Iterable<MNPLinkTransferPacket> createTransferPackets(InputStream data, int length) {
        if (data == null)
            return null;
        return new MNPLTIterable(data, length);
    }

    /**
     * Iterable for creating MNP LT packets to be the target of the "foreach"
     * statement.
     *
     * @author moshe
     */
    private class MNPLTIterable implements Iterable<MNPLinkTransferPacket> {

        private final InputStream data;
        private final int length;

        /**
         * Create an LT iterator.
         *
         * @param data   the data.
         * @param length the data length.
         */
        public MNPLTIterable(InputStream data, int length) {
            super();
            this.data = data;
            this.length = length;
        }

        @Override
        public Iterator<MNPLinkTransferPacket> iterator() {
            return new MNPLTIterator(data, length);
        }
    }

    /**
     * Iterator for creating MNP LT packets to be the target of the "foreach"
     * statement.
     *
     * @author moshe
     */
    private class MNPLTIterator implements Iterator<MNPLinkTransferPacket> {

        private final InputStream data;
        private int length;

        /**
         * Create an LT iterator.
         *
         * @param data   the data.
         * @param length the data length.
         */
        public MNPLTIterator(InputStream data, int length) {
            super();
            this.data = data;
            this.length = length;
        }

        @Override
        public boolean hasNext() {
            return length > 0;
        }

        @Override
        public MNPLinkTransferPacket next() {
            MNPLinkTransferPacket packet = null;
            try {
                packet = createLT(data, length);
                byte[] b = packet.getData();
                length -= b.length;
            } catch (IOException ioe) {
                throw new NoSuchElementException();
            }
            return packet;
        }

        @Override
        public void remove() {
            // Implementation not required.
        }
    }
}
