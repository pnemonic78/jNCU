package net.sf.jncu.fdil;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Symbols
 * <p>
 * A symbol object is a variable-size object used as a token or as an
 * identifier. Most often it is used as a slot name or object class. It is
 * composed of ASCII characters with values between 32 and 127 inclusive,
 * excluding the vertical bar (<tt>|</tt>) and backslash (<tt>\</tt>)
 * characters. A symbol must be shorter than 254 characters. When symbols are
 * compared to each other, a case-insensitive comparison is performed.
 * 
 * @author moshew
 */
public class FDSymbol extends FDBinaryObject {

	/**
	 * Symbols are a pooled resource. When a symbol is created, it stored in an
	 * internal table. If a new symbol is subsequently created with the same
	 * string, a reference to the first symbol is returned; therefore only one
	 * version of the symbol exists. Note that this comparison of strings is
	 * case-insensitive.
	 */
	private static final Map<String, FDSymbol> pool = new TreeMap<String, FDSymbol>();

	private final String name;

	/**
	 * Creates a new symbol.
	 * 
	 * @param name
	 *            the name.
	 * @throws UnsupportedEncodingException
	 *             if invalid characters are found.
	 */
	protected FDSymbol(String name) throws UnsupportedEncodingException {
		super();
		int len = name.length();
		if (len > 254) {
			throw new StringIndexOutOfBoundsException();
		}
		char c;
		for (int i = 0; i < len; i++) {
			c = name.charAt(i);
			if ((c < 32) || (c > 127)) {
				throw new UnsupportedEncodingException();
			}
		}
		this.name = name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Get a symbol from the pool. If no symbol exists, then it is created and
	 * cached.
	 * 
	 * @param name
	 *            the name.
	 * @return the symbol.
	 * @throws UnsupportedEncodingException
	 *             if invalid characters are found.
	 */
	public static FDSymbol getSymbol(String name) throws UnsupportedEncodingException {
		FDSymbol symbol = pool.get(name);
		if (symbol == null) {
			symbol = new FDSymbol(name);
			pool.put(name, symbol);
		}
		return symbol;
	}
}
