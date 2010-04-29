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
		// 0 <= value <= 254: unsigned byte
		// else: byte 0xFF followed by signed long
		int b = in.read();
		if (b == -1) {
			throw new EOFException();
		}
		if (b >= 0xFF) {
			b = htonl(in);
		}
		setValue(b);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.nsof.NewtonStreamedObjectFormat#encode(java.io.OutputStream
	 * )
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		if (value < 0xFF) {
			out.write(value & 0xFF);
		} else {
			out.write(0xFF);
			ntohl(value, out);
		}
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

	/*
	 * (non-Javadoc)
	 * @see com.mmw.newton.nsof.NewtonStreamedObjectFormat#getId()
	 */
	@Override
	public int getId() {
		return -1;
	}
}
