package net.sf.jncu.protocol.v2_0.file;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * <h1>File browsing</h1>
 * <p>
 * File browsing will use the same protocol described above with the following
 * additions. For synchronise, the process is completely driven from the desktop
 * side. For file browsing/importing, however, the process is driven from the
 * Newton.
 * <p>
 * 
 <h2>Examples</h2>
 * <p>
 * After the session has started (see above) these commands would be sent:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * <th width=50%></th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDRequestToBrowse</td>
 * </tr>
 * <tr>
 * <td>kDGetInternalStore</td>
 * <td>-></td>
 * <td>// optional</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDInternalStore // optional</td>
 * </tr>
 * <tr>
 * <td>kDResult</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDGetDevices // Only for Windows</td>
 * </tr>
 * <tr>
 * <td>kDDevices</td>
 * <td>-></td>
 * <td>// Only for Windows</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDGetFilters // Windows only</td>
 * </tr>
 * <tr>
 * <td>kDFilters</td>
 * <td>-></td>
 * <td>// Windows only</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDGetDefaultPath</td>
 * </tr>
 * <tr>
 * <td>kDPath</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDGetFilesAndFolders</td>
 * </tr>
 * <tr>
 * <td>kDFilesAndFolders</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td colspan=4>
 * <p>
 * Note that we must start the transaction with a requestToDock to force 1.0 and
 * 2.0 versions of Connection to display the correct message.
 * <p>
 * When the user changes the path by tapping on a folder, picking a new level
 * from the path popup, or picking a new drive on the drive popup:
 * </td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSetPath</td>
 * </tr>
 * <tr>
 * <td>kDFilesAndFolders</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td colspan=4>
 * <p>
 * When the user changes the drive by picking a drive on the drive pop-up
 * (Windows only), the desktop will change the drive and set the directory to
 * the current directory for that drive, and return the new path to the Newton:
 * </td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSetDrive</td>
 * </tr>
 * <tr>
 * <td>kDPath</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDGetFilesAndFolders</td>
 * </tr>
 * <tr>
 * <td>kDFilesAndFolders</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td colspan=4>
 * <p>
 * If the folder is an alias, it's like this (Macintosh only):</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSetPath</td>
 * </tr>
 * <tr>
 * <td>kDPath</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDGetFilesAndFolders</td>
 * </tr>
 * <tr>
 * <td>kDFilesAndFolders</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td colspan=4>
 * <p>
 * <tr>
 * <td>When the user taps on the File Info button:</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDGetFileInfo</td>
 * </tr>
 * <tr>
 * <td>kDFileInfo</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td colspan=4>
 * <p>
 * If the selected item is an alias (Macintosh only), before doing import,
 * getfileinfo, or setpath:
 * </td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResolveAlias // name of alias</td>
 * </tr>
 * <tr>
 * <td>kDAliasResolved</td>
 * <td>-></td>
 * <td>// 0 or 1, 0=can't resolve</td>
 * </tr>
 * <tr>
 * <td colspan=4>
 * <p>
 * If the Windows user picks a new filter from the list:</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSetFilter</td>
 * </tr>
 * <tr>
 * <td>kDFilesAndFolders</td>
 * <td>-></td>
 * </tr>
 * </table>
 */
