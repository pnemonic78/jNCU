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
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;

import net.sf.jncu.newton.stream.NSOFArray;
import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.newton.stream.NSOFInteger;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.newton.stream.NSOFSymbol;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSoupsChanged</tt><br>
 * This command returns information about what was imported into the Newton.
 * Each array element specifies a soup and how many entries were added to it.
 * There will typically be only one frame in the array. The frame will look like
 * this:<br>
 * <code>[{soupName: "Notes", count: 7}, {soupName: "Names", count: 3}]</code>
 * 
 * <pre>
 * 'schg'
 * length
 * array
 * </pre>
 * 
 * @author moshew
 */
public class DSoupsChanged extends DockCommandToNewton {

	public static final String COMMAND = "schg";

	private final Set<Soup> soups = new TreeSet<Soup>();

	private static final NSOFSymbol ENTRY_SOUP = new NSOFSymbol("soupName");
	private static final NSOFSymbol ENTRY_COUNT = new NSOFSymbol("count");

	/**
	 * Constructs a new command.
	 */
	public DSoupsChanged() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFObject[] items = new NSOFObject[soups.size()];
		int i = 0;
		for (Soup soup : soups) {
			items[i++] = soup.toFrame();
		}
		NSOFArray arr = new NSOFArray(items);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(arr, data);
	}

	/**
	 * Add a changed soup.
	 * 
	 * @param soup
	 *            the soup.
	 */
	public void addSoup(Soup soup) {
		soups.add(soup);
	}

	public static class Soup implements Comparable<Soup> {

		private String soupName;
		private int count;

		/**
		 * Creates a new soup frame.
		 */
		public Soup() {
			super();
		}

		/**
		 * Get the soup name.
		 * 
		 * @return the soup name.
		 */
		public String getSoupName() {
			return soupName;
		}

		/**
		 * Set the soup name.
		 * 
		 * @param soupName
		 *            the soup name.
		 */
		public void setSoupName(String soupName) {
			this.soupName = soupName;
		}

		/**
		 * Get the count.
		 * 
		 * @return the count.
		 */
		public int getCount() {
			return count;
		}

		/**
		 * Set the count.
		 * 
		 * @param count
		 *            the count.
		 */
		public void setCount(int count) {
			this.count = count;
		}

		@Override
		public int hashCode() {
			return getSoupName().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			Soup that = (Soup) obj;
			return compareTo(that) == 0;
		}

		public int compareTo(Soup that) {
			if (this == that) {
				return 0;
			}
			if (that == null) {
				return 1;
			}
			return this.getSoupName().compareTo(that.getSoupName());
		}

		/**
		 * Get the frame.
		 * 
		 * @return the frame.
		 */
		public NSOFFrame toFrame() {
			NSOFFrame frame;
			frame = new NSOFFrame();
			frame.put(ENTRY_SOUP, new NSOFString(getSoupName()));
			frame.put(ENTRY_COUNT, new NSOFInteger(getCount()));
			return frame;
		}
	}
}
