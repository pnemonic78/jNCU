package net.sf.jncu.protocol.v1_0;

/**
 * Newton Docking Protocol event commands for 1.0 Newton ROM Extensions.<br>
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
 * <p>
 * 
 * Examples
 * 
 * Every session starts like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDRequestToDock</td>
 * </tr>
 * <tr>
 * <td>kDInitiateDocking</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDNewtonName</td>
 * </tr>
 * <tr>
 * <td>kDSetTimeout</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * </table>
 * No matter what the intent of the desktop or the Newton, these commands must
 * always start a session (the desktop can substitute a kDResult for the
 * kDSetTimeout if it doesn't want to set the timeout).
 * <p>
 * A typical synchronize session might continue like this:
 * 
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDGetStoreNames</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDStoreNames</td>
 * </tr>
 * <tr>
 * <td>kDLastSyncTime</td>
 * <td>-></td>
 * <td>// This ones fake just to get the Newton time</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDCurrentTime</td>
 * </tr>
 * <tr>
 * <td>kDSetCurrentStore</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDLastSyncTIme</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDCurrentTime</td>
 * </tr>
 * <tr>
 * <td>kDGetPatches</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDPatches</td>
 * </tr>
 * <tr>
 * <td>kDGetPackageIDs</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDPackageIDList</td>
 * </tr>
 * <tr>
 * <td>kDBackupPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDPackage</td>
 * </tr>
 * <tr>
 * <td>kDBackupPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDPackage</td>
 * </tr>
 * <tr>
 * <td>kDBackupPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDGetSoupNames</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDSoupNames</td>
 * </tr>
 * <tr>
 * <td>kDGetInheritance</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDInheritance</td>
 * </tr>
 * <tr>
 * <td>kDSetCurrentSoup</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDGetSoupInfo</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDSoupInfo</td>
 * </tr>
 * <tr>
 * <td>kDGetSoupIDs</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDSoupIDs</td>
 * </tr>
 * <tr>
 * <td>kdGetChangedIDs</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDChangedIDs</td>
 * </tr>
 * <tr>
 * <td>kDDeleteEntries</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDAddEntry</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDAddedID</td>
 * </tr>
 * <tr>
 * <td>kDReturnEntry</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDEntry</td>
 * </tr>
 * <tr>
 * <td>kDDisconnect</td>
 * <td>-></td>
 * </tr>
 * </table>
 * <p>
 * A restore session would look like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDGetStoreNames</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDStoreNames</td>
 * </tr>
 * <tr>
 * <td>kDSetCurrentStore</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDDeleteAllPackages</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDGetSoupNames</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDSoupNames</td>
 * </tr>
 * <tr>
 * <td>kDSetCurrentSoup</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDEmptySoup</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDAddEntry</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDDeletePkgDir</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDLoadPackage</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDDisconnect</td>
 * <td>-></td>
 * </tr>
 * </table>
 * <p>
 * A load package session would look like this:
 * <table>
 * <tr>
 * <th>Desktop</th>
 * <th><-></th>
 * <th>Newton</th>
 * </tr>
 * <tr>
 * <td>kDLoadPackage</td>
 * <td>-></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><-
 * <td>kDResult</td>
 * </tr>
 * <tr>
 * <td>kDDisconnect</td>
 * <td>-></td>
 * </tr>
 * </table>
 */
public class DockingEvenCommands {

