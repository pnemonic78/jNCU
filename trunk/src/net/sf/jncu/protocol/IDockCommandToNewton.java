/**
 * 
 */
package net.sf.jncu.protocol;

/**
 * Docking command interface from Newton to desktop.
 * 
 * @author moshew
 */
public interface IDockCommandToNewton extends IDockCommand {

	/**
	 * Get the payload to send.
	 * 
	 * @return the payload.
	 */
	public byte[] getPayload();
}
