/**
 * 
 */
package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Moshe
 * 
 */
public class NSOFPlainArray extends NSOFArray {

	/**
	 * Constructs a new object.
	 * 
	 */
	public NSOFPlainArray() {
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
		// Number of slots (xlong)
		XLong xlong = new XLong();
		xlong.decode(in, decoder);
		int len = xlong.getValue();
		NSOFObject[] slots = new NSOFObject[len];
		// Slot values in ascending order (objects)
		for (int i = 0; i < len; i++) {
			slots[i] = decoder.decode(in);
		}
		setValue(slots);
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
