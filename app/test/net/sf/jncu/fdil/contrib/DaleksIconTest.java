/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * Decode the Daleks icon.
 * <tt>icon={bounds={top: 0, left: 0, bottom: 26, right: 15}, bits='bits, mask='mask}</tt>
 *
 * @author mwaisberg
 */
public class DaleksIconTest {

    private static final byte[] BITS_VALUE = {0, 0, 0, 0, 0, 4, 0, 6, 0, 6, 0, 8, 0, 32, 0, 23, 15, -32, 0, 0, 16, 16, 0, 0, 23, -48, 0, 0, 20, 80, 0, 0, 19, -112, 0, 0, 112, 28,
            0, 0, -128, 2, 0, 0, -128, 2, 0, 0, -107, 82, 0, 0, -112, 18, 0, 0, -107, 82, 0, 0, -112, 18, 0, 0, -112, 18, 0, 0, -104, 50, 0, 0, -120, 34, 0, 0, 72, 36, 0, 0, 56,
            56, 0, 0, 16, 16, 0, 0, 16, 16, 0, 0, 16, 16, 0, 0, 56, 56, 0, 0, 124, 124, 0, 0, 103, -52, 0, 0, 64, 4, 0, 0, 96, 12, 0, 0, 127, -4, 0, 0};
    private static final byte[] MASK_VALUE = {0, 0, 0, 0, 0, 4, 0, 6, 0, 6, 0, 8, 0, 32, 0, 23, 15, -32, 0, 0, 31, -16, 0, 0, 31, -16, 0, 0, 31, -16, 0, 0, 31, -16, 0, 0, 127,
            -4, 0, 0, -1, -2, 0, 0, -1, -2, 0, 0, -1, -2, 0, 0, -1, -2, 0, 0, -1, -2, 0, 0, -1, -2, 0, 0, -1, -2, 0, 0, -1, -2, 0, 0, -1, -2, 0, 0, 127, -4, 0, 0, 63, -8, 0, 0,
            31, -16, 0, 0, 31, -16, 0, 0, 31, -16, 0, 0, 63, -8, 0, 0, 127, -4, 0, 0, 127, -4, 0, 0, 127, -4, 0, 0, 127, -4, 0, 0, 127, -4, 0, 0};

    public DaleksIconTest() {
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

        NSOFSmallRect bounds = new NSOFSmallRect(0, 0, 26, 15);
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
        JOptionPane.showMessageDialog(null, "Daleks", null, JOptionPane.PLAIN_MESSAGE, ii);

        NSOFIcon bmp2 = new NSOFIcon();
        bmp2.setBounds(bounds);
        bmp2.fromIcon(ii);
        Image img2 = bmp2.toImage();
        Icon ii2 = new ImageIcon(img2);
        JOptionPane.showMessageDialog(null, "Daleks #2", null, JOptionPane.PLAIN_MESSAGE, ii2);
    }
}
