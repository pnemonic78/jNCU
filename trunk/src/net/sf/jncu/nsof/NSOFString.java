/**
 * 
 */
package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Moshe
 * 
 */
public class NSOFString extends NSOFObject {

	private String value;

	/**
	 * Constructs a new object.
	 * 
	 */
	public NSOFString() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setValue(null);

		// Number of bytes in string (xlong)
		int len = in.read();
		if (len == -1) {
			throw new EOFException();
		}
		// String (halfwords)
		int hi;
		int lo;
		int c;
		StringBuffer s = new StringBuffer();

		for (int i = 0; i < len; i++) {
			hi = in.read();
			lo = in.read();
			if ((hi == -1) || (lo == -1)) {
				throw new EOFException();
			}
			c = ((hi & 0xFF) << 8) | ((lo & 0xFF) << 0);
			s.append((char) (c & 0xFFFF));
		}
		setValue(s.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (value == null) ? 0 : value.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO implement me!
		// " [ { stringChar | escSeq } ]* [ truncEscape ] ] "
		// stringChar Consists of a tab character or any ASCII character with
		// code 32–127 except the double quote (") or backslash (\).
		// escSeq Consists of either a special character specification
		// sequence or a unicode specification sequence.The
		// special character specification sequence is: backslash (\)
		// followed by a quote ("), backslash (\), the letter n or
		// the letter t. The escape sequence for specifying
		// Unicode begins with backslash-u (\\u), is followed by
		// any number of groups of four hexDigits, and ends
		// with backslash-u (\\u).
		// truncEscape Consists of the shortened unicode specification
		// sequence. It is: backslash-u (\\u), is followed by
		// any number of groups of four hexDigits.
		return (value == null) ? null : "\"" + value + "\"";
	}
}
