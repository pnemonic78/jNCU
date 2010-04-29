package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Immediate.
 * 
 * @author Moshe
 */
public class NSOFImmediate extends NSOFObject {

	private int value;

	/**
	 * Constructs a new immediate.
	 */
	public NSOFImmediate() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setValue(0);
		// Immediate Ref (xlong)
		XLong xlong = new XLong();
		xlong.decode(in, decoder);
		setValue(xlong.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return value;
	}
}
