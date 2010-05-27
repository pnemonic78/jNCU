package net.sf.jncu.protocol.v2_0.io.win;

import net.sf.jncu.protocol.v2_0.io.DockCommandFile;

@Deprecated
public class DockCommandFileWindows extends DockCommandFile {

	/** Desktop to Newton. */
	@Deprecated
	public static class DesktopToNewton extends DockCommandFile.DesktopToNewton {
		/**
		 * This command returns an array of frames describing devices. These are
		 * the devices which will appear in the devices pop-up in the Windows
		 * file browsing dialog. Each frame in the array should look like this:<br>
		 * <code>{<br>
		 * &nbsp;&nbsp;name: "c:mydisk",<br>
		 * &nbsp;&nbsp;disktype: 1<br>
		 * }</code><br>
		 * where (floppy = 0, hardDrive = 1, cdRom = 2, netDrive = 3). The icon
		 * is displayed in the pop-up. This may not be possible in which case
		 * this slot will be optional.
		 * 
		 * <pre>
		 * 'devs'
		 * length
		 * array
		 * </pre>
		 */
		public static final String kDDevices = "devs";
		/**
		 * This command returns an array of filters to the Newton. It's sent in
		 * response to a <tt>kDGetFilters</tt> command. The filter should be an
		 * array of strings which are displayed in the filter pop-up. If the
		 * filter array is <tt>nil</tt> no pop-up is displayed. Windows only.
		 * 
		 * <pre>
		 * 'filt'
		 * length
		 * filter array
		 * </pre>
		 */
		public static final String kDFilters = "filt";
	}

	/** Newton to Desktop. */
	@Deprecated
	public static class NewtonToDesktop extends DockCommandFile.NewtonToDesktop {
		/**
		 * This command asks the desktop system to return an array of device
		 * names. This is only used for the Windows platform.
		 * 
		 * <pre>
		 * 'gdev'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetDevices = "gdev";
		/**
		 * This command asks the desktop to send a list of filters to display in
		 * the Windows file browser. A <tt>kDFilters</tt> command is expected in
		 * response.
		 * 
		 * <pre>
		 * 'gflt'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetFilters = "gflt";
		/**
		 * This command changes the current filter being used. A
		 * <tt>kDFilesAndFolders</tt> command is expected in return. The index
		 * is a long indicating which item in the filters array sent from the
		 * desktop should be used as the current filter. Index is 0-based.
		 * Windows only.
		 * 
		 * <pre>
		 * 'sflt'
		 * length
		 * index
		 * </pre>
		 */
		public static final String kDSetFilter = "sflt";
		/**
		 * This command asks the desktop to change the drive on the desktop and
		 * set the directory to the current directory for that drive. The string
		 * contains the drive letter followed by a colon e.g. "<tt>C:</tt>".
		 * Windows only.
		 * 
		 * <pre>
		 * 'sdrv'
		 * length
		 * drive string
		 * </pre>
		 */
		public static final String kDSetDrive = "sdrv";
	}

}
