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
package net.sf.jncu.fdil.zip;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import net.sf.jncu.fdil.NSOFLargeBinary;

/**
 * Base class for all decompressors.
 * 
 * @author mwaisberg
 */
public abstract class TDecompressor {

	/**
	 * Creates a new decompressor.
	 */
	public TDecompressor() {
	}

	/**
	 * Create the inflater stream.
	 * 
	 * @param in
	 *            the input stream to inflate.
	 * @return the inflater input stream.
	 */
	protected abstract InflaterInputStream createInflaterStream(InputStream in);

	/**
	 * Decompress the input.
	 * 
	 * @param in
	 *            the input stream.
	 * @return the inflater stream.
	 */
	public InputStream decompress(InputStream in) {
		return createInflaterStream(in);
	}

	/**
	 * Decompress the input.
	 * 
	 * @param b
	 *            the input array.
	 * @return the inflater stream.
	 */
	public InputStream decompress(byte[] b) {
		return decompress(new ByteArrayInputStream(b));
	}

	/**
	 * Decompress the input.
	 * 
	 * @param blob
	 *            the BLOB.
	 * @return the inflater stream.
	 */
	public InputStream decompress(NSOFLargeBinary blob) {
		return decompress(blob.getValue());
	}
}
