package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

public class DockCommandApplication extends DockingEventCommands {

	public static final class DesktopToNewton {
		/**
		 * This command tells the newton to delete a package. It can be used
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
		 * kDPackageInfo command described below Note that multiple packages
		 * could be returned because there may be multiple packages with the
		 * same title but different package ids. Note that this finds packages
		 * only in the current store.
		 * 
		 * <pre>
		 * 'gpin'
		 * length
		 * title ref
		 * </pre>
		 */
		public static final String kDGetPackageInfo = "gpin";
		/**
		 * This command asks the newton to call the specified function and
		 * return it's result. This function must be a global function. The
		 * return value from the function is sent to the desktop with a
		 * kDCallResult command.
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
		 * This command asks the newton to call the specified root method. The
		 * return value from the method is sent to the desktop with a
		 * kDCallResult command.
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
		 * This command installs a protocol extension into the newton. The
		 * extension lasts for the length of the current connection (in other
		 * words, you have to install the extension every time you connect). The
		 * function is a newton script closure that would have to be compiled on
		 * the desktop. See the Dante Connection (ROM) API IU document for
		 * details. A kDResult with value 0 (or the error value if an error
		 * occurred) is sent to the desktop in response.
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
	}

	public static final class NewtonToDesktop {
		/**
		 * This command returns the names of the applications present on the
		 * newton. It also, optionally, returns the names of the soups
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
		 * received with kDGetAppNames.
		 * 
		 * <pre>
		 * 'appn'
		 * length
		 * result frame
		 * </pre>
		 */
		public static final String kDAppNames = "appn";
		/**
		 * This command is sent in response to a kDGetPackageInfo command. An
		 * array is returned that contains a frame for each package with the
		 * specified name (there may be more than one package with the same name
		 * but different package id). The returned frame looks like this:<br>
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
		 * This command is sent in response to a kDCallGlobalfunction or
		 * kDCallRootMethod command. The ref is the return value from the
		 * function or method called.
		 * 
		 * <pre>
		 * 'cres'
		 * length
		 * ref
		 * </pre>
		 */
		public static final String kDCallResult = "cres";
	}

}
