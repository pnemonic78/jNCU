package net.sf.jncu.protocol.v2_0;

/**
 * Newton Docking Protocol event commands for 2.0 Newton ROM Extensions.<br>
 * Newton communicates with the desktop by exchanging Newton event commands.
 * <p>
 * In the commands below, all data is padded with nulls to 4 byte boundaries.
 * The length associated with each command is the length (in bytes) of the data
 * following the length field. All strings are C strings unless otherwise
 * specified.
 * <p>
 * All commands begin with the sequence -- '<tt>newt</tt>', '<tt>dock</tt>'. <br>
 * Newton communicates with the desktop by exchanging newton event commands. The
 * general command structure looks like this:
 * 
 * <pre>
 * 'newt'
 * 'dock'
 * 'aaaa'   // the specific command
 * length   // the length of the following command
 * data     // data, if any
 * </pre>
 * 
 * @author moshew
 */
public class DockingEventCommands {

	/**
	 * All of the commands in this section are based on the NewtonScript query
	 * functions. Please see the Newton Programmer's Guide for details about the
	 * functions performed by the commands. The query command returns a long
	 * representing the queries cursor. Each of the other commands take this
	 * cursor as a parameter. Entries are returned with the kDEntry command.
	 */
	public static final class RemoteQuery {

		public static final class DesktopToNewton {
			/**
			 * The parameter frame must contain a queryspec slot and may contain
			 * a soupname slot. Performs the specified query on the current
			 * store. The query spec is a full query spec including valid test,
			 * etc. functions. Soup name is a string that's used to find a soup
			 * in the current store to query. If the soup name is an empty
			 * string or a NILREF the query is done on the current soup. A
			 * kDLongData is returned with a cursor ID that should be used with
			 * the rest of the remote query commands.
			 * 
			 * <pre>
			 * 'qury'
			 * length
			 * parameter frame
			 * </pre>
			 */
			public static final String kDQuery = "qury";
			/**
			 * The entry at the specified key location is returned. Nil is
			 * returned if there is no entry with the specified key.
			 * 
			 * <pre>
			 * 'goto'
			 * length
			 * cursor id
			 * key
			 * </pre>
			 */
			public static final String kDCursorGotoKey = "goto";
			/**
			 * Applies the specified function to each of the cursor's entries in
			 * turn and returns an array of the results. A kDRefResult is
			 * returned. See MapCursor in NPG.
			 * 
			 * <pre>
			 * 'cmap'
			 * length
			 * cursor id
			 * function
			 * </pre>
			 */
			public static final String kDCursorMap = "cmap";
			/**
			 * Returns the entry at the current cursor.
			 * 
			 * <pre>
			 * 'crsr'
			 * length
			 * cursor id
			 * </pre>
			 */
			public static final String kDCursorEntry = "crsr";
			/**
			 * Moves the cursor forward count entries from its current position
			 * and returns that entry. Returns nil if the cursor is moved past
			 * the last entry.
			 * 
			 * <pre>
			 * 'move'
			 * length
			 * cursor id
			 * count
			 * </pre>
			 */
			public static final String kDCursorMove = "move";
			/**
			 * Moves the cursor to the next entry in the set of entries
			 * referenced by the cursor and returns the entry. Returns nil if
			 * the cursor is moved past the last entry.
			 * 
			 * <pre>
			 * 'next'
			 * length
			 * cursor id
			 * </pre>
			 */
			public static final String kDCursorNext = "next";
			/**
			 * Moves the cursor to the previous entry in te set of entries
			 * referenced by the cursor and returns the entry. If the cursor is
			 * moved before the first entry nil is returned.
			 * 
			 * <pre>
			 * 'prev'
			 * length
			 * cursor id
			 * </pre>
			 */
			public static final String kDCursorPrev = "prev";
			/**
			 * Resets the cursor to its initial state. A kDRes of 0 is returned.
			 * 
			 * <pre>
			 * 'rset'
			 * length
			 * cursor id
			 * </pre>
			 */
			public static final String kDCursorReset = "rset";
			/**
			 * Resets the cursor to the rightmost entry in the valid subset. A
			 * kDRes of 0 is returned.
			 * 
			 * <pre>
			 * 'rend'
			 * length
			 * cursor id
			 * </pre>
			 */
			public static final String kDCursorResetToEnd = "rend";
			/**
			 * Returns the count of the entries matching the query
			 * specification. A kDLongData is returned.
			 * 
			 * <pre>
			 * 'cnt '
			 * length
			 * cursor id
			 * </pre>
			 */
			public static final String kDCursorCountEntries = "cnt ";
			/**
			 * Returns kDLongData with a 0 for unknown, 1 for start and 2 for
			 * end.
			 * 
			 * <pre>
			 * 'whch'
			 * length
			 * cursor id
			 * </pre>
			 */
			public static final String kDCursorWhichEnd = "whch";
			/**
			 * Disposes the cursor and returns a kDRes with a 0 or error.
			 * 
			 * <pre>
			 * 'cfre'
			 * length
			 * cursor id
			 * </pre>
			 */
			public static final String kDCursorFree = "cfre";
		}

