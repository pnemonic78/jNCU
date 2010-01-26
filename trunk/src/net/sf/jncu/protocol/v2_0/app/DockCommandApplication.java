package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * Application commands.
 */
public class DockCommandApplication extends DockingEventCommands {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockingEventCommands.DesktopToNewton {
		/**
		 * This command tells the Newton to delete a package. It can be used
		 * during selective restore or any other time.
		 * 
		 * <pre>
		 * 'rmvp'
		 * length
		 * name ref
		 * </pre>
		 */
		public static final String kDRemovePackage = "rmvp";
		/**
		 * The package info for the specified package is returned. See the
		 * <tt>kDPackageInfo</tt> command described below Note that multiple
		 * packages could be returned because there may be multiple packages
		 * with the same title but different package ids. Note that this finds
		 * packages only in the current store.
		 * 
		 * <pre>
		 * 'gpin'
		 * length
		 * title ref
		 * </pre>
		 */
		public static final String kDGetPackageInfo = "gpin";
		/**
		 * This command asks the Newton to call the specified function and
		 * return it's result. This function must be a global function. The
		 * return value from the function is sent to the desktop with a
		 * <tt>kDCallResult</tt> command.
		 * 
		 * <pre>
		 * 'cgfn'
		 * length
		 * function name symbol
		 * args array
		 * </pre>
		 */
		public static final String kDCallGlobalFunction = "cgfn";
		/**
		 * This command asks the Newton to call the specified root method. The
		 * return value from the method is sent to the desktop with a
		 * <tt>kDCallResult</tt> command.
		 * 
		 * <pre>
		 * 'crmd'
		 * length
		 * method name symbol
		 * args array
		 * </pre>
		 */
		public static final String kDCallRootMethod = "crmd";
		/**
		 * This command installs a protocol extension into the Newton. The
		 * extension lasts for the length of the current connection (in other
		 * words, you have to install the extension every time you connect). The
		 * function is a Newton script closure that would have to be compiled on
		 * the desktop. See the Dante Connection (ROM) API IU document for
		 * details. A <tt>kDResult</tt> with value 0 (or the error value if an
		 * error occurred) is sent to the desktop in response.
		 * 
		 * <pre>
		 * 'pext'
		 * length
		 * command
		 * function
		 * </pre>
		 */
		public static final String kDRegProtocolExtension = "pext";
		/**
		 * This command removes a previously installed protocol extension.
		 * 
		 * <pre>
		 * 'prex'
		 * length
		 * command
		 * </pre>
		 */
		public static final String kDRemoveProtocolExtension = "rpex";
		/**
		 * Reports a desktop error to the Newton. The string is included since
		 * the Newton doesn't know how to decode all the desktop errors
		 * (especially since the Macintosh and Windows errors are different).
		 * <tt>ErrorString</tt> is a ref.
		 * 
		 * <pre>
		 * 'ress'
		 * length
		 * errorNumber
		 * errorStringRef
		 * </pre>
		 */
		public static final String kDResultString = "ress";
		/**
		 * This command controls which VBOs are sent compressed to the desktop.
		 * VBO can always be sent compressed, never compressed or only package
		 * VBOs sent compressed.
		 * 
		 * <pre>
		 * 'cvbo'
		 * length
		 * what
		 * </pre>
		 * 
		 * @see eUncompressedVBOs
		 * @see eCompressedPackagesOnly
		 * @see eCompressedVBOs
		 */
		public static final String kDSetVBOCompression = "cvbo";
		/**
		 * This command is used to restore the patch backed up with
		 * <tt>kDGetPatches</tt>. The Newton returns a <tt>kDResult</tt> of 0
		 * (or an error if appropriate) if the patch wasn't installed. If the
		 * patch was installed the Newton restarts.
		 * 
		 * <pre>
		 * 'rpat'
		 * length
		 * patch
		 * </pre>
		 */
		public static final String kDRestorePatch = "rpat";
		/**
		 * This command asks the Newton to send information about the
		 * applications installed on the Newton. See the <tt>kDAppNames</tt>
		 * description above for details of the information returned. The
		 * <tt>return what</tt> parameter determines what information is
		 * returned. Here are the choices:
		 * <ul>
		 * <li>0: return names and soups for all stores
		 * <li>1: return names and soups for current store
		 * <li>2: return just names for all stores
		 * <li>3: return just names for current store
		 * </ul>
		 * 
		 * <pre>
		 * 'gapp'
		 * length
		 * return what
		 * </pre>
		 */
		public static final String kDGetAppNames = "gapp";

