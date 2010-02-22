package net.sf.jncu.fdil;

/**
 * An FDIL string object.
 * 
 * @author moshew
 */
public class FDString extends FDBinaryObject {

	private final String value;

	/**
	 * Creates a new string.
	 * 
	 * @param value
	 *            the value to represent.
	 */
	public FDString(String value) {
		super();
		this.value = value;
	}

	/**
	 * Creates a new string.
	 * 
	 * @param value
	 *            the value to represent.
	 */
	public FDString(char[] value) {
		this(new String(value));
	}

	/**
	 * Creates a new string.
	 * 
	 * @param value
	 *            the value to represent.
	 */
	public FDString(byte[] value) {
		this(new String(value));
	}

	/**
	 * Is this a rich string?
	 * <p>
	 * You may receive a rich string from a Newton device. A rich string is a
	 * string with embedded ink data. You cannot create a rich string, nor
	 * interpret the data in the ink portion of a rich string. When translating
	 * rich strings, a 0xF700 or 0x1A character is inserted in the place of the
	 * embedded ink, depending on whether you are extracting 16-bit or 8-bit
	 * characters.
	 * 
	 * @return true if rich string.
	 */
	public boolean isRich() {
		return false;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return value.equals(obj);
	}
}
