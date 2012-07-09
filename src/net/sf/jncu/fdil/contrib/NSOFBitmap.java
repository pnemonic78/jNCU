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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFSmallRect;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton Streamed Object Format - Bitmap object.
 * 
 * @author moshew
 * @see "developer/QAs-2.x/html/newtbitm.htm"
 */
public class NSOFBitmap extends NSOFFrame {

	/** Default bitmap class. */
	public static final NSOFSymbol CLASS_BITMAP = new NSOFSymbol("bitmap");

	public static final NSOFSymbol SLOT_BOUNDS = new NSOFSymbol("bounds");
	public static final NSOFSymbol SLOT_BITS = new NSOFSymbol("bits");
	public static final NSOFSymbol SLOT_MASK = new NSOFSymbol("mask");

	/**
	 * Creates a new bitmap.
	 */
	public NSOFBitmap() {
		super();
		setObjectClass(CLASS_BITMAP);
	}

	/**
	 * Get the bounds frame.
	 * 
	 * @return the bounds.
	 */
	public NSOFSmallRect getBounds() {
		return (NSOFSmallRect) get(SLOT_BOUNDS);
	}

	/**
	 * Set the bounds frame.
	 * 
	 * @param bounds
	 *            the bounds.
	 */
	public void setBounds(NSOFSmallRect bounds) {
		put(SLOT_BOUNDS, bounds);
	}

	/**
	 * Get the raw bitmap data.
	 * 
	 * @return the bits.
	 */
	public NSOFRawBitmap getBits() {
		return (NSOFRawBitmap) get(SLOT_BITS);
	}

	/**
	 * Set the raw bitmap data.
	 * 
	 * @param bits
	 *            the bits.
	 */
	public void setBits(NSOFRawBitmap bits) {
		put(SLOT_BITS, bits);
	}

	/**
	 * Get the raw bitmap data for mask.
	 * 
	 * @return the mask.
	 */
	public NSOFRawBitmap getMask() {
		return (NSOFRawBitmap) get(SLOT_MASK);
	}

	/**
	 * Set the raw bitmap data for mask.
	 * 
	 * @param mask
	 *            the mask.
	 */
	public void setMask(NSOFRawBitmap mask) {
		put(SLOT_MASK, mask);
	}

	/**
	 * Set the value from the binary object.
	 * 
	 * @param value
	 *            the encoded bitmap.
	 * @param decoder
	 *            the decoder.
	 * @throws IOException
	 *             if a decoding error occurs.
	 */
	public void setValue(byte[] value, NSOFDecoder decoder) throws IOException {
		// Decode the frame.
		InputStream in = new ByteArrayInputStream(value);
		NSOFObject o = decoder.inflate(in);
		if (!NSOFImmediate.isNil(o)) {
			NSOFBitmap bmp = (NSOFBitmap) o;
			setBits(bmp.getBits());
			setBounds(bmp.getBounds());
			setMask(bmp.getMask());
			setObjectClass(bmp.getObjectClass());
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		NSOFBitmap copy = new NSOFBitmap();
		copy.setBits(this.getBits());
		copy.setBounds(this.getBounds());
		copy.setMask(this.getMask());
		return copy;
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		NSOFBitmap copy = new NSOFBitmap();
		copy.setBits((NSOFRawBitmap) this.getBits().deepClone());
		copy.setBounds((NSOFSmallRect) this.getBounds().deepClone());
		copy.setMask((NSOFRawBitmap) this.getMask().deepClone());
		return copy;
	}

	/**
	 * Convert this bitmap to an AWT image.
	 * 
	 * @return the image.
	 */
	public Image toImage() {
		NSOFRawBitmap bits = getBits();
		int width = bits.getRight() - bits.getLeft();
		int height = bits.getBottom() - bits.getTop();
		int rowBytes = bits.getRowBytes();
		byte[] pixels = bits.getPixels();
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		int off = 0;
		int x;
		int pixelByte;
		int pixelMask;
		int bit;
		int rgb;

		for (int y = 0; y < height; y++) {
			x = 0;

			for (int c = 0; c < rowBytes; c++) {
				pixelMask = 0x80;
				pixelByte = pixels[off++] & 0xFF;

				for (int b = 0; (b < 8) && (x < width); b++, x++) {
					bit = pixelByte & pixelMask;
					rgb = (bit == NSOFRawBitmap.PIXEL_OFF) ? 0x00000000 : 0xFF000000;
					img.setRGB(x, y, rgb);
					pixelMask >>>= 1;
				}
			}
		}

		return img;
	}

	/**
	 * Decode an AWT image.
	 * 
	 * @param image
	 *            the source image.
	 */
	public void fromImage(Image image) {
		if (image instanceof BufferedImage) {
			fromImage((BufferedImage) image);
			return;
		}

		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.finalize();
		fromImage(bimage);
	}

	/**
	 * Decode an AWT image.
	 * 
	 * @param image
	 *            the source image.
	 */
	public void fromImage(BufferedImage image) {
		NSOFRawBitmap bits = new NSOFRawBitmap();
		bits.setObjectClass(NSOFRawBitmap.CLASS_BITS);

		NSOFRawBitmap mask = new NSOFRawBitmap();
		mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);

		int width = image.getWidth(null);
		int height = image.getHeight(null);
		bits.setRight(width);
		bits.setBottom(height);
		mask.setRight(width);
		mask.setBottom(height);
		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, width, height);
		setBounds(bounds);

		int rowBytes = width >> 3;
		if ((width & 0x07) > 0)
			rowBytes++;
		bits.setRowBytes(rowBytes);

		int rgb;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				rgb = image.getRGB(x, y);
				bits.setRGB(x, y, rgb);
				mask.setPixel(x, y, NSOFRawBitmap.PIXEL_ON);
			}
		}

		setBits(bits);
		setMask(mask);
	}

	/**
	 * Decode a Swing icon.
	 * 
	 * @param icon
	 *            the source icon.
	 * @see #fromImage(Image)
	 */
	public void fromIcon(ImageIcon icon) {
		fromImage(icon.getImage());
	}

	/**
	 * Decode a Swing icon.
	 * 
	 * @param icon
	 *            the source icon.
	 */
	public void fromIcon(Icon icon) {
		if (icon instanceof ImageIcon) {
			fromIcon((ImageIcon) icon);
			return;
		}

		int width = icon.getIconWidth();
		int height = icon.getIconHeight();
		Component c = new JPanel();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		icon.paintIcon(c, g, 0, 0);
		g.finalize();
		fromImage(image);
	}

}
