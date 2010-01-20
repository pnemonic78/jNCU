package net.sf.jncu.protocol.v2_0.op;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

public class DockCommandOperation extends DockingEventCommands {

	public static final class DesktopToNewton {
		/**
		 * Reports a desktop error to the Newton. The string is included since
		 * the Newton doesn't know how to decode all the desktop errors
		 * (especially since the Mac and Windows errors are different).
		 * ErrorString is a ref.
		 * 
		 * <pre>
		 * 'ress'
		 * length
		 * errorNumber
		 * errorStringRef
		 * </pre>
		 */
		public static final String kDResultString = "ress";
	}

	public static final class NewtonToDesktop {
	}

	/**
	 * This command is sent in response to a kDOperationCanceled.
	 * 
	 * <pre>
	 * 'ocaa'
	 * length = 0
	 * </pre>
	 */
	public static final String kDOpCanceledAck = "ocaa";
	/**
	 * This command is sent when the user cancels an operation. Usually no
	 * action is required on the receivers part exept to return to the "ready"
	 * state.
	 * 
	 * <pre>
	 * 'opcn'
	 * length = 0
	 * </pre>
	 */
	public static final String kDOperationCanceled = "opcn";
	/**
	 * This command is sent when an operation is completed. It't only sent in
	 * situations where there might be some ambiguity. Currently, there are two
	 * situations where this is sent. When the desktop finishes a restore it
	 * sends this command. When a sync is finished and there are no sync results
	 * (conflicts) to send to the newton the desktop sends this command.
	 * 
	 * <pre>
	 * 'opdn'
	 * length = 0
	 * </pre>
	 */
	public static final String kDOperationDone = "opdn";
	/**
	 * This command is first sent from the desktop to the Newton. The newton
	 * immediately echos the object back to the desktop. The object can be any
	 * newtonscript object (anything that can be sent through the object
	 * read/write). This command can also be sent with no ref attached. If the
	 * length is 0 the command is echoed back to the desktop with no ref
	 * included.
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
	 * parameter is the 4 char command that wasn't recognized. The data is not
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
