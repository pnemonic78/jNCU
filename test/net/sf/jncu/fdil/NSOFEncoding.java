package net.sf.jncu.fdil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import net.sf.junit.SFTestCase;

import org.junit.Test;

public class NSOFEncoding extends SFTestCase {

	private static final char gASCIIToUnicode[] = { 0x00C4, 0x00C5, 0x00C7, 0x00C9, 0x00D1, 0x00D6, 0x00DC, 0x00E1, 0x00E0, 0x00E2, 0x00E4, 0x00E3, 0x00E5, 0x00E7, 0x00E9, 0x00E8,
			0x00EA, 0x00EB, 0x00ED, 0x00EC, 0x00EE, 0x00EF, 0x00F1, 0x00F3, 0x00F2, 0x00F4, 0x00F6, 0x00F5, 0x00FA, 0x00F9, 0x00FB, 0x00FC, 0x2020, 0x00B0, 0x00A2, 0x00A3, 0x00A7,
			0x2022, 0x00B6, 0x00DF, 0x00AE, 0x00A9, 0x2122, 0x00B4, 0x00A8, 0x2260, 0x00C6, 0x00D8, 0x221E, 0x00B1, 0x2264, 0x2265, 0x00A5, 0x00B5, 0x2202, 0x2211, 0x220F, 0x03C0,
			0x222B, 0x00AA, 0x00BA, 0x03A9/* 0x2126 */, 0x00E6, 0x00F8, 0x00BF, 0x00A1, 0x00AC, 0x221A, 0x0192, 0x2248, 0x2206, 0x00AB, 0x00BB, 0x2026, 0x00A0, 0x00C0, 0x00C3,
			0x00D5, 0x0152, 0x0153, 0x2013, 0x2014, 0x201C, 0x201D, 0x2018, 0x2019, 0x00F7, 0x25CA, 0x00FF, 0x0178, 0x2044, 0x20AC/* 0x00A4 */, 0x2039, 0x203A, 0xFB01, 0xFB02,
			0x2021, 0x00B7, 0x201A, 0x201E, 0x2030, 0x00C2, 0x00CA, 0x00C1, 0x00CB, 0x00C8, 0x00CD, 0x00CE, 0x00CF, 0x00CC, 0x00D3, 0x00D4, 0xF8FF/* 0xF7FF */, 0x00D2, 0x00DA,
			0x00DB, 0x00D9, 0x0131, 0x02C6, 0x02DC, 0x00AF, 0x02D8, 0x02D9, 0x02DA, 0x00B8, 0x02DD, 0x02DB, 0x02C7 };

	/**
	 * Creates a new test case.
	 */
	public NSOFEncoding() {
		super();
	}

	/**
	 * Test character encoding.
	 */
	@Test
	public void testEncoding() {
		Charset cs = Charset.forName(NSOFString.CHARSET_MAC);
		assertNotNull(cs);

		CharBuffer mac = CharBuffer.allocate(256);
		mac.mark();
		for (char c = 1; c < 128; c++) {
			mac.append(c);
		}
		for (int i = 0; i < gASCIIToUnicode.length; i++) {
			mac.append(gASCIIToUnicode[i]);
		}
		assertEquals(255, mac.position());
		mac.reset();
		assertEquals(0, mac.position());

		ByteBuffer bb = ByteBuffer.allocate(256);
		bb.mark();
		for (int c = 1; c <= 0xFF; c++) {
			bb.put((byte) c);
		}
		assertEquals(255, bb.position());
		bb.reset();

		CharBuffer cb = bb.asCharBuffer();
		assertEquals(0, cb.position());

		// Convert pure-ASCII to MacRoman.
		CharBuffer mac2 = cs.decode(bb);
		assertNotNull(mac2);
		assertEquals(256, mac2.limit());

		assertEquals(mac, mac2);
	}

	private static final byte[] REPEAT_NOTES = { 2, 3, 26, 7, 13, 's', 't', 'r', 'i', 'n', 'g', '.', 'n', 'o', 'h', 'i', 'n', 't', 0, 82, 0, 101, 0, 112, 0, 101, 0, 97, 0, 116, 0,
			32, 0, 78, 0, 111, 0, 116, 0, 101, 0, 115, 0, 0 };

