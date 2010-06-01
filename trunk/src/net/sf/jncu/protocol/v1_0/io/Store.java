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
package net.sf.jncu.protocol.v1_0.io;

import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.newton.stream.NSOFArray;
import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.newton.stream.NSOFImmediate;
import net.sf.jncu.newton.stream.NSOFInteger;
import net.sf.jncu.newton.stream.NSOFNil;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.newton.stream.NSOFPlainArray;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.newton.stream.NSOFSymbol;
import net.sf.jncu.newton.stream.NSOFTrue;

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
public class Store {

	public static final String KIND_INTERNAL = "Internal";

	protected static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	protected static final NSOFSymbol SLOT_SIGNATURE = new NSOFSymbol("signature");
	protected static final NSOFSymbol SLOT_TOTALSIZE = new NSOFSymbol("totalsize");
	protected static final NSOFSymbol SLOT_USEDSIZE = new NSOFSymbol("usedsize");
	protected static final NSOFSymbol SLOT_KIND = new NSOFSymbol("kind");
	protected static final NSOFSymbol SLOT_INFO = new NSOFSymbol("info");
	protected static final NSOFSymbol SLOT_READONLY = new NSOFSymbol("readOnly");
	protected static final NSOFSymbol SLOT_DEFAULT = new NSOFSymbol("defaultStore");
	protected static final NSOFSymbol SLOT_PASSWORD = new NSOFSymbol("storePassword");
	protected static final NSOFSymbol SLOT_SOUPS = new NSOFSymbol("soups");
	protected static final NSOFSymbol SLOT_SIGNATURES = new NSOFSymbol("signatures");

	private String name;
	private int signature;
	private int totalSize;
	private int usedSize;
	private String kind;
	private NSOFFrame info;
	private boolean readOnly;
	private boolean defaultStore;
	private int password;
	private List<String> soups;
	private List<Integer> soupSignatures;

	/**
	 * Creates a new store.
	 */
	public Store() {
		super();
	}

	/**
	 * Get the frame.
	 * 
	 * @return the frame.
	 */
	public NSOFFrame toFrame() {
		NSOFFrame frame;
		frame = new NSOFFrame();
		frame.put(SLOT_NAME, new NSOFString(getName()));
		frame.put(SLOT_SIGNATURE, new NSOFInteger(getSignature()));
		frame.put(SLOT_TOTALSIZE, new NSOFInteger(getTotalSize()));
		frame.put(SLOT_USEDSIZE, new NSOFInteger(getUsedSize()));
		frame.put(SLOT_KIND, new NSOFString(getKind()));
		frame.put(SLOT_INFO, getInfo());
		frame.put(SLOT_READONLY, isReadOnly() ? new NSOFTrue() : new NSOFNil());
		frame.put(SLOT_DEFAULT, isDefaultStore() ? new NSOFTrue() : new NSOFNil());
		if (getPassword() != 0) {
			frame.put(SLOT_PASSWORD, new NSOFInteger(getPassword()));
		}
		if (getSoups() != null) {
			List<String> soupNames = getSoups();
			NSOFString[] entries = new NSOFString[soupNames.size()];
			int i = 0;
			for (String soup : soupNames) {
				entries[i++] = new NSOFString(soup);
			}
			frame.put(SLOT_SOUPS, new NSOFPlainArray(entries));
		}
		if (getSoupSignatures() != null) {
			List<Integer> soupIds = getSoupSignatures();
			NSOFInteger[] entries = new NSOFInteger[soupIds.size()];
			int i = 0;
			for (Integer soup : soupIds) {
				entries[i++] = new NSOFInteger(soup);
			}
			frame.put(SLOT_SIGNATURES, new NSOFPlainArray(entries));
		}
		return frame;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame.
	 */
	public void decodeFrame(NSOFFrame frame) {
		NSOFObject value;

		value = frame.get(SLOT_DEFAULT);
		setDefaultStore(false);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setDefaultStore(imm.isTrue());
		}

		value = frame.get(SLOT_INFO);
		setInfo((NSOFFrame) value);

		value = frame.get(SLOT_KIND);
		setKind(null);
		if (value != null) {
			NSOFString s = (NSOFString) value;
			setKind(s.getValue());
		}

		value = frame.get(SLOT_NAME);
		setName(null);
		if (value != null) {
			NSOFString s = (NSOFString) value;
			setName(s.getValue());
		}

		value = frame.get(SLOT_PASSWORD);
		setPassword(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setPassword(imm.getValue());
		}

		value = frame.get(SLOT_READONLY);
		setReadOnly(false);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setReadOnly(imm.isTrue());
		}

