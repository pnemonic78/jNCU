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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.fdil.NSOFLargeBinary;

/**
 * Base class for all compressors.
 * 
 * @author mwaisberg
 */
public abstract class Compressor {

	/**
	 * Creates a new compressor.
	 */
	public Compressor() {
	}

	/**
	 * Create the inflater stream.
	 * 
	 * @param out
	 *            the output stream to deflate.
	 * @return the inflater input stream.
	 */
	protected abstract OutputStream createDeflaterStream(OutputStream out);

	/**
	 * Decompress the input.
	 * 
	 * @param out
	 *            the output stream.
	 * @return the inflater stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public OutputStream compress(OutputStream out) throws IOException {
		return createDeflaterStream(out);
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
	public OutputStream compress(byte[] b) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(b.length);
		out.write(b);
		return compress(out);
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
	public OutputStream compress(NSOFLargeBinary blob) throws IOException {
		return compress(blob.getValue());
	}

}
