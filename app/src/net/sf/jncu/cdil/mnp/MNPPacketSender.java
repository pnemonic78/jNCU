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

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * Send queued MNP packets.
 *
 * @author moshew
 */
public class MNPPacketSender extends Thread implements MNPPacketListener {

    protected final MNPPipe pipe;
    protected final MNPPacketLayer packetLayer;
    protected final BlockingQueue<MNPPacket> queueSend = new LinkedBlockingQueue<MNPPacket>();
    protected boolean running = false;
    private int sequenceAcknowledged = -1;
    private MNPPacket packetAcknowledge = null;

    /**
     * Creates a new packet sender.
     *
     * @param pipe        the pipe.
     * @param packetLayer the packet layer.
     */
    public MNPPacketSender(MNPPipe pipe, MNPPacketLayer packetLayer) {
        super();
        setName("MNPPacketSender-" + getId());
        this.pipe = pipe;
        this.packetLayer = packetLayer;
        packetLayer.addPacketListener(this);
    }

    /**
     * Queue a packet for sending and wait for acknowledgement.<br>
     * Does not wait for acknowledgement if the packet is itself an
     * acknowledgement.
     *
     * @param packet the packet to send.
     * @throws TimeoutException if a timeout occurs.
     */
    public void sendQueued(MNPPacket packet) throws TimeoutException {
        try {
            queueSend.put(packet);
        } catch (InterruptedException ie) {
            throw new TimeoutException(ie.getMessage());
        }
    }

    /**
     * Send packets while waiting for more to become available. Possibly re-send
     * multiple times until acknowledged. Packets are queued until the first
     * packet is sent.
     *
     * @throws TimeoutException if timeout occurs.
     */
    private void runSend() throws TimeoutException {
        MNPPacket packet;
        MNPPacket next = null;
        long timeout; // Enough time to wait for acknowledgement.
        long now;
        int retry;
        boolean allowSend;
        boolean resend = true;
        int sequenceToAcknowledge;

        while (running && !isInterrupted()) {
            try {
                next = queueSend.take();
            } catch (InterruptedException ie) {
                if (!queueSend.isEmpty())
                    ie.printStackTrace();
            }

            if (next != null) {
                packetAcknowledge = null;
                // LA and LD packets don't need acknowledgement.
                switch (next.getType()) {
                    case MNPPacket.LR:
                        sequenceToAcknowledge = 0;
                        packetAcknowledge = next;
                        break;
                    case MNPPacket.LT:
                        sequenceToAcknowledge = ((MNPLinkTransferPacket) next).getSequence();
                        packetAcknowledge = next;
                        break;
                    default:
                        sequenceToAcknowledge = -1;
                        break;
                }
                retry = 5;
                resend = true;
                packet = next;

                do {
                    allowSend = (pipe == null) || pipe.allowSend();
                    if (allowSend) {
                        try {
                            timeout = System.currentTimeMillis() + 5000L;
                            packetLayer.send(packet);
                            // TODO move this section to #packetSent
                            if (sequenceToAcknowledge >= 0) {
                                // Wait for acknowledgement.
                                do {
                                    yield();
                                    resend &= (sequenceAcknowledged < sequenceToAcknowledge);
                                    resend &= running;
                                    now = System.currentTimeMillis();
                                } while (resend && (now < timeout));
                            } else {
                                resend = false;
                            }
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            packetEOF();
                        }
                    }
                    allowSend = (pipe == null) || pipe.allowSend();

                    resend &= running;
                    resend &= allowSend;
                    resend &= !isInterrupted();
                    if (resend) {
                        retry--;
                        if (retry < 0) {
                            throw new TimeoutException();
                        }
                    }
                } while (resend);
            }
        }
    }

    @Override
    public void packetAcknowledged(MNPPacket packet) {
        // Nothing to do.
    }

    @Override
    public void packetEOF() {
        cancel();
    }

    @Override
    public void packetReceived(MNPPacket packet) {
        final byte packetType = packet.getType();

        if (packetType == MNPPacket.LA) {
            MNPLinkAcknowledgementPacket ack = (MNPLinkAcknowledgementPacket) packet;
            sequenceAcknowledged = Math.max(sequenceAcknowledged, ack.getSequence());
            if (packetAcknowledge != null) {
                packetLayer.runAcknowledged(packetAcknowledge);
                packetAcknowledge = null;
            }
        } else if (packetType == MNPPacket.LR) {
            sequenceAcknowledged = Math.max(sequenceAcknowledged, 0);
            if (packetAcknowledge != null) {
                packetLayer.runAcknowledged(packetAcknowledge);
                packetAcknowledge = null;
            }
        }
    }

    @Override
    public void packetSending(MNPPacket packet) {
        // Nothing to do.
    }

    @Override
    public void packetSent(MNPPacket packet) {
        // Nothing to do.
    }

    /**
     * Cancel all sending requests.
     */
    public void cancel() {
        running = false;
        packetLayer.removePacketListener(this);
        queueSend.clear();
        // Kill "queueSend.take();"
        interrupt();
    }

    @Override
    public void run() {
        running = true;
        try {
            runSend();
        } catch (TimeoutException te) {
            te.printStackTrace();
            // Notify the owner that error occurred.
            packetLayer.notifyTimeout(te);
        }
        running = false;
    }

    /**
     * Clear the queue.
     */
    public void clear() {
        queueSend.clear();
    }

}
