package net.sf.jncu.cdil.mnp;

import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;

import javax.swing.ProgressMonitor;

import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;

/**
 * Decode trace dumps.
 * 
 * @author moshew
 */
public class TraceDecode {

	private static final char DIRECTION_IN = CommTrace.CHAR_DIRECTION_1TO2;
	private static final char DIRECTION_OUT = CommTrace.CHAR_DIRECTION_2TO1;

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
	 * @param args
	 *            the array of arguments.
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
	}

	private void setFile(File f) {
		this.file = f;
	}

	public void run() throws Exception {
		parse(file);
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
		DecodePayload dp = new DecodePayload(receivedFromNewton, sentToNewton);
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

		Thread.sleep(2000);// Wait for commands to finish.

		bufFromNewton.close();
		bufToNewton.close();
	}

	class DecodePayload extends Thread implements MNPPacketListener, DockCommandListener {

		private boolean runReceived;
		private boolean runSent;
		private CDLayer layer;
		private MNPPipe pipe;
		private final TraceDecodePacketLayer packetLayer;
		private final CDCommandLayer<MNPPacket> cmdLayer;
		private ProgressMonitor progress;

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
				} while (runSent && (packet != null));
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
					pipe.disconnect();
					pipe.dispose();
					layer.shutDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void packetReceived(MNPPacket packet) {
			processPacket(DIRECTION_IN, packet);
		}

		@Override
		public void packetSent(MNPPacket packet) {
			processPacket(DIRECTION_OUT, packet);
		}

		@Override
		public void packetAcknowledged(MNPPacket packet) {
		}

		@Override
		public void packetEOF() {
			System.out.println("packet EOF");
			runReceived = false;
			runSent = false;
			notifyDone();
		}

		@Override
		public void commandReceiving(IDockCommandFromNewton command, int progress, int total) {
			System.out.println(DIRECTION_IN + "\tcmd rcv:" + command + " " + progress + "/" + total);
			ProgressMonitor monitor = getProgress();
			monitor.setMaximum(total);
			monitor.setProgress(progress);
			monitor.setNote(String.format("Receiving %d%%\u2026", (progress * 100) / total));
		}

		@Override
		public void commandReceived(IDockCommandFromNewton command) {
			System.out.println(DIRECTION_IN + "\tcmd rcv:" + command);
		}

		@Override
		public void commandSending(IDockCommandToNewton command, int progress, int total) {
			System.out.println(DIRECTION_OUT + "\tcmd snd:" + command + " " + progress + "/" + total);
			ProgressMonitor monitor = getProgress();
			monitor.setMaximum(total);
			monitor.setProgress(progress);
			monitor.setNote(String.format("Sending %d%%\u2026", (progress * 100) / total));
		}

		@Override
		public void commandSent(IDockCommandToNewton command) {
			System.out.println(DIRECTION_OUT + "\tcmd snt:" + command);
		}

		@Override
		public void commandEOF() {
			System.out.println("cmd EOF");
		}

		private void processPacket(char direction, MNPPacket packet) {
			if (packet instanceof MNPLinkAcknowledgementPacket) {
				processLA(direction, (MNPLinkAcknowledgementPacket) packet);
			} else if (packet instanceof MNPLinkDisconnectPacket) {
				processLD(direction, (MNPLinkDisconnectPacket) packet);
			} else if (packet instanceof MNPLinkRequestPacket) {
				processLR(direction, (MNPLinkRequestPacket) packet);
			} else if (packet instanceof MNPLinkTransferPacket) {
				processLT(direction, (MNPLinkTransferPacket) packet);
			} else {
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
					+ packet.getFramingMode() + " info:" + packet.getMaxInfoLength() + " outstanding:" + packet.getMaxOutstanding());
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
				if ((b >= 0x020) && (b <= 0x7E)) {
					buf.appendCodePoint(b);
				} else {
					buf.append("0x");
					if (b < 0x10) {
						buf.append('0');
					}
					buf.append(Integer.toHexString(b));
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
		protected ProgressMonitor getProgress() {
			if (progress == null) {
				progress = new ProgressMonitor(null, "Decode Payload", null, 0, 255);
			}
			return progress;
		}
	}

}