		value = frame.get(SLOT_SIGNATURE);
		setSignature(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setSignature(imm.getValue());
		}

		value = frame.get(SLOT_SIGNATURES);
		setSoupSignatures(null);
		if (value != null) {
			NSOFArray arr = (NSOFArray) value;
			List<Integer> soups = new ArrayList<Integer>();
			NSOFObject[] entries = arr.getValue();
			for (NSOFObject entry : entries) {
				soups.add(((NSOFImmediate) entry).getValue());
			}
			setSoupSignatures(soups);
		}

		value = frame.get(SLOT_SOUPS);
		setSoups(null);
		if (value != null) {
			NSOFArray arr = (NSOFArray) value;
			List<String> soups = new ArrayList<String>();
			NSOFObject[] entries = arr.getValue();
			for (NSOFObject entry : entries) {
				soups.add(((NSOFString) entry).getValue());
			}
			setSoups(soups);
		}

		value = frame.get(SLOT_TOTALSIZE);
		setTotalSize(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setTotalSize(imm.getValue());
		}

		value = frame.get(SLOT_USEDSIZE);
		setUsedSize(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setUsedSize(imm.getValue());
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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the signature.
	 * 
	 * @return the signature.
	 */
	public int getSignature() {
		return signature;
	}

	/**
	 * Set the signature.
	 * 
	 * @param signature
	 *            the signature.
	 */
	public void setSignature(int signature) {
		this.signature = signature;
	}

	/**
	 * Get the total size.
	 * 
	 * @return the size.
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * Set the total size.
	 * 
	 * @param totalSize
	 *            the size.
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * Get the used size.
	 * 
	 * @return the size.
	 */
	public int getUsedSize() {
		return usedSize;
	}

	/**
	 * Set the used size.
	 * 
	 * @param usedSize
	 *            the size.
	 */
	public void setUsedSize(int usedSize) {
		this.usedSize = usedSize;
	}

	/**
	 * Get the kind.
	 * 
	 * @return the kind.
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * Set the kind.
	 * 
	 * @param kind
	 *            the kind.
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * Get the information.
	 * 
	 * @return the information.
	 */
	public NSOFFrame getInfo() {
		return info;
	}

	/**
	 * Set the information.
	 * 
	 * @param info
	 *            the information.
	 */
	public void setInfo(NSOFFrame info) {
		this.info = info;
	}

	/**
	 * Is read-only?
	 * 
	 * @return true if read-only.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Set read-only.
	 * 
	 * @param readOnly
	 *            true if read-only.
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * Is default store?
	 * 
	 * @return true if the default store.
	 */
	public boolean isDefaultStore() {
		return defaultStore;
	}

	/**
	 * Set as the default store.
	 * 
	 * @param defaultStore
	 *            true if the default store.
	 */
	public void setDefaultStore(boolean defaultStore) {
		this.defaultStore = defaultStore;
	}

	/**
	 * Get the password.
	 * 
	 * @return the password.
	 */
	public int getPassword() {
		return password;
	}

	/**
	 * Set the password.
	 * 
	 * @param password
	 *            the password.
	 */
	public void setPassword(int password) {
		this.password = password;
	}

	/**
	 * Get the soup names.
	 * 
	 * @return the soups.
	 */
	public List<String> getSoups() {
		return soups;
	}

	/**
	 * Set the soup names.
	 * 
	 * @param soups
	 *            the soups.
	 */
	public void setSoups(List<String> soups) {
		this.soups = soups;
	}

	/**
	 * Get the soup signatures.
	 * 
	 * @return the signatures.
	 */
	public List<Integer> getSoupSignatures() {
		return soupSignatures;
	}

	/**
	 * Set the soup signatures.
	 * 
	 * @param soupSignatures
	 *            the signatures.
	 */
	public void setSoupSignatures(List<Integer> soupSignatures) {
		this.soupSignatures = soupSignatures;
	}

}
