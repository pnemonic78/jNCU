package net.sf.jncu.protocol;

/**
 * Newton version information.
 * 
 * @author moshew
 */
public class NewtonInfo {

	/** English. */
	public static final int ENGLISH = 0;
	/** French. */
	public static final int FRENCH = 1;
	/** German. */
	public static final int GERMAN = 2;

	private int newtonId;
	private int manufacturerId;
	private int machineType;
	private int romVersion;
	private int romStage;
	private int ramSize;
	private int screenHeight;
	private int screenWidth;
	private int patchVersion;
	private int objectSystemVersion;
	private int internalStoreSignature;
	private int screenResolutionVertical;
	private int screenResolutionHorizontal;
	private int screenDepth;
	private long serialNumber;
	private int targetProtocol;

	/**
	 * Creates a new Newton information.
	 */
	public NewtonInfo() {
		super();
	}

	/**
	 * Get the Newton id.
	 * <p>
	 * An almost unique ID which represents a particular Newton. It is a random
	 * number from a very large domain, so very close to unique.
	 * 
	 * @return the Newton id.
	 */
	public int getNewtonId() {
		return newtonId;
	}

	/**
	 * Set the Newton id.
	 * 
	 * @param newtonId
	 *            the Newton id.
	 */
	public void setNewtonId(int newtonId) {
		this.newtonId = newtonId;
	}

	/**
	 * Get the manufacturer id.
	 * <p>
	 * An integer indicating the manufacturer of the Newton device.
	 * 
	 * @return the manufacturer id.
	 */
	public int getManufacturerId() {
		return manufacturerId;
	}

	/**
	 * Set the manufacturer id.
	 * 
	 * @param manufacturerId
	 *            the manufacturer id.
	 */
	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	/**
	 * Get the machine type.
	 * <p>
	 * An integer indicating the hardware type this ROM was built for.
	 * 
	 * @return the machine type.
	 */
	public int getMachineType() {
		return machineType;
	}

	/**
	 * Set the machine type.
	 * 
	 * @param machineType
	 *            the machine type.
	 */
	public void setMachineType(int machineType) {
		this.machineType = machineType;
	}

	/**
	 * Get the major ROM version number.
	 * 
	 * @return the version.
	 */
	public int getMajorVersion() {
		return (romVersion >> 16) & 0xFFFF;
	}

	/**
	 * Get the minor ROM version number.
	 * 
	 * @return the version.
	 */
	public int getMinorVersion() {
		return romVersion & 0xFFFF;
	}

	/**
	 * Set the ROM version.
	 * 
	 * @param romVersion
	 *            the version.
	 */
	public void setRomVersion(int romVersion) {
		this.romVersion = romVersion;
	}

	/**
	 * Get the ROM language.
	 * <p>
	 * An integer indicating the language (English, German, French).
	 * 
	 * @return the ROM stage.
	 * @see #ENGLISH
	 * @see #FRENCH
	 * @see #GERMAN
	 */
	public int getLanguage() {
		return (romStage >> 16) & 0xFFFF;
	}

	/**
	 * Get the ROM stage.
	 * <p>
	 * An integer indicating the stage of the ROM (alpha, beta, final).
	 * 
	 * @return the ROM stage.
	 */
	public int getStage() {
		return romStage & 0xFFFF;
	}

	/**
	 * Set the ROM stage.
	 * 
	 * @param romStage
	 *            the ROM stage.
	 */
	public void setRomStage(int romStage) {
		this.romStage = romStage;
	}

	/**
	 * Get the RAM size.
	 * <p>
	 * The amount of RAM on the Newton device.
	 * 
	 * @return the RAM size.
	 */
	public int getRamSize() {
		return ramSize;
	}

	/**
	 * Set the RAM size.
	 * 
	 * @param ramSize
	 *            the RAM size.
	 */
	public void setRamSize(int ramSize) {
		this.ramSize = ramSize;
	}

	/**
	 * Get the screen height.
	 * <p>
	 * An integer representing the height of the screen in pixels. The height
	 * takes into account the current screen orientation.
	 * 
	 * @return the screen height.
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Set the screen height.
	 * 
	 * @param screenHeight
	 *            the screen height.
	 */
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	/**
	 * Get the screen width.
	 * <p>
	 * An integer representing the width of the screen in pixels. The width
	 * takes into account the current screen orientation.
	 * 
	 * @return the screen width.
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Set the screen width.
	 * 
	 * @param screenWidth
	 *            the screen width.
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * Get the patch version.
	 * 
	 * @return the patch version.
	 */
	public int getPatchVersion() {
		return patchVersion;
	}

