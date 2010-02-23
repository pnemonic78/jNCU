package net.sf.jncu.fdil;

/**
 * There are two Boolean objects: the true object, and the false object. The nil
 * object <tt>kFD_NIL</tt> is not the same as the false object.
 * 
 * @author moshew
 */
public class FDBoolean extends FDImmediate {

	/** The true object. */
	public static final FDBoolean kFD_False = new FDBoolean(false);
	/** The true object. */
	public static final FDBoolean kFD_True = new FDBoolean(true);

	private final boolean value;

	/**
	 * Creates a new boolean.
	 * 
	 * @param value
	 *            the value.
	 */
	private FDBoolean(boolean value) {
		super();
		this.value = value;
	}

	/**
	 * Is the value true?
	 * 
	 * @return the value.
	 */
	public boolean isValue() {
		return value;
	}
}
