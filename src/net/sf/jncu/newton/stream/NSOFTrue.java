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
		XLong.encode(0x1A, out);
	}
}
