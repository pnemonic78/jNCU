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
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import net.sf.jncu.fdil.NSOFLargeBinary;

/**
 * Base class for all decompressors.
 * 
 * @author mwaisberg
 */
public abstract class TDecompressor {

	protected static final int LENGTH_VERSION = 4;
	protected static final int LENGTH_SIZE = 4;

	private int length = 0;

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
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public InflaterInputStream decompress(InputStream in) throws IOException {
		// Skip some header - version (usually 0x00000001).
		in.skip(LENGTH_VERSION);

		// Read some header - uncompressed length.
		int n24 = (in.read() & 0xFF) << 24;
		int n16 = (in.read() & 0xFF) << 16;
		int n08 = (in.read() & 0xFF) << 8;
		int n00 = (in.read() & 0xFF) << 0;
		setLength(n24 | n16 | n08 | n00);

		return createInflaterStream(in);
	}

	/**
	 * Decompress the input.
	 * 
	 * @param b
	 *            the input array.
	 * @return the inflater stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public InflaterInputStream decompress(byte[] b) throws IOException {
		return decompress(new ByteArrayInputStream(b));
	}

	/**
	 * Decompress the input.
	 * 
	 * @param blob
	 *            the BLOB.
	 * @return the inflater stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public InflaterInputStream decompress(NSOFLargeBinary blob) throws IOException {
		return decompress(blob.getValue());
	}

	/**
	 * Set the uncompressed length.
	 * 
	 * @param length
	 *            the length.
	 */
	protected void setLength(int length) {
		this.length = length;
	}

	/**
	 * Get the uncompressed length.
	 * 
	 * @return the length.
	 */
	public int getLength() {
		return length;
	}
}
