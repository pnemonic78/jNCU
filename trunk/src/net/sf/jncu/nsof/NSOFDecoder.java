package net.sf.jncu.newton.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Moshe
 * 
 */
public class NSOFDecoder {

	private final Map<Integer, NewtonStreamedObjectFormat> precedents = new TreeMap<Integer, NewtonStreamedObjectFormat>();

	private int id = 0;

	/**
	 * Constructs a new decoder.
	 * 
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
		case NewtonStreamedObjectFormat.kArray:
			nsof = new NSOFArray();
			break;
		case NewtonStreamedObjectFormat.kBinaryObject:
			nsof = new NSOFBinaryObject();
			break;
		case NewtonStreamedObjectFormat.kCharacter:
			nsof = new NSOFCharacter();
		case NewtonStreamedObjectFormat.kFrame:
			nsof = new NSOFFrame();
			break;
		case NewtonStreamedObjectFormat.kImmediate:
			nsof = new NSOFImmediate();
			break;
		case NewtonStreamedObjectFormat.kLargeBinary:
			nsof = new NSOFLargeBinary();
			break;
		case NewtonStreamedObjectFormat.kNIL:
			nsof = new NSOFNil();
			break;
		case NewtonStreamedObjectFormat.kPlainArray:
			nsof = new NSOFPlainArray();
			break;
		case NewtonStreamedObjectFormat.kPrecedent:
			nsof = new NSOFPrecedent();
			break;
		case NewtonStreamedObjectFormat.kSmallRect:
			nsof = new NSOFSmallRect();
			break;
		case NewtonStreamedObjectFormat.kString:
			nsof = new NSOFString();
			break;
		case NewtonStreamedObjectFormat.kSymbol:
			nsof = new NSOFSymbol();
			break;
		case NewtonStreamedObjectFormat.kUnicodeCharacter:
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
