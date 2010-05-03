package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format encoder.
 * 
 * @author moshew
 */
public class NSOFEncoder {

	/**
	 * Creates a new encoder.
	 */
	public NSOFEncoder() {
		super();
	}

	/**
	 * Encode the NewtonScript object, recursively.
	 * 
	 * @param object
	 *            the object to encode.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an encoding error occurs.
	 */
	public void encode(NSOFObject object, OutputStream out) throws IOException {
		out.write(NewtonStreamedObjectFormat.VERSION);
		object.encode(out);
	}

}
