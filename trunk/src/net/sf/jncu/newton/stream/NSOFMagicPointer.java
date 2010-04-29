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
		// TODO Auto-generated method stub
		super.encode(out);
	}

	/**
	 * Decoder can test if the immediate is an integer.
	 * 
	 * @param r
	 *            the Ref of an Immediate.
	 * @return true if an integer.
	 */
	public static boolean isRefMagicPointer(int r) {
		return (r & 0x3) == 0x3;
	}
}
