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
 * Decode the Attaxx icon.
 * <tt>icon={bounds={top: 0, left: 0, bottom: 31, right: 31}, bits='bits, mask='mask}</tt>
 *
 * @author mwaisberg
 */
public class AttaxxIconTest {

    private static final byte[] BITS_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 31, 0, 31, -1, -1, -1, -2, -128, 32, 8, 2, -114, 32, 8, -30, -111, 32, 9, 50, -84, -96, 10,
            -6, -88, -96, 10, -6, -96, -96, 11, -6, -111, 32, 9, -14, -114, 32, 8, -30, -128, 32, 8, 2, -1, -1, -1, -2, -128, 32, 8, 2, -128, 39, -56, 2, -128, 42, -88, 2, -128,
            45, 104, 2, -128, 42, -88, 2, -128, 45, 104, 2, -128, 42, -88, 2, -128, 39, -56, 2, -128, 32, 8, 2, -1, -1, -1, -2, -128, 32, 8, 2, -114, 32, 8, -30, -109, 32, 9, 18,
            -81, -96, 10, -54, -81, -96, 10, -118, -65, -96, 10, 10, -97, 32, 9, 18, -114, 32, 8, -30, -128, 32, 8, 2, -1, -1, -1, -2};
    private static final byte[] MASK_VALUE = {0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 31, 0, 31, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2,
            -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1,
            -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2,
            -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2};

    public AttaxxIconTest() {
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

        NSOFSmallRect bounds = new NSOFSmallRect(0, 0, 31, 31);
        icon.setBounds(bounds);

        InputStream bitsIn = new ByteArrayInputStream(BITS_VALUE);
        NSOFRawBitmap bits = new NSOFRawBitmap();
        bits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
        bits.inflate(bitsIn, null);
        icon.setBits(bits);
        byte[] pixelsBits = bits.getPixels();

        InputStream maskIn = new ByteArrayInputStream(MASK_VALUE);
        NSOFRawBitmap mask = new NSOFRawBitmap();
        mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);
        mask.inflate(maskIn, null);
        icon.setMask(mask);

        Image img = icon.toImage();
        Icon ii = new ImageIcon(img);
        JOptionPane.showMessageDialog(null, "Attaxx", null, JOptionPane.PLAIN_MESSAGE, ii);

        NSOFIcon bmp2 = new NSOFIcon();
        bmp2.setBounds(bounds);
        bmp2.fromIcon(ii);

        byte[] pixels2 = bmp2.getBits().getPixels();
        for (int i = 0; i < pixelsBits.length; i++)
            if (pixels2[i] != pixelsBits[i])
                throw new ArrayIndexOutOfBoundsException("At index " + i + ", expected " + pixelsBits[i] + " but was " + pixels2[i]);

        Image img2 = bmp2.toImage();
        Icon ii2 = new ImageIcon(img2);
        JOptionPane.showMessageDialog(null, "Attaxx #2", null, JOptionPane.PLAIN_MESSAGE, ii2);
    }
}
