package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Symbol.
 * 
 * @author Moshe
 */
public class NSOFSymbol extends NSOFString {

	/**
	 * Constructs a new symbol.
	 */
	public NSOFSymbol() {
		super();
		setNSClass("symbol");
	}

	/**
	 * Constructs a new symbol.
	 * 
	 * @param name
	 *            the symbol name.
	 */
	public NSOFSymbol(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of characters in name (xlong)
		int len = XLong.decodeValue(in);
		// Name (bytes)
		byte[] name = new byte[len];
		in.read(name);
		setValue(new String(name, "US-ASCII"));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(SYMBOL);

		String name = getValue();
		// Number of characters in name (xlong)
		XLong.encode(name.length(), out);
		// Name (bytes)
		out.write(name.getBytes("US-ASCII"));
	}

}
