package net.sf.jncu.cdil.mnp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Decode trace dumps.
 * 
 * @author moshew
 */
public class TraceDecode {

	private boolean direction1To2 = true;
	private static final char CHAR_DIRECTION_1TO2 = CommTrace.CHAR_DIRECTION_1TO2;
	private static final char CHAR_DIRECTION_2TO1 = CommTrace.CHAR_DIRECTION_2TO1;
	private static final String HEX = "0123456789ABCDEF";

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

		b = reader.read();
		while (b != -1) {
			c = (char) b;
			value = 0;

			if (c == CHAR_DIRECTION_1TO2) {
				direction1To2 = true;
			} else if (c == CHAR_DIRECTION_2TO1) {
				direction1To2 = false;
			} else if (Character.isDigit(c)) {
				hex = HEX.indexOf(c);
				value = hex;
				b = reader.read();
				c = (char) b;
				hex = HEX.indexOf(c);
				value = (value << 4) | hex;
			}

			b = reader.read();
		}
	}
}
