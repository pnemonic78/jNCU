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
package net.sf.jncu.fdil.contrib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFLargeBinary;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFSmallRect;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton Streamed Object Format - Raw bitmap data.
 * <p>
 * <em>Warning</em>: Different formats may be used by images or functions in
 * future ROMs. This format will still be supported for displaying images. This
 * format does not describe images created by other applications nor any images
 * provided or found in the Newton ROM. You can use the following format
 * information to create and manipulate your own bitmaps -- preferably at
 * compile time:
 * <p>
 * Binary object <raw bitmap data> - class <tt>'bits</tt>
 * <table>
 * <tr>
 * <th>bytes</th>
 * <th>data-type</th>
 * <th>description</th>
 * </tr>
 * <tr>
 * <td>0-3</td>
 * <td>long</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td>4-5</td>
 * <td>word</td>
 * <td>#bytes per row of the bitmap data (must be a multiple of 4)</td>
 * </tr>
 * <tr>
 * <td>6-7</td>
 * <td>word</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td>8-15</td>
 * <td>bitmap</td>
 * <td>rectangle - portion of bits to use--see IM I</td>
 * </tr>
 * <tr>
 * <td>&nbsp;&nbsp;8-9</td>
 * <td>word</td>
 * <td>top</td>
 * </tr>
 * <tr>
 * <td>&nbsp;&nbsp;10-11</td>
 * <td>word</td>
 * <td>left</td>
 * </tr>
 * <tr>
 * <td>&nbsp;&nbsp;12-13</td>
 * <td>word</td>
 * <td>bottom</td>
 * </tr>
 * <tr>
 * <td>&nbsp;&nbsp;14-15</td>
 * <td>word</td>
 * <td>right</td>
 * </tr>
 * <tr>
 * <td>16-*</td>
 * <td>bits</td>
 * <td>pixel data, 1 for "on" pixel, 0 for "off"</td>
 * </tr>
 * </table>
 * 
 * @author Moshe
 */
public class NSOFRawBitmap extends NSOFLargeBinary {

	/** Default bitmap bits class. */
	public static final NSOFSymbol CLASS_BITS = new NSOFSymbol("bits");

	private int header;
	private int rowBytes;
	private int top, left, bottom, right;
	private byte[] pixels;

	/**
	 * Constructs a new bitmap.
	 */
	public NSOFRawBitmap() {
		super();
		setObjectClass(CLASS_BITS);
	}

	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		super.decode(in, decoder);

		// 0-3 long ignored
		this.header = ntohl(in);
		// 4-5 word #bytes per row of the bitmap data (must be a multiple of 4)
		setRowBytes(ntohs(in));
		// 6-7 word ignored
		in.skip(2);
		// 8-9 word top
		setTop(ntohs(in));
		// 10-11 word left
		setLeft(ntohs(in));
		// 12-13 word bottom
		setBottom(ntohs(in));
		// 14-15 word right
		setRight(ntohs(in));
		// 16-* bits pixel data, 1 for "on" pixel, 0 for "off"
		byte[] pixels = new byte[in.available()];
		readAll(in, pixels);
		setPixels(pixels);
	}

	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		super.encode(out, encoder);

		// 0-3 long ignored
		htonl(header, out);
		// 4-5 word #bytes per row of the bitmap data (must be a multiple of 4)
		htons(rowBytes, out);
		// 6-7 word ignored
		htons(0, out);
		// 8-9 word top
		htons(top, out);
		// 10-11 word left
		htons(left, out);
		// 12-13 word bottom
		htons(bottom, out);
		// 14-15 word right
		htons(right, out);
		// 16-* bits pixel data, 1 for "on" pixel, 0 for "off"
		out.write(pixels);
	}

	/**
	 * Get the number of bytes per row.
	 * 
	 * @return the number of bytes.
	 */
	public int getRowBytes() {
		return rowBytes;
	}

	/**
	 * Set the number of bytes per row. Will always be 32-bit aligned.
	 * 
	 * @param rowBytes
	 *            the number of bytes.
	 */
	protected void setRowBytes(int rowBytes) {
		this.rowBytes = rowBytes & 0xFC;
	}

	/**
	 * Set the rectangle bounds.
	 * 
	 * @param bounds
	 *            the rectangle.
	 */
	public void setBounds(NSOFSmallRect bounds) {
		setTop(bounds.getTop());
		setLeft(bounds.getLeft());
		setBottom(bounds.getBottom());
		setRight(bounds.getRight());
	}

	/**
	 * Get the rectangle top.
	 * 
	 * @return the top.
	 */
	public int getTop() {
		return top;
	}

	/**
	 * Set the rectangle top.
	 * 
	 * @param top
	 *            the top.
	 */
	public void setTop(int top) {
		this.top = top;
	}

	/**
	 * Get the rectangle left.
	 * 
	 * @return the left.
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * Set the rectangle left.
	 * 
	 * @param left
	 *            the left.
	 */
	public void setLeft(int left) {
		this.left = left;

		final int width = getRight() - left;
		int rowBytes = width >> 3;
		if ((width & 7) > 0)
			rowBytes++;
		setRowBytes(rowBytes);
	}

	/**
	 * Get the rectangle bottom.
	 * 
	 * @return the bottom.
	 */
	public int getBottom() {
		return bottom;
	}

	/**
	 * Set the rectangle bottom.
	 * 
	 * @param bottom
	 *            the bottom.
	 */
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	/**
	 * Get the rectangle right.
	 * 
	 * @return the right.
	 */
	public int getRight() {
		return right;
	}

	/**
	 * Set the rectangle right.
	 * 
	 * @param right
	 *            the right.
	 */
	public void setRight(int right) {
		this.right = right;

		final int width = right - getLeft();
		int rowBytes = width >> 3;
		if ((width & 7) > 0)
			rowBytes++;
		setRowBytes(rowBytes);
	}

	/**
	 * Get the bits pixel data.
	 * 
	 * @return the pixels.
	 */
	public byte[] getPixels() {
		return pixels;
	}

	/**
	 * Set the bits pixel data.
	 * 
	 * @param pixels
	 *            the pixels.
	 */
	public void setPixels(byte[] pixels) {
		this.pixels = pixels;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		NSOFRawBitmap copy = new NSOFRawBitmap();
		copy.bottom = this.bottom;
		copy.header = this.header;
		copy.rowBytes = this.rowBytes;
		copy.top = this.top;
		copy.left = this.left;
		copy.bottom = this.bottom;
		copy.right = this.right;
		copy.pixels = this.pixels;
		return copy;
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		NSOFRawBitmap copy = new NSOFRawBitmap();
		copy.bottom = this.bottom;
		copy.header = this.header;
		copy.rowBytes = this.rowBytes;
		copy.top = this.top;
		copy.left = this.left;
		copy.bottom = this.bottom;
		copy.right = this.right;
		if (this.pixels != null) {
			copy.pixels = new byte[this.pixels.length];
			System.arraycopy(this.pixels, 0, copy.pixels, 0, this.pixels.length);
		}
		return copy;
	}
}
