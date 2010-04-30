package net.sf.jncu.protocol.v2_0.session;

/**
 * Newton version information.
 * 
 * @author moshew
 */
public class NewtonInfo {

	/**
	 * An almost unique ID which represents a particular Newton. It is a random
	 * number from a very large domain, so very close to unique.
	 */
	public int newtonId;
	/**
	 * An integer indicating the manufacturer of the Newton device.
	 */
	public int manufacturerId;
	/**
	 * An integer indicating the hardware type this ROM was built for.
	 */
	public int machineType;
	/**
	 * An integer indicating the ROM version number.
	 */
	public int romVersion;
	/**
	 * An integer indicating the language (English, German, French) and the
	 * stage of the ROM (alpha, beta, final).
	 */
	public int romStage;
	/**
	 * The amount of RAM on the Newton device.
	 */
	public int ramSize;
	/**
	 * An integer representing the height of the screen in pixels. The height
	 * takes into account the current screen orientation.
	 */
	public int screenHeight;
	/**
	 * An integer representing the width of the screen in pixels. The width
	 * takes into account the current screen orientation.
	 */
	public int screenWidth;
	/**
	 * This value is 0 on an unpatched Newton device, and non-zero otherwise.
	 */
	public int patchVersion;
	/**
	 * Newton object system version.<br>
	 * The version of the NewtonScript interpreter.
	 */
	public int objectSystemVersion;
	/**
	 * The signature of the internal store. Note that this value is changed with
	 * a hard reset.
	 */
	public int internalStoreSignature;
	/** The number of horizontal pixels per inch. */
	public int screenResolutionVertical;
	/** The number of vertical pixels per inch. */
	public int screenResolutionHorizontal;
	/** The number of bits per pixel. */
	public int screenDepth;
	/**
	 * A bit field. The following two bits are defined:<br>
	 * 1 = has serial number <br>
	 * 2 = has target protocol
	 */
	public int systemFlags;
	/**
	 * An 8-byte object containing the unique hardware serial number of the
	 * Newton device on those devices that contain this hardware.
	 */
	public long serialNumber;
	/**
	 * The version of the protocol used by the Dock application. On Newton 2.0
	 * devices this is 9. On Newton 2.1 devices this is 11.
	 */
	public int targetProtocol;

	/**
	 * Creates a new version information.
	 */
	public NewtonInfo() {
		super();
	}

}
