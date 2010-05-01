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
public class NSOFArray extends NSOFObject {

	private NSOFObject[] value;
	private NSOFSymbol arrayClass;

	/**
	 * Constructs a new array.
	 */
	public NSOFArray() {
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
		NSOFObject[] slots = new NSOFObject[len];
		// Class (object)
		// TODO implement me
		// Slot values in ascending order (objects)
		for (int i = 0; i < len; i++) {
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

		XLong.encode(length, out);

		arrayClass.encode(out);

		for (int i = 0; i < length; i++) {
			slots[i].encode(out);
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

	/**
	 * Set the array class.
	 * 
	 * @param arrayClass
	 *            the array class.
	 */
	public void setArrayClass(NSOFSymbol arrayClass) {
		this.arrayClass = arrayClass;
	}

	/**
	 * Set the array class.
	 * 
	 * @param arrayClass
	 *            the array class.
	 */
	public void setArrayClass(String arrayClass) {
		setArrayClass(new NSOFSymbol(arrayClass));
	}

	/**
	 * Get the array class.
	 * 
	 * @return the array class.
	 */
	public NSOFSymbol getArrayClass() {
		return arrayClass;
	}
}
