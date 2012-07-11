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

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.fdil.NSOFBinaryObject;
import net.sf.jncu.fdil.NSOFDecoder;
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
 * <td align="right">0-3</td>
 * <td>long</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td align="right">4-5</td>
 * <td>word</td>
 * <td>#bytes per row of the bitmap data (must be a multiple of 4)</td>
 * </tr>
 * <tr>
 * <td align="right">6-7</td>
 * <td>word</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td align="right">8-15</td>
 * <td>bitmap</td>
 * <td>rectangle - portion of bits to use--see IM I</td>
 * </tr>
 * <tr>
 * <td align="right">&nbsp;&nbsp;&nbsp;&nbsp;8-9</td>
 * <td>word</td>
 * <td>top</td>
 * </tr>
 * <tr>
 * <td align="right">&nbsp;&nbsp;&nbsp;&nbsp;10-11</td>
 * <td>word</td>
 * <td>left</td>
 * </tr>
 * <tr>
 * <td align="right">&nbsp;&nbsp;&nbsp;&nbsp;12-13</td>
 * <td>word</td>
 * <td>bottom</td>
 * </tr>
 * <tr>
 * <td align="right">&nbsp;&nbsp;&nbsp;&nbsp;14-15</td>
 * <td>word</td>
 * <td>right</td>
 * </tr>
 * <tr>
 * <td align="right">16-*</td>
 * <td>bits</td>
 * <td>pixel data, 1 for "on" pixel, 0 for "off"</td>
 * </tr>
 * </table>
 * 
 * @author Moshe
 * @see "developer/QAs-2.x/html/newtbitm.htm"
 */
public class NSOFRawBitmap extends NSOFBinaryObject {

	/** Default bitmap bits class. */
	public static final NSOFSymbol CLASS_BITS = new NSOFSymbol("bits");
	/** Default bitmap mask class. */
	public static final NSOFSymbol CLASS_MASK = new NSOFSymbol("mask");
	/** Default bitmap colour bits class. */
	public static final NSOFSymbol CLASS_COLOR_BITS = new NSOFSymbol("cbits");

	/** Pixel is "on", i.e. black. */
	public static final int PIXEL_ON = 1;
	/** Pixel is "off", i.e. white or transparent. */
	public static final int PIXEL_OFF = 0;

	/** Bit depth for black and white. */
	public static final int BIT_DEPTH_1 = 1;
	/** Bit depth for 16 shades of gray. */
	public static final int BIT_DEPTH_4 = 4;

	/** ARGB for transparent colour. */
	protected static final int COLOR_TRANSPARENT = 0x00000000;
	/** Black colour. */
	protected static final int COLOR_BLACK = Color.BLACK.getRGB();
	/** ARGB component for opaque colours. */
	protected static final int COLOR_OPAQUE = 0xFF000000;
	protected static final int MASK_ALPHA = 0xFF000000;
	protected static final int MASK_RGB = 0x00FFFFFF;

	private int header;
	private int rowBytes;
	private int top, left, bottom, right;
	private byte[] pixels;
	/**
	 * Number of pxiels per byte.
	 * <p>
	 * 1 bit depth => 1 byte = 8 pixels<br>
	 * 4 bit depth => 1 byte = 2 pixels
	 */
	private int bitDepth = 0;
	private int pixelMask = 1;
	private int pixelShift = 7;

	/**
	 * Constructs a new bitmap.
	 */
	public NSOFRawBitmap() {
		super();
		setObjectClass(CLASS_BITS);
	}

	/**
	 * Constructs a new bitmap.
	 * 
	 * @param bitDepth
	 *            the bit depth.
	 */
	public NSOFRawBitmap(int bitDepth) {
		this();
		setBitDepth(bitDepth);
		setObjectClass((bitDepth == BIT_DEPTH_1) ? CLASS_BITS : CLASS_COLOR_BITS);
	}

	/**
	 * Set the number of bits per pixel.
	 * 
	 * @param bitDepth
	 *            the bit depth.
	 */
	public void setBitDepth(int bitDepth) {
		if ((bitDepth != BIT_DEPTH_1) && (bitDepth != BIT_DEPTH_4))
			throw new IllegalArgumentException("Invalid bit depth: " + bitDepth);
		this.bitDepth = bitDepth;
		this.pixelMask = (1 << bitDepth) - 1;
		this.pixelShift = 8 - bitDepth;
	}

