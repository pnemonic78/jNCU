package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

public class DockCommandData {

	public static final class DesktopToNewton extends DockingEventCommands {
		/**
		 * Tells the Newton the version that the subsequent data is from.
		 * For example, if a 1.x data file is being restored the desktop
		 * would tell the Newton that version 1 data was comming. Same for
		 * importing a 1.x NTF file. Otherwise, it should indicate that 2.x
		 * data is comming. When the connection is first started the version
		 * defaults to 2.x. This information is necessary for the Newton to
		 * know whether or not it should run the conversion scripts. A kDRes
		 * command with value 0 is sent by the Newton in response to this
		 * command. This commands affects only data added to the Newton with
		 * kDAddEntry and kDAddEntryWithUniqueID commands. In particular,
		 * note that data returned by kDReturnEntry isn't converted if it's
		 * a different version than was set by this command.
		 * <p>
		 * <tt>manufacturer</tt> and <tt>machinetype</tt> tell the Newton
		 * the type of newton that's the source of the data being restored.
		 * These are sent at the beginning of a connection as part of the
		 * Newtonname command.
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
		 * This command is sent when the PC wants to add an entry to the
		 * current soup. The entry is added with the ID specified in the
		 * data frame. If the id already exists an error is returned.
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
		 * This command returns info about the default store. This info is
		 * the same as the info returned by the kDGetStoreNames command (see
		 * kDStoreNames for details). The default store is the one used by
		 * LoadPackage.
		 * 
		 * <pre>
		 * 'gdfs'
		 * length
		 * </pre>
		 */
		public static final String kDGetDefaultStore = "gdfs";
		/**
		 * This command creates a soup on the current store. It uses a
		 * registered soupdef to create the soup meaning that the indexes,
		 * etc. are determined by the ROM. If no soupdef exists for the
		 * specified soup an error is returned. A kDResult is returned.
		 * 
		 * <pre>
		 * 'cdsp'
		 * length
		 * soup name (c string)
		 * </pre>
		 */
		public static final String kDCreateDefaultSoup = "cdsp";
		/**
		 * This command asks the Newton to send information about the
		 * applications installed on the Newton. See the kDAppNames
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
		 * This commands sets the signature of the current store to the
		 * specified value. A kDResult with value 0 (or the error value if
		 * an error occurred) is sent to the desktop in response.
		 * 
		 * <pre>
		 * 'ssig'
		 * length
		 * new signature
		 * </pre>
		 */
		public static final String kDSetStoreSignature = "ssig";
		/**
		 * This commands sets the signature of the current soup to the
		 * specified value. A kDResult with value 0 (or the error value if
		 * an error occurred) is sent to the desktop in response.
		 * 
		 * <pre>
		 * 'ssos'
		 * length
		 * new signature
		 * </pre>
		 */
		public static final String kDSetSoupSignature = "ssos";
		/**
		 * The following is a possible example of what would be displayed on
		 * the Newton following the kDImportParametersSlip command:
		 * <p>
		 * The slip will, at minimum, display 2 text string fields
		 * corresponding to the slip title and a filename. Up to 5
		 * additional fields, plus the CloseBox, could be displayed. While
		 * the slip is displayed, "helos" are sent to the desktop. When the
		 * user taps on the "Import" button or the CloseBox, a
		 * kDImportParameterSlipResult is sent to the desktop. Each of the
		 * other 5 fields is shown if the slot defining it exists in the
		 * frame parameter.
		 * <p>
		 * The frame contains the following slots used to configure the
		 * display of the slip:<br>
		 * <code>{<br>
		 * &nbsp;&nbsp;SlipTitle: "string1",        //this slot is required. Text string for slip title<br>
		 * &nbsp;&nbsp;FileName: "string2",         //this slot is required. Text of file name being imported<br>
		 * &nbsp;&nbsp;AppListInfo: {<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;Title: "string",  // Text string for title field above textlist.<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;ListItems: array of strings,  // array of strings corresponding to applications listed in textlist<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;Selected: array of indexes // array in indexes of items in the listitems array to select. e.g. [1,3] would select 1st and 3rd items<br>
		 * &nbsp;&nbsp;},<br>
		 * &nbsp;&nbsp;ConflictsInfo: {<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;Text: "string",  // Text string for labelpicker label<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;LabelCommands: array of strings        // array of strings corresponding to available choices in picker list<br> 
		 * &nbsp;&nbsp;},<br>                                                                                   
		 * &nbsp;&nbsp;DatesInfo: {<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;Title: "string1", // Text string for title field above datedurationtextpicker<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;Text: "string2",  // Text string for datedurationtextpicker label<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;StartTime: num, // start time in minutes from 1/1/04 12:00 am<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;StopTime: num // stop time in minutes from 1/1/04 12:00 am<br>
		 * &nbsp;&nbsp;},<br>
		 * &nbsp;&nbsp;ImportInfo: {<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;Title: "string", // This slot is required. Text string for button<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;ImportParametersDoneScript: function object // function object to call after button is tapped<br>
		 * &nbsp;&nbsp;}<br>
		 * }</code>
		 * 
		 * <pre>
		 * 'islp'
		 * length
		 * frame containing info to display
		 * </pre>
		 */
		public static final String kDImportParametersSlip = "islp";
		/**
		 * This command displays the password slip to let the user enter a
		 * password. The string is displayed as the title of the slip. A
		 * kDPassword command is returned.
		 * 
		 * <pre>
		 * 'gpwd'
		 * length
		 * string ref
		 * </pre>
		 */
		public static final String kDGetPassword = "gpwd";
		/**
		 * This command requests that all of the entries in a soup be
		 * returned to the desktop. The newton responds with a series of
		 * kDEntry commands for all the entries in the current soup followed
		 * by a kDBackupSoupDone command. All of the entries are sent
		 * without any request from the desktop (in other words, a series of
		 * commands is sent). The process can be interrupted by the desktop
		 * by sending a kDOperationCanceled command. The cancel will be
		 * detected between entries. The kDEntry commands are sent exactly
		 * as if they had been requested by a kDReturnEntry command (they
		 * are long padded).
		 * 
		 * <pre>
		 * 'snds'
		 * length
		 * </pre>
		 */
		public static final String kDSendSoup = "snds";
		/**
		 * This command is used to backup a soup. The result is a series of
		 * commands that includes all entries changed since the last sync
		 * time (set by a previous command), all entries with a uniqeu ID
		 * greater than the one specified, and the unique ids of all other
		 * entries to be used to determine if any entries were deleted. The
		 * changed entries are sent with kDEntry commands. The unique ids
		 * are sent with a kDBackupIDs command. A kDBackupSoupDone command
		 * finishes the sequence. If there are any IDs > 0x7FFF there could
		 * also be a kDSetBaseID command. The changed entries and unique ids
		 * are sent in unique id sequence. The newton checks for
		 * kDOperationCanceled commands occasionally. If the soup hasn't
		 * been changed since the last backup a kDSoupNotDirty command is
		 * sent instead of the ids. A typical sequence could look like this:
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
		/**
		 * This command requests that the name of the current store be set
		 * to the specified name.
		 * 
		 * <pre>
		 * 'ssna'
		 * length
		 * name ref
		 * </pre>
		 */
		public static final String kDSetStoreName = "ssna";
		/**
		 * This command controls which VBOs are sent compressed to the
		 * desktop. VBO can always be sent compressed, never compressed or
		 * only package VBOs sent compressed.
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
		 * kDGetPatches. The newton returns a kDResult of 0 (or an error if
		 * appropriate) if the patch wasn't installed. If the patch was
		 * installed the newton restarts.
		 * 
		 * <pre>
		 * 'rpat'
		 * length
		 * patch
		 * </pre>
		 */
		public static final String kDRestorePatch = "rpat";
	
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
	
