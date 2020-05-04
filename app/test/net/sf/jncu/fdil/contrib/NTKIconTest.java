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

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFSmallRect;

/**
 * Decode the NTK icons.<br>
 * <tt>{unhilited={bounds={top: 0, left: 0, bottom: 19, right: 29}, colordata=[{bitdepth=4, cbits='cbits}, {bitdepth=1, cbits='bits}], mask='mask}, hilited={bounds={top: 0, left: 0, bottom: 32, right: 32}, colordata=[{bitdepth=4, cbits='cbits}, {bitdepth=1, cbits='bits}], mask='mask}}</tt>
 * 
 * @author mwaisberg
 */
public class NTKIconTest {

	private static final byte[] BITS_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 19, 0, 29, 0, 0, 0, 0, 0, 31, -64, 0, 0, 63, -32, 0, 31, -16, 127, -64, 63, -1, -1, -32, 48,
			0, 0, 96, 48, 0, 0, 96, 48, 15, -128, 96, 63, -1, -1, -32, 63, -1, -1, -32, 48, 13, -128, 96, 58, -81, -86, -32, 48, 15, -128, 96, 48, 0, 0, 96, 48, 0, 0, 96, 63, -1,
			-1, -32, 31, -1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static final byte[] MASK_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 19, 0, 29, 0, 0, 0, 0, 0, 31, -64, 0, 0, 63, -32, 0, 31, -1, -1, -64, 63, -1, -1, -32, 63,
			-1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32,
			63, -1, -1, -32, 31, -1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static final byte[] CBITS_1_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 19, 0, 29, 0, 0, 0, 0, 0, 31, -64, 0, 0, 63, -32, 0, 31, -16, 127, -64, 63, -1, -1, -32,
			48, 0, 0, 96, 48, 0, 0, 96, 48, 15, -128, 96, 63, -1, -1, -32, 63, -1, -1, -32, 48, 13, -128, 96, 58, -81, -86, -32, 48, 15, -128, 96, 48, 0, 0, 96, 48, 0, 0, 96, 63,
			-1, -1, -32, 31, -1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static final byte[] CBITS_4_VALUE = { 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 19, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1, -1, -1,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 15, -1, -1, -1, -1, 0, 0, 15, -1, -1, -1, -1, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -16, 0, 0, 0, -1, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 127, -16, 0, 0, 0, -1, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 127, -16,
			0, 0, 0, -1, 119, 119, 119, 119, -1, -1, -9, 119, 119, 119, 127, -16, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -16, 0, 0, 0, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -16, 0, 0, 0, -1, 119, 119, 119, 119, -1, 15, -9, 119, 119, 119, 127, -16, 0, 0, 0, -1, -9, -9, -9, -9, -1, -1, -9, -9, -9, -9, -1, -16, 0, 0,
			0, -1, 119, 119, 119, 119, -1, -1, -9, 119, 119, 119, 127, -16, 0, 0, 0, -1, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 127, -16, 0, 0, 0, -1, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 127, -16, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -16, 0, 0, 0, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private static final byte[] UNHILITED_CBITS_1_VALUE = CBITS_1_VALUE;
	private static final byte[] UNHILITED_CBITS_4_VALUE = CBITS_4_VALUE;
	private static final byte[] UNHILITED_MASK_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 19, 0, 29, 0, 0, 0, 0, 0, 31, -64, 0, 0, 63, -32, 0, 31, -1, -1, -64, 63, -1, -1,
			-32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1,
			-1, -32, 63, -1, -1, -32, 31, -1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static final byte[] HILITED_CBITS_1_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 19, 0, 29, 0, 0, 0, 0, 0, 31, -64, 0, 0, 63, -32, 0, 31, -16, 127, -64, 63, -1,
			-1, -32, 48, 0, 0, 96, 48, 0, 0, 96, 48, 15, -128, 96, 63, -1, -1, -32, 63, -1, -1, -32, 48, 13, -128, 96, 58, -81, -86, -32, 48, 15, -128, 96, 48, 0, 0, 96, 48, 0, 0,
			96, 63, -1, -1, -32, 31, -1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static final byte[] HILITED_CBITS_4_VALUE = { 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 19, 0, 29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, -1,
			-1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -16, 0, 0, 0, 0, 0, 0, 0, 15, -1, -1, -1, -1, 0, 0, 15, -1, -1, -1, -1, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -16, 0, 0, 0, -1, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 127, -16, 0, 0, 0, -1, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119,
			127, -16, 0, 0, 0, -1, 119, 119, 119, 119, -1, -1, -9, 119, 119, 119, 127, -16, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -16, 0, 0, 0, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -16, 0, 0, 0, -1, 119, 119, 119, 119, -1, 15, -9, 119, 119, 119, 127, -16, 0, 0, 0, -1, -9, -9, -9, -9, -1, -1, -9, -9, -9, -9, -1,
			-16, 0, 0, 0, -1, 119, 119, 119, 119, -1, -1, -9, 119, 119, 119, 127, -16, 0, 0, 0, -1, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 127, -16, 0, 0, 0, -1, 119,
			119, 119, 119, 119, 119, 119, 119, 119, 119, 127, -16, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -16, 0, 0, 0, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static final byte[] HILITED_MASK_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 19, 0, 29, 0, 0, 0, 0, 0, 31, -64, 0, 0, 63, -32, 0, 31, -1, -1, -64, 63, -1, -1,
			-32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1, -1, -32, 63, -1,
			-1, -32, 63, -1, -1, -32, 31, -1, -1, -64, 0, 0, 0, 0, 0, 0, 0, 0 };

	private static final int WIDTH = 29;
	private static final int HEIGHT = 19;

	public NTKIconTest() {
	}

	public static void main(String[] args) {
		try {
			testIcon();
			testIconPro();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testIcon() throws Exception {
		NSOFIcon icon = new NSOFIcon();

		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, HEIGHT, WIDTH);
		icon.setBounds(bounds);

		InputStream bitsIn = new ByteArrayInputStream(BITS_VALUE);
		NSOFRawBitmap bits = new NSOFRawBitmap();
		bits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
		bits.inflate(bitsIn, null);
		icon.setBits(bits);

		InputStream maskIn = new ByteArrayInputStream(MASK_VALUE);
		NSOFRawBitmap mask = new NSOFRawBitmap();
		mask.setObjectClass(NSOFRawBitmap.CLASS_BITS);
		mask.inflate(maskIn, null);
		icon.setMask(mask);

		Image img = icon.toImage();
		Icon ii = new ImageIcon(img);
		JOptionPane.showMessageDialog(null, "NTK", null, JOptionPane.PLAIN_MESSAGE, ii);
	}

	private static void testIconPro() throws Exception {
		testIconProUnhilited();
		testIconProHilited();
		testIconProUnhilited4();
		testIconProHilited4();
	}

	private static void testIconProUnhilited() throws Exception {
		NSOFIconPro iconPro = new NSOFIconPro();

		NSOFIcon icon = new NSOFIcon();

		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, HEIGHT, WIDTH);
		icon.setBounds(bounds);

		InputStream cbitsIn = new ByteArrayInputStream(UNHILITED_CBITS_1_VALUE);
		NSOFRawBitmap cbits = new NSOFRawBitmap();
		cbits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
		cbits.inflate(cbitsIn, null);
		NSOFFrame colordata = new NSOFFrame();
		colordata.put(NSOFIcon.SLOT_BIT_DEPTH, new NSOFInteger(1));
		colordata.put(NSOFIcon.SLOT_COLOR_BITS, cbits);
		icon.setColorData(colordata);

		InputStream maskIn = new ByteArrayInputStream(UNHILITED_MASK_VALUE);
		NSOFRawBitmap mask = new NSOFRawBitmap();
		mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);
		mask.inflate(maskIn, null);
		icon.setMask(mask);

		iconPro.setUnhilited(icon);

		Image img = icon.toImage();
		Icon ii = new ImageIcon(img);
		JOptionPane.showMessageDialog(null, "NTK Pro Unhilited 1", null, JOptionPane.PLAIN_MESSAGE, ii);
	}