		/**
		 * VBO sent uncompressed.
		 */
		public static final int eUncompressedVBOs = 0;
		/**
		 * Only package VBOs sent compressed.
		 */
		public static final int eCompressedPackagesOnly = 1;
		/**
		 * VBO sent compressed.
		 */
		public static final int eCompressedVBOs = 2;
	}

	/** Newton to Desktop. */
	public static final class NewtonToDesktop extends DockingEventCommands.NewtonToDesktop {
		/**
		 * This command returns the names of the applications present on the
		 * Newton. It also, optionally, returns the names of the soups
		 * associated with each application. The array looks like this:
		 * <code>[{name: "app name", soups: ["soup1", "soup2"]},<br/>
		 * &nbsp;{name: "another app name", ...}, ...]</code>
		 * <p>
		 * Some built-in names are included. "System information" includes the
		 * system and directory soups. If there are packages installed, a
		 * "Packages" item is listed. If a card is present and has a backup
		 * there will be a "Card backup" item. If there are soups that don't
		 * have an associated application (or whose application I can't figure
		 * out) there's an "Other information" entry.
		 * <p>
		 * The soup names are optionally returned depending on the value
		 * received with <tt>kDGetAppNames</tt>.
		 * 
		 * <pre>
		 * 'appn'
		 * length
		 * result frame
		 * </pre>
		 */
		public static final String kDAppNames = "appn";
		/**
		 * This command is sent in response to a <tt>kDGetPackageInfo</tt>
		 * command. An array is returned that contains a frame for each package
		 * with the specified name (there may be more than one package with the
		 * same name but different package id). The returned frame looks like
		 * this:<br>
		 * <code>
		 * {<br>
		 * &nbsp;&nbsp;name: "The name passed in",<br>
		 * &nbsp;&nbsp;packagesize: 123,<br>
		 * &nbsp;&nbsp;packageid: 123,<br>
		 * &nbsp;&nbsp;packageversion: 1,<br>
		 * &nbsp;&nbsp;format: 1,<br>
		 * &nbsp;&nbsp;devicekind: 1,<br>
		 * &nbsp;&nbsp;devicenumber: 1,<br>
		 * &nbsp;&nbsp;deviceid: 1,<br>
		 * &nbsp;&nbsp;modtime: 123213213,<br>
		 * &nbsp;&nbsp;iscopyprotected: true,<br>
		 * &nbsp;&nbsp;length: 123,<br>
		 * &nbsp;&nbsp;safetoremove: true<br>
		 * }</code>
		 * 
		 * <pre>
		 * 'pinf'
		 * length
		 * info ref
		 * </pre>
		 */
		public static final String kDPackageInfo = "pinf";
		/**
		 * This command is sent in response to a <tt>kDCallGlobalfunction</tt>
		 * or <tt>kDCallRootMethod</tt> command. The ref is the return value
		 * from the function or method called.
		 * 
		 * <pre>
		 * 'cres'
		 * length
		 * ref
		 * </pre>
		 */
		public static final String kDCallResult = "cres";
	}

	/**
	 * This command is sent in response to a <tt>kDOperationCanceled</tt>.
	 * 
	 * <pre>
	 * 'ocaa'
	 * length = 0
	 * </pre>
	 */
	public static final String kDOpCanceledAck = "ocaa";
	/**
	 * This command is sent when the user cancels an operation. Usually no
	 * action is required on the receivers part except to return to the "ready"
	 * state.
	 * 
	 * <pre>
	 * 'opcn'
	 * length = 0
	 * </pre>
	 */
	public static final String kDOperationCanceled = "opcn";
	/**
	 * This command is sent when an operation is completed. It's only sent in
	 * situations where there might be some ambiguity. Currently, there are two
	 * situations where this is sent. When the desktop finishes a restore it
	 * sends this command. When a sync is finished and there are no sync results
	 * (conflicts) to send to the Newton the desktop sends this command.
	 * 
	 * <pre>
	 * 'opdn'
	 * length = 0
	 * </pre>
	 */
	public static final String kDOperationDone = "opdn";
	/**
	 * This command is first sent from the desktop to the Newton. The Newton
	 * immediately echos the object back to the desktop. The object can be any
	 * NewtonScript object (anything that can be sent through the object
	 * read/write).
	 * <p>
	 * This command can also be sent with no ref attached. If the length is 0
	 * the command is echoed back to the desktop with no ref included.
	 * 
	 * <pre>
	 * 'rtst'
	 * length
	 * object
	 * </pre>
	 */
	public static final String kDRefTest = "rtst";
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
