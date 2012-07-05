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

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

/**
 * This class implements an input stream filter for reading files in the Simple
 * Store file format.
 * 
 * @author mwaisberg
 * 
 */
public class SimpleStoreInputStream extends InflaterInputStream {

	private int pos;
	private int posNext = 0;

	/**
	 * Creates a new input stream.
	 * 
	 * @param in
	 *            the actual input stream.
	 */
	public SimpleStoreInputStream(InputStream in) {
		super(in);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (pos == posNext) {
			if (posNext == 0) {
				in.skip(16);
				pos += 16;
				posNext += 12;
			} else {
				in.skip(8);
				pos += 8;
			}
			posNext += 1030;
		}
		int count = in.read(b, off, Math.min(1024, len));
		if (count > 0)
			pos += count;
		return count;
	}
}
