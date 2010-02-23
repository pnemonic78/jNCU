package net.sf.jncu.fdil;

/**
 * Integer objects are just that: objects containing integral values. The
 * integers are stored in a 30-bit field, allowing them a range of
 * <tt>-536,870,912...536,870,911</tt>.
 * 
 * @author moshew
 */
public class FDInteger extends FDObject {

	/**
	 * A constant holding the minimum value an <code>integer</code> can have,
	 * -2<sup>29</sup>.
	 */
	public static final int MIN_VALUE = -536870912;
	/**
	 * A constant holding the maximum value an <code>integer</code> can have,
	 * 2<sup>29</sup>-1.
	 */
	public static final int MAX_VALUE = 536870911;

	private final int value;

	/**
	 * Creates a new integer.
	 * 
	 * @param value
	 *            the value.
	 */
	public FDInteger(int value) {
		super();
		this.value = value & 0x1FFFFFFF;
	}

	/**
	 * Get the value.
	 * 
	 * @return the value.
	 */
	public int getValue() {
		return value;
	}
}
