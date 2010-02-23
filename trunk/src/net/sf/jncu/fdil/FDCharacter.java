package net.sf.jncu.fdil;

/**
 * Character objects are immediate objects which contain a 16 bit Unicode
 * character.
 * 
 * @author moshew
 */
public class FDCharacter extends FDImmediate {

	protected static final String HEX = "0123456789ABDEF";

	private final char value;

	/**
	 * Creates a new character.
	 * 
	 * @param value
	 *            the value.
	 */
	public FDCharacter(char value) {
		super();
		this.value = value;
	}

	/**
	 * Get the value.<br>
	 * <tt>char FD_GetChar(FD_Handle obj)</tt>
	 * 
	 * @return the value
	 */
	public char getChar() {
		return getWideChar();
	}

	/**
	 * Get the value.<br>
	 * <tt>DIL_WideChar FD_GetWideChar(FD_Handle obj)</tt>
	 * 
	 * @return the value
	 */
	public char getWideChar() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return value;
	}

	/*
	 * (non-Javadoc)
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
