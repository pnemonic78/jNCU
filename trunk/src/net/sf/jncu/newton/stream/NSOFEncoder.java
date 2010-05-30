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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

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
			NewtonStreamedObjectFormat.ntohl(0, out);
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
}