	@Override
	public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
		// 0-3 long ignored
		this.header = ntohl(in);
		// 4-5 word #bytes per row of the bitmap data (must be a multiple of 4)
		int rowBytes = ntohs(in);
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
		setRowBytes(rowBytes);
		if (this.bitDepth == 0) {
			int bitDepth = BIT_DEPTH_1;
			if (rowBytes > 4) {
				bitDepth = (rowBytes << 3) / getWidth();
				if (bitDepth < BIT_DEPTH_4)
					bitDepth = BIT_DEPTH_1;
			}
			setBitDepth(bitDepth);
		}
		// 16-* bits pixel data, 1 for "on" pixel, 0 for "off"
		int size = rowBytes * getHeight();
		if (size < in.available())
			throw new ArrayIndexOutOfBoundsException("Expected array length " + size);
		byte[] pixels = new byte[size];
		readAll(in, pixels);
		setPixels(pixels);
	}

	@Override
	public byte[] getValue() {
		byte[] value = super.getValue();

		if (value == null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream(16 + pixels.length);
			try {
				// 0-3 long ignored
				htonl(header, out);
				// 4-5 word #bytes per row of the bitmap data (must be a
				// multiple of 4)
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
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			value = out.toByteArray();
			super.setValue(value);
		}

		return value;
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
	 * Set the number of bytes per row.<br>
	 * <em>Must always be 32 bit aligned.</em>
	 * 
	 * @param rowBytes
	 *            the number of bytes.
	 */
	protected void setRowBytes(int rowBytes) {
		if ((rowBytes & 0x03) != 0)
			rowBytes++;
		this.rowBytes = rowBytes;
		setValue(null);
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
		setValue(null);
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
		int rowBytes = (width * bitDepth) >> 3;
		if ((width & 7) != 0)
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
		setValue(null);
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
		int rowBytes = (width * bitDepth) >> 3;
		if ((width & 7) != 0)
			rowBytes++;
		setRowBytes(rowBytes);
	}

	/**
	 * Get the height.
	 * 
	 * @return the height.
	 */
	public int getHeight() {
		return getBottom() - getTop();
	}

	/**
	 * Get the width.
	 * 
	 * @return the width.
	 */
	public int getWidth() {
		return getRight() - getLeft();
	}

	/**
	 * Get the bits pixel data.<br>
	 * {@code 1} for "on" pixel, {@code 0} for "off".
	 * 
	 * @return the pixels.
	 */
	public byte[] getPixels() {
		return pixels;
	}

	/**
	 * Set the bits pixel data.<br>
	 * {@code 1} for "on" pixel, {@code 0} for "off".
	 * 
	 * @param pixels
	 *            the pixels.
	 */
	public void setPixels(byte[] pixels) {
		this.pixels = pixels;
		setValue(null);
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

	/**
	 * Get the pixel.
	 * 
	 * @param x
	 *            the X co-ordinate of the pixel.
	 * @param ythe
	 *            Y co-ordinate of the pixel.
	 * @return the pixel.
	 * @see #PIXEL_OFF
	 * @see #PIXEL_ON
	 */
	public int getPixel(int x, int y) {
		int xx = x * bitDepth;
		int off = (y * rowBytes) + (xx >> 3);
		int shift = pixelShift - (xx & 7);
		int pixelByte = pixels[off] & 0xFF;
		return (pixelByte >> shift) & pixelMask;
	}

	/**
	 * Sets a pixel to the RGB color.
	 * 
	 * @param x
	 *            the X co-ordinate of the pixel.
	 * @param y
	 *            the Y co-ordinate of the pixel.
	 * @param pixel
	 *            the pixel.
	 * @see #PIXEL_OFF
	 * @see #PIXEL_ON
	 */
	public void setPixel(int x, int y, int pixel) {
		if (bitDepth == BIT_DEPTH_1) {
			if ((pixel != PIXEL_OFF) && (pixel != PIXEL_ON))
				throw new IllegalArgumentException("Invalid pixel " + pixel);
		} else if (bitDepth == BIT_DEPTH_4) {
			if ((pixel < 0) || (pixel > 15))
				throw new IllegalArgumentException("Invalid pixel " + pixel);
		}
		if (pixels == null) {
			this.pixels = new byte[rowBytes * getHeight()];
		}
		int xx = x * bitDepth;
		int off = (y * rowBytes) + (xx >> 3);
		int shift = pixelShift - (xx & 7);
		int mask = (pixel & pixelMask) << shift;
		pixels[off] &= ~mask; // Clear the bit.
		pixels[off] |= mask; // Set the bit.
		setValue(null);
	}

	/**
	 * Get the pixel as an RGB color.
	 * 
	 * @param x
	 *            the X co-ordinate of the pixel.
	 * @param y
	 *            the Y co-ordinate of the pixel.
	 * @return the pixel color.
	 */
	public int getRGB(int x, int y) {
		int pixel = getPixel(x, y);
		if (pixel == PIXEL_OFF)
			return COLOR_TRANSPARENT;
		if (bitDepth == BIT_DEPTH_4) {
			int gray = (255 - (pixel * 17)) & 0xFF;
			int argb = COLOR_OPAQUE | (gray << 16) | (gray << 8) | (gray << 0);
			return argb;
		}
		return COLOR_BLACK;
	}

	/**
	 * Sets a pixel to a RGB colour.
	 * 
	 * @param x
	 *            the X co-ordinate of the pixel.
	 * @param y
	 *            the Y co-ordinate of the pixel.
	 * @param argb
	 *            the RGB colour.
	 */
	public void setRGB(int x, int y, int rgb) {
		int pixel = PIXEL_OFF;
		// Is non-transparent?
		if ((rgb != COLOR_TRANSPARENT) && ((rgb & MASK_ALPHA) != 0)) {
			if (bitDepth == BIT_DEPTH_1) {
				if (rgb == COLOR_BLACK) {// Is black?
					pixel = PIXEL_ON;
				} else if ((rgb & MASK_RGB) < 0x00888888) {// Is colour dark?
					pixel = PIXEL_ON;
				}
			} else if (bitDepth == BIT_DEPTH_4) {
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb >> 0) & 0xFF;
				float gray = (r * 0.30f) + (g * 0.59f) + (b * 0.11f);
				pixel = (255 - Math.round(gray)) / 17;
			}
		}
		setPixel(x, y, pixel);
	}
}
