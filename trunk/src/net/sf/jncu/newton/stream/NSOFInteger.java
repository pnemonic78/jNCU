package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Integer.
 * 
 * @author moshew
 */
public class NSOFInteger extends NSOFImmediate {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("integer");

	/**
	 * Creates a new integer.
	 */
	public NSOFInteger() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Creates a new integer.
	 * 
	 * @param value
	 *            the integer value.
	 */
	public NSOFInteger(int value) {
		super(value);
		setNSClass(NS_CLASS);
	}

	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(IMMEDIATE);
		XLong.encode(getValue() << 2, out);
	}
}
