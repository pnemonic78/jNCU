/**
 * 
 */
package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Moshe
 * 
 */
public class NSOFSymbol extends NSOFString {

	/**
	 * Constructs a new object.
	 * 
	 */
	public NSOFSymbol() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setValue(null);
		// Number of characters in name (xlong)
		XLong xlong = new XLong();
		xlong.decode(in, decoder);
		int len = xlong.getValue();
		// Name (bytes)
		int c;
		StringBuffer s = new StringBuffer();

		for (int i = 0; i < len; i++) {
			c = in.read();
			if ((c == -1)) {
				throw new EOFException();
			}
			s.append((char) (c & 0xFF));
		}
		setValue(s.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		// TODO Auto-generated method stub

	}

}
