package net.sf.jncu.newton.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.junit.SFTestCase;

/**
 * <pre>
 * x := {name: "Walter Smith",
 *       cats: 2,
 *       bounds: {left: 10, top: 14, right: 40, bottom: 100},
 *       right: $\u2022,
 *       phones: ["408-996-1010", nil]};
 * x.phones[1] := SetClass("408-974-9094", 'faxPhone);
 * x.nameAgain := x.name;
 * </pre>
 * 
 * <pre>
 * { name: "Walter Smith",
 *       cats: 2,
 *       bounds: {left: 10, top: 14, right: 40, bottom: 100},
 *       uchar: $\u2022,
 *       phones: ["408-996-1010", <faxPhone, length 26>],
 *       nameAgain: "Walter Smith"}
 * </pre>
 * 
 * <br>
 * The streamed representation of this frame is:<br>
 * <tt>02060607046E616D650704636174730706626F756E6473070575636861720706<br>
 * 70686F6E657307096E616D65416761696E081A00570061006C00740065007200<br>
 * 200053006D006900740068000000080B0E0A64280220220502081A0034003000<br>
 * 38002D003900390036002D00310030003100300000031A070866617850686F6E<br>
 * 65003400300038002D003900370034002D003900300039003400000907</tt>
 * <p>
 * This streamed representation translates as:
 * 
 * <pre>
 * 02—version number
 * 06—kFrame [ID 0]
 * 06—length, 6 slots
 * Slot tags:
 *    07 (kSymbol) 04 (length of name) 6E616D65 ("name") [ID 1]
 *    07 04 63617473 ("cats") [ID 2]
 *    07 06 626F756E6473 ("bounds") [ID 3]
 *    07 05 7563686172 ("uchar") [ID 4]
 *    07 06 70686F6E6573 ("phones") [ID 5]
 *    07 09 6E616D65416761696E ("nameAgain") [ID 6]
 * Slot values:
 *    08—kString [ID 7]
 *    1A—length, 26 bytes
 *    00570061006C00740065007200200053006D0069007400680000 ("Walter Smith")
 *    00—kImmediate
 *    08—Ref of the integer 2
 *    0B—kSmallRect [ID 8]
 *       0E (top=14) 0A (left=10) 64 (bottom=100) 28 (right=40)
 *       02—kUnicodeCharacter
 *       2022—The character code
 *       05—kPlainArray [ID 9]
 *       02—length, 2 slots
 *    Slot values:
 *       08—kString [ID 10]
 *       1A—length, 26 bytes
 *       003400300038002D003900390036002D00310030003100300000 ("408-996-1010")
 *       03—kBinaryObject [ID 11]
 *       1A—length, 26 bytes
 *       Class:
 *          07(kSymbol) 08 (length of name) 66617850686F6E65 ("faxPhone")[ID 12]
 *       003400300038002D003900370034002D00390030003900340000 ("408-974-9094")
 *    09—kPrecedent
 *    07—ID 7 ("Walter Smith" object above)
 * </pre>
 * 
 * @author moshew
 */
public class NSOFExample extends SFTestCase {

	private static final byte[] nsof = { 0x02, 0x06, 0x06, 0x07, 0x04, 0x6E, 0x61, 0x6D, 0x65, 0x07, 0x04, 0x63, 0x61, 0x74, 0x73, 0x07, 0x06, 0x62, 0x6F,
			0x75, 0x6E, 0x64, 0x73, 0x07, 0x05, 0x75, 0x63, 0x68, 0x61, 0x72, 0x07, 0x06, 0x70, 0x68, 0x6F, 0x6E, 0x65, 0x73, 0x07, 0x09, 0x6E, 0x61, 0x6D,
			0x65, 0x41, 0x67, 0x61, 0x69, 0x6E, 0x08, 0x1A, 0x00, 0x57, 0x00, 0x61, 0x00, 0x6C, 0x00, 0x74, 0x00, 0x65, 0x00, 0x72, 0x00, 0x20, 0x00, 0x53,
			0x00, 0x6D, 0x00, 0x69, 0x00, 0x74, 0x00, 0x68, 0x00, 0x00, 0x00, 0x08, 0x0B, 0x0E, 0x0A, 0x64, 0x28, 0x02, 0x20, 0x22, 0x05, 0x02, 0x08, 0x1A,
			0x00, 0x34, 0x00, 0x30, 0x00, 0x38, 0x00, 0x2D, 0x00, 0x39, 0x00, 0x39, 0x00, 0x36, 0x00, 0x2D, 0x00, 0x31, 0x00, 0x30, 0x00, 0x31, 0x00, 0x30,
			0x00, 0x00, 0x03, 0x1A, 0x07, 0x08, 0x66, 0x61, 0x78, 0x50, 0x68, 0x6F, 0x6E, 0x65, 0x00, 0x34, 0x00, 0x30, 0x00, 0x38, 0x00, 0x2D, 0x00, 0x39,
			0x00, 0x37, 0x00, 0x34, 0x00, 0x2D, 0x00, 0x39, 0x00, 0x30, 0x00, 0x39, 0x00, 0x34, 0x00, 0x00, 0x09, 0x07 };

	/**
	 * Creates a new test case.
	 */
	public NSOFExample() {
		super();
	}

	/**
	 * Test encoding.
	 * 
	 * @throws IOException
	 */
	public void testEncode() throws IOException {
		NSOFObject[] phones = new NSOFObject[] { new NSOFString("408-996-1010"), null };
		NSOFBinaryObject faxPhone = new NSOFBinaryObject();
		faxPhone.setClass("faxPhone");
		byte[] utf16 = "408-974-9094".getBytes("UTF-16");
		System.arraycopy(utf16, 2, utf16, 0, utf16.length - 2);
		utf16[utf16.length - 2] = 0;
		utf16[utf16.length - 1] = 0;
		faxPhone.setValue(utf16);
		phones[1] = faxPhone;

		NSOFFrame x = new NSOFFrame();
		assertNotNull(x);
		x.put("name", new NSOFString("Walter Smith"));
		x.put("cats", new NSOFInteger(2));
		x.put("bounds", new NSOFSmallRect(14, 10, 100, 40));
		x.put("right", new NSOFUnicodeCharacter('\u2022'));
		x.put("phones", new NSOFPlainArray(phones));
		x.put("nameAgain", x.get("name"));

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(x, out);
		out.close();

		byte[] buf = out.toByteArray();
		assertNotNull(buf);
		assertEquals(nsof, buf);
	}

	/**
	 * Test decoding.
	 */
	public void testDecode() {
		// TODO implement me!
	}
}
