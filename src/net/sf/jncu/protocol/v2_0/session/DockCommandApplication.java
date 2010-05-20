package net.sf.jncu.protocol.v2_0.session;

/**
 * <h1>Application commands</h1>
 */
public class DockCommandApplication extends DockCommandSession {

	/**
	 * This command is sent when a message is received that is unknown. When the
	 * desktop receives this command it can either install a protocol extension
	 * and try again or return an error to the Newton. If the built-in Newton
	 * code receives this command it always signals an error. The bad command
	 * parameter is the 4 char command that wasn't recognised. The data is not
	 * returned.
	 * 
	 * <pre>
	 * 'unkn'
	 * length
	 * bad command
	 * </pre>
	 */
	public static final String kDUnknownCommand = "unkn";

}
