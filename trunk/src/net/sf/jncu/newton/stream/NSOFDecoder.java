package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Newton Streamed Object Format decoder.
 * 
 * @author Moshe
 */
public class NSOFDecoder {

	private final Map<Integer, NewtonStreamedObjectFormat> precedents = new TreeMap<Integer, NewtonStreamedObjectFormat>();

	private int id = 0;

	/**
	 * Constructs a new decoder.
	 */
	public NSOFDecoder() {
		super();
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public NSOFObject decode(InputStream in) throws IOException {
		NSOFObject nsof = null;
		int dataType = in.read();
		if (dataType == -1) {
			throw new EOFException();
		}

		switch (dataType) {
		case NewtonStreamedObjectFormat.ARRAY:
			nsof = new NSOFArray();
			break;
		case NewtonStreamedObjectFormat.BINARY_OBJECT:
			nsof = new NSOFBinaryObject();
			break;
		case NewtonStreamedObjectFormat.CHARACTER:
			nsof = new NSOFCharacter();
		case NewtonStreamedObjectFormat.FRAME:
			nsof = new NSOFFrame();
			break;
		case NewtonStreamedObjectFormat.IMMEDIATE:
			nsof = new NSOFImmediate();
			break;
		case NewtonStreamedObjectFormat.LARGE_BINARY:
			nsof = new NSOFLargeBinary();
			break;
		case NewtonStreamedObjectFormat.NIL:
			nsof = new NSOFNil();
			break;
		case NewtonStreamedObjectFormat.PLAIN_ARRAY:
			nsof = new NSOFPlainArray();
			break;
		case NewtonStreamedObjectFormat.PRECEDENT:
			nsof = new NSOFPrecedent();
			break;
		case NewtonStreamedObjectFormat.SMALL_RECT:
			nsof = new NSOFSmallRect();
			break;
		case NewtonStreamedObjectFormat.STRING:
			nsof = new NSOFString();
			break;
		case NewtonStreamedObjectFormat.SYMBOL:
			nsof = new NSOFSymbol();
			break;
		case NewtonStreamedObjectFormat.UNICODE_CHARACTER:
			nsof = new NSOFUnicodeCharacter();
			break;
		}
		if (nsof == null) {
			throw new InvalidObjectException("unknown data type " + dataType);
		}
		nsof.setId(id++);
		nsof.decode(in, this);
		precedents.put(id, nsof);

		return nsof;
	}

	/**
	 * @param id
	 * @return
	 */
	public NewtonStreamedObjectFormat getPrecedent(int id) {
		return precedents.get(id);
	}
}
