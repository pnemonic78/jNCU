package net.sf.jncu.protocol.v1_0;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * Unknown command from Newton with raw data.
 * 
 * @author Moshe
 */
public class DRawCommand extends DockCommandFromNewton {

	private byte[] data;

	public DRawCommand(String cmd) {
		super(cmd);
	}

	public DRawCommand(byte[] cmd) {
		super(cmd);
	}

	/**
	 * Get the data.
	 * 
	 * @return the data.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Set the data.
	 * 
	 * @param data
	 *            the data.
	 */
	protected void setData(byte[] data) {
		this.data = data;
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		final int length = getLength();
		byte[] raw = new byte[length];
		data.read(raw);
		setData(raw);
	}

	@Override
	public String toString() {
		return "Raw command: " + getCommand();
	}
}
