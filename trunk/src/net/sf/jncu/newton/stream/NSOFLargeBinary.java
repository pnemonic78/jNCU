package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Large Binary.
 * 
 * @author Moshe
 */
public class NSOFLargeBinary extends NSOFBinaryObject {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("largeBinary");

	/**
	 * Constructs a new large binary object.
	 */
	public NSOFLargeBinary() {
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
		// Class (object)
		NSOFSymbol symbol = (NSOFSymbol) decoder.decode(in);
		setNSClass(symbol);
		// compressed? (non-zero means compressed) (byte)
		// TODO implement me!
		// Number of bytes of data (long)
		// TODO implement me!
		// Number of characters in compander name (long)
		// TODO implement me!
		// Number of byte of compander parameters (long)
		// TODO implement me!
		// Reserved (encode zero, ignore when decoding) (long)
		// TODO implement me!
		// Compander name (bytes)
		// TODO implement me!
		// Compander parameters (bytes)
		// TODO implement me!
		// Data (bytes)
		// TODO implement me!
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(LARGE_BINARY);
		// Class (object)
		getNSClass().encode(out);
		// compressed? (non-zero means compressed) (byte)
		// TODO implement me!
		// Number of bytes of data (long)
		// TODO implement me!
		// Number of characters in compander name (long)
		// TODO implement me!
		// Number of byte of compander parameters (long)
		// TODO implement me!
		// Reserved (encode zero, ignore when decoding) (long)
		// TODO implement me!
		// Compander name (bytes)
		// TODO implement me!
		// Compander parameters (bytes)
		// TODO implement me!
		// Data (bytes)
		// TODO implement me!
	}

}
