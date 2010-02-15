package net.sf.jncu;

/** Newton Information block. */
public class NewtonInfo {

	/**
	 * An almost unique ID which represents a particular Newton. It is a random
	 * number from a very large domain, so very close to unique.
	 */
	private int fNewtonID;
	/**
	 * An integer indicating the manufacturer of the Newton device.
	 */
	private int fManufacturer;
	/**
	 * An integer indicating the hardware type this ROM was built for.
	 */
	private int fMachineType;
	/** An integer indicating the ROM version number. */
	private int fROMVersion;
	/**
	 * An integer indicating the language (English, German, French) and the
	 * stage of the ROM (alpha, beta, final).
	 */
	private int fROMStage;
	/** The amount of RAM on the Newton device. */
	private int fRAMSize;
	/**
	 * An integer representing the height of the screen in pixels. The height
	 * takes into account the current screen orientation.
	 */
	private int fScreenHeight;
	/**
	 * An integer representing the width of the screen in pixels. The width
	 * takes into account the current screen orientation.
	 */
	private int fScreenWidth;
	/**
	 * This value is 0 on an unpatched Newton device, and non-zero otherwise.
	 */
	private int fPatchVersion;
	/**
	 * The version of the NewtonScript interpreter.
	 */
	private int fNOSVersion;
	/**
	 * The signature of the internal store. Note that this value is changed with
	 * a hard reset.
	 */
	private int fInternalStoreSig;
	/** The number of horizontal pixels per inch. */
	private int fScreenResolutionV;
	/** The number of vertical pixels per inch. */
	private int fScreenResolutionH;
	/** The number of bits per pixel. */
	private int fScreenDepth;
	/**
	 * A bit field. The following two bits are defined:<br>
	 * 1 = has serial number <br>
	 * 2 = has target protocol
	 */
	private int fSystemFlags;
	/**
	 * An 8-byte object containing the unique hardware serial number of the
	 * Newton device on those devices that contain this hardware.
	 */
	private long fSerialNumber;
	/**
	 * The version of the protocol used by the Dock application. On Newton 2.0
	 * devices this is 9. On Newton 2.1 devices this is 11.
	 */
	private int fTargetProtocol;

}
