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
		 * data = # of seconds
		 * 
		 * <pre></pre>
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
		 * Ask PC to start docking process.
		 * 
		 * <pre></pre>
		 */
		public static final String kDRequestToDock = "rtdk";
		/**
		 * The name of the Newton.
		 * 
		 * <pre></pre>
		 */
		public static final String kDNewtonName = "name";
		/**
		 * The current time on the Newton.
		 * 
		 * <pre></pre>
		 */
		public static final String kDCurrentTime = "time";
		/**
		 * data = array of store names & signatures
		 * 
		 * <pre></pre>
		 */
		public static final String kDStoreNames = "stor";
		/**
		 * data = array of soup names & signatures
		 * 
		 * <pre></pre>
		 */
		public static final String kDSoupNames = "soup";
		/**
		 * data = array of ids for the soup
		 * 
		 * <pre></pre>
		 */
		public static final String kDSoupIDs = "sids";
		/**
		 * data = array of ids
		 * 
		 * <pre></pre>
		 */
		public static final String kDChangedIDs = "cids";
		/**
		 * data = command & result (error)
		 * 
		 * <pre></pre>
		 */
		public static final String kDResult = "dres";
		/**
		 * data = the id of the added entry
		 * 
		 * <pre></pre>
		 */
		public static final String kDAddedID = "adid";
		/**
		 * data = entry being returned
		 * 
		 * <pre></pre>
		 */
		public static final String kDEntry = "entr";
		/**
		 * data = list of package ids
		 * 
		 * <pre></pre>
		 */
		public static final String kDPackageIDList = "pids";
		/**
		 * data = package
		 * 
		 * <pre></pre>
		 */
		public static final String kDPackage = "apkg";
		/**
		 * data = index description array
		 * 
		 * <pre></pre>
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
