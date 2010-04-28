package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * Command to initiate docking.
 * 
 * @author moshew
 */
public class DCmdInitiateDocking extends DockCommandToNewton {

	public static final int SESSION_NONE = 0;
	public static final int SESSION_SETTING_UP = 1;
	public static final int SESSION_SYNCHRONIZE = 2;
	public static final int SESSION_RESTORE = 3;
	public static final int SESSION_LOAD_PACKAGE = 4;
	public static final int SESSION_TEST_COMM = 5;
	public static final int SESSION_LOAD_PATCH = 6;
	public static final int SESSION_UPDATING_STORES = 7;

	private int session;

	/**
	 * Creates a new command.
	 */
	public DCmdInitiateDocking() {
		super(DockCommandSession.DesktopToNewton.kDInitiateDocking);
	}

	/**
	 * Get the session type.
	 * 
	 * @return the session.
	 */
	public int getSession() {
		return session;
	}

	/**
	 * Set the session type.
	 * 
	 * @param session
	 *            the session.
	 */
	public void setSession(int session) {
		this.session = session;
	}

	@Override
	protected ByteArrayOutputStream getData() {
		ByteArrayOutputStream data = new ByteArrayOutputStream(LENGTH_WORD);
		ntohl(session, data);
		return data;
	}
}
