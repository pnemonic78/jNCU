package net.sf.jncu.protocol.v1_0.query;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDInheritance</tt><br>
 * Inheritance. This is a response to a <tt>kDGetInheritance</tt> request.
 * 
 * <pre>
 * 'dinh'
 * length
 * array of class, superclass pairs
 * </pre>
 */
public class DInheritance extends DockCommandFromNewton {

	public static final String COMMAND = "dinh";

	/**
	 * Creates a new command.
	 */
	public DInheritance() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
