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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFSmallRect;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton Streamed Object Format - Bitmap object.
 * 
 * @author moshew
 */
public class NSOFBitmap extends NSOFFrame {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("bitmap");

	public static final NSOFSymbol SLOT_BOUNDS = new NSOFSymbol("bounds");
	public static final NSOFSymbol SLOT_BITS = new NSOFSymbol("bits");
	public static final NSOFSymbol SLOT_MASK = new NSOFSymbol("mask");

	/**
	 * Creates a new bitmap.
	 */
	public NSOFBitmap() {
		super();
		setNSClass(NS_CLASS);
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
		NSOFBitmap bmp = (NSOFBitmap) decoder.decode(in);
		setBits(bmp.getBits());
		setBounds(bmp.getBounds());
		setMask(bmp.getMask());
		setNSClass(bmp.getNSClass());
	}
}
