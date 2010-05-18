package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDGetPackageInfo</tt><br>
 * The package info for the specified package is returned. See the
 * <tt>kDPackageInfo</tt> command described below Note that multiple packages
 * could be returned because there may be multiple packages with the same title
 * but different package ids. Note that this finds packages only in the current
 * store.
 * 
 * <pre>
 * 'gpin'
 * length
 * title ref
 * </pre>
 * 
 * @author Moshe
 */
public class DGetPackageInfo extends DockCommandToNewton {

	public static final String COMMAND = "gpin";

	/**
	 * Constructs a new command.
	 */
	public DGetPackageInfo() {
		super(COMMAND);
	}

}
