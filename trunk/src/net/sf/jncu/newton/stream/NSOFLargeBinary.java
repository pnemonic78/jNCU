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
package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Large Binary Object.<br>
 * Also known as Virtual Binary Object (VBO).
 * <p>
 * A compandor/compander (compressor-expander) is an object that transparently
 * compresses data as it is stored and expands data as it is read.
 * 
 * @author Moshe
 */
public class NSOFLargeBinary extends NSOFBinaryObject {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("largeBinary");

	/**
	 * Specifies the use of the Lempel-Ziv compressor-expander.
	 */
	public static final String COMPANDER_LZ = "TLZStoreCompander";
	/**
	 * Specifies the use of a compander specialized for pixel map data. (A
	 * bitmap is a pixel map having a bit depth of 1.) This compander assumes
	 * that the data in the VBO is a pixel map and that the pixel map data is
	 * 32-bit aligned; that is, the length of the rows in the pixel map is an
	 * even multiple of 4 bytes.
	 */
	public static final String COMPANDER_PIXELMAP = "TPixelMapCompander";

	/** Data is compressed. */
	protected static final byte FLAG_COMPRESSED = 1;
	/** Data is not compressed. */
	protected static final byte FLAG_UNCOMPRESSED = 0;

	private boolean compressed = false;
	/**
	 * A string value specifying the implementation of the store compander
	 * protocol used when the VBO created by this object is written to or read
	 * from a soup entry. If the value of this parameter is nil, an uncompressed
	 * object is created.
	 * 
	 * @see #COMPANDER_LZ
	 * @see #COMPANDER_PIXELMAP
	 */
	private String companderName;
	/**
	 * Arguments for instantiating the specified compander. In the current
	 * implementation, always pass nil as the value of this parameter.
	 * <p>
	 *"Because both companders provided by the current system initialize
	 * themselves automatically, you must always pass nil as the value of the
	 * companderArgs parameter."
	 */
	private byte[] companderArgs;

	/**
	 * Constructs a new large binary object.
	 */
	public NSOFLargeBinary() {
		super();
		setNSClass(NS_CLASS);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Class (object)
		NSOFSymbol symbol = (NSOFSymbol) decoder.decode(in);
		setNSClass(symbol);
		// compressed? (non-zero means compressed) (byte)
		int compressed = in.read();
		if (compressed == -1) {
			throw new EOFException();
		}
		setCompressed(compressed != FLAG_UNCOMPRESSED);
		// Number of bytes of data (long)
		int numBytesData = ntohl(in);
		// Number of characters in compander name (long)
		int numBytesCompanderName = ntohl(in);
		// Number of bytes of compander parameters (long)
		int numBytesCompanderArgs = ntohl(in);
		// Reserved (encode zero, ignore when decoding) (long)
		ntohl(in);
		// Compander name (bytes)
		if (numBytesCompanderName == 0) {
			setCompanderName(null);
		} else {
			byte[] companderName = new byte[numBytesCompanderName];
			in.read(companderName);
			setCompanderName(new String(companderName));
		}
		// Compander parameters (bytes)
		if (numBytesCompanderArgs == 0) {
			setCompanderArguments(null);
		} else {
			byte[] companderArgs = new byte[numBytesCompanderArgs];
			in.read(companderArgs);
			setCompanderArguments(companderArgs);
		}
		// Data (bytes)
		if (numBytesData == 0) {
			setValue(null);
		} else {
			byte[] data = new byte[numBytesData];
			in.read(data);
			setValue(data);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(LARGE_BINARY);
		String companderName = getCompanderName();
		byte[] companderNameBytes = (companderName == null) ? null : companderName.getBytes();
		byte[] args = getCompanderArguments();
		byte[] data = getValue();
		// Class (object)
		encoder.encode(getNSClass(), out);
		// compressed? (non-zero means compressed) (byte)
		out.write(isCompressed() ? FLAG_UNCOMPRESSED : FLAG_UNCOMPRESSED);
		// Number of bytes of data (long)
		htonl((data == null) ? 0 : data.length, out);
		// Number of characters in compander name (long)
		htonl((companderNameBytes == null) ? 0 : companderNameBytes.length, out);
		// Number of bytes of compander parameters (long)
		htonl((args == null) ? 0 : args.length, out);
		// Reserved (encode zero, ignore when decoding) (long)
		htonl(0, out);
		// Compander name (bytes)
		if (companderNameBytes != null) {
			out.write(companderNameBytes);
		}
		// Compander parameters (bytes)
		if (args != null) {
			out.write(args);
		}
		// Data (bytes)
		if (data != null) {
			out.write(data);
		}
	}

	/**
	 * Is the data compressed?
	 * 
	 * @return compressed?
	 */
	public boolean isCompressed() {
		return compressed;
	}

	/**
	 * Set data compression.
	 * 
	 * @param compressed
	 *            compress?
	 */
	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}

	/**
	 * Get the compander name.
	 * 
	 * @return the compander name.
	 */
	public String getCompanderName() {
		return companderName;
	}

	/**
	 * Set the compander name.
	 * 
	 * @param companderName
	 *            the compander name.
	 */
	public void setCompanderName(String companderName) {
		this.companderName = companderName;
	}

	/**
	 * Get the compander arguments/parameters.
	 * 
	 * @return the compander arguments.
	 */
	public byte[] getCompanderArguments() {
		return companderArgs;
	}

	/**
	 * Set the compander arguments/parameters.
	 * 
	 * @param companderArgs
	 *            the compander arguments.
	 */
	public void setCompanderArguments(byte[] companderArgs) {
		this.companderArgs = companderArgs;
	}

}
