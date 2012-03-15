/**
 * 
 */
package net.sf.jncu.io;

import jssc.SerialPortException;

/**
 * The port requested does not exist.
 * 
 * @author moshe
 */
public class NoSuchPortException extends SerialPortException {

	/**
	 * Create a new serial port exception.
	 * 
	 * @param name
	 *            the port name.
	 */
	public NoSuchPortException(String name) {
		super(name, null, TYPE_PORT_NOT_FOUND);
	}
}