	/**
	 * Test string encoding.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStringRepeatNotes() throws Exception {
		NSOFSymbol nohint = new NSOFSymbol("string.nohint");
		String val = "Repeat Notes";

		InputStream in = new ByteArrayInputStream(REPEAT_NOTES);
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFObject o = decoder.inflate(in);
		assertNotNull(o);
		assertTrue(o instanceof NSOFString);
		NSOFString str = (NSOFString) o;
		NSOFSymbol sym = str.getObjectClass();
		assertEquals(nohint, sym);
		assertEquals(val, str.getValue());

		ByteArrayOutputStream out = new ByteArrayOutputStream(REPEAT_NOTES.length);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(str, out);
		assertEquals(REPEAT_NOTES, out.toByteArray());
	}

	// extracted from DSoupInfo command.
	private static final byte[] SOUP_INFO_NAMES_EXTERNAL = { 2, 6, 2, 7, 7, 115, 111, 117, 112, 68, 101, 102, 7, 17, 78, 67, 75, 76, 97, 115, 116, 66, 97, 99, 107, 117, 112, 84,
			105, 109, 101, 6, 6, 7, 8, 111, 119, 110, 101, 114, 65, 112, 112, 7, 9, 117, 115, 101, 114, 68, 101, 115, 99, 114, 7, 7, 105, 110, 100, 101, 120, 101, 115, 7, 4, 110,
			97, 109, 101, 7, 8, 117, 115, 101, 114, 78, 97, 109, 101, 7, 12, 111, 119, 110, 101, 114, 65, 112, 112, 78, 97, 109, 101, 7, 8, 99, 97, 114, 100, 102, 105, 108, 101,
			8, 60, 0, 83, 0, 111, 0, 117, 0, 112, 0, 32, 0, 119, 0, 105, 0, 116, 0, 104, 0, 32, 0, 78, 0, 97, 0, 109, 0, 101, 0, 115, 0, 32, 0, 97, 0, 110, 0, 100, 0, 32, 0, 65,
			0, 100, 0, 100, 0, 114, 0, 101, 0, 115, 0, 115, 0, 101, 0, 115, 0, 0, 5, 2, 6, 3, 7, 4, 116, 121, 112, 101, 7, 4, 112, 97, 116, 104, 7, 9, 115, 116, 114, 117, 99, 116,
			117, 114, 101, 7, 6, 115, 116, 114, 105, 110, 103, 7, 6, 115, 111, 114, 116, 111, 110, 7, 4, 115, 108, 111, 116, 6, 4, 9, 14, 9, 15, 7, 4, 116, 97, 103, 115, 9, 16, 9,
			21, 7, 6, 108, 97, 98, 101, 108, 115, 10, 9, 19, 8, 12, 0, 78, 0, 97, 0, 109, 0, 101, 0, 115, 0, 0, 9, 23, 9, 23, 0, -1, 13, -99, -118, 60 };

	/**
	 * Test soup encoding of Name on External card.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSoupInfoNamesExternal() throws Exception {
		assertEquals(265, SOUP_INFO_NAMES_EXTERNAL.length);

		InputStream in = new ByteArrayInputStream(SOUP_INFO_NAMES_EXTERNAL);
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFObject o = decoder.inflate(in);
		assertNotNull(o);
		assertTrue(o instanceof NSOFFrame);
		NSOFFrame f = (NSOFFrame) o;

		ByteArrayOutputStream out = new ByteArrayOutputStream(SOUP_INFO_NAMES_EXTERNAL.length);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(f, out);
		assertEquals(SOUP_INFO_NAMES_EXTERNAL, out.toByteArray());
	}

	// extracted from DSoupInfo command.
	private static final byte[] SOUP_INFO_NAMES_INTERNAL = { /*
															 * TODO POPULATE ME
															 * from
															 * "trace/backup_names.txt"
															 */};

	/**
	 * Test soup encoding of Name on Internal card.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSoupInfoNamesInternal() throws Exception {
		assertEquals(530, SOUP_INFO_NAMES_INTERNAL.length);

		InputStream in = new ByteArrayInputStream(SOUP_INFO_NAMES_INTERNAL);
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFObject o = decoder.inflate(in);
		assertNotNull(o);
		assertTrue(o instanceof NSOFFrame);
		NSOFFrame f = (NSOFFrame) o;

		ByteArrayOutputStream out = new ByteArrayOutputStream(SOUP_INFO_NAMES_INTERNAL.length);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(f, out);
		byte[] b = out.toByteArray();
		assertEquals(530, b.length);
		assertEquals(SOUP_INFO_NAMES_INTERNAL, b);
	}

	/**
	 * Test soup encoding of Name on Internal card.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSoupInfoNamesInternalPartial() throws Exception {
		assertEquals(530, SOUP_INFO_NAMES_INTERNAL.length);

		InputStream in = new ByteArrayInputStream(SOUP_INFO_NAMES_INTERNAL);
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFObject o = decoder.inflate(in);
		assertNotNull(o);
		assertTrue(o instanceof NSOFFrame);
		NSOFFrame f = (NSOFFrame) o;

		NSOFFrame f1 = new NSOFFrame();
		f1.put("soupDef", f.get("soupDef"));
		assertEquals(1, f1.size());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(f1, out);
		in = new ByteArrayInputStream(out.toByteArray());
		decoder = new NSOFDecoder();
		o = decoder.inflate(in);
		assertNotNull(o);
		assertTrue(o instanceof NSOFFrame);
		assertEquals(f1, o);

		NSOFFrame f2 = new NSOFFrame();
		f2.put("soupDef", f.get("soupDef"));
		f2.put("NCKLastBackupTime", f.get("NCKLastBackupTime"));
		assertEquals(2, f2.size());
		out = new ByteArrayOutputStream();
		encoder = new NSOFEncoder();
		encoder.flatten(f2, out);
		in = new ByteArrayInputStream(out.toByteArray());
		decoder = new NSOFDecoder();
		o = decoder.inflate(in);
		assertNotNull(o);
		assertTrue(o instanceof NSOFFrame);
		assertEquals(f2, o);

		NSOFFrame f3 = new NSOFFrame();
		f3.put("NCKLastBackupTime", f.get("NCKLastBackupTime"));
		f3.put("customFields", f.get("customFields"));
		assertEquals(2, f3.size());
		out = new ByteArrayOutputStream();
		encoder = new NSOFEncoder();
		encoder.flatten(f3, out);
		in = new ByteArrayInputStream(out.toByteArray());
		decoder = new NSOFDecoder();
		o = decoder.inflate(in);
		assertNotNull(o);
		assertTrue(o instanceof NSOFFrame);
		assertEquals(f3, o);
	}

	// {soupDef={ownerApp='cardfile, userDescr="Soup with Names and Addresses",
	// indexes=[2 Elements], name="Names", userName="Names",
	// ownerAppName="Names"}, NCKLastBackupTime=57107087, name="Names",
	// signature=159604293}
	private static final byte[] SOUP_NAMES_EXTERNAL = { 2, 6, 4, 7, 7, 115, 111, 117, 112, 68, 101, 102, 7, 17, 78, 67, 75, 76, 97, 115, 116, 66, 97, 99, 107, 117, 112, 84, 105,
			109, 101, 7, 4, 110, 97, 109, 101, 7, 9, 115, 105, 103, 110, 97, 116, 117, 114, 101, 6, 6, 7, 8, 111, 119, 110, 101, 114, 65, 112, 112, 7, 9, 117, 115, 101, 114, 68,
			101, 115, 99, 114, 7, 7, 105, 110, 100, 101, 120, 101, 115, 9, 3, 7, 8, 117, 115, 101, 114, 78, 97, 109, 101, 7, 12, 111, 119, 110, 101, 114, 65, 112, 112, 78, 97,
			109, 101, 7, 8, 99, 97, 114, 100, 102, 105, 108, 101, 8, 60, 0, 83, 0, 111, 0, 117, 0, 112, 0, 32, 0, 119, 0, 105, 0, 116, 0, 104, 0, 32, 0, 78, 0, 97, 0, 109, 0, 101,
			0, 115, 0, 32, 0, 97, 0, 110, 0, 100, 0, 32, 0, 65, 0, 100, 0, 100, 0, 114, 0, 101, 0, 115, 0, 115, 0, 101, 0, 115, 0, 0, 5, 2, 6, 3, 7, 4, 116, 121, 112, 101, 7, 4,
			112, 97, 116, 104, 7, 9, 115, 116, 114, 117, 99, 116, 117, 114, 101, 7, 6, 115, 116, 114, 105, 110, 103, 7, 6, 115, 111, 114, 116, 111, 110, 7, 4, 115, 108, 111, 116,
			6, 4, 9, 15, 9, 16, 7, 4, 116, 97, 103, 115, 9, 17, 9, 22, 7, 6, 108, 97, 98, 101, 108, 115, 10, 9, 20, 8, 12, 0, 78, 0, 97, 0, 109, 0, 101, 0, 115, 0, 0, 9, 24, 9,
			24, 0, -1, 13, -99, -118, 60, 9, 24, 0, -1, 38, 13, 121, 20 };

	/**
	 * Test soup encoding of Name on External card.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSoupNamesExternal() throws Exception {
		assertEquals(286, SOUP_NAMES_EXTERNAL.length);

		InputStream in = new ByteArrayInputStream(SOUP_NAMES_EXTERNAL);
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFObject o = decoder.inflate(in);
		assertNotNull(o);
		assertTrue(o instanceof NSOFFrame);
		NSOFFrame f = (NSOFFrame) o;
		NSOFObject n = f.get("name");
		assertNotNull(n);
		assertFalse(NSOFImmediate.isNil(n));
		assertTrue(n instanceof NSOFString);
		NSOFString name = (NSOFString) n;
		assertEquals("Names", name.getValue());
		NSOFObject s = f.get("signature");
		assertNotNull(s);
		assertFalse(NSOFImmediate.isNil(s));
		assertTrue(s instanceof NSOFInteger);
		NSOFInteger sig = (NSOFInteger) s;
		assertEquals(159604293, sig.getValue());

		ByteArrayOutputStream out = new ByteArrayOutputStream(SOUP_NAMES_EXTERNAL.length);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(f, out);
		assertEquals(SOUP_NAMES_EXTERNAL, out.toByteArray());
	}

	// {soupDef={ownerApp='cardfile, userDescr="Soup with Names and Addresses",
	// indexes=[2 Elements], name="Names", userName="Names",
	// ownerAppName="Names"}, NCKLastBackupTime=57107087,
	// customFields={custom3={label="Clubs & Societies"},
	// custom8={label="Blood"}, custom5={label="Occupation"},
	// custom2={label="URL"}, custom7={label=""}, custom4={label="Car Plate"},
	// custom1={label="Employee"}, custom6={label="ID"}}, name="Names",
	// signature=123338611}
	private static final byte[] SOUP_NAMES_INTERNAL = { /*
														 * TODO POPULATE ME from
														 * "trace/backup_names.txt"
														 */};

	/**
	 * Test soup encoding of Name on Internal card.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSoupNamesInternal() throws Exception {
		assertEquals(551, SOUP_NAMES_INTERNAL.length);

		InputStream in = new ByteArrayInputStream(SOUP_NAMES_INTERNAL);
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFObject o = decoder.inflate(in);
		assertNotNull(o);
		assertTrue(o instanceof NSOFFrame);
		NSOFFrame f = (NSOFFrame) o;
		NSOFObject n = f.get("name");
		assertNotNull(n);
		assertFalse(NSOFImmediate.isNil(n));
		assertTrue(n instanceof NSOFString);
		NSOFString name = (NSOFString) n;
		assertEquals("Names", name.getValue());
		NSOFObject s = f.get("signature");
		assertNotNull(s);
		assertFalse(NSOFImmediate.isNil(s));
		assertTrue(s instanceof NSOFInteger);
		NSOFInteger sig = (NSOFInteger) s;
		assertEquals(123338611, sig.getValue());

		ByteArrayOutputStream out = new ByteArrayOutputStream(SOUP_NAMES_INTERNAL.length);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(f, out);
		assertEquals(SOUP_NAMES_INTERNAL, out.toByteArray());
	}

	/**
	 * Test string encoding.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStrings() throws Exception {
		NSOFSymbol label = new NSOFSymbol("label");
		NSOFString val = new NSOFString("Value");
		NSOFString blank = new NSOFString("");
		NSOFString nil = new NSOFString(null);

		NSOFFrame f = new NSOFFrame();
		f.put(label, val);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(f, out);
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFObject o = decoder.inflate(in);
		assertEquals(f, o);

		f = new NSOFFrame();
		f.put(label, blank);
		out = new ByteArrayOutputStream();
		encoder = new NSOFEncoder();
		encoder.flatten(f, out);
		in = new ByteArrayInputStream(out.toByteArray());
		decoder = new NSOFDecoder();
		o = decoder.inflate(in);
		assertEquals(f, o);

		f = new NSOFFrame();
		f.put(label, nil);
		out = new ByteArrayOutputStream();
		encoder = new NSOFEncoder();
		encoder.flatten(f, out);
		in = new ByteArrayInputStream(out.toByteArray());
		decoder = new NSOFDecoder();
		o = decoder.inflate(in);
		assertEquals(f, o);

		out = new ByteArrayOutputStream();
		encoder = new NSOFEncoder();
		encoder.flatten(val, out);
		encoder.flatten(blank, out);
		encoder.flatten(nil, out);
		in = new ByteArrayInputStream(out.toByteArray());
		decoder = new NSOFDecoder();
		o = decoder.inflate(in);
		assertEquals(val, o);
		o = decoder.inflate(in);
		assertEquals(blank, o);
		o = decoder.inflate(in);
		assertEquals(nil, o);

		out = new ByteArrayOutputStream();
		encoder = new NSOFEncoder();
		encoder.flatten(blank, out);
		encoder.flatten(nil, out);
		encoder.flatten(val, out);
		in = new ByteArrayInputStream(out.toByteArray());
		decoder = new NSOFDecoder();
		o = decoder.inflate(in);
		assertEquals(blank, o);
		o = decoder.inflate(in);
		assertEquals(nil, o);
		o = decoder.inflate(in);
		assertEquals(val, o);
	}
}