public class DockCommandFile extends DockingEventCommands {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockingEventCommands.DesktopToNewton {
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
		 * filter array is nil no pop-up is displayed. Windows only.
		 * 
		 * <pre>
		 * 'filt'
		 * length
		 * filter array
		 * </pre>
		 */
		public static final String kDFilters = "filt";
		/**
		 * This command returns the initial strings for the folder pop-up in the
		 * Macintosh version of the window and for the directories list in the
		 * Windows version. It is also returned after the user taps on a folder
		 * alias. In this case the path must be changed to reflect the new
		 * location. Each element of the array is a frame that takes this form:<br>
		 * <code>{<br>
		 * &nbsp;&nbsp;name: "my hard disk",<br>
		 * &nbsp;&nbsp;type: disk,<br>
		 * &nbsp;&nbsp;disktype: harddrive,<br>
		 * &nbsp;&nbsp;whichVol: 0,			// Optional - see below<br>
		 * }</code><br>
		 * <p>
		 * The possible values for type are (desktop = 0, file = 1, folder = 2,
		 * disk = 3). If the type is disk, there is an additional slot
		 * <tt>disktype</tt> with the values (floppy = 0, hardDrive = 1, cdRom =
		 * 2, netDrive = 3). Finally, for the second frame in the array i.e. the
		 * one after Desktop, there will be an additional slot <tt>whichvol</tt>
		 * , which will be a 0 if the item is disk or a <tt>volRefNum</tt> if
		 * the item is a folder on the desktop.
		 * <p>
		 * For example, the Macintosh might send:<br>
		 * <code>[{name: "desktop", type: desktop}, {name: "my hard disk", type: disk, disktype: harddrive, whichvol: 0}, {name: "business", type: folder}]</code>
		 * <br>
		 * or for some folder on the desktop it it might send:<br>
		 * <code>[{name: "desktop", type: desktop}, {name: "business", type: folder, whichvol: -1}, {name: "my folder", type: folder}]</code>
		 * <p>
		 * For Windows it might be: [{name:
		 * "c:\", type: 'folder}, {name: "business", type: 'folder}]
		 * 
		 * <pre>
		 * 'path'
		 * length
		 * folder array
		 * </pre>
		 */
		public static final String kDPath = "path";
		/**
		 * This command returns an array of information that's used to display a
		 * standard file like dialog box on the Newton. Each element of the
		 * array is a frame describing one file, folder or device. The
		 * individual frame would look like this:<br>
		 * <code>{<br>
		 * &nbsp;&nbsp;name: "whatever",<br>
		 * &nbsp;&nbsp;type: kFolder,<br>
		 * &nbsp;&nbsp;disktype: 0,		// optional if type = disk<br>
		 * &nbsp;&nbsp;whichVol: 0,		// optional if name is on the desktop<br>
		 * &nbsp;&nbsp;alias: nil,		// optional if it's an alias<br>
		 * }</code>
		 * <br>
		 * The possible values for type are desktop, file, folder or disk (0, 1,
		 * 2, 3). The frames should be in the order in the array that they are
		 * to be displayed in on the Newton. For example, the array might look
		 * like this:<br>
		 * <code>[{name: "Applications", type: kFolder},<br>
		 * &nbsp;{name: "important info", type: kFile},<br>
		 * &nbsp;{name: "System", type: kFolder}]</code>
		 * <p>
		 * If the type is a disk, then the frame will have an additional slot
		 * <tt>disktype</tt> with the values (floppy = 0, hardDrive = 1, cdRom =
		 * 2, netDrive = 3). Also, if the current location is the desktop, there
		 * is an additional slot <tt>whichvol</tt> to indicate the location of
		 * the individual files, folders and disks with the values 0 for disks
		 * and a negative number for the <tt>volRefNum</tt> for files and
		 * folders on the desktop.
		 * <p>
		 * If the item is an alias there is an <tt>alias</tt> slot. The
		 * existence of this slot indicates that the item is an alias.
		 * 
		 * <pre>
		 * 'file'
		 * length
		 * file/folder array
		 * </pre>
		 */
		public static final String kDFilesAndFolders = "file";
		/**
		 * This command is sent in response to a <tt>kDGetFileInfo</tt> command.
		 * It returns a frame that looks like this:<br>
		 * <code>{<br>
		 * &nbsp;&nbsp;kind: "Microsoft Word document",<br>
		 * &nbsp;&nbsp;size: 20480,<br>
		 * &nbsp;&nbsp;created: 3921837,<br>
		 * &nbsp;&nbsp;modified: 3434923,<br>
		 * &nbsp;&nbsp;icon: &lt;binary object of icon&gt;,<br>
		 * &nbsp;&nbsp;path: "hd:files:another folder:"<br>
		 * }</code>
		 * <p>
		 * <tt>kind</tt> is a description of the file.<br>
		 * <tt>size</tt> is the number of bytes (actual, not the amount used on
		 * the disk).<br>
		 * <tt>create</tt> is the creation date in Newton date format.<br>
		 * <tt>modified</tt> is the modification date of the file.<br>
		 * <tt>icon</tt> is an icon to display. This is optional.<br>
		 * <tt>path</tt> is the "user understandable" path description<br>
		 * 
		 * <pre>
		 * 'finf'
		 * length
		 * frame of info
		 * </pre>
		 */
		public static final String kDFileInfo = "finf";
		/**
		 * This command is sent by the desktop in response to the
		 * <tt>kDResolveAlias</tt> command. If the value is 0, the alias can't
		 * be resolved. If the data is <tt>1</tt> (or non-zero) the alias can be
		 * resolved.
		 * 
		 * <pre>
		 * 'alir'
		 * length
		 * resolved
		 * </pre>
		 */
		public static final String kDAliasResolved = "alir";
		/**
		 * This command returns info about the default store. This info is the
		 * same as the info returned by the <tt>kDGetStoreNames</tt> command
		 * (see <tt>kDStoreNames</tt> for details). The default store is the one
		 * used by LoadPackage.
		 * 
		 * <pre>
		 * 'gdfs'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetDefaultStore = "gdfs";
		/**
		 * This command requests the Newton to return info about the internal
		 * store. The result is described with the <tt>KDInternalStore</tt>
		 * command.
		 * 
		 * <pre>
		 * 'gist'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetInternalStore = "gist";
		/**
		 * This command is the same as <tt>kDSetCurrentStore</tt> except that it
		 * returns the names of the soups on the stores as if you'd send a
		 * <tt>kDGetSoupNames</tt> command. It sets the current store on the
		 * Newton. A store frame is sent to uniquely identify the store to be
		 * set: <br>
		 * <code>{<br>
		 * &nbsp;&nbsp;name: "foo",<br>
		 * &nbsp;&nbsp;kind: "bar",<br>
		 * &nbsp;&nbsp;signature: 1234,<br>
		 * &nbsp;&nbsp;info: {&lt;info frame&gt;}		// This one is optional<br>
		 * }</code>
		 * <br>
		 * A <tt>kDSoupNames</tt> is sent by the Newton in response.
		 * 
		 * <pre>
		 * 'ssgn'
		 * length
		 * store frame
		 * </pre>
		 */
		public static final String kDSetStoreGetNames = "ssgn";
		/**
		 * This command requests that the name of the current store be set to
		 * the specified name.
		 * 
		 * <pre>
		 * 'ssna'
		 * length
		 * name ref
		 * </pre>
		 */
		public static final String kDSetStoreName = "ssna";
		/**
		 * This commands sets the signature of the current store to the
		 * specified value. A <tt>kDResult</tt> with value 0 (or the error value
		 * if an error occurred) is sent to the desktop in response.
		 * 
		 * <pre>
		 * 'ssig'
		 * length
		 * new signature
		 * </pre>
		 */
		public static final String kDSetStoreSignature = "ssig";
		/**
		 * This command sets the current store on the Newton. A store frame is
		 * sent to uniquely identify the store to be set: <br>
		 * <code>
		 * {<br>&nbsp;&nbsp;name: "foo",<br>
		 * &nbsp;&nbsp;kind: "bar",<br>
		 * &nbsp;&nbsp;signature: 1234,<br>
		 * &nbsp;&nbsp;info: {&lt;info frame&gt;}		// This one is optional<br>
		 * }</code>
		 * 
		 * <pre>
		 * 'ssto'
		 * length
		 * store frame
		 * </pre>
		 */
		public static final String kDSetCurrentStore = "ssto";