	/**
	 * Set the patch version.
	 * <p>
	 * This value is <tt>0</tt> on an unpatched Newton device, and non-zero on a
	 * patched Newton.
	 * 
	 * @param patchVersion
	 *            the patch version.
	 */
	public void setPatchVersion(int patchVersion) {
		this.patchVersion = patchVersion;
	}

	/**
	 * Get the object system version.
	 * <p>
	 * Newton Object System (NOS) version, or Newton Streamed Object Format
	 * (NSOF) version.<br>
	 * The version of the NewtonScript interpreter.
	 * 
	 * @return the version.
	 */
	public int getObjectSystemVersion() {
		return objectSystemVersion;
	}

	/**
	 * Set the NOS version.
	 * 
	 * @param objectSystemVersion
	 *            the version.
	 */
	public void setObjectSystemVersion(int objectSystemVersion) {
		this.objectSystemVersion = objectSystemVersion;
	}

	/**
	 * Get the internal store signature.
	 * <p>
	 * The signature of the internal store. Note that this value is changed with
	 * a hard reset.
	 * 
	 * @return the signature.
	 */
	public int getInternalStoreSignature() {
		return internalStoreSignature;
	}

	/**
	 * Set the internal store signature.
	 * 
	 * @param internalStoreSignature
	 *            the signature.
	 */
	public void setInternalStoreSignature(int internalStoreSignature) {
		this.internalStoreSignature = internalStoreSignature;
	}

	/**
	 * Get the vertical screen resolution.
	 * 
	 * @return the resolution.
	 */
	public int getScreenResolutionVertical() {
		return screenResolutionVertical;
	}

	/**
	 * Set the vertical screen resolution.
	 * <p>
	 * The number of horizontal pixels per inch.
	 * 
	 * @param screenResolutionVertical
	 *            the resolution.
	 */
	public void setScreenResolutionVertical(int screenResolutionVertical) {
		this.screenResolutionVertical = screenResolutionVertical;
	}

	/**
	 * Get the horizontal screen resolution.
	 * 
	 * @return the resolution.
	 */
	public int getScreenResolutionHorizontal() {
		return screenResolutionHorizontal;
	}

	/**
	 * Set the horizontal screen resolution.
	 * <p>
	 * The number of vertical pixels per inch.
	 * 
	 * @param screenResolutionHorizontal
	 *            the resolution.
	 */
	public void setScreenResolutionHorizontal(int screenResolutionHorizontal) {
		this.screenResolutionHorizontal = screenResolutionHorizontal;
	}

	/**
	 * Get the screen depth.
	 * 
	 * @return the depth.
	 */
	public int getScreenDepth() {
		return screenDepth;
	}

	/**
	 * Set the screen depth.
	 * <p>
	 * The number of bits per pixel.<br>
	 * The bit depth of the LCD screen. For the MessagePad 120, the LCD supports
	 * a monochrome screen depth of 1. The eMate 300 and MessagePad 200 have 4
	 * bit depth LCD screens.
	 * 
	 * @param screenDepth
	 *            the depth.
	 */
	public void setScreenDepth(int screenDepth) {
		this.screenDepth = screenDepth;
	}

	/**
	 * Get the serial number.
	 * 
	 * @return the serial number.
	 */
	public long getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Set the serial number.
	 * <p>
	 * An 8-byte object containing the unique hardware serial number of the
	 * Newton device on those devices that contain this hardware.
	 * 
	 * @param serialNumber
	 *            the serial number.
	 */
	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * Get the target protocol.
	 * 
	 * @return the protocol.
	 */
	public int getTargetProtocol() {
		return targetProtocol;
	}

	/**
	 * Set the target protocol.
	 * <p>
	 * The version of the protocol used by the Dock application. On Newton 2.0
	 * devices this is <tt>9</tt>. On Newton 2.1 devices this is <tt>11</tt>.
	 * 
	 * @param targetProtocol
	 *            the protocol.
	 */
	public void setTargetProtocol(int targetProtocol) {
		this.targetProtocol = targetProtocol;
	}

}
