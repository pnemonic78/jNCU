package net.sf.jncu.newton.stream;

/**
 * Precedent.
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
 * @author moshew
 */
public interface Precedent {

	/**
	 * Get the id.
	 * 
	 * @return the id
	 */
	public int getId();

	/**
	 * Set the id.
	 * 
	 * @param id
	 *            the id.
	 */
	public void setId(int id);
}
