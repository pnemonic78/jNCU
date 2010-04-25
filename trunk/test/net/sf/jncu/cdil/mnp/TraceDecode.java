package net.sf.jncu.cdil.mnp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Decode trace dumps.
 * 
 * @author moshew
 */
public class TraceDecode {

	private static final char CHAR_DIRECTION_1TO2 = CommTrace.CHAR_DIRECTION_1TO2;
	private static final char CHAR_DIRECTION_2TO1 = CommTrace.CHAR_DIRECTION_2TO1;
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
	 *          the array of arguments.
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
		int b;
		char c;
		int hex;
		int value;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] payload;
		char direction1To2 = CHAR_DIRECTION_1TO2;
		char direction = direction1To2;

		b = reader.read();
		while (b != -1) {
			c = (char) b;
			value = 0;

			if (c == CHAR_DIRECTION_1TO2) {
				direction1To2 = CHAR_DIRECTION_1TO2;
			} else if (c == CHAR_DIRECTION_2TO1) {
				direction1To2 = CHAR_DIRECTION_2TO1;
			} else if (Character.isLetterOrDigit(c)) {
				hex = HEX.indexOf(c);
				value = hex;
				b = reader.read();
				c = (char) b;
				hex = HEX.indexOf(c);
				value = (value << 4) | hex;
				out.write(value);
			}

			if (direction != direction1To2) {
				out.close();
				payload = out.toByteArray();
				processPayload(direction, payload);
				out = new ByteArrayOutputStream();
				direction = direction1To2;
			}

			b = reader.read();
		}

		out.close();
		payload = out.toByteArray();
		processPayload(direction, payload);
	}

	private void processPayload(char direction, byte[] payload) throws IOException {
		if ((payload == null) || (payload.length == 0)) {
			return;
		}
		processPayload(direction, new ByteArrayInputStream(payload));
	}

	private void processPayload(char direction, InputStream in) throws IOException {
		if (in.available() == 0) {
			return;
		}
		MNPPacket packet;
		while (in.available() > 0) {
			packet = layer.receive(in);
			processPacket(direction, packet);
		}
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
				+ packet.getData());
	}

}
