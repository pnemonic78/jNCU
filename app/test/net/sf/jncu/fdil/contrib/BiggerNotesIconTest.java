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

import net.sf.jncu.fdil.NSOFSmallRect;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Decode the BiggerNotes icon.
 * <tt>icon={bounds={top: 0, left: 0, bottom: 27, right: 24}, bits='bits, mask='mask}</tt>
 *
 * @author mwaisberg
 */
public class BiggerNotesIconTest {

    private static final byte[] BITS_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 27, 0, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, 0, 0, 0, 63, 0, 0, 0, 51, 0, 31, -1, 123, 0, 63,
            -1, 110, 0, 48, 0, -50, 0, 55, -4, -52, 0, 55, -3, -100, 0, 52, 5, -104, 0, 53, 83, 56, 0, 52, 3, 48, 0, 53, 86, 112, 0, 52, 6, 96, 0, 53, 84, -32, 0, 52, 12, -64, 0,
            53, 79, -64, 0, 52, 11, -128, 0, 54, 15, 0, 0, 55, -2, -128, 0, 55, -3, -128, 0, 48, 11, -128, 0, 24, 3, 0, 0, 31, -1, 0, 0, 15, -2, 0, 0, 0, 0, 0, 0};
    private static final byte[] MASK_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 27, 0, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, 0, 0, 0, 63, 0, 0, 0, 63, 0, 31, -1, 127, 0, 63,
            -1, 126, 0, 63, -1, -2, 0, 63, -1, -4, 0, 63, -1, -4, 0, 63, -1, -8, 0, 63, -1, -8, 0, 63, -1, -16, 0, 63, -1, -16, 0, 63, -1, -32, 0, 63, -1, -32, 0, 63, -1, -64, 0,
            63, -1, -64, 0, 63, -1, -128, 0, 63, -1, 0, 0, 63, -1, -128, 0, 63, -1, -128, 0, 63, -1, -128, 0, 31, -1, 0, 0, 31, -1, 0, 0, 15, -2, 0, 0, 0, 0, 0, 0};

    public BiggerNotesIconTest() {
    }

    public static void main(String[] args) {
        try {
            testIcon();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testIcon() throws Exception {
        NSOFIcon icon = new NSOFIcon();

        NSOFSmallRect bounds = new NSOFSmallRect(0, 0, 27, 24);
        icon.setBounds(bounds);

        InputStream bitsIn = new ByteArrayInputStream(BITS_VALUE);
        NSOFRawBitmap bits = new NSOFRawBitmap();
        bits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
        bits.inflate(bitsIn, null);
        icon.setBits(bits);

        InputStream maskIn = new ByteArrayInputStream(MASK_VALUE);
        NSOFRawBitmap mask = new NSOFRawBitmap();
        mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);
        mask.inflate(maskIn, null);
        icon.setMask(mask);

        Image img = icon.toImage();
        Icon ii = new ImageIcon(img);
        JOptionPane.showMessageDialog(null, "BiggerNotes", null, JOptionPane.PLAIN_MESSAGE, ii);
    }
}
