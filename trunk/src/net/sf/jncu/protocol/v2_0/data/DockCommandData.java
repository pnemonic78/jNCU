package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * <h1>Data commands</h1>
 * <p>
 */
public class DockCommandData extends DockingEventCommands {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockingEventCommands.DesktopToNewton {
		/**
		 * Tells the Newton the version that the subsequent data is from. For
		 * example, if a 1.x data file is being restored the desktop would tell
		 * the Newton that version <tt>1</tt> data was coming. Same for
		 * importing a 1.x NTF file. Otherwise, it should indicate that 2.x data
		 * is coming. When the connection is first started the version defaults
		 * to 2.x. This information is necessary for the Newton to know whether
		 * or not it should run the conversion scripts. A <tt>kDRes</tt> command
		 * with value <tt>0</tt> is sent by the Newton in response to this
		 * command. This commands affects only data added to the Newton with
		 * <tt>kDAddEntry</tt> and <tt>kDAddEntryWithUniqueID</tt> commands. In
		 * particular, note that data returned by <tt>kDReturnEntry</tt> isn't
		 * converted if it's a different version than was set by this command.
		 * <p>
		 * <tt>manufacturer</tt> and <tt>machinetype</tt> tell the Newton the
		 * type of Newton that's the source of the data being restored. These
		 * are sent at the beginning of a connection as part of the Newton name
		 * command.
		 * 
		 * <pre>
		 * 'sver'
		 * length
		 * vers
		 * manufacturer
		 * machineType
		 * </pre>
		 */
		public static final String kDSourceVersion = "sver";
		/**
		 * This command creates a soup on the current store. It uses a
		 * registered <tt>soupdef</tt> to create the soup meaning that the
		 * indexes, etc. are determined by the ROM. If no <tt>soupdef</tt>
		 * exists for the specified soup an error is returned. A kDResult is
		 * returned.
		 * 
		 * <pre>
		 * 'cdsp'
		 * length
		 * soup name
		 * </pre>
		 */
		public static final String kDCreateDefaultSoup = "cdsp";
		/**
		 * This commands sets the signature of the current soup to the specified
		 * value. A <tt>kDResult</tt> with value <tt>0</tt> (or the error value
		 * if an error occurred) is sent to the desktop in response.
		 * 
		 * <pre>
		 * 'ssos'
		 * length
		 * new signature
		 * </pre>
		 */
		public static final String kDSetSoupSignature = "ssos";
		/**
		 * This command requests that all of the entries in a soup be returned
		 * to the desktop. The Newton responds with a series of <tt>kDEntry</tt>
		 * commands for all the entries in the current soup followed by a
		 * <tt>kDBackupSoupDone</tt> command. All of the entries are sent
		 * without any request from the desktop (in other words, a series of
		 * commands is sent). The process can be interrupted by the desktop by
		 * sending a <tt>kDOperationCanceled</tt> command. The cancel will be
		 * detected between entries. The <tt>kDEntry</tt> commands are sent
		 * exactly as if they had been requested by a <tt>kDReturnEntry</tt>
		 * command (they are long padded).
		 * 
		 * <pre>
		 * 'snds'
		 * length
		 * </pre>
		 */
		public static final String kDSendSoup = "snds";

		/** Source versions. */
		public enum eSourceVersion {
			/** Unknown version. */
			eNone,
			/** <tt>1.x</tt> data file. */
			eOnePointXData,
			/** <tt>2.x</tt> data file. */
			eTwoPointXData
		}

		/**
		 * This command is like a combination of <tt>kDSetCurrentSoup</tt> and
		 * <tt>kDGetChangedInfo</tt>. It sets the current soup -- see
		 * <tt>kDSetCurrentSoup</tt> for details. A <tt>kDSoupInfo</tt> or
		 * <tt>kDRes</tt> command is sent by the Newton in response.
		 * 
		 * <pre>
		 * 'ssgi'
		 * length
		 * soup name
		 * </pre>
		 */
		public static final String kDSetSoupGetInfo = "ssgi";
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockingEventCommands.NewtonToDesktop {
	}

}