		public enum eSourceVersion {
			eNone, eOnePointXData, eTwoPointXData
		}
	}

	public static final class NewtonToDesktop {
		/**
		 * This command returns a store info frame describing the default
		 * store. This frame contains the same info returned for all stores
		 * by the kDStoreNames command except that it doesn't include the
		 * store info. It contains the name, signature, total size, used
		 * size and kind.
		 * 
		 * <pre>
		 * 'dfst'
		 * length
		 * store frame
		 * </pre>
		 */
		public static final String kDDefaultStore = "dfst";
		/**
		 * This command is sent after the user closes the slip displayed by
		 * kDImportParametersSlip. The result is a frame containing the
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
		 * kDBackupIDs commands. The new base is a long which should be
		 * added to every id in all kDBackupIDs commands until a
		 * kDBackupSoupDone command is received.
		 * 
		 * <pre>
		 * 'base'
		 * length
		 * new base
		 * </pre>
		 */
		public static final String kDSetBaseID = "base";
		/**
		 * This command is sent in response to a kDBackupSoup command--see
		 * that command for command sequence details. The length for this
		 * command is always set to -1 indicating that the length is
		 * unknown. The ids are specified as a compressed array of 16 bit
		 * numbers. Each id should be offset by any value specified by a
		 * previous kDSetBaseID command (this is how we can specify a 32 bit
		 * value in 15 bits). Each id is a number between 0 and 0x7FFF
		 * (32767). Negative numbers specify a count of the number of
		 * entries above the previous number before the next break
		 * (non-contiguous id). The sequence is ended by a 0x8000 word. So,
		 * if the newton contains ids
		 * <tt>0, 1, 2, 3, 4, 10, 20, 21, 30, 31, 32</tt> the array would
		 * look like <tt>0, -4, 10, 20, -1, 30, -2, 0x8000</tt><br>
		 * Thus we send 8 words instead of 11 longs. Is it worth it? If
		 * there are a lot of entries it should be.
		 * 
		 * <pre>
		 * 'bids'
		 * length = -1
		 * ids
		 * </pre>
		 */
		public static final String kDBackupIDs = "bids";
		/**
		 * This command terminates the sequence of commands sent in response
		 * to a kDBackupSoup command.
		 * 
		 * <pre>
		 * 'bsdn'
		 * length
		 * </pre>
		 */
		public static final String kDBackupSoupDone = "bsdn";
		/**
		 * This command is sent in response to a kDBackupSoup command if the
		 * soup is unchanged from the last backup.
		 * 
		 * <pre>
		 * 'ndir'
		 * length
		 * </pre>
		 */
		public static final String kDSoupNotDirty = "ndir";
		/**
		 * This command is sent to the desktop when the user taps the
		 * Synchronize button on the newton.
		 * 
		 * <pre>
		 * 'sync'
		 * length
		 * </pre>
		 */
		public static final String kDSynchronize = "sync";
	}

}
