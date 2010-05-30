package net.sf.jncu.protocol.v1_0.data;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSoupNames</tt><br>
 * This command is sent in response to a <tt>kDGetSoupNames</tt> command. It
 * returns the names and signatures of all the soups in the current store.
 * 
 * <pre>
 * 'soup'
 * length
 * array of string names
 * array of corresponding soup signatures
 * </pre>
 */
public class DSoupNames extends DockCommandFromNewton {

	public static final String COMMAND = "soup";

	/**
	 * Creates a new command.
	 */
	public DSoupNames() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
