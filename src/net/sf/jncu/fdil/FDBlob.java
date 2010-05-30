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

import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

/**
 * <b>B</b>inary <b>L</b>arge <b>OB</b>ject.
 * <p>
 * A large binary object mimics the functionality of a virtual binary object
 * (VBO). It contains a large amount of unformatted binary data, that is paged
 * in from a backing store, and optionally compressed.
 * 
 * @author moshew
 */
public class FDBlob extends FDPointer {

	private final Blob value;

	/**
	 * Creates a new BLOB.
	 * 
	 * @param value
	 *            the value.
	 */
	public FDBlob(byte[] value) {
		super();
		try {
			this.value = new SerialBlob(value);
		} catch (SerialException se) {
			throw new IllegalArgumentException(se);
		} catch (SQLException sqle) {
			throw new IllegalArgumentException(sqle);
		}
	}

	/**
	 * Creates a new BLOB.
	 * 
	 * @param value
	 *            the value.
	 */
	public FDBlob(Blob value) {
		super();
		this.value = value;
	}

	/**
	 * Get the BLOB.
	 * 
	 * @return the value.
	 */
	public Blob getBlob() {
		return value;
	}

}
