/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io.win;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDSetDrive</tt><br>
 * This command asks the desktop to change the drive on the desktop and set the
 * directory to the current directory for that drive. The string contains the
 * drive letter followed by a colon e.g. "<tt>C:</tt>". Windows only.
 * 
 * <pre>
 * 'sdrv'
 * length
 * drive string
 * </pre>
 * 
 * @author moshew
 */
public class DSetDrive extends DockCommandFromNewton {

	public static final String COMMAND = "sdrv";

	public DSetDrive() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}
}
