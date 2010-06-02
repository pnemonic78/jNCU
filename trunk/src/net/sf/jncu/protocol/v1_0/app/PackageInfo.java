package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.newton.stream.NSOFImmediate;
import net.sf.jncu.newton.stream.NSOFInteger;
import net.sf.jncu.newton.stream.NSOFNil;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.newton.stream.NSOFSymbol;
import net.sf.jncu.newton.stream.NSOFTrue;

/**
 * Package information.
 * 
 * @author moshew
 */
public class PackageInfo {

	public static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	public static final NSOFSymbol SLOT_SIZE = new NSOFSymbol("packagesize");
	public static final NSOFSymbol SLOT_ID = new NSOFSymbol("packageid");
	public static final NSOFSymbol SLOT_VERSION = new NSOFSymbol("packageversion");
	public static final NSOFSymbol SLOT_FORMAT = new NSOFSymbol("format");
	public static final NSOFSymbol SLOT_DEVICE_KIND = new NSOFSymbol("devicekind");
	public static final NSOFSymbol SLOT_DEVICE_NUM = new NSOFSymbol("devicenumber");
	public static final NSOFSymbol SLOT_DEVICE_ID = new NSOFSymbol("deviceid");
	public static final NSOFSymbol SLOT_MODIFIED = new NSOFSymbol("modtime");
	public static final NSOFSymbol SLOT_COPY_PROTECT = new NSOFSymbol("iscopyprotected");
	public static final NSOFSymbol SLOT_REMOVE = new NSOFSymbol("safetoremove");

	private String name;
	private int packageSize;
	private int packageId;
	private int packageVersion;
	private int format;
	private int deviceKind;
	private int deviceNumber;
	private int deviceId;
	private int modifyDate;
	private boolean copyProtected;
	private boolean safeToRemove;

	/**
	 * Creates a new package info.
	 */
	public PackageInfo() {
		super();
	}

	/**
	 * Get the package size.
	 * 
	 * @return the size.
	 */
	public int getPackageSize() {
		return packageSize;
	}

	/**
	 * Set the package size.
	 * 
	 * @param packageSize
	 *            the size.
	 */
	public void setPackageSize(int packageSize) {
		this.packageSize = packageSize;
	}

	/**
	 * Get the package id.
	 * 
	 * @return the id.
	 */
	public int getPackageId() {
		return packageId;
	}

	/**
	 * Set the package id.
	 * 
	 * @param packageId
	 *            the id.
	 */
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	/**
	 * @return the packageVersion
	 */
	public int getPackageVersion() {
		return packageVersion;
	}

	/**
	 * @param packageVersion
	 *            the packageVersion to set
	 */
	public void setPackageVersion(int packageVersion) {
		this.packageVersion = packageVersion;
	}

	/**
	 * Get the format.
	 * 
	 * @return the format.
	 */
	public int getFormat() {
		return format;
	}

	/**
	 * Set the format.
	 * 
	 * @param format
	 *            the format.
	 */
	public void setFormat(int format) {
		this.format = format;
	}

	/**
	 * Get the device kind.
	 * 
	 * @return the kind.
	 */
	public int getDeviceKind() {
		return deviceKind;
	}

	/**
	 * Set the device kind.
	 * 
	 * @param deviceKind
	 *            the kind.
	 */
	public void setDeviceKind(int deviceKind) {
		this.deviceKind = deviceKind;
	}

	/**
	 * Get the device number.
	 * 
	 * @return the number.
	 */
	public int getDeviceNumber() {
		return deviceNumber;
	}

