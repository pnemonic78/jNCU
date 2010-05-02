package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Plain Array.
 * 
 * @author Moshe
 */
public class NSOFPlainArray extends NSOFArray {

	/**
	 * Constructs a new array.
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
		int len = XLong.decodeValue(in);
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

		NSOFObject[] slots = getValue();
		if (slots == null) {
			XLong.encode(0, out);
		} else {
			int length = slots.length;
			XLong.encode(length, out);
			for (int i = 0; i < length; i++) {
				slots[i].encode(out);
			}
		}
	}
}
