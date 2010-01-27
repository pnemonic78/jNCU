package net.sf.jncu.protocol.v2_0.data;

public class DockCommandBackup extends DockCommandData {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockCommandData.DesktopToNewton {
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
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockCommandData.NewtonToDesktop {
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
		 * if the soup is unchanged from the last backup.
		 * 
		 * <pre>
		 * 'ndir'
		 * length
		 * </pre>
		 */
		public static final String kDSoupNotDirty = "ndir";
	}

}
