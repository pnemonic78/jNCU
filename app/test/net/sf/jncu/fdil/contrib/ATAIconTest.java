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
 * Decode the ATA icons.<br>
 * <tt>{unhilited={bounds={top: 0, left: 0, bottom: 32, right: 32}, colordata=[{bitdepth=4, cbits='cbits}, {bitdepth=1, cbits='bits}], mask='mask}, hilited={bounds={top: 0, left: 0, bottom: 32, right: 32}, colordata=[{bitdepth=4, cbits='cbits}, {bitdepth=1, cbits='bits}], mask='mask}}</tt>
 *
 * @author mwaisberg
 */
public class ATAIconTest {

    private static final byte[] BITS_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 32, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 124, 12, 0, 0, 126, 28, 0, 0, -17, 22, 0,
            0, -53, -90, 0, 1, -58, -93, 0, 1, -123, -61, -128, 3, -121, -127, -128, 3, 19, -116, -64, 6, 51, 12, -64, 6, 115, 30, 96, 12, -7, 0, 112, 8, -7, -128, 48, 25, -7,
            -128, -8, 12, -4, -57, -32, 6, -4, -1, 0, 2, 124, -8, 0, 3, 62, 88, 0, 1, -66, 104, 0, 0, -97, 88, 0, 0, -33, 44, 0, 0, 111, 52, 0, 0, 39, -84, 0, 0, 55, -106, 0, 0,
            16, 14, 0, 0, 24, 28, 0, 0, 15, -4, 0, 0, 7, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final byte[] MASK_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 32, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 11, 0, 0, 0, 26, 16, 0, 0, 16, 56, 0, 0,
            -30, 56, 0, 1, 67, 124, 0, 0, 35, -16, 0, 0, 49, 64, 0, 0, -68, -64, 0, 0, 52, -64, 0, 0, 6, 28, 0, 16, 28, 76, 0, 48, 12, 60, 0, 112, 14, -8, 0, 120, 1, -32, 0, 56,
            56, 0, 0, 24, 7, 0, 0, 28, 0, 0, 0, 12, 0, 0, 0, 14, 0, 0, 0, 22, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0};

    private static final byte[] UNHILITED_CBITS_1_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 32, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 124, 12, 0, 0, 126, 28, 0, 0,
            -17, 22, 0, 0, -53, -90, 0, 1, -58, -93, 0, 1, -123, -61, -128, 3, -121, -127, -128, 3, 19, -116, -64, 6, 51, 12, -64, 6, 115, 30, 96, 12, -7, 0, 112, 8, -7, -128, 48,
            25, -7, -128, -8, 12, -4, -57, -32, 6, -4, -1, 0, 2, 124, -8, 0, 3, 62, 88, 0, 1, -66, 104, 0, 0, -97, 88, 0, 0, -33, 44, 0, 0, 111, 52, 0, 0, 39, -84, 0, 0, 55, -106,
            0, 0, 16, 14, 0, 0, 24, 28, 0, 0, 15, -4, 0, 0, 7, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final byte[] UNHILITED_CBITS_4_VALUE = {0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 32, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, -42, 0, 0, 0, 0, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, -1, -42, 0, 0, 1, -24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 47, -3, -33, -42, 0, 9,
            -51, 48, 0, 0, 0, 0, 0, 0, 0, 0, -97, -42, -49, -1, 48, 45, 61, -64, 0, 0, 0, 0, 0, 0, 0, 2, -1, 100, -103, -17, -128, -76, 9, -9, 0, 0, 0, 0, 0, 0, 0, 9, -5, 51, 121,
            -49, -44, -96, 2, -2, 16, 0, 0, 0, 0, 0, 0, 63, -28, 51, 89, -81, -3, 32, 0, -113, -112, 0, 0, 0, 0, 0, 0, -65, -125, 51, 57, -98, -10, 0, 68, 30, -12, 0, 0, 0, 0, 0,
            4, -3, 51, 68, 55, -99, -64, 0, -69, 8, -4, 0, 0, 0, 0, 0, 11, -11, 51, -55, 53, -99, 48, 4, -1, 65, -33, 112, 0, 0, 0, 0, 79, -93, 56, -3, 51, -102, 0, 11, -1, -80,
            111, -30, 0, 0, 0, 0, -66, 51, 94, -2, 115, 124, 16, 0, 0, 0, 12, -6, 0, 0, 0, 4, -9, 51, -65, -18, -77, 90, -112, 0, 0, 0, 4, -1, 64, 0, 0, 6, -9, 55, -2, -18, -43,
            57, -44, 0, 0, 3, -115, -1, -47, 0, 0, 0, -50, 69, -34, -35, -39, 55, -84, 0, 89, -33, -1, -90, 16, 0, 0, 0, 63, -93, -99, -35, -36, 68, -99, -50, -1, -6, 97, 0, 0, 0,
            0, 0, 7, -10, 76, -36, -52, 115, -118, -1, -58, 16, 0, 0, 0, 0, 0, 0, 0, -50, 55, -52, -52, -93, 105, -33, -96, 0, 0, 0, 0, 0, 0, 0, 0, 63, -109, -84, -69, -75, 89,
            -81, -15, 0, 0, 0, 0, 0, 0, 0, 0, 7, -11, 91, -69, -72, 56, -98, -10, 0, 0, 0, 0, 0, 0, 0, 0, 0, -51, 56, -70, -86, 70, -100, -6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, -125,
            -86, -86, 117, -102, -1, 16, 0, 0, 0, 0, 0, 0, 0, 0, 7, -27, 106, -103, -109, -119, -17, 96, 0, 0, 0, 0, 0, 0, 0, 0, 1, -36, 56, -103, -106, 105, -49, -96, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 79, 117, 119, 100, 73, -33, -80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, -12, 51, 51, 59, -1, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -35, -52, -52, -50, -5, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 55, -121, -117, -69, -76, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final byte[] UNHILITED_MASK_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 32, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 124, 12, 0, 0, 126, 28, 0, 0,
            -1, 30, 0, 0, -1, -66, 0, 1, -1, -65, 0, 1, -1, -1, -128, 3, -1, -1, -128, 3, -1, -1, -64, 7, -1, -1, -64, 7, -1, -1, -32, 15, -1, -1, -16, 15, -1, -1, -16, 31, -1,
            -1, -8, 15, -1, -1, -32, 7, -1, -1, 0, 3, -1, -8, 0, 3, -1, -8, 0, 1, -1, -8, 0, 0, -1, -8, 0, 0, -1, -4, 0, 0, 127, -4, 0, 0, 63, -4, 0, 0, 63, -2, 0, 0, 31, -2, 0,
            0, 31, -4, 0, 0, 15, -4, 0, 0, 7, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final byte[] HILITED_CBITS_1_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 32, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 7, 0, 0, 124, 7, 0, 0, 126, 6, 16, 0,
            -17, 6, 56, 0, -53, 68, 56, 1, -57, -32, 124, 1, -123, -32, 112, 3, -121, -80, -64, 3, 19, 48, 0, 6, 51, 56, 0, 6, 115, 24, 124, 12, -23, 28, 60, 8, -55, -116, 12, 25,
            -119, -114, 0, 12, -124, -58, 0, 6, -60, -57, 0, 2, 100, -1, 0, 3, 34, 88, 0, 1, -78, 104, 0, 0, -111, 88, 0, 0, -55, 44, 0, 0, 109, 52, 0, 0, 36, -84, 0, 0, 55, -106,
            0, 0, 16, 14, 0, 0, 24, 28, 0, 0, 15, -4, 0, 0, 7, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final byte[] HILITED_CBITS_4_VALUE = {0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 32, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, -42, 0, 0, 0, 0, 0, 68, 16, 0, 0, 0, 0, 0, 0, 0, 9, -1, -42, 0, 0, 0, 0, 102, 100, 0, 0, 0, 0, 0, 0, 0, 47, -3, -33, -42, 0,
            0, 0, 102, 97, 0, 0, 0, 0, 0, 0, 0, -97, -42, -49, -1, 32, 0, 1, 102, 16, 0, 2, 0, 0, 0, 0, 2, -1, 100, -103, -17, 97, 0, 1, 97, 0, 0, 68, 0, 0, 0, 0, 9, -5, 51, 121,
            -49, -102, 96, 3, 32, 0, 4, 102, 32, 0, 0, 0, 63, -45, 50, 89, -81, -36, -64, 0, 0, 0, 70, 102, 64, 0, 0, 0, -68, 98, 34, 39, -98, -74, -10, 0, 0, 4, 102, 67, 16, 0,
            0, 4, -55, 33, 34, 36, -115, 49, -19, 0, 0, 51, 32, 0, 0, 0, 0, 10, -77, 17, 67, 19, 121, 0, -113, 112, 0, 0, 0, 0, 0, 0, 0, 76, 97, 19, 53, 18, 106, 0, 47, -31, 0, 0,
            0, 0, 0, 0, 0, -86, 33, 34, 20, 49, 90, 80, 10, -9, 0, 19, 18, 16, 0, 0, 4, -60, 17, 64, 2, 82, 73, -96, 4, -2, 16, 4, 102, 102, 32, 0, 6, -76, 19, 16, 0, 99, 40, -78,
            0, -33, 112, 0, 54, 101, 0, 0, 0, -104, 18, 0, 0, 53, 38, -103, 0, 111, -31, 0, 2, 99, 0, 0, 0, 40, 81, 32, 0, 22, 51, -100, 16, 30, -7, 0, 0, 33, 0, 0, 0, 5, 114, 16,
            0, 5, 82, -119, -53, -17, -1, 32, 0, 0, 0, 0, 0, 0, 118, 18, 0, 1, 115, 105, -33, 84, 68, 32, 0, 0, 0, 0, 0, 0, 39, 65, 16, 0, 101, 73, -81, 112, 0, 0, 0, 0, 0, 0, 0,
            0, 5, 114, 32, 0, 39, 40, -98, -32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 117, 33, 16, 7, 86, -100, -10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 39, 50, 16, 4, -124, -102, -5, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 4, 98, 34, 1, -107, -119, -17, 48, 0, 0, 0, 0, 0, 0, 0, 0, 1, 101, 35, 87, -88, 105, -49, -112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 39, 50, 35, 69, 73, -33, -80, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 5, 114, 34, 51, 59, -1, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -103, -102, -68, -50, -5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 39, 103, -117, -69, -76,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final byte[] HILITED_MASK_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 32, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 124, 12, 0, 0, 126, 28, 0, 0, -1,
            30, 0, 0, -1, -66, 0, 1, -1, -65, 0, 1, -1, -1, -128, 3, -1, -1, -128, 3, -1, -1, -64, 7, -1, -1, -64, 7, -1, -1, -32, 15, -1, -1, -16, 15, -1, -1, -16, 31, -1, -1,
            -8, 15, -1, -1, -32, 7, -1, -1, 0, 3, -1, -8, 0, 3, -1, -8, 0, 1, -1, -8, 0, 0, -1, -8, 0, 0, -1, -4, 0, 0, 127, -4, 0, 0, 63, -4, 0, 0, 63, -2, 0, 0, 31, -2, 0, 0,
            31, -4, 0, 0, 15, -4, 0, 0, 7, -8, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    public ATAIconTest() {
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
        mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);
        mask.inflate(maskIn, null);
        // icon.setMask(mask);

        Image img = icon.toImage();
        Icon ii = new ImageIcon(img);
        JOptionPane.showMessageDialog(null, "ATA", null, JOptionPane.PLAIN_MESSAGE, ii);
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
        JOptionPane.showMessageDialog(null, "ATA Pro Unhilited 1", null, JOptionPane.PLAIN_MESSAGE, ii);
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
        JOptionPane.showMessageDialog(null, "ATA Pro Hilited 1", null, JOptionPane.PLAIN_MESSAGE, ii);
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
        JOptionPane.showMessageDialog(null, "ATA Pro Unhilited 4", null, JOptionPane.PLAIN_MESSAGE, ii);

        NSOFIcon bmp2 = new NSOFIcon();
        bmp2.setBounds(bounds);
        bmp2.fromIcon(ii);
        Image img2 = bmp2.toImage();
        Icon ii2 = new ImageIcon(img2);
        JOptionPane.showMessageDialog(null, "Attaxx Pro Unhilited 4 #2", null, JOptionPane.PLAIN_MESSAGE, ii2);
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
        JOptionPane.showMessageDialog(null, "ATA Pro Hilited 4", null, JOptionPane.PLAIN_MESSAGE, ii);
    }
}
