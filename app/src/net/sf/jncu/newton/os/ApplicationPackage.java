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

import net.sf.jncu.fdil.NSOFBinaryObject;

/**
 * Application package information.
 * 
 * @author moshew
 */
public class ApplicationPackage {

	private String name;
	private NSOFBinaryObject binary;

	/**
	 * Creates a new package.
	 */
	public ApplicationPackage() {
		super();
	}

	/**
	 * Creates a new package.
	 * 
	 * @param name
	 *            the name.
	 */
	public ApplicationPackage(String name) {
		super();
		this.name = name;
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
	 * Set the package binary.
	 * 
	 * @param binary
	 *            the binary object.
	 */
	public void setBinary(NSOFBinaryObject binary) {
		this.binary = binary;
	}

	/**
	 * Get the package binary.
	 * 
	 * @return the binary object.
	 */
	public NSOFBinaryObject getBinary() {
		return binary;
	}
}
