package net.sf.jncu.protocol.v2_0.session;

/**
 * <h1>Application commands</h1>
 */
public class DockCommandApplication extends DockCommandSession {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockCommandSession.DesktopToNewton {
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
		 * details. A <tt>kDResult</tt> with value <tt>0</tt> (or the error
		 * value if an error occurred) is sent to the desktop in response.
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
	public static class NewtonToDesktop extends DockCommandSession.NewtonToDesktop {
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
