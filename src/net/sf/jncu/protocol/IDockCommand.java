/**
 * 
 */
package net.sf.jncu.protocol;

/**
 * Docking Command interface.
 * 
 * @author moshew
 */
public interface IDockCommand {

	/**
	 * Get the command.
	 * 
	 * @return the command.
	 */
	public String getCommand();

	/**
	 * Get the length.
	 * 
	 * @return the length. Default value is <tt>0</tt>.
	 */
	public int getLength();
}
