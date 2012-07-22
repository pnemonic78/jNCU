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
package net.sf.jncu.newton.os;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFBoolean;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Store information.
 * <p>
 * Each array slot contains the following information about a store:<br>
 * <code>
 * {<br>
 * &nbsp;&nbsp;name: "",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;totalsize: 1234,<br>
 * &nbsp;&nbsp;usedsize: 1234,<br>
 * &nbsp;&nbsp;kind: "",<br>
 * &nbsp;&nbsp;info: {store info frame},<br>
 * &nbsp;&nbsp;readOnly: true,<br>
 * &nbsp;&nbsp;defaultStore: true,		// only for the default store<br>
 * &nbsp;&nbsp;storePassword: password  // only if a store password has been set<br>
 * &nbsp;&nbsp;soups: [soup names],<br>
 * &nbsp;&nbsp;signatures: [soup signatures]<br>
 * }</code>
 * 
 * @author moshew
 */
public class Store implements Comparable<Store> {

	/** Internal storage card. */
	public static final String KIND_INTERNAL = "Internal";
	/** Flash storage card. */
	public static final String KIND_FLASH = "Flash storage card";

	public static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	public static final NSOFSymbol SLOT_SIGNATURE = new NSOFSymbol("signature");
	public static final NSOFSymbol SLOT_TOTALSIZE = new NSOFSymbol("totalSize");
	public static final NSOFSymbol SLOT_USEDSIZE = new NSOFSymbol("usedSize");
	public static final NSOFSymbol SLOT_KIND = new NSOFSymbol("kind");
	public static final NSOFSymbol SLOT_INFO = new NSOFSymbol("info");
	public static final NSOFSymbol SLOT_READONLY = new NSOFSymbol("readOnly");
	public static final NSOFSymbol SLOT_DEFAULT = new NSOFSymbol("defaultStore");
	public static final NSOFSymbol SLOT_PASSWORD = new NSOFSymbol("storePassword");
	public static final NSOFSymbol SLOT_SOUPS = new NSOFSymbol("soups");
	public static final NSOFSymbol SLOT_SIGNATURES = new NSOFSymbol("signatures");
	public static final NSOFSymbol SLOT_VERSION = new NSOFSymbol("storeVersion");
	public static final NSOFSymbol SLOT_INFO_DEFAULT = SLOT_DEFAULT;
	public static final NSOFSymbol SLOT_INFO_LAST_RESTORE = new NSOFSymbol("lastRestoreFromCard");

	private String name;
	private final NSOFFrame frame = new NSOFFrame();
	private final Collection<Soup> soups = new HashSet<Soup>();
	private final List<ApplicationPackage> packages = new ArrayList<ApplicationPackage>();

	/**
	 * Creates a new store.
	 * 
	 * @param name
	 *            the store name.
	 */
	public Store(String name) {
		super();
		setName(name);
	}

	/**
	 * Get the frame.
	 * 
	 * @return the frame.
	 */
	public NSOFFrame toFrame() {
		NSOFArray arr = new NSOFPlainArray(soups.size());
		int i = 0;
		for (Soup soup : soups)
			arr.set(i++, soup.toFrame());
		frame.put(SLOT_SOUPS, arr);

		return frame;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame.
	 */
	public void fromFrame(NSOFFrame frame) {
		NSOFObject value;

		this.frame.clear();
		this.frame.putAll(frame);

		value = frame.get(SLOT_SOUPS);
		setSoups(null);
		if (!NSOFImmediate.isNil(value)) {
			NSOFArray arr = (NSOFArray) value;
			List<Soup> soups = new ArrayList<Soup>();
			NSOFObject[] entries = arr.getValue();
			Soup soup;
			String name;
			for (NSOFObject entry : entries) {
				if (entry instanceof NSOFString) {
					name = ((NSOFString) entry).getValue();
					soup = new Soup(name);
				} else {
					soup = new Soup(null);
					soup.fromFrame((NSOFFrame) entry);
				}
				soups.add(soup);
			}
			setSoups(soups);
		}

		value = frame.get(SLOT_SIGNATURES);
		// setSoupSignatures(null);
		if (!NSOFImmediate.isNil(value)) {
			NSOFArray arr = (NSOFArray) value;
			List<Integer> signatures = new ArrayList<Integer>();
			NSOFObject[] entries = arr.getValue();
			for (NSOFObject entry : entries) {
				signatures.add(((NSOFImmediate) entry).getValue());
			}
			setSoupSignatures(signatures);
		}

		if (this.name == null) {
			value = frame.get(SLOT_NAME);
			if (!NSOFImmediate.isNil(value)) {
				NSOFString s = (NSOFString) value;
				this.name = s.getValue();
			}
		}
	}

	/**
	 * Get the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 *            the name.
	 */
	private void setName(String name) {
		this.name = name;
		frame.put(SLOT_NAME, new NSOFString(name));
	}

	/**
	 * Get the signature.
	 * 
	 * @return the signature.
	 */
	public int getSignature() {
		NSOFObject value = frame.get(SLOT_SIGNATURE);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			return imm.getValue();
		}
		return 0;
	}

