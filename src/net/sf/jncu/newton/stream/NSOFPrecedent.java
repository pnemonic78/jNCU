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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Precedent ID.
 * <p>
 * Data types that are assigned precedent IDs:
 * <ul>
 * <li>{@link NSOFArray array}
 * <li>{@link NSOFBinaryObject binary}
 * <li>{@link NSOFFrame frame}
 * <li>{@link NSOFLargeBinary largeBinary}
 * <li>{@link NSOFPlainArray plainArray}
 * <li>{@link NSOFSmallRect smallRect}
 * <li>{@link NSOFString string}
 * <li>{@link NSOFSymbol symbol}
 * </ul>
 * 
 * @author Moshe
 */
public class NSOFPrecedent extends NSOFObject implements Comparable<NSOFPrecedent> {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("precedent");

	private int id;

	/**
	 * Constructs a new precedent.
	 */
	public NSOFPrecedent() {
		super();
		setNSClass(NS_CLASS);
	}

	/**
	 * Constructs a new precedent.
	 * 
	 * @param id
	 *            the id.
	 */
	public NSOFPrecedent(int id) {
		this();
		setId(id);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Precedent ID (xlong)
		setId(XLong.decodeValue(in));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out, NSOFEncoder encoder) throws IOException {
		out.write(PRECEDENT);
		// Precedent ID (xlong)
		XLong.encode(getId(), out);
	}

	/**
	 * Get the id.
	 * 
	 * @return the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id.
	 * 
	 * @param id
	 *            the id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NSOFPrecedent) {
			return this.getId() == ((NSOFPrecedent) obj).getId();
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return String.valueOf(getId());
	}

	public int compareTo(NSOFPrecedent that) {
		if (that == null) {
			return +1;
		}
		return this.getId() - that.getId();
	}
}