		public static final class NewtonToDesktop {
			/**
			 * Newton returns a long value. The interpretation of the data
			 * depends on the command which prompted the return of the long
			 * value.
			 * 
			 * <pre>
			 * 'ldta'
			 * length
			 * data
			 * </pre>
			 */
			public static final String kDLongData = "ldta";
			/**
			 * <pre>
			 * </pre>
			 */
			public static final String kDRefResult = "ref ";
		}
	}

	/**
	 * Desktop initiated keyboard passthrough would look like this:
	 * <table>
	 * <tr>
	 * <th>Desktop</th>
	 * <th><-></th>
	 * <th>Newton</th>
	 * </tr>
	 * <tr>
	 * <td>kDStartKeyboardPassthrough</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDStartKeyboardPassthrough</td>
	 * </tr>
	 * <tr>
	 * <td>kDKeyboardString</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td>kDKeyboardString</td>
	 * <td>-></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>kDOperationCanceled</td>
	 * <td>-></td>
	 * <td>( from either side)</td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDOpCanceledAck</td>
	 * </tr>
	 * </table>
	 * <br>
	 * Newton initiated keyboard passthrough would look like this:
	 * <table>
	 * <tr>
	 * <th>Desktop</th>
	 * <th><-></th>
	 * <th>Newton</th>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDStartKeyboardPassthrough</td>
	 * </tr>
	 * <tr>
	 * <td>kDKeyboardString</td>
	 * <td>-></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>kDKeyboardString</td>
	 * <td>-></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDOperationCanceled</td>
	 * </tr>
	 * <tr>
	 * <td>kDOpCanceledAck</td>
	 * <td>-></td>
	 * <td></td>
	 * </tr>
	 * </table>
	 */
	public static final class Keyboard {

		public static final class DesktopToNewton {
			/**
			 * This command sends 1 character to the Newton for processing. The
			 * char is a 2 byte unicode character + a 2 byte state. The state is
			 * defined as follows:
			 * <ol>
			 * <li>Bit 1 = command key down
			 * </ol>
			 * 
			 * <pre>
			 * 'kbdc'
			 * length
			 * char
			 * state
			 * </pre>
			 */
			public static final String kDKeyboardChar = "kbdc";
			/**
			 * This command sends a string of characters to the newton for
			 * processing. The characters are 2 byte unicode characters. If
			 * there are an odd number of characters the command should be
			 * padded, as usual.
			 * 
			 * <pre>
			 * 'kbds'
			 * length
			 * "string"
			 * </pre>
			 */
			public static final String kDKeyboardString = "kbds";
		}

		/**
		 * This command is sent to enter keyboard passthrough mode. It can be
		 * followed by kDKeyboardChar, kDKeyboardString, kDHello and
		 * kDOperationComplete commands.
		 * 
		 * <pre>
		 * 'kybd'
		 * length
		 * </pre>
		 */
		public static final String kDStartKeyboardPassthrough = "kybd";
	}

