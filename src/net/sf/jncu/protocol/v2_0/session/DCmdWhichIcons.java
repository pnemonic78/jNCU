package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDWhichIcons</tt><br>
 * This command is used to customise the set of icons shown on the Newton. The
 * <tt>iconMask</tt> is a long that indicates which icons should be shown. For
 * example, to show all icons you would use this:
 * <code>kBackupIcon + kSyncIcon + kInstallIcon + kRestoreIcon + kImportIcon + kKeyboardIcon</code>
 * Where:
 * <ul>
 * <li>kBackupIcon = 1
 * <li>kRestoreIcon = 2
 * <li>kInstallIcon = 4
 * <li>kImportIcon = 8
 * <li>kSyncIcon = 16
 * <li>kKeyboardIcon = 32
 * </ul>
 * 
 * <pre>
 * 'wicn'
 * length
 * iconMask
 * </pre>
 * 
 * @author moshew
 */
public class DCmdWhichIcons extends DockCommandToNewton {

	public static final String COMMAND = "wicn";

	private int icons;

	/** "Backup" icon. */
	public static final int kBackupIcon = 1;
	/** "Restore" icon. */
	public static final int kRestoreIcon = 2;
	/** "Install package" icon. */
	public static final int kInstallIcon = 4;
	/** "Import" icon. */
	public static final int kImportIcon = 8;
	/** "Synchronise" icon. */
	public static final int kSyncIcon = 16;
	/** "Keyboard" icon. */
	public static final int kKeyboardIcon = 32;

	/**
	 * Creates a new command.
	 */
	public DCmdWhichIcons() {
		super(COMMAND);
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ntohl(getIcons(), data);
		return data;
	}

	/**
	 * Get the icons.
	 * 
	 * @return the icons.
	 */
	public int getIcons() {
		return icons;
	}

	/**
	 * Set the icons.
	 * 
	 * @param icons
	 *            the icons.
	 */
	public void setIcons(int icons) {
		this.icons = icons;
	}

	public void addIcon(int icon) {
		switch (icon) {
		case kBackupIcon:
		case kRestoreIcon:
		case kInstallIcon:
		case kImportIcon:
		case kSyncIcon:
		case kKeyboardIcon:
			icons |= icon;
			break;
		default:
			throw new IllegalArgumentException("unknown icon");
		}
	}
}
