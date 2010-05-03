package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <tt>xlong</tt>
 * 
 * @author Moshe
 */
public class XLong extends NewtonStreamedObjectFormat {

	private int value;

	/**
	 * Constructs a new xlong.
	 */
	public XLong() {
		super();
	}

	/**
	 * Constructs a new xlong.
	 * 
	 * @param value
	 *            the value.
	 */
	public XLong(int value) {
		super();
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.nsof.NewtonStreamedObjectFormat#decode(java.io.InputStream
	 * )
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setValue(decodeValue(in));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.nsof.NewtonStreamedObjectFormat#encode(java.io.OutputStream
	 * )
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		encode(getValue(), out);
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	/**
	 * Encode the XLong.
	 * 
	 * @param value
	 *            the xlong value.
	 * @param out
	 *            the output stream.
	 * @throws IOException
	 *             if an encoding error occurs.
	 */
	public static void encode(int value, OutputStream out) throws IOException {
		if (value < 0xFF) {
			out.write(value & 0xFF);
		} else {
			out.write(0xFF);
			ntohl(value, out);
		}
	}

	/**
	 * Decode an <tt>XLong</tt>.
	 * 
	 * @param in
	 *            the input stream.
	 * @return the xlong.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static XLong decode(InputStream in) throws IOException {
		return new XLong(decodeValue(in));
	}

	/**
	 * Decode an <tt>XLong</tt>.
	 * 
	 * @param in
	 *            the input stream.
	 * @return the xlong.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static int decodeValue(InputStream in) throws IOException {
		// 0 <= value <= 254: unsigned byte
		// else: byte 0xFF followed by signed long
		int l = in.read();
		if (l == -1) {
			throw new EOFException();
		}
		if (l >= 0xFF) {
			l = htonl(in);
		}
		return l;
	}
}
