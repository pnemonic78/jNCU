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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Lempel-Ziv store decompressor.
 * 
 * @author mwaisberg
 * 
 */
public class LZStoreDecompressor extends StoreDecompressor {

	/**
	 * Creates a new decompressor.
	 */
	public LZStoreDecompressor() {
	}

	@Override
	protected InflaterInputStream createInflaterStream(InputStream in) {
		return new LZStoreInputStream(in);
	}

	public static void main(String[] args) throws Exception {
		Decompressor decomp = CompanderFactory.getInstance().createDecompressor("TLZStoreDecompressor");

		File f = new File("Packages/Hebrew Font:Prism(48).TLZStoreDecompressor");
		File f2 = new File("Packages/Decompressor/Hebrew.pkg");
		InputStream fin = null;
		InputStream de = null;
		OutputStream fout = null;
		int b;

		try {
			fin = new FileInputStream(f);
			de = decomp.decompress(fin);
			f2.getParentFile().mkdirs();
			fout = new FileOutputStream(f2);
			b = de.read();
			while (b != -1) {
				fout.write(b);
				b = de.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fin != null)
				fin.close();
			if (fout != null)
				fout.close();
		}
		if (f2.length() != decomp.getLength())
			throw new ArrayIndexOutOfBoundsException(decomp.getLength());
	}
}
