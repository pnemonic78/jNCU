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
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command controls which VBOs are sent compressed to the desktop. VBO can
 * always be sent compressed, never compressed or only package VBOs sent
 * compressed.
 * 
 * <pre>
 * 'cvbo'
 * length
 * what
 * </pre>
 * 
 * @see #UNCOMPRESSED
 * @see #COMPRESSED_PACKAGES
 * @see #COMPRESSED_VBO
 * @author moshew
 */
public class DSetVBOCompression extends DockCommandToNewtonLong {

	/** <tt>kDSetVBOCompression</tt> */
	public static final String COMMAND = "cvbo";

	/**
	 * <tt>eUncompressedVBOs</tt><br>
	 * VBO sent uncompressed.
	 */
	public static final int UNCOMPRESSED = 0;
	/**
	 * <tt>eCompressedPackagesOnly</tt><br>
	 * Only package VBOs sent compressed.
	 */
	public static final int COMPRESSED_PACKAGES = 1;
	/**
	 * <tt>eCompressedVBOs</tt><br>
	 * VBO sent compressed.
	 */
	public static final int COMPRESSED_VBO = 2;

	/**
	 * Creates a new command.
	 */
	public DSetVBOCompression() {
		super(COMMAND);
	}

	/**
	 * Get the compression.
	 * 
	 * @return the compression.
	 */
	public int getCompression() {
		return getValue();
	}

	/**
	 * Set the compression.
	 * 
	 * @param compression
	 *            the compression.
	 */
	public void setCompression(int compression) {
		setValue(compression);
	}
}
