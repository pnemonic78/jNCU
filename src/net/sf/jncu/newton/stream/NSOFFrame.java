package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Newton Streamed Object Format - Frame.
 * 
 * @author Moshe
 */
public class NSOFFrame extends NSOFObject {

	protected final Map<NSOFSymbol, NSOFObject> slots = new TreeMap<NSOFSymbol, NSOFObject>();

	/**
	 * Constructs a new object.
	 */
	public NSOFFrame() {
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
		// Number of slots (xlong)
		// Slot tags in ascending order (symbol objects)
		// Slot values in ascending order (objects)
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		// TODO Auto-generated method stub

	}

}
