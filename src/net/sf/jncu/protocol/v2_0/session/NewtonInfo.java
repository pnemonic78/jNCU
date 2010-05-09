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
	 * This value is 0 on an unpatched Newton device, and non-zero otherwise.
	 */
	private int patchVersion;
	/**
	 * Newton object system version.<br>
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
	/** The number of bits per pixel. */
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
	 * Creates a new version information.
	 */
	public NewtonInfo() {
		super();
	}

	/**
	 * @return the newtonId
	 */
	public int getNewtonId() {
		return newtonId;
	}

	/**
	 * @param newtonId
	 *            the newtonId to set
	 */
	public void setNewtonId(int newtonId) {
		this.newtonId = newtonId;
	}

	/**
	 * @return the manufacturerId
	 */
	public int getManufacturerId() {
		return manufacturerId;
	}

	/**
	 * @param manufacturerId
	 *            the manufacturerId to set
	 */
	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	/**
	 * @return the machineType
	 */
	public int getMachineType() {
		return machineType;
	}

	/**
	 * @param machineType
	 *            the machineType to set
	 */
	public void setMachineType(int machineType) {
		this.machineType = machineType;
	}

	/**
	 * @return the romVersion
	 */
	public int getRomVersion() {
		return romVersion;
	}

	/**
	 * @param romVersion
	 *            the romVersion to set
	 */
	public void setRomVersion(int romVersion) {
		this.romVersion = romVersion;
	}

	/**
	 * @return the romStage
	 */
	public int getRomStage() {
		return romStage;
	}

	/**
	 * @param romStage
	 *            the romStage to set
	 */
	public void setRomStage(int romStage) {
		this.romStage = romStage;
	}

	/**
	 * @return the ramSize
	 */
	public int getRamSize() {
		return ramSize;
	}

	/**
	 * @param ramSize
	 *            the ramSize to set
	 */
	public void setRamSize(int ramSize) {
		this.ramSize = ramSize;
	}

	/**
	 * @return the screenHeight
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * @param screenHeight
	 *            the screenHeight to set
	 */
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	/**
	 * @return the screenWidth
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * @param screenWidth
	 *            the screenWidth to set
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * @return the patchVersion
	 */
	public int getPatchVersion() {
		return patchVersion;
	}

	/**
	 * @param patchVersion
	 *            the patchVersion to set
	 */
	public void setPatchVersion(int patchVersion) {
		this.patchVersion = patchVersion;
	}

	/**
	 * @return the objectSystemVersion
	 */
	public int getObjectSystemVersion() {
		return objectSystemVersion;
	}

	/**
	 * @param objectSystemVersion
	 *            the objectSystemVersion to set
	 */
	public void setObjectSystemVersion(int objectSystemVersion) {
		this.objectSystemVersion = objectSystemVersion;
	}

	/**
	 * @return the internalStoreSignature
	 */
	public int getInternalStoreSignature() {
		return internalStoreSignature;
	}

	/**
	 * @param internalStoreSignature
	 *            the internalStoreSignature to set
	 */
	public void setInternalStoreSignature(int internalStoreSignature) {
		this.internalStoreSignature = internalStoreSignature;
	}

	/**
	 * @return the screenResolutionVertical
	 */
	public int getScreenResolutionVertical() {
		return screenResolutionVertical;
	}

	/**
	 * @param screenResolutionVertical
	 *            the screenResolutionVertical to set
	 */
	public void setScreenResolutionVertical(int screenResolutionVertical) {
		this.screenResolutionVertical = screenResolutionVertical;
	}

	/**
	 * @return the screenResolutionHorizontal
	 */
	public int getScreenResolutionHorizontal() {
		return screenResolutionHorizontal;
	}

	/**
	 * @param screenResolutionHorizontal
	 *            the screenResolutionHorizontal to set
	 */
	public void setScreenResolutionHorizontal(int screenResolutionHorizontal) {
		this.screenResolutionHorizontal = screenResolutionHorizontal;
	}

	/**
	 * @return the screenDepth
	 */
	public int getScreenDepth() {
		return screenDepth;
	}

	/**
	 * @param screenDepth
	 *            the screenDepth to set
	 */
	public void setScreenDepth(int screenDepth) {
		this.screenDepth = screenDepth;
	}

	/**
	 * @return the serialNumber
	 */
	public long getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber
	 *            the serialNumber to set
	 */
	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the targetProtocol
	 */
	public int getTargetProtocol() {
		return targetProtocol;
	}

	/**
	 * @param targetProtocol
	 *            the targetProtocol to set
	 */
	public void setTargetProtocol(int targetProtocol) {
		this.targetProtocol = targetProtocol;
	}

}
