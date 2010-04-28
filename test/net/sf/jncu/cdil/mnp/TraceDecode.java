package net.sf.jncu.cdil.mnp;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;

import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * Decode trace dumps.
 * 
 * @author moshew
 */
public class TraceDecode {

	private static final char DIRECTION_1TO2 = CommTrace.CHAR_DIRECTION_1TO2;
	private static final char DIRECTION_2TO1 = CommTrace.CHAR_DIRECTION_2TO1;
	private static final String HEX = "0123456789ABCDEF";
	private final MNPPacketLayer layer = new MNPPacketLayer();

	/**
	 * Creates a new decoder.
	 */
	public TraceDecode() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		File f;
		TraceDecode decoder = new TraceDecode();

		try {
			f = new File(args[0]);
			decoder.parse(f);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void parse(File file) throws IOException {
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

	public void parse(Reader reader) throws IOException {
		PipedInputStream in1To2 = new PipedInputStream(1);
		PipedInputStream in2To1 = new PipedInputStream(1);
		PipedOutputStream buf1To2 = new PipedOutputStream(in1To2);
		PipedOutputStream buf2To1 = new PipedOutputStream(in2To1);

		ProcessPayload pp1 = new ProcessPayload(DIRECTION_1TO2, in1To2);
		ProcessPayload pp2 = new ProcessPayload(DIRECTION_2TO1, in2To1);

		pp1.start();
		pp2.start();

		char c;
		int hex;
		int value;
		char direction = DIRECTION_1TO2;
		int b = reader.read();

		while (b != -1) {
			c = (char) b;

			if (c == CommTrace.CHAR_DIRECTION_1TO2) {
				direction = DIRECTION_1TO2;
			} else if (c == CommTrace.CHAR_DIRECTION_2TO1) {
				direction = DIRECTION_2TO1;
			} else if (Character.isLetterOrDigit(c)) {
				hex = HEX.indexOf(c);
				value = hex;
				b = reader.read();
				c = (char) b;
				hex = HEX.indexOf(c);
				value = (value << 4) | hex;
				if (direction == DIRECTION_1TO2) {
					buf1To2.write(value);
					buf1To2.flush();
				} else if (direction == DIRECTION_2TO1) {
					buf2To1.write(value);
					buf2To1.flush();
				}
			}

			b = reader.read();
		}

		buf1To2.close();
		buf2To1.close();
		pp1.cancel();
		pp2.cancel();
	}

	private class ProcessPayload extends Thread {

		private boolean running = false;
		private final char direction;
		private final InputStream in;

		public ProcessPayload(char direction, InputStream in) {
			super();
			this.direction = direction;
			this.in = in;
		}

		@Override
		public void run() {
			running = true;
			try {
				processPayload(direction, in);
				do {
					yield();
				} while (running);
			} catch (EOFException eof) {
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		private void processPayload(char direction, byte[] payload) throws IOException {
			if ((payload == null) || (payload.length == 0)) {
				return;
			}
			processPayload(direction, new ByteArrayInputStream(payload));
		}

		private void processPayload(char direction, InputStream in) throws IOException {
			MNPPacket packet;
			do {
				packet = layer.receive(in);
				processPacket(direction, packet);
			} while (packet != null);
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
				throw new ClassCastException();
			}
		}

		private void processLA(char direction, MNPLinkAcknowledgementPacket packet) {
			System.out.println(direction + " type:(LA)" + packet.getType() + " trans:" + packet.getTransmitted() + " credit:" + packet.getCredit() + " seq:"
					+ packet.getSequence());
		}

		private void processLD(char direction, MNPLinkDisconnectPacket packet) {
			System.out.println(direction + " type:(LD)" + packet.getType() + " trans:" + packet.getTransmitted() + " reason:" + packet.getReasonCode()
					+ " user:" + packet.getUserCode());
		}

		private void processLR(char direction, MNPLinkRequestPacket packet) {
			System.out.println(direction + " type:(LR)" + packet.getType() + " trans:" + packet.getTransmitted() + " dpo:" + packet.getDataPhaseOpt()
					+ " framing:" + packet.getFramingMode() + " info:" + packet.getMaxInfoLength() + " outstanding:" + packet.getMaxOutstanding());
		}

		private void processLT(char direction, MNPLinkTransferPacket packet) {
			System.out.println(direction + " type:(LT)" + packet.getType() + " trans:" + packet.getTransmitted() + " seq:" + packet.getSequence() + " data:"
					+ dataToString(packet.getData()));
			processCmd(direction, packet);
		}

		public void cancel() {
			running = false;
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
					buf.append((char) b);
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

		private void processCmd(char direction, MNPLinkTransferPacket packet) {
			byte[] data = packet.getData();
			if ((data == null) || (data.length == 0)) {
				return;
			}
			if (DockCommandFromNewton.isCommand(data)) {
				DockCommand cmd = null;
				if (direction == DIRECTION_1TO2) {
					cmd = DockCommandFromNewton.deserialize(data);
					System.out.println(direction + "\tcmd:" + cmd);
				}
			}
		}

	}
}
