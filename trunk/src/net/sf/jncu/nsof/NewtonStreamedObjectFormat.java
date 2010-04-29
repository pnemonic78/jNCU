package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format.
 * <p>
 * An encoder processes an object and emits a streamed object.<br>
 * The first byte of a coded object is a version byte that refers to the NSOF
 * version. The version number of the format described here is 2. (Future
 * versions may not be backward compatible.)<br>
 * The rest of the coded object is a recursive description of the DAG of
 * objects, beginning with the root object.<br>
 * The beginning of each object’s description is a tag byte that specifies the
 * encoding type used for the object.<br>
 * The tag byte is followed an ID, called a <em>precedent ID</em>. The IDs
 * are assigned consecutively, starting with 0 for the root object, and are used
 * by the kPrecedent tag to generate backward pointer references to objects that
 * have already been introduced. Note that no object may be traversed more than
 * once; any pointers to previously traversed objects must be represented with
 * kPrecedent. Immediate objects cannot be precedents; all precedents are heap
 * objects (binary objects, arrays, and frames).
 * 
 * @author Moshe
 */
public abstract class NewtonStreamedObjectFormat {

	/** TODO comment me! */
	public static final int kImmediate = 0;

	/** TODO comment me! */
	public static final int kCharacter = 1;

	/** TODO comment me! */
	public static final int kUnicodeCharacter = 2;

	/** TODO comment me! */
	public static final int kBinaryObject = 3;

	/** TODO comment me! */
	public static final int kArray = 4;

	/** TODO comment me! */
	public static final int kPlainArray = 5;

	/** TODO comment me! */
	public static final int kFrame = 6;

	/** TODO comment me! */
	public static final int kSymbol = 7;

	/** TODO comment me! */
	public static final int kString = 8;

	/** TODO comment me! */
	public static final int kPrecedent = 9;

	/** TODO comment me! */
	public static final int kNIL = 10;

	/** TODO comment me! */
	public static final int kSmallRect = 11;

	/** TODO comment me! */
	public static final int kLargeBinary = 12;

	private int id;

	/**
	 * Constructs a new streamed object format.
	 */
	public NewtonStreamedObjectFormat() {
		super();
	}

	/**
	 * Encode the object.
	 * 
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public abstract void encode(OutputStream out) throws IOException;

	/**
	 * Decode the object.
	 * 
	 * @param in
	 *            the input.
	 * @param decoder
	 *            the decoder.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public abstract void decode(InputStream in, NSOFDecoder decoder)
			throws IOException;

	/**
	 * Get the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id.
	 * 
	 * @param id
	 *            the id.
	 */
	public void setId(int id) {
		this.id = id;
	}

}
