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
	private static final int DIRECTION_1TO2 = 1;
	private static final int DIRECTION_2TO1 = 2;
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
		int direction1To2 = DIRECTION_1TO2;
		int direction = direction1To2;

		b = reader.read();
		while (b != -1) {
			c = (char) b;
			value = 0;

			if (c == CHAR_DIRECTION_1TO2) {
				direction1To2 = DIRECTION_1TO2;
			} else if (c == CHAR_DIRECTION_2TO1) {
				direction1To2 = DIRECTION_2TO1;
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

	private void processPayload(int direction, byte[] payload) throws IOException {
		if ((payload == null) || (payload.length == 0)) {
			return;
		}
		processPayload(direction, new ByteArrayInputStream(payload));
	}

	private void processPayload(int direction, InputStream in) throws IOException {
		if (in.available() == 0) {
			return;
		}
		MNPPacket packet;
		while (in.available() > 0) {
			packet = layer.receive(in);
			System.out.println("d:" + direction + " " + packet);
		}
	}
}
