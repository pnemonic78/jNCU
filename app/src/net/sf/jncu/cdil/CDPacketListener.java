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
package net.sf.jncu.cdil;

/**
 * Packet listener interface.
 *
 * @author moshew
 */
public interface CDPacketListener<T extends CDPacket> {

    /**
     * Notification that a packet was received.
     *
     * @param packet the received packet.
     */
    void packetReceived(T packet);

    /**
     * Notification that a packet is being sent.
     *
     * @param packet the sending packet.
     */
    void packetSending(T packet);

    /**
     * Notification that a packet was sent.
     *
     * @param packet the sent packet.
     */
    void packetSent(T packet);

    /**
     * Notification that a packet was acknowledged by the receiver after we sent
     * it.
     *
     * @param packet the sent packet.
     */
    void packetAcknowledged(T packet);

    /**
     * Notification that no more packets will be available.
     */
    void packetEOF();

}
