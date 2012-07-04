/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.fdil.NSOFBoolean;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.util.NewtonDateUtils;

/**
 * Package information.
 * 
 * @author moshew
 */
public class PackageInfo extends NSOFFrame {

	public static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	public static final NSOFSymbol SLOT_SIZE = new NSOFSymbol("packageSize");
	public static final NSOFSymbol SLOT_ID = new NSOFSymbol("packageId");
	public static final NSOFSymbol SLOT_VERSION = new NSOFSymbol("packageVersion");
	public static final NSOFSymbol SLOT_FORMAT = new NSOFSymbol("format");
	public static final NSOFSymbol SLOT_DEVICE_KIND = new NSOFSymbol("deviceKind");
	public static final NSOFSymbol SLOT_DEVICE_NUM = new NSOFSymbol("deviceNumber");
	public static final NSOFSymbol SLOT_DEVICE_ID = new NSOFSymbol("deviceId");
	public static final NSOFSymbol SLOT_MODIFIED = new NSOFSymbol("modTime");
	public static final NSOFSymbol SLOT_COPY_PROTECT = new NSOFSymbol("isCopyProtected");
	public static final NSOFSymbol SLOT_REMOVE = new NSOFSymbol("safeToRemove");
	public static final NSOFSymbol SLOT_LENGTH = new NSOFSymbol("length");

	private String name;

	/**
	 * Creates a new package info.
	 */
	public PackageInfo() {
		super();
	}

	/**
	 * Creates a new package info.
	 * 
	 * @param frame
	 *            the source frame.
	 */
	public PackageInfo(NSOFFrame frame) {
		super();
		this.putAll(frame);
	}

	/**
	 * Get the package size.
	 * 
	 * @return the size.
	 */
	public int getPackageSize() {
		NSOFObject value = this.get(SLOT_SIZE);
		if (value != null)
			return ((NSOFImmediate) value).getValue();
		return 0;
	}

	/**
	 * Set the package size.
	 * 
	 * @param packageSize
	 *            the size.
	 */
	public void setPackageSize(int packageSize) {
		this.put(SLOT_SIZE, new NSOFInteger(packageSize));
	}

	/**
	 * Get the package id.
	 * 
	 * @return the id.
	 */
	public int getPackageId() {
		NSOFObject value = this.get(SLOT_ID);
		if (value != null)
			return ((NSOFImmediate) value).getValue();
		return 0;
	}

	/**
	 * Set the package id.
	 * 
	 * @param packageId
	 *            the id.
	 */
	public void setPackageId(int packageId) {
		this.put(SLOT_ID, new NSOFInteger(packageId));
	}

	/**
	 * @return the packageVersion
	 */
	public int getPackageVersion() {
		NSOFObject value = this.get(SLOT_VERSION);
		if (value != null)
			return ((NSOFImmediate) value).getValue();
		return 0;
	}

	/**
	 * @param packageVersion
	 *            the packageVersion to set
	 */
	public void setPackageVersion(int packageVersion) {
		this.put(SLOT_VERSION, new NSOFInteger(packageVersion));
	}

	/**
	 * Get the format.
	 * 
	 * @return the format.
	 */
	public int getFormat() {
		NSOFObject value = this.get(SLOT_FORMAT);
		if (value != null)
			return ((NSOFImmediate) value).getValue();
		return 0;
	}

	/**
	 * Set the format.
	 * 
	 * @param format
	 *            the format.
	 */
	public void setFormat(int format) {
		this.put(SLOT_FORMAT, new NSOFInteger(format));
	}

	/**
	 * Get the device kind.
	 * 
	 * @return the kind.
	 */
	public int getDeviceKind() {
		NSOFObject value = this.get(SLOT_DEVICE_KIND);
		if (value != null)
			return ((NSOFImmediate) value).getValue();
		return 0;
	}

	/**
	 * Set the device kind.
	 * 
	 * @param deviceKind
	 *            the kind.
	 */
	public void setDeviceKind(int deviceKind) {
		this.put(SLOT_DEVICE_KIND, new NSOFInteger(deviceKind));
	}

	/**
	 * Get the device number.
	 * 
	 * @return the number.
	 */
	public int getDeviceNumber() {
		NSOFObject value = this.get(SLOT_DEVICE_NUM);
		if (value != null)
			return ((NSOFImmediate) value).getValue();
		return 0;
	}

	/**
	 * Set the device number.
	 * 
	 * @param deviceNumber
	 *            the number.
	 */
	public void setDeviceNumber(int deviceNumber) {
		this.put(SLOT_DEVICE_NUM, new NSOFInteger(deviceNumber));
	}

	/**
	 * Get the device id.
	 * 
	 * @return the id.
	 */
	public int getDeviceId() {
		NSOFObject value = this.get(SLOT_DEVICE_ID);
		if (value != null)
			return ((NSOFImmediate) value).getValue();
		return 0;
	}

	/**
	 * Set the device id.
	 * 
	 * @param deviceId
	 *            the id.
	 */
	public void setDeviceId(int deviceId) {
		this.put(SLOT_DEVICE_ID, new NSOFInteger(deviceId));
	}

	/**
	 * Get the modified date.
	 * 
	 * @return the date.
	 */
	public long getModifyDate() {
		NSOFObject value = this.get(SLOT_MODIFIED);
		if (value != null) {
			int minutes = ((NSOFImmediate) value).getValue();
			return NewtonDateUtils.fromMinutes(minutes);
		}
		return 0;
	}

	/**
	 * Set the modified date.
	 * 
	 * @param modifyDate
	 *            the date in milliseconds.
	 */
	public void setModifyDate(long modifyDate) {
		int minutes = NewtonDateUtils.getMinutes(modifyDate);
		this.put(SLOT_MODIFIED, new NSOFInteger(minutes));
	}

	/**
	 * Set the modified date.
	 * 
	 * @param modifyDate
	 *            the date in minutes.
	 */
	public void setModifyDate(int modifyDate) {
		this.put(SLOT_MODIFIED, new NSOFInteger(modifyDate));
	}

	/**
	 * Is copy-protected?
	 * 
	 * @return true if protected.
	 */
	public boolean isCopyProtected() {
		NSOFObject value = this.get(SLOT_COPY_PROTECT);
		if (value != null)
			return ((NSOFImmediate) value).isTrue();
		return false;
	}

	/**
	 * Set copy-protected.
	 * 
	 * @param isCopyProtected
	 *            is protected?
	 */
	public void setCopyProtected(boolean copyProtected) {
		this.put(SLOT_COPY_PROTECT, NSOFBoolean.valueOf(copyProtected));
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
		NSOFObject value = this.get(SLOT_REMOVE);
		if (value != null)
			return ((NSOFImmediate) value).isTrue();
		return false;
	}

	/**
	 * Set safe to remove.s
	 * 
	 * @param safeToRemove
	 *            safe to remove?
	 */
	public void setSafeToRemove(boolean safeToRemove) {
		this.put(SLOT_REMOVE, NSOFBoolean.valueOf(safeToRemove));
	}
}