	/**
	 * Set the signature.
	 * 
	 * @param signature
	 *            the signature.
	 */
	public void setSignature(int signature) {
		frame.put(SLOT_SIGNATURE, new NSOFInteger(signature));
	}

	/**
	 * Get the total size.
	 * 
	 * @return the size.
	 */
	public int getTotalSize() {
		NSOFObject value = frame.get(SLOT_TOTALSIZE);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			return imm.getValue();
		}
		return 0;
	}

	/**
	 * Get the used size.
	 * 
	 * @return the size.
	 */
	public int getUsedSize() {
		NSOFObject value = frame.get(SLOT_USEDSIZE);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			return imm.getValue();
		}
		return 0;
	}

	/**
	 * Get the kind.
	 * 
	 * @return the kind.
	 */
	public String getKind() {
		NSOFObject value = frame.get(SLOT_KIND);
		if (!NSOFImmediate.isNil(value)) {
			NSOFString s = (NSOFString) value;
			return s.getValue();
		}
		return null;
	}

	/**
	 * Set the kind.
	 * 
	 * @param kind
	 *            the kind.
	 */
	public void setKind(String kind) {
		frame.put(SLOT_KIND, new NSOFString(kind));
	}

	/**
	 * Get the information.
	 * 
	 * @return the information.
	 */
	public NSOFFrame getInformation() {
		NSOFObject value = frame.get(SLOT_INFO);
		if (!NSOFImmediate.isNil(value)) {
			return (NSOFFrame) value;
		}

		NSOFFrame info = new NSOFFrame();
		frame.put(SLOT_INFO, info);

		return info;
	}

	/**
	 * Set the information.
	 * 
	 * @param info
	 *            the information.
	 */
	public void setInformation(NSOFFrame info) {
		NSOFFrame myInfo = getInformation();
		myInfo.clear();
		if (info != null)
			myInfo.putAll(info);
		myInfo.put(SLOT_NAME, new NSOFString(name));
	}

	/**
	 * Is read-only?
	 * 
	 * @return true if read-only.
	 */
	public boolean isReadOnly() {
		NSOFObject value = frame.get(SLOT_READONLY);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			return imm.isTrue();
		}
		return false;
	}

	/**
	 * Set read-only.
	 * 
	 * @param readOnly
	 *            true if read-only.
	 */
	public void setReadOnly(boolean readOnly) {
		frame.put(SLOT_READONLY, NSOFBoolean.valueOf(readOnly));
	}

	/**
	 * Is default store?
	 * 
	 * @return true if the default store.
	 */
	public boolean isDefaultStore() {
		NSOFObject value = frame.get(SLOT_DEFAULT);
		if (!NSOFImmediate.isNil(value)) {
			NSOFImmediate imm = (NSOFImmediate) value;
			return imm.isTrue();
		}

		value = getInformation().get(SLOT_INFO_DEFAULT);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			return imm.isTrue();
		}

		return false;
	}

	/**
	 * Set as the default store.
	 * 
	 * @param defaultStore
	 *            true if the default store.
	 */
	public void setDefaultStore(boolean defaultStore) {
		NSOFBoolean value = NSOFBoolean.valueOf(defaultStore);
		frame.put(SLOT_DEFAULT, value);
		getInformation().put(SLOT_INFO_DEFAULT, value);
	}

	/**
	 * Get the password.
	 * 
	 * @return the password.
	 */
	public int getPassword() {
		NSOFObject value = frame.get(SLOT_PASSWORD);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			return imm.getValue();
		}
		return 0;
	}

	/**
	 * Set the password.
	 * 
	 * @param password
	 *            the password.
	 */
	public void setPassword(int password) {
		frame.put(SLOT_PASSWORD, new NSOFInteger(password));
	}

	/**
	 * Get the store version.
	 * 
	 * @return the version.
	 */
	public int getVersion() {
		NSOFObject value = frame.get(SLOT_VERSION);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			return imm.getValue();
		}
		return 0;
	}

	/**
	 * Set the store version.
	 * 
	 * @param version
	 *            the version.
	 */
	public void setVersion(int version) {
		frame.put(SLOT_VERSION, new NSOFInteger(version));
	}

	/**
	 * Get the soups.
	 * 
	 * @return the soups.
	 */
	public Collection<Soup> getSoups() {
		return soups;
	}

	/**
	 * Set the soups.
	 * 
	 * @param soups
	 *            the soups.
	 */
	public void setSoups(Collection<Soup> soups) {
		this.soups.clear();
		if (soups != null)
			this.soups.addAll(soups);
	}

	/**
	 * Add a soup.
	 * 
	 * @param soup
	 *            the soup.
	 */
	public void addSoup(Soup soup) {
		this.soups.add(soup);
	}

	/**
	 * Get the soup signatures.
	 * 
	 * @return the signatures.
	 */
	public List<Integer> getSoupSignatures() {
		List<Integer> signatures = new ArrayList<Integer>();
		for (Soup soup : getSoups()) {
			signatures.add(soup.getSignature());
		}
		return signatures;
	}

	/**
	 * Set the soup signatures.
	 * 
	 * @param soupSignatures
	 *            the signatures.
	 */
	public void setSoupSignatures(List<Integer> soupSignatures) {
		Collection<Soup> soups = getSoups();
		if (soups == null)
			return;
		int i = 0;
		for (Soup soup : soups) {
			soup.setSignature((soupSignatures == null) ? 0 : soupSignatures.get(i++));
		}
	}

	/**
	 * Get the unused size.
	 * 
	 * @return the size.
	 * @see #getTotalSize()
	 * @see #getUsedSize()
	 */
	public int getFreeSize() {
		return getTotalSize() - getUsedSize();
	}

	/**
	 * Get the packages.
	 * 
	 * @return the packages.
	 */
	public List<ApplicationPackage> getPackages() {
		return packages;
	}

	/**
	 * Set the packages.
	 * 
	 * @param packages
	 *            the packages.
	 */
	public void setPackages(List<ApplicationPackage> packages) {
		this.packages.clear();
		if (packages != null)
			this.packages.addAll(packages);
	}

	/**
	 * Add a package.
	 * 
	 * @param pkg
	 *            the package.
	 */
	public void addPackage(ApplicationPackage pkg) {
		this.packages.add(pkg);
	}

	/**
	 * Find a soup.
	 * 
	 * @param name
	 *            the soup name.
	 * @return the soup - {@code null} otherwise.
	 */
	public Soup findSoup(String name) {
		if (name == null)
			return null;
		for (Soup soup : getSoups()) {
			if (name.equals(soup.getName()))
				return soup;
		}
		return null;
	}

	/**
	 * Find a soup.
	 * 
	 * @param name
	 *            the soup name.
	 * @param signature
	 *            the soup signature.
	 * 
	 * @return the soup - {@code null} otherwise.
	 */
	public Soup findSoup(String name, int signature) {
		if (name == null)
			return null;
		for (Soup soup : getSoups()) {
			if (name.equals(soup.getName()) && (signature == soup.getSignature()))
				return soup;
		}
		return null;
	}

	@Override
	public String toString() {
		return toFrame().toString();
	}

	@Override
	public int hashCode() {
		return (name == null) ? 0 : name.hashCode();
	}

	@Override
	public int compareTo(Store that) {
		int c = 0;
		if (this.name == null) {
			if (that.name != null) {
				return -1;
			}
		} else if (that.name == null) {
			return 1;
		} else {
			c = this.name.compareTo(that.name);
		}
		if (c != 0)
			return c;
		c = this.getSignature() - that.getSignature();
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Store) {
			Store that = (Store) obj;
			return compareTo(that) == 0;
		}
		return super.equals(obj);
	}
}
