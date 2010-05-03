package net.sf.jncu.newton.stream;

/**
 * Newton Streamed Object Format - Object.
 * 
 * @author Moshe
 */
public abstract class NSOFObject extends NewtonStreamedObjectFormat {

	private NSOFSymbol nsClass;
	private int id;

	/**
	 * Constructs a new object.
	 */
	public NSOFObject() {
		super();
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param nsClass
	 *            the object class.
	 */
	public void setClass(NSOFSymbol nsClass) {
		setNSClass(nsClass);
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param nsClassName
	 *            the object class name.
	 */
	public void setClass(String nsClassName) {
		setNSClass(nsClassName);
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param nsClass
	 *            the object class.
	 */
	public void setNSClass(NSOFSymbol nsClass) {
		this.nsClass = nsClass;
	}

	/**
	 * Set the NewtonScript object class.
	 * 
	 * @param nsClassName
	 *            the object class name.
	 */
	public void setNSClass(String nsClassName) {
		setNSClass(new NSOFSymbol(nsClassName));
	}

	/**
	 * Get the NewtonScript object class.
	 * 
	 * @return the object class.
	 */
	public NSOFSymbol getNSClass() {
		return nsClass;
	}

	/**
	 * Get the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id.
	 * 
	 * @param id
	 *            the id.
	 */
	public void setId(int id) {
		this.id = id;
	}
}
