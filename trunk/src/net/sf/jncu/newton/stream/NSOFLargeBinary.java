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

	/**
	 * Constructs a new large binary object.
	 */
	public NSOFLargeBinary() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#decode(java.io.InputStream)
	 */
	@Override
	public void decode(InputStream in, NSOFDecoder decoder) throws IOException {
		// TODO Auto-generated method stub
		// Class (object)
		// compressed? (non-zero means compressed) (byte)
		// Number of bytes of data (long)
		// Number of characters in compander name (long)
		// Number of byte of compander parameters (long)
		// Reserved (encode zero, ignore when decoding) (long0
		// Compander name (bytes)
		// Compander parameters (bytes)
		// Data (bytes)
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		// TODO Auto-generated method stub
		// Class (object)
		// compressed? (non-zero means compressed) (byte)
		// Number of bytes of data (long)
		// Number of characters in compander name (long)
		// Number of byte of compander parameters (long)
		// Reserved (encode zero, ignore when decoding) (long0
		// Compander name (bytes)
		// Compander parameters (bytes)
		// Data (bytes)
	}

}
