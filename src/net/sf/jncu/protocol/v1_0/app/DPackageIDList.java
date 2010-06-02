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
package net.sf.jncu.protocol.v1_0.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDPackageIDList</tt><br>
 * This command sends a list of package frames to the desktop. For each package
 * the information sent is this:
 * <ol>
 * <li>ULong packageSize;
 * <li>ULong packageId;
 * <li>ULong packageVersion;
 * <li>ULong format;
 * <li>ULong deviceKind;
 * <li>ULong deviceNumber;
 * <li>ULong deviceId;
 * <li>ULong modifyDate;
 * <li>ULong isCopyProtected;
 * <li>ULong length;
 * <li>Character name; (length bytes of Unicode string)
 * </ol>
 * Note that this is not sent as an array! It's sent as binary data. Note that
 * this finds packages only in the current store.
 * 
 * <pre>
 * 'pids'
 * length
 * count
 * package info
 * </pre>
 */
public class DPackageIDList extends DockCommandFromNewton {

	public static final String COMMAND = "pids";

	private final List<PackageInfo> packages = new ArrayList<PackageInfo>();

	/**
	 * Creates a new command.
	 */
	public DPackageIDList() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		packages.clear();
		int count = ntohl(data);
		PackageInfo pkg;
		for (int i = 0; i < count; i++) {
			pkg = new PackageInfo();
			pkg.setPackageSize(ntohl(data));
			pkg.setPackageId(ntohl(data));
			pkg.setPackageVersion(ntohl(data));
			pkg.setFormat(ntohl(data));
			pkg.setDeviceKind(ntohl(data));
			pkg.setDeviceNumber(ntohl(data));
			pkg.setDeviceId(ntohl(data));
			pkg.setModifyDate(ntohl(data));
			pkg.setCopyProtected(ntohl(data) != FALSE);
			pkg.setName(readString(data));
			packages.add(pkg);
		}
	}

	/**
	 * Get the list of packages.
	 * 
	 * @return the packages.
	 */
	public List<PackageInfo> getPackages() {
		return packages;
	}

	/**
	 * Package information.
	 * 
	 * @author moshew
	 */
	public static class PackageInfo {

		private int packageSize;
		private int packageId;
		private int packageVersion;
		private int format;
		private int deviceKind;
		private int deviceNumber;
		private int deviceId;
		private int modifyDate;
		private boolean copyProtected;
		private String name;

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

	}
}
