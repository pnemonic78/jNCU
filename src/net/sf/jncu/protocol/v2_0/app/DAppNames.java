package net.sf.jncu.protocol.v2_0.app;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * <tt>kDAppNames</tt><br>
 * This command returns the names of the applications present on the Newton. It
 * also, optionally, returns the names of the soups associated with each
 * application. The array looks like this:
 * <code>[{name: "app name", soups: ["soup1", "soup2"]},<br/>
 * &nbsp;{name: "another app name", ...}, ...]</code>
 * <p>
 * Some built-in names are included. "System information" includes the system
 * and directory soups. If there are packages installed, a "Packages" item is
 * listed. If a card is present and has a backup there will be a "Card backup"
 * item. If there are soups that don't have an associated application (or whose
 * application I can't figure out) there's an "Other information" entry.
 * <p>
 * The soup names are optionally returned depending on the value received with
 * <tt>kDGetAppNames</tt>.
 * 
 * <pre>
 * 'appn'
 * length
 * result frame
 * </pre>
 * 
 * @author Moshe
 */
public class DAppNames extends DockCommandFromNewton {

	public static final String COMMAND = "appn";

	/**
	 * Creates a new command.
	 */
	public DAppNames() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		// TODO Auto-generated method stub
	}

}
