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

import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import net.sf.jncu.newton.stream.contrib.NSOFReal;

/**
 * Newton Streamed Object Format encoder.
 * 
 * @author moshew
 */
public class NSOFEncoder {

	private final Map<Precedent, NSOFPrecedent> precedents = new Hashtable<Precedent, NSOFPrecedent>();

	/** <tt>0</tt> is a legal ID. */
	private int idMax = 0;
	/** Written version header? */
	private boolean versioned = false;

	/**
	 * Creates a new encoder.
	 */
	public NSOFEncoder() {
		super();
	}

	/**
	 * Encode the NewtonScript object, recursively.
	 * 
	 * @param object
	 *            the object to encode.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an encoding error occurs.
	 */
	public void encode(NSOFObject object, OutputStream out) throws IOException {
		if (!versioned) {
			out.write(NewtonStreamedObjectFormat.VERSION);
			versioned = true;
		}
		encodeImpl(object, out);
	}

	/**
	 * Encode the NewtonScript object - implementation.
	 * 
	 * @param object
	 *            the object to encode.
	 * @param out
	 *            the output.
	 * @param encodePrecedents
	 *            encode precedent IDs?
	 * @throws IOException
	 *             if an encoding error occurs.
	 */
	protected void encodeImpl(NSOFObject object, OutputStream out) throws IOException {
		if (object == null) {
			NewtonStreamedObjectFormat.htonl(0, out);
		} else {
			if (object instanceof Precedent) {
				Precedent p = (Precedent) object;
				NSOFPrecedent id = precedents.get(p);
				if (id == null) {
					id = new NSOFPrecedent(this.idMax);
					precedents.put(p, id);
					this.idMax++;
				} else {
					object = id;
				}
			}
			object.encode(out, this);
		}
	}

	/**
	 * Convert the Java object to Newton Streamed Object Format.
	 * 
	 * @param o
	 *            the object.
	 * @return the NewtonScript object - <tt>null</tt> otherwise.
	 */
	public static NSOFObject toNS(Object o) {
		if (o == null) {
			return new NSOFNil();
		}
		if (o instanceof Boolean) {
			if (((Boolean) o).booleanValue()) {
				return new NSOFTrue();
			}
			return new NSOFNil();
		}
		if (o instanceof Character) {
			return new NSOFUnicodeCharacter((Character) o);
		}
		if (o instanceof Double) {
			return new NSOFReal((Double) o);
		}
		if (o instanceof Float) {
			return new NSOFReal(((Float) o).doubleValue());
		}
		if (o instanceof Integer) {
			return new NSOFInteger((Integer) o);
		}
		if (o instanceof Number) {
			return new NSOFImmediate(((Number) o).intValue());
		}
		if (o instanceof Rectangle) {
			Rectangle rect = (Rectangle) o;
			return new NSOFSmallRect(rect.y, rect.x, rect.y + rect.height, rect.x + rect.width);
		}
		if (o instanceof String) {
			return new NSOFString((String) o);
		}
		if (o.getClass().isArray()) {
			if (o instanceof byte[]) {
				return new NSOFBinaryObject((byte[]) o);
			}
			if (o instanceof Object[]) {
				Object[] arr = (Object[]) o;
				NSOFObject[] entries = new NSOFObject[arr.length];
				for (int i = 0; i < arr.length; i++) {
					entries[i] = toNS(arr[i]);
				}
				return new NSOFArray(entries);
			}
		}
		if (o instanceof Collection) {
			Collection coll = (Collection) o;
			NSOFObject[] entries = new NSOFObject[coll.size()];
			int i = 0;
			for (Object entry : coll) {
				entries[i++] = toNS(entry);
			}
			return new NSOFArray(entries);
		}
		return null;
	}
}