	public static final class DesktopToNewton {
		/**
		 * data = session type
		 * 
		 * <pre></pre>
		 */
		public static final String kDInitiateDocking = "dock";
		/**
		 * The time of the last sync
		 * 
		 * <pre></pre>
		 */
		public static final String kDLastSyncTIme = "stme";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDGetStoreNames = "gsto";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDGetSoupNames = "gets";
		/**
		 * data = store frane
		 * 
		 * <pre></pre>
		 */
		public static final String kDSetCurrentStore = "ssto";
		/**
		 * data = soup name
		 * 
		 * <pre></pre>
		 */
		public static final String kDSetCurrentSoup = "ssou";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDGetSoupIDs = "gids";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kdGetChangedIDs = "gcid";
		/**
		 * data = list of IDs
		 * 
		 * <pre></pre>
		 */
		public static final String kDDeleteEntries = "dele";
		/**
		 * data = flattened entry
		 * 
		 * <pre></pre>
		 */
		public static final String kDAddEntry = "adde";
		/**
		 * data = ID to return
		 * 
		 * <pre></pre>
		 */
		public static final String kDReturnEntry = "rete";
		/**
		 * data = ID to return
		 * 
		 * <pre></pre>
		 */
		public static final String kDReturnChangedEntry = "rcen";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDEmptySoup = "esou";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDDeleteSoup = "dsou";
		/**
		 * data = package
		 * 
		 * <pre></pre>
		 */
		public static final String kDLoadPackage = "lpkg";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDGetPackageIDs = "gpid";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDBackupPackages = "bpkg";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDDisconnect = "disc";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDDeleteAllPackages = "dpkg";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDGetIndexDescription = "gind";
		/**
		 * data = name + index description
		 * 
		 * <pre></pre>
		 */
		public static final String kDCreateSoup = "csop";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDGetInheritance = "ginh";
		/**
		 * This command sets the timeout for the connection (the time the Newton
		 * will wait to receive data for it disconnects). This time is usually
		 * set to 30 seconds.
		 * 
		 * <pre>
		 * 'stim' 
		 * length
		 * timeout in seconds
		 * </pre>
		 */
		public static final String kDSetTimeout = "stim";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDGetPatches = "gpat";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDDeletePkgDir = "dpkd";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDGetSoupInfo = "gsin";
	}

