package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author moshew
 */
public class NSOFInteger extends NSOFImmediate {

	/**
	 * Creates a new integer.
	 */
	public NSOFInteger() {
		super();
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		super.decode(in, decoder);
		int val = getValue();
		setValue(val >> 2);
	}

	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(IMMEDIATE);
		XLong xlong = new XLong(getValue() << 2);
		xlong.encode(out);
	}

	/**
	 * Decoder can test if the immediate is an integer.
	 * 
	 * @param r
	 *            the Ref of an Immediate.
	 * @return true if an integer.
	 */
	public static boolean isRefInteger(int r) {
		return (r & 0x3) == 0x0;
	}
}
