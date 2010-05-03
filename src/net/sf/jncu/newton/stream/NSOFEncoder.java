package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Newton Streamed Object Format encoder.
 * 
 * @author moshew
 */
public class NSOFEncoder {

	private final Map<Integer, Precedent> precedents = new TreeMap<Integer, Precedent>();

	private int id = 0;

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
