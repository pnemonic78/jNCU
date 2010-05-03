package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - True.
 * 
 * @author moshew
 */
public class NSOFTrue extends NSOFImmediate {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("TRUE");

	/**
	 * Creates a new True.
	 */
	public NSOFTrue() {
		super();
		setNSClass(NS_CLASS);
		setValue(0x1A);
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(IMMEDIATE);
		XLong.encode(0x1A, out);
	}
}
