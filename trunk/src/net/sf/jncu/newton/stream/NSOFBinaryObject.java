package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Binary Object.
 * 
 * @author Moshe
 */
public class NSOFBinaryObject extends NSOFObject {

	private byte[] value;

	/**
	 * Constructs a new binary object.
	 */
	public NSOFBinaryObject() {
		super();
		setNSClass("binary");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of bytes of data (xlong)
		int length = XLong.decodeValue(in);
		byte[] v = new byte[length];

		// Class (object)
		NSOFSymbol symbol = new NSOFSymbol();
		symbol.decode(in, decoder);
		setNSClass(symbol);

		// Data
		if (in.read(v) != length) {
			throw new EOFException();
		}
		setValue(v);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(BINARY_OBJECT);
		byte[] v = getValue();

		// Number of bytes of data (xlong)
		int length = (v == null) ? 0 : v.length;
		XLong.encode(length, out);

		// Class (object)
		getNSClass().encode(out);

		// Data
		if (v != null) {
			out.write(v);
		}
	}

	/**
	 * Get the value.
	 * 
	 * @return the value.
	 */
	public byte[] getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(byte[] value) {
		this.value = value;
	}

}
