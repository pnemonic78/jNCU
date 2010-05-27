/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSetTranslator</tt><br>
 * This command specifies which translator the desktop should use to import the
 * file. The translator index is the index into the translator list sent by the
 * desktop in the <tt>kDTranslatorList</tt> command. The desktop should
 * acknowledge this command with an indication that the import is proceeding.
 * 
 * <pre>
 * 'tran'
 * length
 * translator index
 * </pre>
 * 
 * @author moshew
 */
public class DSetTranslator extends DockCommandFromNewton {

	public static final String COMMAND = "tran";

	private int translatorIndex;

	public DSetTranslator() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected void decodeData(InputStream data) throws IOException {
		setTranslatorIndex(htonl(data));
	}

	/**
	 * Get the translator index.
	 * 
	 * @return the index.
	 */
	public int getTranslatorIndex() {
		return translatorIndex;
	}

	/**
	 * Set the translator index.
	 * 
	 * @param translatorIndex
	 *            the index.
	 */
	public void setTranslatorIndex(int translatorIndex) {
		this.translatorIndex = translatorIndex;
	}

}
