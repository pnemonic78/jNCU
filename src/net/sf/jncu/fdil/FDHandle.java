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
package net.sf.jncu.fdil;

/**
 * FDIL object handle.
 * 
 * @author Moshe
 */
public class FDHandle extends Object implements Comparable<FDHandle> {

	private final int value;

	/**
	 * Creates a new handle.
	 * 
	 * @param value
	 *            the handle value.
	 */
	public FDHandle(int value) {
		super();
		this.value = value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FDHandle) {
			return value == ((FDHandle) obj).value;
		}
		return false;
	}

	@Override
	public int compareTo(FDHandle that) {
		return this.value - that.value;
	}
}
