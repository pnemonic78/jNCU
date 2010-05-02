package net.sf.jncu.newton.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Newton Streamed Object Format - Frame.
 * 
 * @author Moshe
 */
public class NSOFFrame extends NSOFObject {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("frame");

	protected final SortedMap<NSOFSymbol, NSOFObject> slots = new TreeMap<NSOFSymbol, NSOFObject>();

	/**
	 * Constructs a new frame.
	 */
	public NSOFFrame() {
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
		this.slots.clear();

		// Number of slots (xlong)
		int length = XLong.decodeValue(in);
		NSOFSymbol[] symbols = new NSOFSymbol[length];

		// Slot tags in ascending order (symbol objects)
		for (int i = 0; i < length; i++) {
			symbols[i] = (NSOFSymbol) decoder.decode(in);
		}

		// Slot values in ascending order (objects)
		NSOFObject slot;
		for (int i = 0; i < length; i++) {
			slot = decoder.decode(in);
			slot.decode(in, decoder);
			put(symbols[i], slot);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.mmw.newton.NewtonStreamedObjectFormat#encode(java.io.OutputStream)
	 */
	@Override
	public void encode(OutputStream out) throws IOException {
		out.write(FRAME);

		// Number of slots (xlong)
		XLong.encode(slots.size(), out);

		// Slot tags in ascending order (symbol objects)
		for (NSOFSymbol sym : slots.keySet()) {
			sym.encode(out);
		}

		// Slot values in ascending order (objects)
		for (NSOFObject slot : slots.values()) {
			slot.encode(out);
		}
	}

	@Override
	public int hashCode() {
		return slots.hashCode();
	}

	/**
	 * Is the frame empty?
	 * 
	 * @return true if this frame contains no slots.
	 */
	public boolean isEmpty() {
		return slots.isEmpty();
	}

	/**
	 * Removes all of the slots from this frame.
	 */
	public void clear() {
		slots.clear();
	}

	/**
	 * Associates the specified slot value with the specified symbol.
	 * 
	 * @param key
	 *            the slot symbol.
	 * @param value
	 *            the slot value.
	 */
	public void put(NSOFSymbol key, NSOFObject value) {
		slots.put(key, value);
	}

	/**
	 * Get the mapped slot value for the specified key.
	 * 
	 * @param key
	 *            the slot symbol.
	 * @return the slot value - <tt>null</tt> otherwise.
	 */
	public NSOFObject get(NSOFSymbol key) {
		return slots.get(key);
	}

	/**
	 * Remove a slot entry.
	 * 
	 * @param key
	 *            the slot symbol.
	 * @return the removed slot value.
	 */
	public NSOFObject remove(NSOFSymbol key) {
		return slots.remove(key);
	}
}
