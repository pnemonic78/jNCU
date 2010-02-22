package net.sf.jncu.fdil;

/**
 * A real is a binary object that contains a double precision floating point
 * number. It is an 8-byte binary object containing an IEEE-754 floating point
 * value.
 * 
 * @author moshew
 */
public class FDReal extends FDBinaryObject {

	private final double value;

	/**
	 * Creates a new real.
	 * 
	 * @param value
	 *            the value to represent.
	 */
	public FDReal(double value) {
		super();
		this.value = value;
	}

}
