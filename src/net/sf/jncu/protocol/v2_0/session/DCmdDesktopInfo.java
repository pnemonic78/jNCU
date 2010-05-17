package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import net.sf.jncu.newton.stream.NSOFArray;
import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.newton.stream.NSOFInteger;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDDesktopInfo</tt><br>
 * This command is used to negotiate the real protocol version. The protocol
 * version sent with the <tt>kDRequestToDock</tt> command is now fixed at
 * version 9 (the version used by the 1.0 ROMs) so we can support package
 * loading with NPI 1.0, Connection 2.0 and NTK 1.0. Connection 3.0 will send
 * this command with the real protocol version it wants to use to talk to the
 * Newton. The Newton will respond with a number equal to or lower than the
 * number sent to it by the desktop. The desktop can then decide whether it can
 * talk the specified protocol or not.
 * <p>
 * The desktop type is a long that identifies the sender‚ <tt>0</tt> for the
 * Macintosh and <tt>1</tt> for Windows.
 * <p>
 * The password key is used as part of the password verification.
 * <p>
 * Session type will be the real session type and should override what was sent
 * in <tt>kDInitiateDocking</tt>. In fact, it will either be the same as was
 * sent in <tt>kDInitiateDocking</tt> or "settingUp" to indicate that although
 * the desktop has accepted a connection, the user has not yet specified an
 * operation.
 * <p>
 * <tt>AllowSelectiveSync</tt> is a boolean. The desktop should say no when the
 * user hasn't yet done a full sync and, therefore, can't do a selective sync.
 * <p>
 * <tt>desktopApps</tt> is an array of frames that describes who the Newton is
 * talking with. Each frame in the array looks like this:
 * <code>{name: "Newton Backup Utility", id: 1, version: 1}</code> There might
 * be more than one item in the array if the Newton is connecting with a DIL
 * application. The built-in Connection application expects 1 item in the array
 * that has id:
 * <ul>
 * <li>1: NBU
 * <li>2: NCU
 * </ul>
 * It won't allow connection with any other id. NCK 2.0, NTK and NPI use old
 * revisions of the protocol and aren't considered here.
 * 
 * <pre>
 * 'dinf'
 * length
 * protocol version
 * desktopType		// 0 for Mac, 1 for Windows
 * encrypted key	// 2 longs
 * session type
 * allowSelectiveSync // 0 = no, 1 = yes
 * desktopApps ref
 * </pre>
 * 
 * @see #kNBU
 * @see #kNCU
 * @see #kMacintosh
 * @see #kWindows
 */
public class DCmdDesktopInfo extends DockCommandToNewton {

	public static final String COMMAND = "dinf";

	/** The protocol version. */
	public static final int PROTOCOL_VERSION = 11;

	/** Newton Backup Utility. */
	public static final int kNBU = 1;
	/** Newton Connection Utilities. */
	public static final int kNCU = 2;

	/** Apple Macintosh desktop type. */
	public static final int kMacintosh = 0;
	/** Microsoft Windows desktop type. */
	public static final int kWindows = 1;

	private int sessionType;
	private int desktopType;
	private boolean selectiveSync;
	private long encryptedKey;
	private NSOFArray desktopApps;
	private static final Random rand = new Random();

	/**
	 * Creates a new command.
	 */
	public DCmdDesktopInfo() {
		super(COMMAND);
		setSessionType(DCmdInitiateDocking.SESSION_SETTING_UP);
		setDesktopType(System.getProperty("os.name").startsWith("Windows") ? kWindows : kMacintosh);
		setSelectiveSync(true);
		setEncryptedKey(rand.nextLong());
		setDesktopApps(null);
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ntohl(PROTOCOL_VERSION, data);
		ntohl(getDesktopType(), data);
		ntohl(getEncryptedKey(), data);
		ntohl(getSessionType(), data);
		ntohl(isSelectiveSync() ? TRUE : FALSE, data);
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getDesktopApps(), data);
		return data;
	}

	/**
	 * Get the session type.
	 * 
	 * @return the session type.
	 */
	public int getSessionType() {
		return sessionType;
	}

	/**
	 * Set the session type.
	 * 
	 * @param sessionType
	 *            the session type.
	 * @see DCmdInitiateDocking
	 */
	public void setSessionType(int sessionType) {
		this.sessionType = sessionType;
	}

	/**
	 * Get the desktop type.
	 * 
	 * @return the desktop type.
	 */
	public int getDesktopType() {
		return desktopType;
	}

	/**
	 * Set the desktop type.
	 * 
	 * @param desktopType
	 *            the desktop type.
	 */
	private void setDesktopType(int desktopType) {
		this.desktopType = desktopType;
	}

	/**
	 * Allow selective sync?
	 * 
	 * @return allow?
	 */
	public boolean isSelectiveSync() {
		return selectiveSync;
	}

	/**
	 * Set selective sync.
	 * 
	 * @param selectiveSync
	 *            allow?
	 */
	public void setSelectiveSync(boolean selectiveSync) {
		this.selectiveSync = selectiveSync;
	}

	/**
	 * Get the desktop applications.
	 * 
	 * @return the array of desktop applications.
	 */
	public NSOFArray getDesktopApps() {
		if (desktopApps == null) {
			NSOFFrame app = new NSOFFrame();
			app.put("id", new NSOFInteger(kNCU));
			app.put("name", new NSOFString("Newton Connection Utilities"));
			app.put("version", new NSOFInteger(1));

			this.desktopApps = new NSOFArray();
			desktopApps.setValue(new NSOFFrame[] { app });
		}
		return desktopApps;
	}

	/**
	 * Set the desktop applications.
	 * 
	 * @param desktopApps
	 *            the array of desktop applications.
	 */
	public void setDesktopApps(NSOFArray desktopApps) {
		this.desktopApps = desktopApps;
	}

	/**
	 * Get the encrypted key.
	 * 
	 * @return the encrypted key.
	 */
	public long getEncryptedKey() {
		return encryptedKey;
	}

	/**
	 * Set the encrypted key.
	 * 
	 * @param encryptedKey
	 *            the encrypted key.
	 */
	public void setEncryptedKey(long encryptedKey) {
		this.encryptedKey = encryptedKey;
	}

}