	private static void testIconProHilited() throws Exception {
		NSOFIconPro iconPro = new NSOFIconPro();

		NSOFIcon icon = new NSOFIcon();

		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, HEIGHT, WIDTH);
		icon.setBounds(bounds);

		InputStream cbitsIn = new ByteArrayInputStream(HILITED_CBITS_1_VALUE);
		NSOFRawBitmap cbits = new NSOFRawBitmap();
		cbits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
		cbits.inflate(cbitsIn, null);
		NSOFFrame colordata = new NSOFFrame();
		colordata.put(NSOFIcon.SLOT_BIT_DEPTH, new NSOFInteger(1));
		colordata.put(NSOFIcon.SLOT_COLOR_BITS, cbits);
		icon.setColorData(colordata);

		InputStream maskIn = new ByteArrayInputStream(HILITED_MASK_VALUE);
		NSOFRawBitmap mask = new NSOFRawBitmap();
		mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);
		mask.inflate(maskIn, null);
		icon.setMask(mask);

		iconPro.setUnhilited(icon);

		Image img = icon.toImage();
		Icon ii = new ImageIcon(img);
		JOptionPane.showMessageDialog(null, "NTK Pro Hilited 1", null, JOptionPane.PLAIN_MESSAGE, ii);
	}

	private static void testIconProUnhilited4() throws Exception {
		NSOFIconPro iconPro = new NSOFIconPro();

		NSOFIcon icon = new NSOFIcon();

		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, HEIGHT, WIDTH);
		icon.setBounds(bounds);

		InputStream cbitsIn = new ByteArrayInputStream(UNHILITED_CBITS_4_VALUE);
		NSOFRawBitmap cbits = new NSOFRawBitmap();
		cbits.setObjectClass(NSOFRawBitmap.CLASS_COLOR_BITS);
		cbits.inflate(cbitsIn, null);
		NSOFFrame colordata = new NSOFFrame();
		colordata.put(NSOFIcon.SLOT_BIT_DEPTH, new NSOFInteger(4));
		colordata.put(NSOFIcon.SLOT_COLOR_BITS, cbits);
		icon.setColorData(colordata);

		InputStream maskIn = new ByteArrayInputStream(UNHILITED_MASK_VALUE);
		NSOFRawBitmap mask = new NSOFRawBitmap();
		mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);
		mask.inflate(maskIn, null);
		icon.setMask(mask);

		iconPro.setUnhilited(icon);

		Image img = icon.toImage();
		Icon ii = new ImageIcon(img);
		JOptionPane.showMessageDialog(null, "NTK Pro Unhilited 4", null, JOptionPane.PLAIN_MESSAGE, ii);
	}

	private static void testIconProHilited4() throws Exception {
		NSOFIconPro iconPro = new NSOFIconPro();

		NSOFIcon icon = new NSOFIcon();

		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, HEIGHT, WIDTH);
		icon.setBounds(bounds);

		InputStream cbitsIn = new ByteArrayInputStream(HILITED_CBITS_4_VALUE);
		NSOFRawBitmap cbits = new NSOFRawBitmap();
		cbits.setObjectClass(NSOFRawBitmap.CLASS_COLOR_BITS);
		cbits.inflate(cbitsIn, null);
		NSOFFrame colordata = new NSOFFrame();
		colordata.put(NSOFIcon.SLOT_BIT_DEPTH, new NSOFInteger(4));
		colordata.put(NSOFIcon.SLOT_COLOR_BITS, cbits);
		icon.setColorData(colordata);

		InputStream maskIn = new ByteArrayInputStream(HILITED_MASK_VALUE);
		NSOFRawBitmap mask = new NSOFRawBitmap();
		mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);
		mask.inflate(maskIn, null);
		icon.setMask(mask);

		iconPro.setUnhilited(icon);

		Image img = icon.toImage();
		Icon ii = new ImageIcon(img);
		JOptionPane.showMessageDialog(null, "NTK Pro Hilited 4", null, JOptionPane.PLAIN_MESSAGE, ii);
	}
}
