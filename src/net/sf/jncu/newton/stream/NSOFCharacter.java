package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Character.
 * 
 * @author Moshe
 */
public class NSOFCharacter extends NSOFObject {

	protected static final String HEX = "0123456789ABDEF";

	private char value;

	/**
	 * Constructs a new character.
	 */
	public NSOFCharacter() {
		super();
		setNSClass("character");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		setValue((char) 0);
		// Character code (byte)
		int c = in.read();
		if (c == -1) {
			throw new EOFException();
		}
		setValue((char) (c & 0xFF));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(CHARACTER);
		// Character code (byte)
		out.write(getValue() & 0xFF);
	}

	/**
	 * Get the value.
	 * 
	 * @return the value
	 */
	public char getValue() {
		return value;
	}

	/**
	 * Set the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(char value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		int value = getValue();
		char hex0 = HEX.charAt(value & 0x000F);
		value >>= 4;
		char hex1 = HEX.charAt(value & 0x000F);
		return "$\\" + hex1 + hex0;
	}
}
