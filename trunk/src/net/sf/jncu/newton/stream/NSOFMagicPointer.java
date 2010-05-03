package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Magic Pointer.
 * 
 * @author moshew
 */
public class NSOFMagicPointer extends NSOFImmediate {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("magicptr");

	/**
	 * Creates a new magic pointer.
	 */
	public NSOFMagicPointer() {
		super();
		setNSClass(NS_CLASS);
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(IMMEDIATE);
		XLong.encode((getValue() << 2) | 0x3, out);
	}
}