	public static final class Application {

		public static final class NewtonToDesktop {
			/**
			 * This command returns the names of the applications present on the
			 * newton. It also, optionally, returns the names of the soups
			 * associated with each application. The array looks like this:
			 * <code>[{name: "app name", soups: ["soup1", "soup2"]},<br/>
			 * &nbsp;{name: "another app name", ...}, ...]</code>
			 * <p>
			 * Some built-in names are included. "System information" includes
			 * the system and directory soups. If there are packages installed,
			 * a "Packages" item is listed. If a card is present and has a
			 * backup there will be a "Card backup" item. If there are soups
			 * that don't have an associated application (or whose application I
			 * can't figure out) there's an "Other information" entry.
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
			 * This command is sent in response to a kDGetPackageInfo command.
			 * An array is returned that contains a frame for each package with
			 * the specified name (there may be more than one package with the
			 * same name but different package id). The returned frame looks
			 * like this:<br>
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
			 * same title but different package ids. Note that this finds
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
			 * This command asks the newton to call the specified root method.
			 * The return value from the method is sent to the desktop with a
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
			 * extension lasts for the length of the current connection (in
			 * other words, you have to install the extension every time you
			 * connect). The function is a newton script closure that would have
			 * to be compiled on the desktop. See the Dante Connection (ROM) API
			 * IU document for details. A kDResult with value 0 (or the error
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
		}
	}

	public static final class Data {

		public static final class DesktopToNewton {
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

	public static final class Operation {

		public static final class DesktopToNewton {
			/**
			 * Reports a desktop error to the Newton. The string is included
			 * since the Newton doesn't know how to decode all the desktop
			 * errors (especially since the Mac and Windows errors are
			 * different). ErrorString is a ref.
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
		 * This command is sent when an operation is completed. It't only sent
		 * in situations where there might be some ambiguity. Currently, there
		 * are two situations where this is sent. When the desktop finishes a
		 * restore it sends this command. When a sync is finished and there are
		 * no sync results (conflicts) to send to the newton the desktop sends
		 * this command.
		 * 
		 * <pre>
		 * 'opdn'
		 * length = 0
		 * </pre>
		 */
		public static final String kDOperationDone = "opdn";
		/**
		 * This command is sent when the user cancels an operation. Usually no
		 * action is required on the receivers part exept to return to the
		 * "ready" state.
		 * 
		 * <pre>
		 * 'opcn'
		 * length = 0
		 * </pre>
		 */
		public static final String kDOperationCanceled = "opcn";
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
		 * This command is first sent from the desktop to the Newton. The newton
		 * immediately echos the object back to the desktop. The object can be
		 * any newtonscript object (anything that can be sent through the object
		 * read/write). This command can also be sent with no ref attached. If
		 * the length is 0 the command is echoed back to the desktop with no ref
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
		 * This command is sent when a message is received that is unknown. When
		 * the desktop receives this command it can either install a protocol
		 * extension and try again or return an error to the Newton. If the
		 * built-in Newton code receives this command it always signals an
		 * error. The bad command parameter is the 4 char command that wasn't
		 * recognized. The data is not returned.
		 * 
		 * <pre>
		 * 'unkn'
		 * length
		 * bad command
		 * </pre>
		 */
		public static final String kDUnknownCommand = "unkn";
	}

