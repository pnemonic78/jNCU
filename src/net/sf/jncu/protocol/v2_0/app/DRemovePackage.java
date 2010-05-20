package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * <tt>kDRemovePackage</tt><br>
 * This command tells the Newton to delete a package. It can be used during
 * selective restore or any other time.
 * 
 * <pre>
 * 'rmvp'
 * length
 * name ref
 * </pre>
 * 
 * @author Moshe
 */
public class DRemovePackage extends DockCommandToNewtonScript {

	public static final String COMMAND = "rmvp";

	/**
	 * Constructs a new command.
	 */
	public DRemovePackage() {
		super(COMMAND);
	}

	@Override
	public void setObject(NSOFObject object) {
		if (!(object instanceof NSOFString)) {
			throw new ClassCastException("name required");
		}
		super.setObject(object);
	}

}
