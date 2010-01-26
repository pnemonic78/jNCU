package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * <h1>Data commands</h1>
 * <p>
 * <h2>Sync and Selective Sync</h2>
 * <h3>Examples</h3> After the session is started (see above) these commands
 * would be sent.
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
 * <td>kDRequestToSync</td>
 * </tr>
 * <tr>
 * <td>kDGetSyncOptions</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSyncOptions</td>
 * </tr>
 * <tr>
 * <td>kDLastSyncTime</td>
 * <td>-></td>
 * <td>// This ones fake just to get the Newton time</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDCurrentTime</td>
 * </tr>
 * <tr>
 * <td>kDSetCurrentStore</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDLastSyncTime</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDCurrentTime</td>
 * </tr>
 * <tr>
 * <td colspan=4>The following would appear only if synchronising system info:</td>
 * </tr>
 * <tr>
 * <td>kDGetPatches</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPatches</td>
 * </tr>
 * <tr>
 * <td colspan=4>The following would appear only if synchronising packages:</td>
 * </tr>
 * <tr>
 * <td>kDGetPackageIDs -></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPackageIDList</td>
 * </tr>
 * <tr>
 * <td>kDBackupPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPackage</td>
 * </tr>
 * <tr>
 * <td>kDBackupPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDPackage</td>
 * </tr>
 * <tr>
 * <td>kDBackupPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td colspan=4>Note that the above only synchronises 1.x style packages on
 * locked 1.x cards. To complete the package sync the packages soup should also
 * by synchronised.</td>
 * </tr>
 * <tr>
 * <td colspan=4><br>
 * The sync would continue like this:</td>
 * </tr>
 * <tr>
 * <td>kDSetSoupGetInfo</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSoupInfo</td>
 * </tr>
 * <tr>
 * <td>kDLastSyncTime</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDCurrentTime</td>
 * </tr>
 * <tr>
 * <td>kDGetSoupIDs</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDSoupIDs</td>
 * </tr>
 * <tr>
 * <td>kdGetChangedIDs</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDChangedIDs</td>
 * </tr>
 * <tr>
 * <td>kDDeleteEntries</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDAddEntry</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDAddedID</td>
 * </tr>
 * <tr>
 * <td>kDReturnEntry</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-</td>
 * <td>kDEntry</td>
 * </tr>
 * <tr>
 * <td colspan=4>Repeat the above for each store and soup followed by:</td>
 * </tr>
 * <tr>
 * <td>kDOperationComplete</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td colspan=4><br>
 * Optionally the desktop could send this instead of the operation complete:</td>
 * </tr>
 * <tr>
 * <td>kDSyncResults</td>
 * <td>-></td>
 * </tr>
 * </table>
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
		 * This command is sent when the desktop wants to add an entry to the
		 * current soup. The entry is added with the ID specified in the data
		 * frame. If the id already exists an error is returned.
		 * <p>
		 * <em>Warning! This function should only be used during a restore operation. In other situations there's no way of knowing whether the entrie's id is unique. If an entry is added with this command and the entry isn't unique an error is returned.</em>
		 * 
		 * <pre>
		 * 'auni'
		 * length
		 * data ref
		 * </pre>
		 */
		public static final String kDAddEntryWithUniqueID = "auni";
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
		 * value. A <tt>kDResult</tt> with value 0 (or the error value if an
		 * error occurred) is sent to the desktop in response.
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
		/**
		 * This command is used to backup a soup. The result is a series of
		 * commands that includes all entries changed since the last sync time
		 * (set by a previous command), all entries with a unique ID greater
		 * than the one specified, and the unique ids of all other entries to be
		 * used to determine if any entries were deleted. The changed entries
		 * are sent with <tt>kDEntry</tt> commands. The unique ids are sent with
		 * a <tt>kDBackupIDs</tt> command. A <tt>kDBackupSoupDone</tt> command
		 * finishes the sequence. If there are any IDs > <tt>0x7FFF</tt> there
		 * could also be a <tt>kDSetBaseID</tt> command. The changed entries and
		 * unique ids are sent in unique id sequence. The Newton checks for
		 * <tt>kDOperationCanceled</tt> commands occasionally. If the soup
		 * hasn't been changed since the last backup a <tt>kDSoupNotDirty</tt>
		 * command is sent instead of the ids. A typical sequence could look
		 * like this:
		 * <table>
		 * <tr>
		 * <th>Desktop</th>
		 * <th><-></th>
		 * <th>Newton</th>
		 * </tr>
		 * <tr>
		 * <td>kDBackupSoup</td>
		 * <td>-></td>
		 * </tr>
		 * <tr>
		 * <td></td>
		 * <td><-</td>
		 * <td>kDBackupIDs</td>
		 * </tr>
		 * <tr>
		 * <td></td>
		 * <td><-</td>
		 * <td>kDEntry</td>
		 * </tr>
		 * <tr>
		 * <td></td>
		 * <td><-</td>
		 * <td>kDEntry</td>
		 * </tr>
		 * <tr>
		 * <td></td>
		 * <td><-</td>
		 * <td>kDBackupIDs</td>
		 * </tr>
		 * <tr>
		 * <td></td>
		 * <td><-</td>
		 * <td>kDEntry</td>
		 * </tr>
		 * <tr>
		 * <td></td>
		 * <td><-</td>
		 * <td>kDSetBaseID</td>
		 * </tr>
		 * <tr>
		 * <td></td>
		 * <td><-</td>
		 * <td>kDBackupIDs</td>
		 * </tr>
		 * <tr>
		 * <td></td>
		 * <td><-</td>
		 * <td>kDBackupSoupDone</td>
		 * </tr>
		 * </table>
		 * 
		 * <pre>
		 * 'bksp'
		 * length
		 * last unique id
		 * </pre>
		 */
		public static final String kDBackupSoup = "bksp";

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
		 * This command is sent from the desktop when the desktop wants to start
		 * a sync operation, when both the Newton and the desktop were waiting
		 * for the user to specify an operation.
		 * 
		 * <pre>
		 * 'ssyn'
		 * length = 0
		 * </pre>
		 */
		public static final String kDRequestToSync = "ssyn";
		/**
		 * This command is sent from the desktop when the desktop wants to start
		 * a restore operation, when both the Newton and the desktop were
		 * waiting for the user to specify an operation.
		 * 
		 * <pre>
		 * 'rrst'
		 * length = 0
		 * </pre>
		 */
		public static final String kDRequestToRestore = "rrst";
		/**
		 * This command is sent from the desktop when the desktop wants to start
		 * a load package operation, when both the Newton and the desktop were
		 * waiting for the user to specify an operation.
		 * 
		 * <pre>
		 * 'rins'
		 * length = 0
		 * </pre>
		 */
		public static final String kDRequestToInstall = "rins";

		/**
		 * This command is sent when the desktop wants to get the selective sync
		 * or selective restore info from the Newton.
		 * 
		 * <pre>
		 * 'gsyn'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetSyncOptions = "gsyn";
		/**
		 * This command can optionally be sent at the end of synchronization. If
		 * it is sent, the results are displayed on the Newton. The array looks
		 * like this: <br>
		 * <code>[["store name", restored, "soup name", count, "soup name" count],<br>
		 * &nbsp;["store name", restored, ...]]</code> <br>
		 * Restored is true if the desktop detected that the Newton had been
		 * restore to since the last sync. Count is the number of conflicting
		 * entries that were found for each soup. Soups are only in the list if
		 * they had a conflict. When a conflict is detected, the Newton version
		 * is saved and the desktop version is moved to the archive file.
		 * 
		 * <pre>
		 * 'sres'
		 * length
		 * results array
		 * </pre>
		 */
		public static final String kDSyncResults = "sres";
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
		/**
		 * This command is like <tt>kDGetIndexDescription</tt> except that it
		 * only returns the index description if it has been changed since the
		 * time set by the <tt>kDLastSyncTime</tt> command. If the index hasn't
		 * changed a <tt>kDRes</tt> with 0 is returned.
		 * 
		 * <pre>
		 * 'cidx'
		 * length
		 * </pre>
		 */
		public static final String kDGetChangedIndex = "cidx";
		/**
		 * This command is like <tt>kDGetSoupInfo</tt> except that it only
		 * returns the soup info if it has been changed since the time set by
		 * the <tt>kDLastSyncTime</tt> command. If the info hasn't changed a
		 * <tt>kDRes</tt> with 0 is returned.
		 * 
		 * <pre>
		 * 'cinf'
		 * length
		 * </pre>
		 */
		public static final String kDGetChangedInfo = "cinf";
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockingEventCommands.NewtonToDesktop {
		/**
		 * This command is sent after the user closes the slip displayed by
		 * <tt>kDImportParametersSlip</tt>. The result is a frame containing the
		 * following three slots:
		 * 
		 * <pre>
		 * {
		 *       AppList : Array of strings,  // contains the strings of the items selected
		 *                                    // in the textlist of applications
		 *       Conflicts : "string",        // Text string of labelpicker entry line
		 *       Dates : Two element array of integers // The beginning and ending
		 *                                             // dates of the selected interval
		 *                                             // expressed in minutes
		 * }
		 * </pre>
		 * 
		 * If the user cancels, the result sent is a nil ref.
		 * 
		 * <pre>
		 * 'islr'
		 * length
		 * result
		 * </pre>
		 */
		public static final String kDImportParameterSlipResult = "islr";
		/**
		 * This command sets a new base id for the ids sent with subsequent
		 * <tt>kDBackupIDs</tt> commands. The new base is a long which should be
		 * added to every id in all kDBackupIDs commands until a
		 * <tt>kDBackupSoupDone</tt> command is received.
		 * 
		 * <pre>
		 * 'base'
		 * length
		 * new base
		 * </pre>
		 */
		public static final String kDSetBaseID = "base";
		/**
		 * This command is sent in response to a <tt>kDBackupSoup</tt> command
		 * (see that command for command sequence details). The length for this
		 * command is always set to <tt>-1</tt> indicating that the length is
		 * unknown. The ids are specified as a compressed array of 16 bit
		 * numbers. Each id should be offset by any value specified by a
		 * previous <tt>kDSetBaseID</tt> command (this is how we can specify a
		 * 32 bit value in 15 bits). Each id is a number between <tt>0</tt> and
		 * <tt>0x7FFF (32767)</tt>. Negative numbers specify a count of the
		 * number of entries above the previous number before the next break
		 * (non-contiguous id). The sequence is ended by a <tt>0x8000</tt> word.
		 * So, if the Newton contains ids
		 * <tt>0, 1, 2, 3, 4, 10, 20, 21, 30, 31, 32</tt> the array would look
		 * like <tt>0, -4, 10, 20, -1, 30, -2, 0x8000</tt><br>
		 * Thus we send 8 words instead of 11 longs. Is it worth it? If there
		 * are a lot of entries it should be.
		 * 
		 * <pre>
		 * 'bids'
		 * length = -1
		 * ids
		 * </pre>
		 */
		public static final String kDBackupIDs = "bids";
		/**
		 * This command terminates the sequence of commands sent in response to
		 * a <tt>kDBackupSoup</tt> command.
		 * 
		 * <pre>
		 * 'bsdn'
		 * length
		 * </pre>
		 */
		public static final String kDBackupSoupDone = "bsdn";
		/**
		 * This command is sent in response to a <tt>kDBackupSoup</tt> command
		 * if the soup is unchanged from the last backup.
		 * 
		 * <pre>
		 * 'ndir'
		 * length
		 * </pre>
		 */
		public static final String kDSoupNotDirty = "ndir";
		/**
		 * This command is sent to the desktop when the user taps the
		 * <tt>Synchronize<tt> button on the Newton.
		 * 
		 * <pre>
		 * 'sync'
		 * length
		 * </pre>
		 */
		public static final String kDSynchronize = "sync";
		/**
		 * This command is sent whenever the user on the Newton has selected
		 * selective sync. The frame sent completely specifies which information
		 * is to be synchronised.<br>
		 * <code>
		 * {<br>
		 * &nbsp;&nbsp;packages: TRUEREF,<br>
		 * &nbsp;&nbsp;syncAll: TRUEREF,<br>
		 * &nbsp;&nbsp;stores: [{store info}, {store info}]<br>
		 * }</code><br>
		 * Each store frame in the stores array contains the same information
		 * returned by the kDStoreNames command with the addition of soup
		 * information. It looks like this: <br>
		 * <code>{<br>
		 * &nbsp;&nbsp;name: "",<br>
		 * &nbsp;&nbsp;signature: 1234,<br>
		 * &nbsp;&nbsp;totalsize: 1234,<br>
		 * &nbsp;&nbsp;usedsize: 1234,<br>
		 * &nbsp;&nbsp;kind: "",<br>
		 * &nbsp;&nbsp;soups: [soup names],<br>
		 * &nbsp;&nbsp;signatures: [soup signatures]<br>
		 * &nbsp;&nbsp;info: {store info frame},<br>
		 * }</code><br>
		 * If the user has specified to sync all information the frame will look
		 * the same except there won't be a soups slot--all soups are assumed.
		 * <p>
		 * Note that the user can specify which stores to sync while specifying
		 * that all soups should be synchronised.
		 * <p>
		 * If the user specifies that packages should be synchronised the
		 * packages flag will be true and the packages soup will be specified in
		 * the store frame(s).
		 * 
		 * <pre>
		 * 'sopt'
		 * length
		 * frame of info
		 * </pre>
		 */
		public static final String kDSyncOptions = "sopt";
	}

}
