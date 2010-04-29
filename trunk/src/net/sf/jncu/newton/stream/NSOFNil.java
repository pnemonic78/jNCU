package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Moshe
 */
public class NSOFNil extends NSOFObject {

	/**
	 * Constructs a new Nil.
	 */
	public NSOFNil() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(NIL);
	}

	/**
	 * Decoder can test if the immediate is a NIL.
	 * 
	 * @param r
	 *            the Ref of an Immediate.
	 * @return true if NIL.
	 */
	public static boolean isRefNil(int r) {
		return r == 0x2;
	}

}
