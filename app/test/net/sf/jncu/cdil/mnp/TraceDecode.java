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

import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.swing.JNCUModuleDialog;

import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;

/**
 * Decode trace dumps.
 *
 * @author moshew
 */
public class TraceDecode {

    private static final char DIRECTION_IN = CommTrace.DIRECTION_FROM_NEWTON;
    private static final char DIRECTION_OUT = CommTrace.DIRECTION_FROM_PC;

    private static final String HEX = "0123456789ABCDEF";

    private File file;
    private PipedInputStream receivedFromNewton;
    private PipedInputStream sentToNewton;
    private PipedOutputStream bufFromNewton;
    private PipedOutputStream bufToNewton;

    /**
     * Creates a new decoder.
     */
    public TraceDecode() throws IOException {
        super();
        Thread t = Thread.currentThread();
        t.setName("TraceDecode-" + t.getId());
        receivedFromNewton = new PipedInputStream();
        sentToNewton = new PipedInputStream();
        bufFromNewton = new PipedOutputStream(receivedFromNewton);
        bufToNewton = new PipedOutputStream(sentToNewton);
    }

    /**
     * Main method.
     *
     * @param args the array of arguments.
     */
    public static void main(String[] args) {
        File f = new File(args[0]);
        TraceDecode decoder;
        try {
            decoder = new TraceDecode();
            decoder.setFile(f);
            decoder.run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (!Boolean.getBoolean("debug"))
            System.exit(0);// Kill all threads.
    }

    protected void setFile(File f) {
        this.file = f;
    }

    public void run() throws Exception {
        parse(file);

        // Wait for commands to finish.
        Thread.sleep(2000);

        bufFromNewton.close();
        bufToNewton.close();
    }

    public void parse(File file) throws Exception {
        Reader reader = null;
        try {
            reader = new FileReader(file);
            parse(reader);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void parse(Reader reader) throws Exception {
        DecodePayload dp = createDecodePayload(receivedFromNewton, sentToNewton);
        dp.start();

        char c;
        int hex;
        int value;
        char direction = 0;
        int b = reader.read();

        while (b != -1) {
            c = (char) b;

            if (c == DIRECTION_IN) {
                direction = DIRECTION_IN;
            } else if (c == DIRECTION_OUT) {
                direction = DIRECTION_OUT;
            } else if (Character.isLetterOrDigit(c)) {
                hex = HEX.indexOf(c);
                value = hex;
                b = reader.read();
                c = (char) b;
                hex = HEX.indexOf(c);
                value = (value << 4) | hex;
                if (direction == DIRECTION_IN) {
                    bufFromNewton.write(value);
                    bufFromNewton.flush();
                } else if (direction == DIRECTION_OUT) {
                    bufToNewton.write(value);
                    bufToNewton.flush();
                }
            }

            b = reader.read();
        }
    }

    protected DecodePayload createDecodePayload(InputStream receivedFromNewton, InputStream sentToNewton) throws Exception {
        return new DecodePayload(receivedFromNewton, sentToNewton);
    }

    protected class DecodePayload extends Thread implements MNPPacketListener, DockCommandListener {

        private boolean runReceived;
        private boolean runSent;
        private CDLayer layer;
        private MNPPipe pipe;
        private final TraceDecodePacketLayer packetLayer;
        private final CDCommandLayer<MNPPacket, MNPPacketLayer> cmdLayer;
        private JNCUModuleDialog progress;
        private boolean done;

        public DecodePayload(InputStream receivedFromNewton, InputStream sentToNewton) throws Exception {
            super();
            setName("DecodePayload-" + getId());

            this.layer = CDLayer.getInstance();
            this.pipe = new TraceDecodePipe(layer, receivedFromNewton, sentToNewton);

            this.packetLayer = (TraceDecodePacketLayer) pipe.getPacketLayer();
            packetLayer.addPacketListener(this);

            this.cmdLayer = pipe.getCommandLayer();
            cmdLayer.addCommandListener(this);

            layer.setState(pipe, CDState.DISCONNECTED);
            pipe.startListening();
            runReceived = true;
        }

        public MNPPipe getPipe() {
            return pipe;
        }

        /**
         * Reader is running in packet layer, so writer can run here.<br>
         * Read bytes and re-construct the MNP packets sent.
         */
        @Override
        public void run() {
            runSent = true;

            byte[] payload;
            MNPPacket packet;

            try {
                do {
                    payload = packetLayer.readSent();
                    packet = MNPPacketFactory.getInstance().createLinkPacket(payload);
                    if (runSent && (packet != null))
                        packetLayer.send(packet);
                } while (runSent && (packet != null) && !isInterrupted());
            } catch (EOFException eofe) {
                // ignore
                System.err.println("EOF");
            } catch (Exception e) {
                e.printStackTrace();
            }

            runSent = false;
            notifyDone();
            System.out.println("End run " + getName());
        }

        protected void notifyDone() {
            if (!runReceived && !runSent) {
                try {
                    if (progress != null)
                        progress.close();
                    pipe.disconnect();
                    pipe.dispose();
                    layer.shutDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            done = true;
        }

        @Override
        public void packetAcknowledged(MNPPacket packet) {
        }

        @Override
        public void packetEOF() {
            if (!done)
                System.out.println("packet EOF");
            runReceived = false;
            runSent = false;
            notifyDone();
        }

        @Override
        public void packetReceived(MNPPacket packet) {
            processPacket(DIRECTION_IN, packet);
        }

        @Override
        public void packetSending(MNPPacket packet) {
        }

        @Override
        public void packetSent(MNPPacket packet) {
            processPacket(DIRECTION_OUT, packet);
        }

        @Override
        public void commandReceiving(DockCommandFromNewton command, int progress, int total) {
            System.out.println(DIRECTION_IN + "\tcmd rcv:" + command + " " + progress + "/" + total);
            JNCUModuleDialog monitor = getProgress();
            if (monitor != null) {
                monitor.setMaximum(total);
                monitor.setProgress(progress);
                monitor.setNote(String.format("Receiving %d%%\u2026", (progress * 100) / total));
            }
        }

        @Override
        public void commandReceived(DockCommandFromNewton command) {
            System.out.println(DIRECTION_IN + "\tcmd rvd:" + command);
        }

        @Override
        public void commandSending(DockCommandToNewton command, int progress, int total) {
            System.out.println(DIRECTION_OUT + "\tcmd snd:" + command + " " + progress + "/" + total);
            JNCUModuleDialog monitor = getProgress();
            if (monitor != null) {
                monitor.setMaximum(total);
                monitor.setProgress(progress);
                monitor.setNote(String.format("Sending %d%%\u2026", (progress * 100) / total));
            }
        }

        @Override
        public void commandSent(DockCommandToNewton command) {
            System.out.println(DIRECTION_OUT + "\tcmd snt:" + command);
        }

        @Override
        public void commandEOF() {
            if (!done)
                System.out.println("cmd EOF");
        }

        private void processPacket(char direction, MNPPacket packet) {
            switch (packet.getType()) {
                case MNPPacket.LA:
                    processLA(direction, (MNPLinkAcknowledgementPacket) packet);
                    break;
                case MNPPacket.LD:
                    processLD(direction, (MNPLinkDisconnectPacket) packet);
                    break;
                case MNPPacket.LR:
                    processLR(direction, (MNPLinkRequestPacket) packet);
                    break;
                case MNPPacket.LT:
                    processLT(direction, (MNPLinkTransferPacket) packet);
                    break;
                default:
                    throw new ClassCastException("unknown packet type");
            }
        }

        private void processLA(char direction, MNPLinkAcknowledgementPacket packet) {
            System.out
                    .println(direction + " type:(LA)" + packet.getType() + " trans:" + packet.getTransmitted() + " credit:" + packet.getCredit() + " seq:" + packet.getSequence());
        }

        private void processLD(char direction, MNPLinkDisconnectPacket packet) {
            System.out.println(direction + " type:(LD)" + packet.getType() + " trans:" + packet.getTransmitted() + " reason:" + packet.getReasonCode() + " user:"
                    + packet.getUserCode());
        }

        private void processLR(char direction, MNPLinkRequestPacket packet) {
            System.out.println(direction + " type:(LR)" + packet.getType() + " trans:" + packet.getTransmitted() + " dpo:" + packet.getDataPhaseOpt() + " framing:"
                    + packet.getFramingMode() + " info:" + packet.getMaxInfoLength() + " outstanding:" + packet.getMaxOutstanding() + " protocol:" + packet.getProtocol());
        }

        private void processLT(char direction, MNPLinkTransferPacket packet) {
            System.out.println(direction + " type:(LT)" + packet.getType() + " trans:" + packet.getTransmitted() + " seq:" + packet.getSequence() + " data:"
                    + dataToString(packet.getData()));
        }

        private String dataToString(byte[] data) {
            StringBuffer buf = new StringBuffer();
            buf.append('[');
            int b;
            for (int i = 0; i < data.length; i++) {
                if (i > 0) {
                    buf.append(',');
                }
                b = data[i] & 0xFF;
                buf.append("0x");
                if (b < 0x10) {
                    buf.append('0');
                }
                buf.append(Integer.toHexString(b));
                if ((b >= 0x020) && (b <= 0x7E)) {
                    buf.append(' ');
                    buf.appendCodePoint(b);
                }
            }
            buf.append(']');
            return buf.toString();
        }

        /**
         * Get the progress monitor.
         *
         * @return the progress.
         */
        protected JNCUModuleDialog getProgress() {
            if (progress == null) {
                progress = new JNCUModuleDialog(null, "Decode Payload", null, 0, 255);
            }
            return progress;
        }
    }
}