		/** Floppy disk device. */
		public static final int eFloppyDevice = 0;
		/** Hard disk drive device. */
		public static final int eHardDriveDevice = 1;
		/** CD-ROM disc device. */
		public static final int eCdRomDevice = 2;
		/** Network drive device. */
		public static final int eNetDriveDevice = 3;

		/** Desktop path type. */
		public static final int eDesktopPath = 0;
		/** File path type type. */
		public static final int eFilePath = 1;
		/** Folder path type. */
		public static final int eFolderPath = 2;
		/** Disk path type. */
		public static final int eDiskPath = 3;
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockingEventCommands.NewtonToDesktop {
		/**
		 * This command is sent to a desktop that the Newton wishes to browse
		 * files on. File types can be 'import, 'packages, 'syncFiles' or an
		 * array of strings to use for filtering.
		 * 
		 * <pre>
		 * 'rtbr'
		 * length
		 * file types
		 * </pre>
		 */
		public static final String kDRequestToBrowse = "rtbr";
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
		 * This commands requests the desktop system to return the default path.
		 * This is the list that goes in the folder pop-up for the Macintosh and
		 * in the directories list for Windows.
		 * 
		 * <pre>
		 * 'dpth'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetDefaultPath = "dpth";
		/**
		 * This command requests that the desktop system return the files and
		 * folders necessary to open a standard file like dialog.
		 * 
		 * <pre>
		 * 'gfil'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetFilesAndFolders = "gfil";
		/**
		 * This command tells the desktop that the user has changed the path.
		 * The desktop responds with a new list of files and folders. The path
		 * is sent as an array of strings rather than an array of frames as all
		 * of the other commands are for performance reasons. For the Macintosh,
		 * the array would be something like:
		 * <code>["Desktop",{Name:"My hard disk", whichVol:0}, "Business"]</code>
		 * to set the path to "<tt>My hard disk:business:</tt>". "
		 * <tt>Desktop</tt>" will always be at the start of the list, since
		 * that's the way Standard File works. So if the user wanted to set the
		 * path to somewhere in the Desktop Folder he would send something like
		 * <code>["Desktop",{Name:"Business", whichVol:-1}]</code> to set the
		 * path to "<tt>My hard disk:Desktop Folder:business:</tt>"
		 * <p>
		 * The second item in the array, will always be a frame instead of a
		 * string and will contain an additional slot "<tt>whichvol</tt>" to
		 * indicate to the desktop whether that item is a name of a volume or a
		 * folder in the Desktop Folder and if so it's <tt>volRefNum</tt>.
		 * <p>
		 * For Windows the array would be something like: <code>["c:\", "business"]</code>
		 * to set the path to "<tt>c:\business</tt>".
		 * 
		 * <pre>
		 * 'spth'
		 * length
		 * array of strings
		 * </pre>
		 */
		public static final String kDSetPath = "spth";
		/**
		 * This command asks the desktop to return info about the specified
		 * file. See <tt>kDFileInfo</tt> for info about what's returned.
		 * <p>
		 * If the selected item is at the Desktop level, a frame
		 * <code>{Name:"Business", whichVol:-1}</code> will be sent instead of
		 * the string to indicate the <tt>volRefNum</tt> for the file.
		 * 
		 * <pre>
		 * 'gfin'
		 * length
		 * filename string
		 * </pre>
		 */
		public static final String kDGetFileInfo = "gfin";
		/**
		 * This command returns information about the internal store. The info
		 * is in the form of a frame that looks like this: <br>
		 * <code>{<br>
		 * &nbsp;&nbsp;name: "Internal",<br>
		 * &nbsp;&nbsp;signature: 1234,<br>
		 * &nbsp;&nbsp;totalsize: 1234,<br>
		 * &nbsp;&nbsp;usedsize: 1234,<br>
		 * &nbsp;&nbsp;kind: "Internal",<br>
		 * }</code><br>
		 * This is the same frame returned by <tt>kDStoreNames</tt> except that
		 * the store info isn't returned.
		 * 
		 * <pre>
		 * 'isto'
		 * length
		 * store frame
		 * </pre>
		 */
		public static final String kDInternalStore = "isto";
		/**
		 * Resolve alias.
		 * 
		 * <pre>
		 * 'rali'
		 * length = 0
		 * </pre>
		 */
		public static final String kDResolveAlias = "rali";
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
		/**
		 * This command returns a store info frame describing the default store.
		 * This frame contains the same info returned for all stores by the
		 * <tt>kDStoreNames</tt> command except that it doesn't include the
		 * store info. It contains the name, signature, total size, used size
		 * and kind.
		 * 
		 * <pre>
		 * 'dfst'
		 * length
		 * store frame
		 * </pre>
		 */
		public static final String kDDefaultStore = "dfst";
	}
}
