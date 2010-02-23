package net.sf.jncu.fdil;

/**
 * There is only one special immediate object that you encounter, the nil
 * object. This object, which you can refer to with the constant
 * <tt>kFD_NIL</tt>, is used to signify the lack of information.
 * 
 * @author moshew
 */
public class FDNil extends FDImmediate {

	/** The nil object. */
	public static final FDNil kFD_NIL = new FDNil();

	/**
	 * Creates a new nil.
	 */
	private FDNil() {
		super();
	}

}
