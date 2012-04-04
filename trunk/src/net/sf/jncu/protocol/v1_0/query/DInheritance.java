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
package net.sf.jncu.protocol.v1_0.query;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * Inheritance. This is a response to a <tt>kDGetInheritance</tt> request.
 * 
 * <pre>
 * 'dinh'
 * length
 * array of class, superclass pairs
 * </pre>
 */
public class DInheritance extends DockCommandFromNewton {

	/** <tt>kDInheritance</tt> */
	public static final String COMMAND = "dinh";

	private final Map<NSOFSymbol, NSOFSymbol> classes = new TreeMap<NSOFSymbol, NSOFSymbol>();

	/**
	 * Creates a new command.
	 */
	public DInheritance() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		setInheritances(null);
		NSOFSymbol clazz;
		NSOFSymbol superclass;
		char c;
		StringBuffer buf;
		int count = ntohl(data);

		for (int i = 0; i < count; i++) {
			buf = new StringBuffer();
			c = (char) readByte(data);
			while (c != 0) {
				buf.append(c);
				c = (char) readByte(data);
			}
			clazz = new NSOFSymbol(buf.toString());

			buf = new StringBuffer();
			c = (char) readByte(data);
			while (c != 0) {
				buf.append(c);
				c = (char) readByte(data);
			}
			superclass = new NSOFSymbol(buf.toString());

			setInheritance(clazz, superclass);
		}
	}

	/**
	 * Get the inheritances.
	 * 
	 * @return the inheritances.
	 */
	public Map<NSOFSymbol, NSOFSymbol> getInheritances() {
		return classes;
	}

	/**
	 * Set the inheritances.
	 * 
	 * @param inheritances
	 *            the inheritances.
	 */
	protected void setInheritances(Map<NSOFSymbol, NSOFSymbol> inheritances) {
		classes.clear();
		if (inheritances != null)
			classes.putAll(inheritances);
		NSOFSymbol.setInheritances(inheritances);
	}

	/**
	 * Set an inheritance.
	 * 
	 * @param clazz
	 *            the class.
	 * @param superclass
	 *            the superclass.
	 */
	protected void setInheritance(NSOFSymbol clazz, NSOFSymbol superclass) {
		classes.put(clazz, superclass);
		NSOFSymbol.setInheritance(clazz, superclass);
	}

	/**
	 * Get the inheritance.
	 * 
	 * @param clazz
	 *            the class.
	 * @return the superclass - {@code null} otherwise.
	 */
	public NSOFSymbol getInheritance(NSOFSymbol clazz) {
		return classes.get(clazz);
	}
}
