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
import java.io.InvalidObjectException;
import java.util.Hashtable;
import java.util.Map;

import net.sf.jncu.newton.stream.contrib.NSOFInstructions;
import net.sf.jncu.newton.stream.contrib.NSOFLiterals;
import net.sf.jncu.newton.stream.contrib.NSOFRawBitmap;
import net.sf.jncu.newton.stream.contrib.NSOFReal;

/**
 * Newton Streamed Object Format decoder.
 * 
 * @author Moshe
 */
public class NSOFDecoder {

	private final Map<NSOFPrecedent, Precedent> precedents = new Hashtable<NSOFPrecedent, Precedent>();

	/** {@code 0} is a legal ID. */
	private int idMax = 0;
	/** Written version header? */
	private boolean versioned;

	/**
	 * Constructs a new decoder.
	 */
	public NSOFDecoder() {
		this(false);
	}

	/**
	 * Creates a new decoder.
	 * 
	 * @param versioned
	 *            append version?
	 */
	public NSOFDecoder(boolean versioned) {
		super();
		this.versioned = versioned;
	}

	/**
	 * Decode the NewtonScript object, recursively.
	 * 
	 * @param in
	 *            the input.
	 * @return the decoded object.
	 * @throws IOException
	 *             if a decoding error occurs.
	 */
	public NSOFObject decode(InputStream in) throws IOException {
		if (!versioned) {
			int version = in.read();
			if (version != NewtonStreamedObjectFormat.VERSION) {
				throw new IllegalArgumentException("unknown protocol version: " + version);
			}
			versioned = true;
		}
		int dataType = in.read();
		if (dataType == -1) {
			throw new EOFException();
		}
		NSOFObject object = null;

		switch (dataType) {
		case NewtonStreamedObjectFormat.ARRAY:
			object = new NSOFArray();
			break;
		case NewtonStreamedObjectFormat.BINARY_OBJECT:
			object = new NSOFBinaryObject();
			break;
		case NewtonStreamedObjectFormat.CHARACTER:
			object = new NSOFCharacter();
		case NewtonStreamedObjectFormat.FRAME:
			object = new NSOFFrame();
			break;
		case NewtonStreamedObjectFormat.IMMEDIATE:
			object = new NSOFImmediate();
			break;
		case NewtonStreamedObjectFormat.LARGE_BINARY:
			object = new NSOFLargeBinary();
			break;
		case NewtonStreamedObjectFormat.NIL:
			object = new NSOFNil();
			break;
		case NewtonStreamedObjectFormat.PLAIN_ARRAY:
			object = new NSOFPlainArray();
			break;
		case NewtonStreamedObjectFormat.PRECEDENT:
			object = new NSOFPrecedent();
			break;
		case NewtonStreamedObjectFormat.SMALL_RECT:
			object = new NSOFSmallRect();
			break;
		case NewtonStreamedObjectFormat.STRING:
			object = new NSOFString();
			break;
		case NewtonStreamedObjectFormat.SYMBOL:
			object = new NSOFSymbol();
			break;
		case NewtonStreamedObjectFormat.UNICODE_CHARACTER:
			object = new NSOFUnicodeCharacter();
			break;
		}
		if (object == null) {
			throw new InvalidObjectException("unknown data type " + dataType);
		}
		if (object instanceof Precedent) {
			Precedent p = (Precedent) object;
			NSOFPrecedent id = new NSOFPrecedent(this.idMax);
			precedents.put(id, p);
			this.idMax++;
		}
		object.decode(in, this);
		object = postDecode(object, dataType);

		return object;
	}

	/**
	 * Post decode the object, possibly returning a more specific object.
	 * 
	 * @param object
	 *            the decoded object.
	 * @param dataType
	 *            the data type.
	 * @return the object.
	 */
	protected NSOFObject postDecode(NSOFObject object, int dataType) {
		NSOFSymbol nsClass;

		switch (dataType) {
		case NewtonStreamedObjectFormat.PRECEDENT:
			NSOFPrecedent id = (NSOFPrecedent) object;
			Precedent p = precedents.get(id);
			object = (NSOFObject) p;
			break;
		case NewtonStreamedObjectFormat.BINARY_OBJECT:
			NSOFBinaryObject bin = (NSOFBinaryObject) object;
			nsClass = object.getNSClass();
			if (NSOFInstructions.NS_CLASS.equals(nsClass)) {
				NSOFInstructions bin2 = new NSOFInstructions();
				bin2.setValue(bin.getValue());
				object = bin2;
			} else if (NSOFRawBitmap.NS_CLASS.equals(nsClass)) {
				NSOFRawBitmap bin2 = new NSOFRawBitmap();
				bin2.setValue(bin.getValue());
				object = bin2;
			} else if (NSOFReal.NS_CLASS.equals(nsClass)) {
				NSOFReal bin2 = new NSOFReal();
				bin2.setValue(bin.getValue());
				object = bin2;
			}
			break;
		case NewtonStreamedObjectFormat.ARRAY:
			NSOFArray arr = (NSOFArray) object;
			nsClass = object.getNSClass();
			if (NSOFLiterals.NS_CLASS.equals(nsClass)) {
				NSOFLiterals arr2 = new NSOFLiterals();
				arr2.setValue(arr.getValue());
				object = arr2;
			}
			break;
		}

		return object;
	}

}
