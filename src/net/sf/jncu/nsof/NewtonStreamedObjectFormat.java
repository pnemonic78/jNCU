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
 * The tag byte is followed an ID, called a <em>precedent ID</em>. The IDs are
 * assigned consecutively, starting with 0 for the root object, and are used by
 * the kPrecedent tag to generate backward pointer references to objects that
 * have already been introduced. Note that no object may be traversed more than
 * once; any pointers to previously traversed objects must be represented with
 * kPrecedent. Immediate objects cannot be precedents; all precedents are heap
 * objects (binary objects, arrays, and frames).
 * 
 * @author Moshe
 */
public abstract class NewtonStreamedObjectFormat {

	/** <tt>kImmediate</tt> */
	public static final int IMMEDIATE = 0;
	/** <tt>kCharacter</tt> */
	public static final int CHARACTER = 1;
	/** <tt>kUnicodeCharacter</tt> */
	public static final int UNICODE_CHARACTER = 2;
	/** <tt>kBinaryObject</tt> */
	public static final int BINARY_OBJECT = 3;
	/** <tt>kArray</tt> */
	public static final int ARRAY = 4;
	/** <tt>kPlainArray</tt> */
	public static final int PLAIN_ARRAY = 5;
	/** <tt>kFrame</tt> */
	public static final int FRAME = 6;
	/** <tt>kSymbol</tt> */
	public static final int SYMBOL = 7;
	/** <tt>kString</tt> */
	public static final int STRING = 8;
	/** <tt>kPrecedent</tt> */
	public static final int PRECEDENT = 9;
	/** <tt>kNIL</tt> */
	public static final int NIL = 10;
	/** <tt>kSmallRect</tt> */
	public static final int SMALL_RECT = 11;
	/** <tt>kLargeBinary</tt> */
	public static final int LARGE_BINARY = 12;

	/** NSOF version. */
	public static final int VERSION = 2;

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
	public abstract void decode(InputStream in, NSOFDecoder decoder) throws IOException;

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
