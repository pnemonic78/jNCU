package net.sf.jncu.protocol.v2_0.file;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * File browsing
 * <p>
 * File browsing will use the same protocol described above with the following
 * additions. For synchronise, the process is completely driven from the desktop
 * side. For file browsing/importing, however, the process is driven from the
 * Newton.
 */
public class DockCommandFile extends DockingEventCommands {

	/** Desktop to Newton. */
	public static final class DesktopToNewton {
		/**
		 * Windows only.
		 * 
		 * <pre>
		 * 'devs'
		 * </pre>
		 */
		public static final String kDDevices = "devs";
		/**
		 * Windows only.
		 * 
		 * <pre>
		 * 'filt'
		 * </pre>
		 */
		public static final String kDFilters = "filt";
		/**
		 * <pre>
		 * 'path'
		 * </pre>
		 */
		public static final String kDPath = "path";
		/**
		 * <pre>
		 * 'file'
		 * </pre>
		 */
		public static final String kDFilesAndFolders = "file";
		// about files
		// and folders
		/**
		 * <pre>
		 * 'finf'
		 * </pre>
		 */
		public static final String kDFileInfo = "finf";
		/**
		 * <pre>
		 * 'gist'
		 * </pre>
		 */
		public static final String kDGetInternalStore = "gist";
		/**
		 * <pre>
		 * 'alir'
		 * </pre>
		 */
		public static final String kDAliasResolved = "alir";
	}

	/** Newton to Desktop. */
	public static final class NewtonToDesktop {
		/**
		 * <pre>
		 * 'rtbr'
		 * </pre>
		 */
		public static final String kDRequestToBrowse = "rtbr";
		/**
		 * Windows only.
		 * 
		 * <pre>
		 * 'gdev'
		 * </pre>
		 */
		public static final String kDGetDevices = "gdev";
		/**
		 * Get the starting path.
		 * 
		 * <pre>
		 * 'dpth'
		 * </pre>
		 */
		public static final String kDGetDefaultPath = "dpth";
		/**
		 * Ask the desktop for files and folders.
		 * 
		 * <pre>
		 * 'gfil'
		 * </pre>
		 */
		public static final String kDGetFilesAndFolders = "gfil";
		/**
		 * <pre>
		 * 'spth'
		 * </pre>
		 */
		public static final String kDSetPath = "spth";
		/**
		 * <pre>
		 * 'gfin'
		 * </pre>
		 */
		public static final String kDGetFileInfo = "gfin";
		/**
		 * <pre>
		 * 'isto'
		 * </pre>
		 */
		public static final String kDInternalStore = "isto";
		/**
		 * <pre>
		 * 'rali'
		 * </pre>
		 */
		public static final String kDResolveAlias = "rali";
		/**
		 * Windows only.
		 * 
		 * <pre>
		 * 'gflt'
		 * </pre>
		 */
		public static final String kDGetFilters = "gflt";
		/**
		 * Windows only.
		 * 
		 * <pre>
		 * 'sflt'
		 * </pre>
		 */
		public static final String kDSetFilter = "sflt";
		/**
		 * Windows only.
		 * 
		 * <pre>
		 * 'sdrv'
		 * </pre>
		 */
		public static final String kDSetDrive = "sdrv";
	}
}