	/**
	 * Set the device number.
	 * 
	 * @param deviceNumber
	 *            the number.
	 */
	public void setDeviceNumber(int deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	/**
	 * Get the device id.
	 * 
	 * @return the id.
	 */
	public int getDeviceId() {
		return deviceId;
	}

	/**
	 * Set the device id.
	 * 
	 * @param deviceId
	 *            the id.
	 */
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Get the modified date.
	 * 
	 * @return the date.
	 */
	public int getModifyDate() {
		return modifyDate;
	}

	/**
	 * Set the modified date.
	 * 
	 * @param modifyDate
	 *            the date.
	 */
	public void setModifyDate(int modifyDate) {
		this.modifyDate = modifyDate;
	}

	/**
	 * Is copy-protected?
	 * 
	 * @return true if protected.
	 */
	public boolean isCopyProtected() {
		return copyProtected;
	}

	/**
	 * Set copy-protected.
	 * 
	 * @param isCopyProtected
	 *            is protected?
	 */
	public void setCopyProtected(boolean copyProtected) {
		this.copyProtected = copyProtected;
	}

	/**
	 * Get the package name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the package name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Is safe to remove?
	 * 
	 * @return true if safe to remove.
	 */
	public boolean isSafeToRemove() {
		return safeToRemove;
	}

	/**
	 * Set safe to remove.s
	 * 
	 * @param safeToRemove
	 *            safe to remove?
	 */
	public void setSafeToRemove(boolean safeToRemove) {
		this.safeToRemove = safeToRemove;
	}

	/**
	 * Get the frame.
	 * 
	 * @return the frame.
	 */
	public NSOFFrame toFrame() {
		NSOFFrame frame = new NSOFFrame();
		frame.put(SLOT_COPY_PROTECT, isCopyProtected() ? new NSOFTrue() : new NSOFNil());
		frame.put(SLOT_DEVICE_ID, new NSOFInteger(getDeviceId()));
		frame.put(SLOT_DEVICE_KIND, new NSOFInteger(getDeviceKind()));
		frame.put(SLOT_DEVICE_NUM, new NSOFInteger(getDeviceNumber()));
		frame.put(SLOT_FORMAT, new NSOFInteger(getFormat()));
		frame.put(SLOT_ID, new NSOFInteger(getPackageId()));
		frame.put(SLOT_MODIFIED, new NSOFInteger(getModifyDate()));
		frame.put(SLOT_NAME, new NSOFString(getName()));
		frame.put(SLOT_REMOVE, isSafeToRemove() ? new NSOFTrue() : new NSOFNil());
		frame.put(SLOT_SIZE, new NSOFInteger(getPackageSize()));
		frame.put(SLOT_VERSION, new NSOFInteger(getPackageVersion()));
		return frame;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame.
	 */
	public void decode(NSOFFrame frame) {
		NSOFObject value;

		value = frame.get(SLOT_COPY_PROTECT);
		setCopyProtected(false);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setCopyProtected(imm.isTrue());
		}

		value = frame.get(SLOT_DEVICE_ID);
		setDeviceId(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setDeviceId(imm.getValue());
		}

		value = frame.get(SLOT_DEVICE_KIND);
		setDeviceKind(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setDeviceKind(imm.getValue());
		}

		value = frame.get(SLOT_DEVICE_NUM);
		setDeviceNumber(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setDeviceNumber(imm.getValue());
		}

		value = frame.get(SLOT_FORMAT);
		setFormat(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setFormat(imm.getValue());
		}

		value = frame.get(SLOT_ID);
		setPackageId(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setPackageId(imm.getValue());
		}

		value = frame.get(SLOT_MODIFIED);
		setModifyDate(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setModifyDate(imm.getValue());
		}

		value = frame.get(SLOT_NAME);
		setName(null);
		if (value != null) {
			NSOFString s = (NSOFString) value;
			setName(s.getValue());
		}

		value = frame.get(SLOT_REMOVE);
		setSafeToRemove(false);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setSafeToRemove(imm.isTrue());
		}

		value = frame.get(SLOT_SIZE);
		setPackageSize(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setPackageSize(imm.getValue());
		}

		value = frame.get(SLOT_VERSION);
		setPackageVersion(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setPackageVersion(imm.getValue());
		}
	}

}
