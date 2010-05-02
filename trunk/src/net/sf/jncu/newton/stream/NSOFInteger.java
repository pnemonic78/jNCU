package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Integer.
 * 
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
	public void encode(OutputStream out) throws IOException {
		out.write(IMMEDIATE);
		XLong.encode(getValue() << 2, out);
	}
}
