/**
 * 
 */
package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Moshe
 */
public class NSOFPlainArray extends NSOFArray {

	/**
	 * Constructs a new object.
	 */
	public NSOFPlainArray() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setValue(null);
		// Number of slots (xlong)
		XLong xlong = new XLong();
		xlong.decode(in, decoder);
		int len = xlong.getValue();
		NSOFObject[] entries = new NSOFObject[len];
		// Slot values in ascending order (objects)
		for (int i = 0; i < len; i++) {
			entries[i] = decoder.decode(in);
		}
		setValue(entries);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(PLAIN_ARRAY);

		XLong xlong;
		NSOFObject[] entries = getValue();
		if (entries == null) {
			xlong = new XLong(0);
			xlong.encode(out);
		} else {
			int length = entries.length;
			xlong = new XLong(length);
			xlong.encode(out);
			for (int i = 0; i < length; i++) {
				entries[i].encode(out);
			}
		}
	}
}
