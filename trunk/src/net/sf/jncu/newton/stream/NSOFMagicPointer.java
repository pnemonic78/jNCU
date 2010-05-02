package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Magic Pointer.
 * 
 * @author moshew
 */
public class NSOFMagicPointer extends NSOFImmediate {

	/**
	 * Creates a new magic pointer.
	 */
	public NSOFMagicPointer() {
		super();
		setNSClass("magicptr");
	}

	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(IMMEDIATE);
		XLong.encode((getValue() << 2) | 0x3, out);
	}
}
