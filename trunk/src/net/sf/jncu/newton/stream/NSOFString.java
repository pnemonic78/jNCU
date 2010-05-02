package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Moshe
 */
public class NSOFString extends NSOFObject {

	protected static final String HEX = "0123456789ABDEF";

	private String value;
	private String toString;

	/**
	 * Constructs a new string.
	 */
	public NSOFString() {
		super();
	}

	/**
	 * Constructs a new string.
	 * 
	 * @param value
	 *            the value.
	 */
	public NSOFString(String value) {
		super();
		setValue(value);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of bytes in string (xlong)
		int numBytes = XLong.decodeValue(in);
		// String (halfwords)
		int len = numBytes >> 1;
		byte[] buf = new byte[len];

		in.read(buf);
		setValue(new String(buf, 0, len - 1, "UTF-16"));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(STRING);
		String s = getValue();
		if (s == null) {
			XLong.encode(0, out);
		} else {
			int len = s.length();
			// 2-bytes per character + null-terminated
			XLong.encode((len + 1) << 1, out);
			out.write(s.getBytes("UTF-16"));
			out.write(0);
			out.write(0);
		}
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
		this.toString = null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (value == null) ? 0 : value.hashCode();
	}

	/**
	 * <code>" [ { stringChar | escSeq } ]* [ truncEscape ] ] "</code>
	 * <p>
	 * stringChar Consists of a tab character or any ASCII character with code
	 * 32–127 except the double quote (") or backslash (\).<br>
	 * escSeq Consists of either a special character specification sequence or a
	 * Unicode specification sequence. The special character specification
	 * sequence is: backslash (\) followed by a quote ("), backslash (\), the
	 * letter n or the letter t. The escape sequence for specifying Unicode
	 * begins with backslash-u (\\u), is followed by any number of groups of
	 * four hexDigits, and ends with backslash-u (\\u).<br>
	 * truncEscape Consists of the shortened Unicode specification sequence. It
	 * is: backslash-u (\\u), is followed by any number of groups of four
	 * hexDigits.
	 */
	@Override
	public String toString() {
		if (toString == null) {
			if (value != null) {
				StringBuffer buf = new StringBuffer();
				char[] chars = value.toCharArray();

				for (char c : chars) {
					if ((c >= 32) && (c <= 127)) {
						buf.append(c);
					} else if (c == '\n') {
						buf.append("\\n");
					} else if (c == '\r') {
						buf.append("\\r");
					} else if (c == '\t') {
						buf.append("\\t");
					} else if (c == '\\') {
						buf.append("\\\\");
					} else if (c == '"') {
						buf.append("\\\"");
					} else {
						char hex12 = HEX.charAt((c >>> 12) & 0x000F);
						char hex8 = HEX.charAt((c >>> 8) & 0x000F);
						char hex4 = HEX.charAt((c >>> 4) & 0x000F);
						char hex0 = HEX.charAt((c >>> 0) & 0x000F);
						buf.append("\\u");
						buf.append(hex12);
						buf.append(hex8);
						buf.append(hex4);
						buf.append(hex0);
					}
				}
				toString = "\"" + value + "\"";
			}
		}
		return toString;
	}
}
