package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author moshew
 */
public class NSOFMagicPointer extends NSOFImmediate {

	/**
	 *Creates a new magic pointer.
	 */
	public NSOFMagicPointer() {
		super();
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// TODO Auto-generated method stub
		super.decode(in, decoder);
	}

	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(IMMEDIATE);
		XLong.encode((getValue() << 2) | 0x3, out);
	}
}
