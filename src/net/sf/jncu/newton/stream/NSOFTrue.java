package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author moshew
 */
public class NSOFTrue extends NSOFImmediate {

	/**
	 * Creates a new True.
	 */
	public NSOFTrue() {
		super();
		setValue(0x1A);
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		super.decode(in, decoder);
		setValue(1);
	}

	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(IMMEDIATE);
		XLong xlong = new XLong(0x1A);
		xlong.encode(out);
	}

	/**
	 * Decoder can test if the immediate is TRUE.
	 * 
	 * @param r
	 *            the Ref of an Immediate.
	 * @return true if TRUE.
	 */
	public static boolean isRefTrue(int r) {
		return (r == 0x1A);
	}
}
