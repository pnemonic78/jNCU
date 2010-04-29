package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * Command to initiate docking.
 * 
 * @author moshew
 */
public class DCmdDesktopInfo extends DockCommandToNewton {
	/**
	 * <tt>kDDesktopInfo</tt><br>
	 * This command is used to negotiate the real protocol version. The protocol
	 * version sent with the <tt>kDRequestToDock</tt> command is now fixed at
	 * version 9 (the version used by the 1.0 ROMs) so we can support package
	 * loading with NPI 1.0, Connection 2.0 and NTK 1.0. Connection 3.0 will
	 * send this command with the real protocol version it wants to use to talk
	 * to the Newton. The Newton will respond with a number equal to or lower
	 * than the number sent to it by the desktop. The desktop can then decide
	 * whether it can talk the specified protocol or not.
	 * <p>
	 * The desktop type is a long that identifies the sender‚ <tt>0</tt> for the
	 * Macintosh and <tt>1</tt> for Windows. The password key is used as part of
	 * the password verification.
	 * <p>
	 * Session type will be the real session type and should override what was
	 * sent in <tt>kDInitiateDocking</tt>. In fact, it will either be the same
	 * as was sent in <tt>kDInitiateDocking</tt> or "settingUp" to indicate that
	 * although the desktop has accepted a connection, the user has not yet
	 * specified an operation.
	 * <p>
	 * <tt>AllowSelectiveSync</tt> is a boolean. The desktop should say no when
	 * the user hasn't yet done a full sync and, therefore, can't do a selective
	 * sync.
	 * <p>
	 * <tt>desktopApps</tt> is an array of frames that describes who the Newton
	 * is talking with. Each frame in the array looks like this:
	 * <code>{name: "Newton Backup Utility", id: 1, version: 1}</code> There
	 * might be more than one item in the array if the Newton is connecting with
	 * a DIL application. The built-in Connection application expects 1 item in
	 * the array that has id:
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
	public static final String COMMAND = "dinf";

	/** The protocol version. */
	public static final int kProtocolVersion = 10;

	/** Newton Backup Utility. */
	public static final int kNBU = 1;
	/** Newton Connection Utilities. */
	public static final int kNCU = 2;

	/** Apple Macintosh desktop type. */
	public static final int kMacintosh = 0;
	/** Microsoft Windows desktop type. */
	public static final int kWindows = 1;

	private static final int desktopType = (System.getProperty("os.name").startsWith("Windows")) ? kWindows : kMacintosh;

	/**
	 * Creates a new command.
	 */
	public DCmdDesktopInfo() {
		super(COMMAND);
	}

	@Override
	protected ByteArrayOutputStream getData() {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		// protocol version
		ntohl(kProtocolVersion, data);
		// desktopType // 0 for Mac, 1 for Windows
		ntohl(desktopType, data);
		// encrypted key // 2 longs
		ntohl(0, data);
		ntohl(0, data);
		// session type
		ntohl(DCmdInitiateDocking.SESSION_SETTING_UP, data);
		// allowSelectiveSync // 0 = no, 1 = yes
		ntohl(FALSE, data);
		// desktopApps ref
		ntohl(0, data);
		return data;
	}
}
