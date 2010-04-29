package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Moshe
 */
public class NSOFSymbol extends NSOFString {

	/**
	 * Constructs a new symbol.
	 */
	public NSOFSymbol() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of characters in name (xlong)
		XLong xlong = new XLong();
		xlong.decode(in, decoder);
		int len = xlong.getValue();
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
		XLong xlong = new XLong(name.length());
		xlong.encode(out);
		out.write(name.getBytes("US-ASCII"));
	}

}
