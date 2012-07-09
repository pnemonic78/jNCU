package net.sf.jncu.fdil.contrib;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFSmallRect;

/**
 * Decode the Attaxx bitmap.
 * <tt>icon={bounds={top: 0, left: 0, bottom: 31, right: 31}, bits='bits, mask='mask}</tt>
 * 
 * @author mwaisberg
 * 
 */
public class AttaxxBitmapDecode {

	private static final byte[] BITS_ATTAXX = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 31, 0, 31, -1, -1, -1, -2, -128, 32, 8, 2, -114, 32, 8, -30, -111, 32, 9, 50, -84, -96, 10,
			-6, -88, -96, 10, -6, -96, -96, 11, -6, -111, 32, 9, -14, -114, 32, 8, -30, -128, 32, 8, 2, -1, -1, -1, -2, -128, 32, 8, 2, -128, 39, -56, 2, -128, 42, -88, 2, -128,
			45, 104, 2, -128, 42, -88, 2, -128, 45, 104, 2, -128, 42, -88, 2, -128, 39, -56, 2, -128, 32, 8, 2, -1, -1, -1, -2, -128, 32, 8, 2, -114, 32, 8, -30, -109, 32, 9, 18,
			-81, -96, 10, -54, -81, -96, 10, -118, -65, -96, 10, 10, -97, 32, 9, 18, -114, 32, 8, -30, -128, 32, 8, 2, -1, -1, -1, -2 };
	private static final byte[] MASK_ATTAXX = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 31, 0, 31, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2,
			-1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1,
			-1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2,
			-1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2 };

	public AttaxxBitmapDecode() {
	}

	public static void main(String[] args) {
		try {
			NSOFIcon bmp = new NSOFIcon();

			NSOFSmallRect bounds = new NSOFSmallRect(0, 0, 31, 31);
			bmp.setBounds(bounds);

			InputStream bitsIn = new ByteArrayInputStream(BITS_ATTAXX);
			NSOFDecoder bitsDecoder = new NSOFDecoder();
			NSOFRawBitmap bits = new NSOFRawBitmap();
			bits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
			bits.inflate(bitsIn, bitsDecoder);
			bmp.setBits(bits);
			byte[] pixelsBits = bits.getPixels();

			InputStream maskIn = new ByteArrayInputStream(MASK_ATTAXX);
			NSOFDecoder maskDecoder = new NSOFDecoder();
			NSOFRawBitmap mask = new NSOFRawBitmap();
			mask.setObjectClass(NSOFRawBitmap.CLASS_MASK);
			mask.inflate(maskIn, maskDecoder);
			bmp.setMask(mask);

			Image img = bmp.toImage();
			Icon icon = new ImageIcon(img);
			// JOptionPane.showMessageDialog(null, "Attaxx", null,
			// JOptionPane.PLAIN_MESSAGE, icon);

			NSOFIcon bmp2 = new NSOFIcon();
			bmp2.setBounds(bounds);
			bmp2.fromIcon(icon);
			byte[] pixels2 = bmp2.getBits().getPixels();
			Image img2 = bmp2.toImage();

			for (int i = 0; i < pixelsBits.length; i++)
				if (pixels2[i] != pixelsBits[i])
					throw new ArrayIndexOutOfBoundsException(i);

			Icon icon2 = new ImageIcon(img2);
			JOptionPane.showMessageDialog(null, "Attaxx icon#2", null, JOptionPane.PLAIN_MESSAGE, icon2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
