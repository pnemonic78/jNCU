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
 * Decode the Attaxx bitmap.
 * <tt>icon={bounds={top: 0, left: 0, bottom: 31, right: 31}, bits='bits, mask='mask}</tt>
 * 
 * @author mwaisberg
 * 
 */
public class NBasisIconTest {

	private static final byte[] BITS_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 24, 0, 27, 0, 0, 0, 0, 127, -1, -1, -64, 64, 0, 0, 64, 80, -31, 16, 64, 81, 17, 16, 64, 81,
			17, 16, 64, 81, 17, 16, 64, 81, 17, 16, 64, 80, -31, 16, 64, 106, -128, 0, 64, 85, 64, 0, 64, 106, -64, 0, 64, 85, 64, 0, 64, 106, -60, 0, 64, 85, 70, 30, 64, 106,
			-57, 17, 64, 85, 71, -111, 64, 106, -1, -33, 64, 85, 87, -111, 64, 106, -81, 17, 64, 85, 86, 30, 64, 106, -84, 0, 64, 127, -1, -1, -64, 0, 0, 0, 0 };
	private static final byte[] MASK_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 24, 0, 27, 0, 0, 0, 0, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1,
			-64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64,
			127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 127, -1, -1, -64, 0, 0,
			0, 0 };

	private static final byte[] UNHILITED_CBITS_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 24, 0, 27, 0, 0, 0, 0, 127, -1, -1, -64, 64, 0, 0, 64, 80, -31, 16, 64, 81, 17,
			16, 64, 81, 17, 16, 64, 81, 17, 16, 64, 81, 17, 16, 64, 80, -31, 16, 64, 106, -128, 0, 64, 85, 64, 0, 64, 106, -64, 0, 64, 85, 64, 0, 64, 106, -60, 0, 64, 85, 70, 30,
			64, 106, -57, 17, 64, 85, 71, -111, 64, 106, -1, -33, 64, 85, 87, -111, 64, 106, -81, 17, 64, 85, 86, 30, 64, 106, -84, 0, 64, 127, -1, -1, -64, 0, 0, 0, 0 };
	private static final byte[] UNHILITED_BITS_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 24, 0, 27, 0, 0, 0, 0, 127, -1, -1, -64, 64, 0, 0, 64, 80, -31, 16, 64, 81, 17, 16,
			64, 81, 17, 16, 64, 81, 17, 16, 64, 81, 17, 16, 64, 80, -31, 16, 64, 106, -128, 0, 64, 85, 64, 0, 64, 106, -64, 0, 64, 85, 64, 0, 64, 106, -60, 0, 64, 85, 70, 30, 64,
			106, -57, 17, 64, 85, 71, -111, 64, 106, -1, -33, 64, 85, 87, -111, 64, 106, -81, 17, 64, 85, 86, 30, 64, 106, -84, 0, 64, 127, -1, -1, -64, 0, 0, 0, 0 };
	private static final byte[] HILITED_BITS_VALUE = { 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 24, 0, 27, 0, 0, 0, 0, 0, 0, 0, 0, 63, -1, -1, -128, 47, 30, -17, -128, 46, -18, -17,
			-128, 46, -18, -17, -128, 46, -18, -17, -128, 46, -18, -17, -128, 47, 30, -17, -128, 21, 127, -1, -128, 42, -65, -1, -128, 21, 63, -1, -128, 42, -65, -1, -128, 21, 59,
			-1, -128, 42, -71, -31, -128, 21, 56, -18, -128, 42, -72, 110, -128, 21, 0, 32, -128, 42, -88, 110, -128, 21, 80, -18, -128, 42, -87, -31, -128, 21, 83, -1, -128, 0,
			0, 0, 0, 0, 0, 0, 0 };

	public NBasisIconTest() {
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

		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, 24, 27);
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
		JOptionPane.showMessageDialog(null, "nBasis", null, JOptionPane.PLAIN_MESSAGE, ii);
	}

	private static void testIconPro() throws Exception {
		testIconProUnhilited();
		testIconProHilited();
	}

	private static void testIconProUnhilited() throws Exception {
		NSOFIconPro iconPro = new NSOFIconPro();

		NSOFIcon icon = new NSOFIcon();

		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, 24, 27);
		icon.setBounds(bounds);

		InputStream bitsIn = new ByteArrayInputStream(UNHILITED_BITS_VALUE);
		NSOFRawBitmap bits = new NSOFRawBitmap();
		bits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
		bits.inflate(bitsIn, null);
		icon.setBits(bits);

		InputStream cbitsIn = new ByteArrayInputStream(UNHILITED_CBITS_VALUE);
		NSOFRawBitmap cbits = new NSOFRawBitmap();
		cbits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
		cbits.inflate(cbitsIn, null);
		NSOFFrame colordata = new NSOFFrame();
		colordata.put(NSOFIcon.SLOT_BIT_DEPTH, new NSOFInteger(1));
		colordata.put(NSOFIcon.SLOT_COLOR_BITS, cbits);
		icon.setColorData(colordata);

		iconPro.setUnhilited(icon);

		Image img = icon.toImage();
		Icon ii = new ImageIcon(img);
		JOptionPane.showMessageDialog(null, "nBasis Pro Unhilited", null, JOptionPane.PLAIN_MESSAGE, ii);
	}

	private static void testIconProHilited() throws Exception {
		NSOFIconPro iconPro = new NSOFIconPro();

		NSOFIcon icon = new NSOFIcon();

		NSOFSmallRect bounds = new NSOFSmallRect(0, 0, 24, 27);
		icon.setBounds(bounds);

		InputStream bitsIn = new ByteArrayInputStream(HILITED_BITS_VALUE);
		NSOFRawBitmap bits = new NSOFRawBitmap();
		bits.setObjectClass(NSOFRawBitmap.CLASS_BITS);
		bits.inflate(bitsIn, null);
		icon.setBits(bits);

		iconPro.setHilited(icon);

		Image img = icon.toImage();
		Icon ii = new ImageIcon(img);
		JOptionPane.showMessageDialog(null, "nBasis Pro Hilited", null, JOptionPane.PLAIN_MESSAGE, ii);
	}
}
