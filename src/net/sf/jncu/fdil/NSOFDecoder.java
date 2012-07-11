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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.util.Hashtable;
import java.util.Map;

import net.sf.jncu.fdil.contrib.NSOFInstructions;
import net.sf.jncu.fdil.contrib.NSOFLiterals;
import net.sf.jncu.fdil.contrib.NSOFRawBitmap;

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
	 * Decode the NewtonScript object, recursively.<br>
	 * Converts a flat stream of bytes in Newton Stream Object Format (NSOF)
	 * into an FDIL object.
	 * 
	 * @param in
	 *            the input.
	 * @return the decoded object.
	 * @throws IOException
	 *             if a decoding error occurs.
	 * @throws UnknownStreamVersionException
	 *             if the stream version is unknown.
	 */
	public NSOFObject inflate(InputStream in) throws IOException, UnknownStreamVersionException {
		if (!versioned) {
			int version = in.read();
			if (version != NewtonStreamedObjectFormat.VERSION) {
				throw new UnknownStreamVersionException("unknown protocol version: " + version);
			}
			versioned = true;
		}
		int dataType = in.read();
		if (dataType == -1) {
			throw new EOFException();
		}
		NSOFObject object = null;
		NSOFObject object2 = null;

		switch (dataType) {
		case NewtonStreamedObjectFormat.NSOF_ARRAY:
			object = new NSOFArray();
			break;
		case NewtonStreamedObjectFormat.NSOF_BINARY:
			object = new NSOFBinaryObject();
			break;
		case NewtonStreamedObjectFormat.NSOF_CHARACTER:
			object = new NSOFAsciiCharacter();
		case NewtonStreamedObjectFormat.NSOF_FRAME:
			object = new NSOFFrame();
			break;
		case NewtonStreamedObjectFormat.NSOF_IMMEDIATE:
			object = inflateImmediate(in);
			break;
		case NewtonStreamedObjectFormat.NSOF_LARGE_BINARY:
			object = new NSOFLargeBinary();
			break;
		case NewtonStreamedObjectFormat.NSOF_NIL:
			object = NSOFNil.NIL;
			break;
		case NewtonStreamedObjectFormat.NSOF_PLAIN_ARRAY:
			object = new NSOFPlainArray();
			break;
		case NewtonStreamedObjectFormat.NSOF_PRECEDENT:
			object = new NSOFPrecedent();
			break;
		case NewtonStreamedObjectFormat.NSOF_SMALL_RECT:
			object = new NSOFSmallRect();
			break;
		case NewtonStreamedObjectFormat.NSOF_STRING:
			object = new NSOFString();
			break;
		case NewtonStreamedObjectFormat.NSOF_SYMBOL:
			object = new NSOFSymbol();
			break;
		case NewtonStreamedObjectFormat.NSOF_UNICODE_CHARACTER:
			object = new NSOFUnicodeCharacter();
			break;
		}
		if (object == null) {
			throw new InvalidObjectException("unknown data type " + dataType);
		}
		NSOFPrecedent id = null;
		if (object instanceof Precedent) {
			Precedent p = (Precedent) object;
			id = new NSOFPrecedent(this.idMax);
			precedents.put(id, p);
			this.idMax++;
		}
		object.inflate(in, this);
		object2 = postInflate(object, dataType);
		object = object2;

		// Replace the old precedent.
		if ((id != null) && (object2 instanceof Precedent)) {
			Precedent p = (Precedent) object2;
			precedents.put(id, p);
		}

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
	 * @throws IOException
	 *             if a decoding error occurs.
	 */
	protected NSOFObject postInflate(NSOFObject object, int dataType) throws IOException {
		NSOFSymbol nsClass;

		switch (dataType) {
		case NewtonStreamedObjectFormat.NSOF_PRECEDENT:
			NSOFPrecedent id = (NSOFPrecedent) object;
			Precedent p = precedents.get(id);
			object = (NSOFObject) p;
			break;
		case NewtonStreamedObjectFormat.NSOF_BINARY:
			NSOFBinaryObject bin = (NSOFBinaryObject) object;
			nsClass = object.getObjectClass();

			if (NSOFInstructions.CLASS_INSTRUCTIONS.equals(nsClass)) {
				object = new NSOFInstructions(bin);
			} else if (NSOFRawBitmap.CLASS_BITS.equals(nsClass)) {
				object = new NSOFRawBitmap(bin);
			} else if (NSOFRawBitmap.CLASS_COLOR_BITS.equals(nsClass)) {
				object = new NSOFRawBitmap(bin);
			} else if (NSOFRawBitmap.CLASS_MASK.equals(nsClass)) {
				object = new NSOFRawBitmap(bin);
			} else if (NSOFReal.CLASS_REAL.equals(nsClass)) {
				object = new NSOFReal(bin);
			}
			break;
		case NewtonStreamedObjectFormat.NSOF_ARRAY:
			NSOFArray arr = (NSOFArray) object;
			nsClass = object.getObjectClass();

			if (NSOFLiterals.CLASS_LITERALS.equals(nsClass)) {
				object = new NSOFLiterals(arr);
			}
			break;
		}

		return object;
	}

	/**
	 * Decode the Immediate object.
	 * 
	 * @param in
	 *            the input.
	 * @return the immediate value.
	 * @throws IOException
	 *             if a decoding error occurs.
	 */
	public NSOFImmediate inflateImmediate(InputStream in) throws IOException {
		// Immediate Ref (xlong)
		int ref = XLong.decodeValue(in);
		int val = ref;

		NSOFImmediate imm = null;
		if (NSOFImmediate.isRefCharacter(ref)) {
			val = (ref >> 4) & 0xFFFF;
			imm = new NSOFCharacter((char) val);
		} else if (NSOFImmediate.isRefInteger(ref)) {
			val = ref >> 2;
			imm = new NSOFInteger(val);
		} else if (NSOFImmediate.isRefMagicPointer(ref)) {
			val = ref >> 2;
			imm = new NSOFMagicPointer(val);
		} else if (NSOFImmediate.isRefNil(ref)) {
			imm = new NSOFNil();
		} else if (NSOFImmediate.isRefTrue(ref)) {
			imm = new NSOFTrue();
		} else {
			val = ref >> 2;
			imm = new NSOFImmediate(val, NSOFImmediate.IMMEDIATE_INTEGER);
		}

		return imm;
	}
}
