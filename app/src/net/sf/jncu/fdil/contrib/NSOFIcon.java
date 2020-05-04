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
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFSmallRect;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton Streamed Object Format - Icon object.
 * 
 * @author moshew
 * @see "developer/QAs-2.x/html/newtbitm.htm"
 */
public class NSOFIcon extends NSOFFrame {

	/** Default icon class. */
	public static final NSOFSymbol CLASS_ICON = new NSOFSymbol("icon");

	public static final NSOFSymbol SLOT_BOUNDS = new NSOFSymbol("bounds");
	public static final NSOFSymbol SLOT_BITS = new NSOFSymbol("bits");
	public static final NSOFSymbol SLOT_MASK = new NSOFSymbol("mask");
	public static final NSOFSymbol SLOT_COLOR_DATA = new NSOFSymbol("colordata");
	public static final NSOFSymbol SLOT_BIT_DEPTH = new NSOFSymbol("bitdepth");
	public static final NSOFSymbol SLOT_COLOR_BITS = new NSOFSymbol("cbits");

	/**
	 * Creates a new icon.
	 */
	public NSOFIcon() {
		super();
		setObjectClass(CLASS_ICON);
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
	 * @return the bits - {@code null} otherwise.
	 */
	public NSOFRawBitmap getBits() {
		NSOFObject slot = get(SLOT_BITS);
		if (NSOFImmediate.isNil(slot))
			return null;
		return (NSOFRawBitmap) slot;
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
	 * Get the raw bitmap mask data.
	 * 
	 * @return the mask - {@code null} otherwise.
	 */
	public NSOFRawBitmap getMask() {
		NSOFObject slot = get(SLOT_MASK);
		if (NSOFImmediate.isNil(slot))
			return null;
		return (NSOFRawBitmap) slot;
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		NSOFIcon copy = new NSOFIcon();
		copy.putAll(this);
		return copy;
	}

	@Override
	public NSOFObject deepClone() throws CloneNotSupportedException {
		NSOFIcon copy = new NSOFIcon();
		copy.putAll((NSOFFrame) super.deepClone());
		return copy;
	}

	/**
	 * Convert this bitmap to an AWT image.
	 * 
	 * @return the image.
	 */
	public Image toImage() {
		NSOFRawBitmap bits = getBits();
		NSOFRawBitmap mask = getMask();
		if (bits == null) {
			NSOFFrame colorData = findColorData(NSOFRawBitmap.BIT_DEPTH_4);
			if (colorData == null)
				colorData = findColorData(NSOFRawBitmap.BIT_DEPTH_1);
			if (colorData != null)
				bits = (NSOFRawBitmap) colorData.get(SLOT_COLOR_BITS);
			if ((bits == null) || NSOFImmediate.isNil(bits))
				throw new IllegalArgumentException("Bits required");
		}
		int width = bits.getWidth();
		int height = bits.getHeight();
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int rgb;
		int maskPixel;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				rgb = bits.getRGB(x, y);
				maskPixel = (mask == null) ? NSOFRawBitmap.PIXEL_ON : mask.getPixel(x, y);
				if (maskPixel == NSOFRawBitmap.PIXEL_ON)
					img.setRGB(x, y, rgb);
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
		Graphics g = bimage.getGraphics();
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
		int width = image.getWidth();
		int height = image.getHeight();

		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, width, height);
		setBounds(bounds);

		Set<Integer> colors = countHistogram(image);

		NSOFRawBitmap bits = new NSOFRawBitmap((colors.size() < 4) ? NSOFRawBitmap.BIT_DEPTH_1 : NSOFRawBitmap.BIT_DEPTH_4);
		bits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
		bits.setRight(width);
		bits.setBottom(height);
		setBits(bits);

		NSOFRawBitmap mask = new NSOFRawBitmap(NSOFRawBitmap.BIT_DEPTH_1);
		mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);
		mask.setRight(width);
		mask.setBottom(height);
		setMask(mask);

		int rgb;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				rgb = image.getRGB(x, y);
				bits.setRGB(x, y, rgb);
				if (rgb == NSOFRawBitmap.COLOR_TRANSPARENT)
					mask.setPixel(x, y, NSOFRawBitmap.PIXEL_OFF);
				else
					mask.setPixel(x, y, NSOFRawBitmap.PIXEL_ON);
			}
		}
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

	/**
	 * Get the colour data. The is relevant to "iconPro" icons.
	 * 
	 * @return the data frame or array of data frames.
	 */
	public NSOFObject getColorData() {
		return get(SLOT_COLOR_DATA);
	}

	/**
	 * Set the colour data. The is relevant to "iconPro" icons.
	 * 
	 * @param data
	 *            the data frame.
	 */
	public void setColorData(NSOFFrame data) {
		put(SLOT_COLOR_DATA, data);
	}

	/**
	 * Set the colour data. The is relevant to "iconPro" icons.
	 * 
	 * @param data
	 *            the array of data frames.
	 */
	public void setColorData(NSOFArray data) {
		put(SLOT_COLOR_DATA, data);
	}

	/**
	 * Find colour data.
	 * 
	 * @param bitDepth
	 *            the pixel bit depth. .
	 * @return the colour data - {@code null} otherwise.
	 * @see NSOFRawBitmap#BIT_DEPTH_1
	 * @see NSOFRawBitmap#BIT_DEPTH_4
	 */
	public NSOFFrame findColorData(int bitDepth) {
		if ((bitDepth != NSOFRawBitmap.BIT_DEPTH_1) && (bitDepth != NSOFRawBitmap.BIT_DEPTH_4))
			throw new IllegalArgumentException("Invalid bit depth: " + bitDepth);

		NSOFObject colorData = getColorData();
		if (colorData == null)
			return null;
		NSOFFrame frame;
		NSOFImmediate depth;

		if (colorData instanceof NSOFFrame) {
			frame = (NSOFFrame) colorData;
			depth = (NSOFImmediate) frame.get(SLOT_BIT_DEPTH);
			if (depth.getValue() == bitDepth)
				return frame;
		} else if (colorData instanceof NSOFArray) {
			NSOFArray arr = (NSOFArray) colorData;
			int length = arr.length();
			for (int i = 0; i < length; i++) {
				frame = (NSOFFrame) arr.get(i);
				depth = (NSOFImmediate) frame.get(SLOT_BIT_DEPTH);
				if (depth.getValue() == bitDepth)
					return frame;
			}
		}
		return null;
	}

	/**
	 * Set colour data bits.
	 * 
	 * @param bitDepth
	 *            the pixel bit depth.
	 * @param bits
	 *            the icon bits.
	 * @see NSOFRawBitmap#BIT_DEPTH_1
	 * @see NSOFRawBitmap#BIT_DEPTH_4
	 */
	public void setColorBits(int bitDepth, NSOFRawBitmap bits) {
		NSOFFrame data = new NSOFFrame();
		data.put(SLOT_BIT_DEPTH, new NSOFInteger(bitDepth));
		data.put(SLOT_COLOR_BITS, bits);

		NSOFObject colorData = getColorData();
		if (NSOFImmediate.isNil(colorData)) {
			setColorData(data);
			return;
		}
		NSOFArray arr;
		NSOFFrame frame;
		NSOFImmediate depth;
		int bitDepthValue;

		if (colorData instanceof NSOFFrame) {
			frame = (NSOFFrame) colorData;
			depth = (NSOFImmediate) frame.get(SLOT_BIT_DEPTH);
			if (NSOFImmediate.isNil(depth)) {
				// Should not happen - bitDepth slot should always exist.
				setColorData(data);
				return;
			}
			bitDepthValue = depth.getValue();
			if (bitDepthValue == bitDepth) {
				setColorData(data);
				return;
			}
			arr = new NSOFPlainArray(2);
			if (bitDepthValue < bitDepth) {
				arr.set(0, frame);
				arr.set(1, data);
			} else {
				arr.set(0, data);
				arr.set(1, frame);
			}
		} else if (colorData instanceof NSOFArray) {
			arr = (NSOFArray) colorData;
			int length = arr.length();
			int insertAt = length;
			for (int i = 0; i < length; i++) {
				frame = (NSOFFrame) arr.get(i);
				depth = (NSOFImmediate) frame.get(SLOT_BIT_DEPTH);
				bitDepthValue = depth.getValue();
				if (bitDepthValue == bitDepth) {
					arr.set(i, data);
					return;
				}
				if (bitDepth < bitDepthValue)
					insertAt = i;
			}
			arr.insert(insertAt, data);
		}
	}

	/**
	 * Count the number of unique colours, including transparent.
	 * 
	 * @param image
	 *            the image to count.
	 * @return a type of histogram.
	 */
	protected Set<Integer> countHistogram(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		Set<Integer> colors = new HashSet<Integer>();

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				colors.add(image.getRGB(x, y));
			}
		}

		return colors;
	}
}
