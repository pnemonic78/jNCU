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
package net.sf.jncu.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Find and Replace in the input stream.
 * 
 * @author mwaisberg
 */
public class ReplaceInputStream extends BufferedInputStream {

	protected final byte[] find;
	protected final byte[] replace;

	// having reached the end of the underlying input stream
	private boolean done = false;

	/*
	 * the buffer holding data that have been read in from the underlying
	 * stream, but have not been processed by the cipher engine. the size 512
	 * bytes is somewhat randomly chosen
	 */
	private byte[] ibuffer;

	// the offset pointing to the next "new" byte
	private int ipos = 0;

	public ReplaceInputStream(InputStream in, byte[] find, byte[] replace) {
		this(in, find, replace, 256);
	}

	public ReplaceInputStream(InputStream in, byte[] find, byte[] replace, int size) {
		super(in, size);
		this.find = find;
		this.replace = replace;
		this.ibuffer = new byte[Math.max(size, find.length << 1)];
	}

	@Override
	public synchronized int read(byte b[], int off, int len) throws IOException {
		if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (len == 0) {
			return 0;
		}
		if (pos >= count) {
			// we loop for new data as the spec says we are blocking
			int i = 0;
			while (i == 0)
				i = fill();
			if (i == -1)
				return -1;
		}
		len = Math.min(len, available());
		if (len == 0) {
			return 0;
		}
		for (int i = 0; i < len; i++) {
			b[off++] = (byte) read();
		}
		return len;
	}

	@Override
	public synchronized int read() throws IOException {
		if (pos >= count) {
			// we loop for new data as the spec says we are blocking
			int i = 0;
			while (i == 0)
				i = fill();
			if (i == -1)
				return -1;
		}
		return (buf[pos++] & 0xff);
	}

	@Override
	public int available() throws IOException {
		int avail = count - pos;
		if (/* (avail == 0) && */!done && (in.available() > 0)) {
			avail = fill();
		}
		return avail;
	}

	@Override
	public long skip(long n) throws IOException {
		long available = available();
		if (n > available) {
			n = available;
		}
		if (n < 0) {
			return 0;
		}
		pos += n;
		return n;
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	private int fill() throws IOException {
		if (done)
			return -1;
		int readin = in.read(ibuffer, ipos, ibuffer.length - ipos);
		if (readin < 0) {
			done = true;
			return count - pos;
		}
		if (readin == 0)
			return count - pos;
		int icount = ipos + readin;

		// Find all matches.
		final int findLength = find.length;
		final int replaceLength = replace.length;
		int matchStart = 0;
		int matchCount = ipos;
		int fpos = ipos;

		while (ipos < icount) {
			if (ibuffer[ipos] == find[fpos]) {
				if (matchCount == 0)
					matchStart = ipos;
				matchCount++;
				fpos++;

				if (matchCount == findLength) {
					append(ibuffer, matchStart, false);
					append(replace, replaceLength, false);
					int shift = matchStart + matchCount;
					System.arraycopy(ibuffer, shift, ibuffer, 0, ibuffer.length - shift);
					ipos = 0;
					icount -= shift;
					matchStart = ipos;
					matchCount = 0;
				}
			} else {
				matchStart = ipos + 1;
				matchCount = 0;
				fpos = 0;
			}
			ipos++;
		}

		append(ibuffer, matchStart, true);
		ipos = matchCount;

		return count - pos;
	}

	/**
	 * Block copy from the input buffer to output buffer and shift the input
	 * buffer.
	 * 
	 * @param src
	 *            the source buffer.
	 * @param length
	 *            the number of source bytes to copy.
	 * @param shift
	 *            shift the source buffer?
	 */
	private void append(byte[] src, int length, boolean shift) {
		if (length > 0) {
			int opos = pos + count;
			int ocount = opos + length;
			if (ocount >= buf.length) { /* no room left in buffer */
				if (pos > 0) { /* can throw away early part of the buffer */
					System.arraycopy(buf, pos, buf, 0, count);
					pos = 0;
					opos = pos + count;
					ocount = opos + length;
				}
				if (ocount >= buf.length) { /* definitely no room left in buffer */
					final byte[] bufOld = buf;
					buf = new byte[ocount];
					System.arraycopy(bufOld, pos, buf, 0, count);
				}
			}

			System.arraycopy(src, 0, buf, opos, length);
			count += length;
		}

		if (shift) {
			System.arraycopy(src, length, src, 0, src.length - length);
		}
	}
}
