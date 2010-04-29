/**
 * 
 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.nsof.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// TODO Auto-generated method stub
		// 0 <= value <= 254: unsigned byte
		// else: byte 0xFF followed by signed long
		int b = in.read();
		if (b == -1) {
			throw new EOFException();
		}
		if ((0 <= b) && (b <= 254)) {
			setValue(b);
		} else if (b == 0xFF) {
			int b3 = in.read();
			int b2 = in.read();
			int b1 = in.read();
			int b0 = in.read();
			if ((b0 == -1) || (b1 == -1) || (b2 == -1) || (b3 == -1)) {
				throw new EOFException();
			}
			int signedLong = ((b3 & 0xFF) << 24) | ((b2 & 0xFF) << 16)
					| ((b1 & 0xFF) << 8) | ((b0 & 0xFF) << 0);
			setValue(signedLong);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.nsof.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
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
	 * 
	 * @see com.mmw.newton.nsof.NewtonStreamedObjectFormat#getId()
	 */
	@Override
	public int getId() {
		return -1;
	}
}
