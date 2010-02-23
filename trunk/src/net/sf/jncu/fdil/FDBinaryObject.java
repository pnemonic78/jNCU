package net.sf.jncu.fdil;

/**
 * A binary object consist of a series of raw bytes. You may store any data you
 * wish in a binary object. The object may also contain a class symbol
 * identifying the data.
 * <p>
 * Binary objects are limited to a size of 16 MB.
 * 
 * @author moshew
 */
public class FDBinaryObject extends FDPointer {

	private byte[] value;

	/**
	 * Creates a new binary object.
	 * 
	 * @param value
	 *            the value.
	 */
	public FDBinaryObject(byte[] value) {
		super();
		this.value = value;
	}

	/**
	 * Creates a new binary object.
	 */
	protected FDBinaryObject() {
		this(null);
	}

	/**
	 * Get the value.
	 * 
	 * @return the value.
	 */
	public byte[] getData() {
		return value;
	}

	/**
	 * Returns the length of the array.
	 * 
	 * @return the length.
	 */
	public int getLength() {
		return value.length;
	}

	/**
	 * Grow or shrink the data.
	 * <p>
	 * You can change the size of a binary object with the <tt>FD_SetLength</tt>
	 * function. However, any pointers to a binary object’s contents are
	 * invalidated by calling <tt>FD_SetLength</tt>, since the data might have
	 * been moved.
	 * 
	 * @param length
	 *            the array length.
	 */
	public void setLength(int length) {
		byte[] value2 = new byte[length];
		System.arraycopy(value, 0, value2, 0, Math.min(value.length, length));
		this.value = value2;
	}
}