	public static final class NewtonToDesktop {
		/**
		 * Ask PC to start docking process.<br>
		 * This command is sent to a docker that the junior wishes to connect
		 * with (on the network, serial, etc.). The Newt expects a
		 * kDInitiateDocking command in response. The protocol version is the
		 * version of the messaging protocol that's being used.
		 * 
		 * <pre>
		 * 'rtdk'
		 * length
		 * protocol version
		 * </pre>
		 */
		public static final String kDRequestToDock = "rtdk";
		/**
		 * The name of the Newton.<br>
		 * This command is sent in response to a correct kDInitiateDocking
		 * command from the docker. The Newton's name is used to locate the
		 * proper synchronize file. The version info includes things like
		 * machine type (e.g. J1), ROM version, etc.
		 * 
		 * <pre>
		 * 'name'
		 * length
		 * version info
		 * name
		 * </pre>
		 */
		public static final String kDNewtonName = "name";
		/**
		 * The current time on the Newton.
		 * 
		 * <pre></pre>
		 */
		public static final String kDCurrentTime = "time";
		/**
		 * This command is sent in response to a kDGetStoreNames command. It
		 * returns information about all the stores on the Newton. Each array
		 * slot contains the following information about a store:<br>
		 * <code>
{<br>
&nbsp;&nbsp;name: "",<br>
&nbsp;&nbsp;signature: 1234,<br>
&nbsp;&nbsp;totalsize: 1234,<br>
&nbsp;&nbsp;usedsize: 1234,<br>
&nbsp;&nbsp;kind: "",<br>
&nbsp;&nbsp;info: {store info frame},<br>
&nbsp;&nbsp;readOnly: true,<br>
&nbsp;&nbsp;defaultStore: true,		// only for the default store<br>
&nbsp;&nbsp;storePassword: password  // only if a store password has been set<br>
}
</code>
		 * 
		 * <pre>
		 * 'stor'
		 * length
		 * array of frames
		 * </pre>
		 */
		public static final String kDStoreNames = "stor";
		/**
		 * This command is sent in response to a kDGetSoupNames command. It
		 * returns the names and signatures of all the soups in the current
		 * store.
		 * 
		 * <pre>
		 * 'soup'
		 * length
		 * array of string names
		 * array of corresponding soup signatures
		 * </pre>
		 */
		public static final String kDSoupNames = "soup";
		/**
		 * This command is sent in response to a kDGetSoupIDs command. It
		 * returns all the IDs from the current soup.
		 * 
		 * <pre>
		 * 'sids'
		 * length
		 * count
		 * array of ids for the soup
		 * </pre>
		 */
		public static final String kDSoupIDs = "sids";
		/**
		 * This command is sent in response to a kDGetChangedIDs command. It
		 * returns all the ids with mod time > the last sync time. If the last
		 * sync time is 0, no changed entries are returned (this would happen on
		 * the first sync).
		 * 
		 * <pre>
		 * 'cids'
		 * length
		 * count
		 * array of ids for the soup
		 * </pre>
		 */
		public static final String kDChangedIDs = "cids";
		/**
		 * This command is sent in response to any of the commands from the PC
		 * that don't request data. It lets the PC know that things are still
		 * proceeding OK.
		 * 
		 * <pre>
		 * 'dres'
		 * length
		 * error code
		 * </pre>
		 */
		public static final String kDResult = "dres";
		/**
		 * This command is sent in response to a kDAddEntry command from the PC.
		 * It returns the ID that the entry was given when it was added to the
		 * current soup.
		 * 
		 * <pre>
		 * 'adid'
		 * length
		 * id
		 * </pre>
		 */
		public static final String kDAddedID = "adid";
		/**
		 * This command is sent in response to a KDReturnEntry command. The
		 * entry in the current soup specified by the ID in the KDReturnEntry
		 * command is returned.
		 * 
		 * <pre>
		 * 'entr'
		 * length
		 * entry  // binary data
		 * </pre>
		 */
		public static final String kDEntry = "entr";
		/**
		 * This command sends a list of package frames to the desktop. For each
		 * package the information sent is this:
		 * <ol>
		 * <li>ULong packageSize;
		 * <li>ULong packageId;
		 * <li>ULong packageVersion;
		 * <li>ULong format;
		 * <li>ULong deviceKind;
		 * <li>ULong deviceNumber;
		 * <li>ULong deviceId;
		 * <li>ULong modifyDate;
		 * <li>ULong isCopyProtected;
		 * <li>ULong length;
		 * <li>Character name; (length bytes of Unicode string)
		 * </ol>
		 * Note that this is not sent as an array! It's sent as binary data.
		 * Note that this finds packages only in the current store.
		 * 
		 * <pre>
		 * 'pids'
		 * length
		 * count
		 * package info
		 * </pre>
		 */
		public static final String kDPackageIDList = "pids";
		/**
		 * This command sends a package to the pc. It's issued repeatedly in
		 * response to a kDBackupPackages message.
		 * 
		 * <pre>
		 * 'apkg'
		 * length
		 * package id
		 * package data
		 * </pre>
		 */
		public static final String kDPackage = "apkg";
		/**
		 * This command specifies the indexes that should be created for the
		 * current soup.
		 * 
		 * <pre>
		 * 'didx'
		 * length
		 * indexes
		 * </pre>
		 */
		public static final String kDIndexDescription = "indx";
		/**
		 * data = array of class, supperclass pairs
		 * 
		 * <pre></pre>
		 */
		public static final String kDInheritance = "dinh";
		/**
		 * no data
		 * 
		 * <pre></pre>
		 */
		public static final String kDPatches = "patc";
	}

	/**
	 * data = entry being returned
	 * 
	 * <pre></pre>
	 */
	public static final String kDChangedEntry = "cent";
	/**
	 * variable length data
	 * 
	 * <pre></pre>
	 */
	public static final String kDTest = "test";
	/**
	 * no data
	 * 
	 * <pre></pre>
	 */
	public static final String kDHello = "helo";
	/**
	 * data = soup info frame
	 * 
	 * <pre></pre>
	 */
	public static final String kDSoupInfo = "sinf";

}
