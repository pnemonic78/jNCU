package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Array.
 * 
 * @author Moshe
 */
public class NSOFArray extends NSOFObject {

	private NSOFObject[] value;

	/**
	 * Constructs a new array.
	 */
	public NSOFArray() {
		super();
		setNSClass("array");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Number of slots (xlong)
		int length = XLong.decodeValue(in);
		NSOFObject[] slots = new NSOFObject[length];

		// Class (object)
		NSOFSymbol symbol = new NSOFSymbol();
		symbol.decode(in, decoder);
		setNSClass(symbol);

		// Slot values in ascending order (objects)
		for (int i = 0; i < length; i++) {
			slots[i] = decoder.decode(in);
		}
		setValue(slots);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(ARRAY);

		NSOFObject[] slots = getValue();
		int length = (slots == null) ? 0 : slots.length;

		// Number of slots (xlong)
		XLong.encode(length, out);

		// Class (object)
		getNSClass().encode(out);

		// Slot values in ascending order (objects)
		if (slots != null) {
			for (int i = 0; i < length; i++) {
				slots[i].encode(out);
			}
		}
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public NSOFObject[] getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(NSOFObject[] value) {
		this.value = value;
	}
}
