package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * @author moshew
 */
public class DInitiateDocking extends DockCommandToNewton {

	public static final int kInitiateSession0 = 0;
	public static final int kInitiateSession1 = 1;
	public static final int kInitiateSession2 = 2;
	public static final int kInitiateSession3 = 3;
	public static final int kInitiateSessionPackage = 4;

	private int session;

	/**
	 * Creates a new initiate docking command.
	 */
	public DInitiateDocking() {
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
	protected byte[] encode() {
		byte[] data = new byte[LENGTH_WORD];
		data[0] = (byte) ((session >> 24) & 0xFF);
		data[1] = (byte) ((session >> 16) & 0xFF);
		data[2] = (byte) ((session >> 8) & 0xFF);
		data[3] = (byte) ((session >> 0) & 0xFF);
		return data;
	}
}
