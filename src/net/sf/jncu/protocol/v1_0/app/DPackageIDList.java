package net.sf.jncu.protocol.v1_0.app;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDPackageIDList</tt><br>
 * This command sends a list of package frames to the desktop. For each package
 * the information sent is this:
 * <ol>
 * <li>ULong packageSize;
 * <li>ULong packageId;
 * <li>ULong packageVersion;
 * <li>ULong format;
 * <li>ULong deviceKind;
 * <li>ULong deviceNumber;
 * <li>ULong deviceId;
 * <li>ULong modifyDate;
 * <li>ULong isCopyProtected;
 * <li>ULong length;
 * <li>Character name; (length bytes of Unicode string)
 * </ol>
 * Note that this is not sent as an array! It's sent as binary data. Note that
 * this finds packages only in the current store.
 * 
 * <pre>
 * 'pids'
 * length
 * count
 * package info
 * </pre>
 */
public class DPackageIDList extends DockCommandFromNewton {

	public static final String COMMAND = "pids";

	/**
	 * Creates a new command.
	 */
	public DPackageIDList() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
