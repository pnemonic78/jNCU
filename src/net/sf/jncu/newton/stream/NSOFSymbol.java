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

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("symbol");

	private static final String ENCODING = "MacRoman";

	/**
	 * Constructs a new symbol.
	 */
	public NSOFSymbol() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Constructs a new symbol.
	 * 
	 * @param name
	 *            the symbol name.
	 */
	public NSOFSymbol(String name) {
		this();
		setValue(name);
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
		setValue(new String(name, ENCODING));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(SYMBOL);

		String name = getValue();
		// Number of characters in name (xlong)
		XLong.encode(name.length(), out);
		// Name (bytes)
		out.write(name.getBytes(ENCODING));
	}

}
