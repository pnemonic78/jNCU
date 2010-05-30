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
 * A real is a binary object that contains a double precision floating point
 * number. It is an 8-byte binary object containing an IEEE-754 floating point
 * value.
 * 
 * @author moshew
 */
public class FDReal extends FDBinaryObject {

	private final double value;

	/**
	 * Creates a new real.
	 * 
	 * @param value
	 *            the value to represent.
	 */
	public FDReal(double value) {
		super();
		this.value = value;
	}

	/**
	 * Get the value.
	 * 
	 * @return the value.
	 */
	public double getReal() {
		return value;
	}

}
