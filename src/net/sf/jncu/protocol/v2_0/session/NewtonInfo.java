package net.sf.jncu.protocol.v2_0.session;

/**
 * Newton version information.
 * 
 * @author moshew
 */
public class NewtonInfo {

	private int newtonId;
	/**
	 * An integer indicating the manufacturer of the Newton device.
	 */
	private int manufacturerId;
	/**
	 * An integer indicating the hardware type this ROM was built for.
	 */
	private int machineType;
	/**
	 * An integer indicating the ROM version number.
	 */
	private int romVersion;
	/**
	 * An integer indicating the language (English, German, French) and the
	 * stage of the ROM (alpha, beta, final).
	 */
	private int romStage;
	/**
	 * The amount of RAM on the Newton device.
	 */
	private int ramSize;
	/**
	 * An integer representing the height of the screen in pixels. The height
	 * takes into account the current screen orientation.
	 */
	private int screenHeight;
	/**
	 * An integer representing the width of the screen in pixels. The width
	 * takes into account the current screen orientation.
	 */
	private int screenWidth;
	/**
	 * This value is <tt>0</tt> on an unpatched Newton device, and non-zero on a
	 * patched Newton.
	 */
	private int patchVersion;
	/**
	 * Newton Object System (NOS) version.<br>
	 * The version of the NewtonScript interpreter.
	 */
	private int objectSystemVersion;
	/**
	 * The signature of the internal store. Note that this value is changed with
	 * a hard reset.
	 */
	private int internalStoreSignature;
	/** The number of horizontal pixels per inch. */
	private int screenResolutionVertical;
	/** The number of vertical pixels per inch. */
	private int screenResolutionHorizontal;
	/**
	 * The number of bits per pixel.<br>
	 * The bit depth of the LCD screen. For the MessagePad 120, the LCD supports
	 * a monochrome screen depth of 1. The eMate 300 and MessagePad 200 have 4
	 * bit depth LCD screens.
	 */
	private int screenDepth;
	/**
	 * An 8-byte object containing the unique hardware serial number of the
	 * Newton device on those devices that contain this hardware.
	 */
	private long serialNumber;
	/**
	 * The version of the protocol used by the Dock application. On Newton 2.0
	 * devices this is 9. On Newton 2.1 devices this is 11.
	 */
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
	 * Get the major ROM version.
	 * 
	 * @return the version.
	 */
	public int getRomMajorVersion() {
		return (romVersion >> 16) & 0xFFFF;
	}

	/**
	 * Get the minor ROM version.
	 * 
	 * @return the version.
	 */
	public int getRomMinorVersion() {
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
	 * Get the ROM stage.
	 * 
	 * @return the ROM stage.
	 */
	public int getRomStage() {
		return romStage;
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
	 * 
	 * @param patchVersion
	 *            the patch version.
	 */
	public void setPatchVersion(int patchVersion) {
		this.patchVersion = patchVersion;
	}

	/**
	 * Get the NOS version.
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
	 * 
	 * @param targetProtocol
	 *            the protocol.
	 */
	public void setTargetProtocol(int targetProtocol) {
		this.targetProtocol = targetProtocol;
	}

}