	/**
	 * Examples: When a 2.0 ROM Newton is communicating with a 3.0 Connection
	 * the session would start like this:
	 * <table>
	 * <tr>
	 * <th>Desktop</th>
	 * <th><-></th>
	 * <th>Newton</th>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDRequestToDock</td>
	 * </tr>
	 * <tr>
	 * <td>kDInitiateDocking</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDNewtonName</td>
	 * </tr>
	 * <tr>
	 * <td>kDDesktopInfo</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDNewtonInfo</td>
	 * </tr>
	 * <tr>
	 * <td>kDWhichIcons</td>
	 * <td>-></td>
	 * <td>// optional</td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDResult</td>
	 * </tr>
	 * <tr>
	 * <td>kDSetTimeout</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDPassword</td>
	 * </tr>
	 * <tr>
	 * <td>kDPassword</td>
	 * <td>-></td>
	 * </tr>
	 * </table>
	 * <br>
	 * If the password sent from the Newton is wrong it would look like this
	 * instead. The password exchange can occurr up to 3 times before the
	 * desktop gives up.
	 * <table>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDPassword</td>
	 * </tr>
	 * <tr>
	 * <td>kDPWWrong</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDPassword</td>
	 * </tr>
	 * <tr>
	 * <td>kDPWWrong</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDPassword</td>
	 * </tr>
	 * <tr>
	 * <td>kDPassword</td>
	 * <td>-></td>
	 * </tr>
	 * </table>
	 * <br>
	 * If the password sent from the Desktop is wrong the Newton signals an
	 * error immediately.
	 * <table>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDPassword</td>
	 * </tr>
	 * <tr>
	 * <td>kDPassword</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDResult</td>
	 * </tr>
	 * </table>
	 * <br>
	 * If the desktop decides that the Newton has had enough guesses a kDResult
	 * can be sent instead of a kDPWWrong. A kDBadPassword error should be
	 * specified. No matter what the intent of the desktop or the Newton, these
	 * commands must always start a session (the desktop can substitute a
	 * kDResult for the kDSetTimeout if it doesn't want to set the timeout). If
	 * no password has been specied, the key is returned unencrypted. When a 2.0
	 * ROM Newton is communicating with NPI 1.0, NTK 1.0 or 2.0 Connection the
	 * session would look like this:
	 * <table>
	 * <tr>
	 * <th>Desktop</th>
	 * <th><-></th>
	 * <th>Newton</th>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><- kDRequestToDock</td>
	 * </tr>
	 * <tr>
	 * <td>kDInitiateDocking -></td>
	 * <td>// session type must be loadpackage</td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDNewtonName</td>
	 * </tr>
	 * <tr>
	 * <td>kDSetTimeout</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDResult</td>
	 * </tr>
	 * <tr>
	 * <td>kDLoadPackage</td>
	 * <td>-></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td><-</td>
	 * <td>kDResult</td>
	 * </tr>
	 * <tr>
	 * <td>kDDisconnect</td>
	 * <td>-></td>
	 * </tr>
	 * </table>
	 * <br>
	 */
	public static final class Session {

		public static final String kDNewton = "newt";

		public static final class DesktopToNewton {
			/**
			 * Ask Newton to start docking process. <br>
			 * This command should be sent to the Newton in response to a
			 * kDRequestToDock command. Session type should be 4 to load a
			 * package.
			 * 
			 * <pre>
			 * 'dock'
			 * length = 4
			 * session type = 4
			 * </pre>
			 */
			public static final String kDInitiateDocking = "dock";
			/** Define which icons are shown. */
			public static final String kDWhichIcons = "wicn";
			/**
			 * This command is sent to the desktop after the connection is
			 * established using AppleTalk, serial, etc. (when the user taps the
			 * connect button). The protocol version is the version of the
			 * messaging protocol that's being used and should always be set to
			 * the number 9 for the version of the protocol defined here.
			 * 
			 * <pre>
			 * 'rtdk'
			 * length = 4
			 * protocol version = 9
			 * </pre>
			 */
			public static final String kDRequestToDock = "rtdk";
		}

		public static final class NewtonToDesktop {
			/**
			 * <pre>
			 * 'pass'
			 * data = encrypted key
			 * </pre>
			 */
			public static final String kDPassword = "pass";
			/**
			 * <pre>
			 * 'name'
			 * length
			 * version info
			 * name
			 * </pre>
			 */
			public static final String kDNewtonName = "name";
			public static final String kDNewtonInfo = "ninf";
		}
	}
}
