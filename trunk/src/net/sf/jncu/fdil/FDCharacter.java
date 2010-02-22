package net.sf.jncu.fdil;

/**
 * @author moshew
 */
public class FDCharacter extends FDImmediate {

	protected static final String HEX = "0123456789ABDEF";

	private char value;

	/**
	 * Creates a new .
	 */
	public FDCharacter() {
		super();
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public char getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(char value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		char hex0 = HEX.charAt(value & 0x000F);
		char hex1 = HEX.charAt((value >> 4) & 0x000F);
		char hex2 = HEX.charAt((value >> 8) & 0x000F);
		char hex3 = HEX.charAt((value >> 12) & 0x000F);
		return "$\\" + hex3 + hex2 + hex1 + hex0;
	}
}
