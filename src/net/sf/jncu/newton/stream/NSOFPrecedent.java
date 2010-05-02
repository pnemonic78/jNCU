package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Precedent ID.
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
 * @author Moshe
 */
public class NSOFPrecedent extends NSOFObject {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("precedent");

	private int value;

	/**
	 * Constructs a new precedent.
	 */
	public NSOFPrecedent() {
		super();
		setNSClass(NS_CLASS);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// Precedent ID (xlong)
		setValue(XLong.decodeValue(in));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(PRECEDENT);
		// Precedent ID (xlong)
		XLong.encode(getValue(), out);
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

}
