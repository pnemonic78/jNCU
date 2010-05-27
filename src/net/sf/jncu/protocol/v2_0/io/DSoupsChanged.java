/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

	/**
	 * Constructs a new command.
	 */
	public DSoupsChanged() {
		super(COMMAND);
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();

		NSOFObject[] items = new NSOFObject[soups.size()];
		int i = 0;
		for (Soup soup : soups) {
			items[i++] = soup.frame;
		}
		NSOFArray arr = new NSOFArray(items);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(arr, data);
		return null;
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

		private final NSOFFrame frame = new NSOFFrame();

		private static final NSOFSymbol ENTRY_SOUP = new NSOFSymbol("soupName");
		private static final NSOFSymbol ENTRY_COUNT = new NSOFSymbol("count");

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
		public NSOFString getSoupName() {
			return (NSOFString) frame.get(ENTRY_SOUP);
		}

		/**
		 * Set the soup name.
		 * 
		 * @param soupName
		 *            the soup name.
		 */
		public void setSoupName(NSOFString soupName) {
			frame.put(ENTRY_SOUP, soupName);
		}

		/**
		 * Set the soup name.
		 * 
		 * @param soupName
		 *            the soup name.
		 */
		public void setSoupName(String soupName) {
			setSoupName(new NSOFString(soupName));
		}

		/**
		 * Get the count.
		 * 
		 * @return the count.
		 */
		public NSOFInteger getCount() {
			return (NSOFInteger) frame.get(ENTRY_COUNT);
		}

		/**
		 * Set the count.
		 * 
		 * @param count
		 *            the count.
		 */
		public void setCount(NSOFInteger count) {
			frame.put(ENTRY_COUNT, count);
		}

		/**
		 * Set the count.
		 * 
		 * @param count
		 *            the count.
		 */
		public void setCount(int count) {
			setCount(new NSOFInteger(count));
		}

		@Override
		public int hashCode() {
			return frame.hashCode();
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
	}
}
