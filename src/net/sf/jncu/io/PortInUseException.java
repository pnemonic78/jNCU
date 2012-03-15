/**
 * 
 */
package net.sf.jncu.io;

import jssc.SerialPortException;

/**
 * The port requested is currently in use.
 * 
 * @author moshe
 */
public class PortInUseException extends SerialPortException {

	/**
	 * Create a new serial port exception.
	 * 
	 * @param name
	 *            the port name.
	 */
	public PortInUseException(String name) {
		super(name, null, TYPE_PORT_BUSY);
	}
}
