package net.sf.jncu.protocol.v1_0.app;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDLoadPackage</tt><br>
 * This command will load a package into the Newton's RAM. The package data
 * should be padded to an even multiple of 4 by adding zero bytes to the end of
 * the package data.
 * 
 * <pre>
 * 'lpkg'
 * length
 * package data
 * </pre>
 * 
 * @author Moshe
 */
public class DLoadPackage extends DockCommandToNewton {

	public static final String COMMAND = "lpkg";

	/**
	 * Creates a new command.
	 */
	public DLoadPackage() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
