package net.sf.jncu.protocol.v1_0.app;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDPackage</tt><br>
 * This command sends a package to the desktop. It's issued repeatedly in
 * response to a <tt>kDBackupPackages</tt> message.
 * 
 * <pre>
 * 'apkg'
 * length
 * package id
 * package data
 * </pre>
 */
public class DPackage extends DockCommandFromNewton {

	public static final String COMMAND = "apkg";

	/**
	 * Creates a new command.
	 */
	public DPackage() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
