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
package net.sf.jncu.fdil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format.
 * <p>
 * An encoder processes an object and emits a streamed object.<br>
 * The first byte of a coded object is a version byte that refers to the NSOF
 * version. The version number of the format described here is 2. (Future
 * versions may not be backward compatible.)<br>
 * The rest of the coded object is a recursive description of the DAG of
 * objects, beginning with the root object.<br>
 * The beginning of each object's description is a tag byte that specifies the
 * encoding type used for the object.<br>
 * The tag byte is followed an ID, called a <em>precedent ID</em>. The IDs are
 * assigned consecutively, starting with {@code 0} for the root object, and are
 * used by the <tt>kPrecedent</tt> tag to generate backward pointer references
 * to objects that have already been introduced. Note that no object may be
 * traversed more than once; any pointers to previously traversed objects must
 * be represented with <tt>kPrecedent</tt>. Immediate objects cannot be
 * precedents; all precedents are heap objects (binary objects, arrays, and
 * frames).
 * 
 * @author Moshe
 */
public abstract class NewtonStreamedObjectFormat {

	/** <tt>kImmediate</tt> */
	public static final int NSOF_IMMEDIATE = 0;
	/** <tt>kCharacter</tt> */
	public static final int NSOF_CHARACTER = 1;
	/** <tt>kUnicodeCharacter</tt> */
	public static final int NSOF_UNICODE_CHARACTER = 2;
	/** <tt>kBinaryObject</tt> */
	public static final int NSOF_BINARY = 3;
	/** <tt>kArray</tt> */
	public static final int NSOF_ARRAY = 4;
	/** <tt>kPlainArray</tt> */
	public static final int NSOF_PLAIN_ARRAY = 5;
	/** <tt>kFrame</tt> */
	public static final int NSOF_FRAME = 6;
	/** <tt>kSymbol</tt> */
	public static final int NSOF_SYMBOL = 7;
	/** <tt>kString</tt> */
	public static final int NSOF_STRING = 8;
	/** <tt>kPrecedent</tt> */
	public static final int NSOF_PRECEDENT = 9;
	/** <tt>kNIL</tt> */
	public static final int NSOF_NIL = 10;
	/** <tt>kSmallRect</tt> */
	public static final int NSOF_SMALL_RECT = 11;
	/** <tt>kLargeBinary</tt> */
	public static final int NSOF_LARGE_BINARY = 12;

	/** NSOF version. */
	public static final int VERSION = 2;

	/**
	 * Constructs a new streamed object format.
	 */
	public NewtonStreamedObjectFormat() {
		super();
	}

	/**
	 * Encode the object.<br>
	 * Converts this object into a flat stream of bytes in Newton Stream Object
	 * Format (NSOF) suitable for saving to disk or for transmission to a Newton
	 * device.
	 * 
	 * @param out
	 *            the output.
	 * @param encoder
	 *            the encoder.
	 * @throws IOException
	 *             if an encoding error occurs.
	 */
	public abstract void flatten(OutputStream out, NSOFEncoder encoder) throws IOException;

	/**
	 * Decode the object.<br>
	 * Converts a flat stream of bytes in Newton Stream Object Format (NSOF)
	 * into this object.
	 * 
	 * @param in
	 *            the input.
	 * @param decoder
	 *            the decoder.
	 * @throws IOException
	 *             if a decoding error occurs.
	 */
	public abstract void inflate(InputStream in, NSOFDecoder decoder) throws IOException;

	/**
	 * Network to host - long.<br>
	 * Read 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param in
	 *            the input.
	 * @return the number.
	 * @throws IOException
	 *             if read past buffer.
	 */
	protected static int ntohl(InputStream in) throws IOException {
		int n24 = (in.read() & 0xFF) << 24;
		int n16 = (in.read() & 0xFF) << 16;
		int n08 = (in.read() & 0xFF) << 8;
		int n00 = (in.read() & 0xFF) << 0;

		return n24 | n16 | n08 | n00;
	}

	/**
	 * Host to network - long.<br>
	 * Write 4 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param in
	 *            the number.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected static void htonl(int n, OutputStream out) throws IOException {
		out.write((n >> 24) & 0xFF);
		out.write((n >> 16) & 0xFF);
		out.write((n >> 8) & 0xFF);
		out.write((n >> 0) & 0xFF);
	}

	/**
	 * Host to network - big long.<br>
	 * Write 8 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param in
	 *            the number.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected static void htonl(long n, OutputStream out) throws IOException {
		htonl((int) ((n >> 32) & 0xFFFFFFFFL), out);
		htonl((int) ((n >> 0) & 0xFFFFFFFFL), out);
	}

	/**
	 * Network to host - word.<br>
	 * Read 2 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param in
	 *            the input.
	 * @return the number.
	 * @throws IOException
	 *             if read past buffer.
	 */
	protected static int ntohs(InputStream in) throws IOException {
		int n08 = (in.read() & 0xFF) << 8;
		int n00 = (in.read() & 0xFF) << 0;

		return n08 | n00;
	}

	/**
	 * Host to network - word.<br>
	 * Write 2 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param in
	 *            the number.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected static void htons(short n, OutputStream out) throws IOException {
		out.write((n >> 8) & 0xFF);
		out.write((n >> 0) & 0xFF);
	}

	/**
	 * Host to network - word.<br>
	 * Write 2 bytes as an unsigned integer in network byte order (Big Endian).
	 * 
	 * @param in
	 *            the number.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected static void htons(int n, OutputStream out) throws IOException {
		out.write((n >> 8) & 0xFF);
		out.write((n >> 0) & 0xFF);
	}

	/**
	 * Read into the whole array.
	 * 
	 * @param in
	 *            the input.
	 * @param b
	 *            the destination buffer.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void readAll(InputStream in, byte[] b) throws IOException {
		int count = 0;
		while (count < b.length)
			count += in.read(b, count, b.length - count);
	}
}
