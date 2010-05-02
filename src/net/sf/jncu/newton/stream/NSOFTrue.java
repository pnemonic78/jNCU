package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - True.
 * 
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
	public void encode(OutputStream out) throws IOException {
		out.write(IMMEDIATE);
		XLong.encode(0x1A, out);
	}
}
