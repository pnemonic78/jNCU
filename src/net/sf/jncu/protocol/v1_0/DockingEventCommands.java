package net.sf.jncu.protocol.v1_0;

public class DockingEventCommands {

	public static class DesktopToNewton {
		/**
		 * This command is sent to the newt in response to a
		 * <tt>kDRequestToDock</tt> command. Session type can be one of
		 * 
		 * <tt>{none, settingUp, synchronize, restore, loadPackage, testComm, loadPatch, updatingStores}</tt>
		 * .
		 * 
		 * <pre>
		 * 'dock'
		 * length
		 * session type
		 * </pre>
		 */
		public static final String kDInitiateDocking = "dock";
		/**
		 * The time of the last sync.
		 * 
		 * <pre>
		 * 'stme'
		 * </pre>
		 */
		public static final String kDLastSyncTIme = "stme";
		/**
		 * This command is sent when a list of store names is needed.
		 * 
		 * <pre>
		 * 'gsto'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetStoreNames = "gsto";
		/**
		 * This command is sent when a list of soup names is needed. It expects
		 * to receive a <tt>kDSoupNames</tt> command in response.
		 * 
		 * <pre>
		 * 'gets'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetSoupNames = "gets";
		/**
		 * This command sets the current store on the Newton. A store frame is
		 * sent to uniquely identify the store to be set: <br>
		 * <code>
		 * {<br>&nbsp;&nbsp;name: "foo",<br>
		 * &nbsp;&nbsp;kind: "bar",<br>
		 * &nbsp;&nbsp;signature: 1234,<br>
		 * &nbsp;&nbsp;info: {&lt;info frame&gt;}		// This one is optional<br>
		 * }</code>
		 * 
		 * <pre>
		 * 'ssto'
		 * length
		 * store frame
		 * </pre>
		 */
		public static final String kDSetCurrentStore = "ssto";
		/**
		 * This command sets the current soup. Most of the other commands
		 * pertain to this soup so this command must precede any command that
		 * uses the current soup. If the soup doesn't exist a
		 * <tt>kDSoupNotFound</tt> error is returned but the connection is left
		 * alive so the desktop can create the soup if necessary. Soup names
		 * must be < 25 characters.
		 * 
		 * <pre>
		 * 'ssou'
		 * length
		 * soup name
		 * </pre>
		 */
		public static final String kDSetCurrentSoup = "ssou";
		/**
		 * This command is sent to request a list of entry IDs for the current
		 * soup. It expects to receive a <tt>kDSoupIDs</tt> command in response.
		 * 
		 * <pre>
		 * 'gids'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetSoupIDs = "gids";
		/**
		 * This command is sent to request a list of changed IDs for the current
		 * soup. It expects to receive a <tt>kDChangedIDs</tt> command in
		 * response.
		 * 
		 * <pre>
		 * 'gids'
		 * length = 0
		 * </pre>
		 */
		public static final String kdGetChangedIDs = "gcid";
		/**
		 * This command is sent to delete one or more entries from the current
		 * soup.
		 * 
		 * <pre>
		 * 'dele'
		 * length
		 * count
		 * array of ids
		 * </pre>
		 */
		public static final String kDDeleteEntries = "dele";
		/**
		 * This command is sent when the desktop wants to add an entry to the
		 * current soup.
		 * 
		 * <pre>
		 * 'adde'
		 * length
		 * entry ref
		 * </pre>
		 */
		public static final String kDAddEntry = "adde";
		/**
		 * This command is sent when the desktop wants to retrieve an entry from
		 * the current soup.
		 * 
		 * <pre>
		 * 'rete'
		 * length
		 * id
		 * </pre>
		 */
		public static final String kDReturnEntry = "rete";
		/**
		 * This command is sent when the desktop wants to retrieve a changed
		 * entry from the current soup.
		 * 
		 * <pre>
		 * 'rcen'
		 * length
		 * id
		 * </pre>
		 */
		public static final String kDReturnChangedEntry = "rcen";
		/**
		 * This command is used by restore to remove all entries from a soup
		 * before the soup data is restored.
		 * 
		 * <pre>
		 * 'dsou'
		 * length
		 * soup name
		 * </pre>
		 */
		public static final String kDEmptySoup = "esou";
		/**
		 * This command is used by restore to delete a soup if it exists on the
		 * Newton but not on the desktop.
		 * 
		 * <pre>
		 * 'dsou'
		 * length
		 * soup name
		 * </pre>
		 */
		public static final String kDDeleteSoup = "dsou";
		/**
		 * This command will load a package into the Newton's RAM. The package
		 * data should be padded to an even multiple of 4 by adding zero bytes
		 * to the end of the package data.
		 * 
		 * <pre>
		 * 'lpkg'
		 * length
		 * package data
		 * </pre>
		 */
		public static final String kDLoadPackage = "lpkg";
		/**
		 * This command is sent to request a list of package ids. This list is
		 * used to remove any packages from the desktop that have been deleted
		 * on the Newt.
		 * 
		 * <pre>
		 * 'gpid'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetPackageIDs = "gpid";
		/**
		 * This command is sent to backup any packages that are installed on the
		 * Newton. It expects a <tt>kDPackage</tt> command or a kDResponse with
		 * an error of <tt>0</tt> (to indicate that there are no more packages)
		 * in response.
		 * 
		 * <pre>
		 * 'bpkg'
		 * length = 0
		 * </pre>
		 */
		public static final String kDBackupPackages = "bpkg";
		/**
		 * Delete all packages.
		 * 
		 * <pre>
		 * 'dpkg'
		 * </pre>
		 */
		public static final String kDDeleteAllPackages = "dpkg";
		/**
		 * This command requests the definition of the indexes that should be
		 * created for the current soup.
		 * 
		 * <pre>
		 * 'gidx'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetIndexDescription = "gind";
		/**
		 * Create a soup.
		 * 
		 * <pre>
		 * 'csop'
		 * length
		 * soup name
		 * index description
		 * </pre>
		 */
		public static final String kDCreateSoup = "csop";
		/**
		 * Get inheritance.
		 * 
		 * <pre>
		 * 'ginh'
		 * </pre>
		 */
		public static final String kDGetInheritance = "ginh";
		/**
		 * Get patches.
		 * 
		 * <pre>
		 * 'gpat'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetPatches = "gpat";
		/**
		 * Delete package dir.
		 * 
		 * <pre>
		 * 'dpkd'
		 * length = 0
		 * </pre>
		 */
		public static final String kDDeletePkgDir = "dpkd";
		/**
		 * Get soup information.
		 * 
		 * <pre>
		 * 'gsin'
		 * length = 0
		 * </pre>
		 */
		public static final String kDGetSoupInfo = "gsin";
	}

	public static class NewtonToDesktop {

		/**
		 * This command is sent in response to a <tt>kDGetStoreNames</tt>
		 * command. It returns information about all the stores on the Newton.
		 * Each array slot contains the following information about a store:<br>
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
}</code>
		 * 
		 * <pre>
		 * 'stor'
		 * length
		 * array of frames
		 * </pre>
		 */
		public static final String kDStoreNames = "stor";
		/**
		 * This command is sent in response to a <tt>kDGetSoupNames</tt>
		 * command. It returns the names and signatures of all the soups in the
		 * current store.
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
		 * This command is sent in response to a <tt>kDGetSoupIDs</tt> command.
		 * It returns all the IDs from the current soup.
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
		 * This command is sent in response to a <tt>kDGetChangedIDs</tt>
		 * command. It returns all the ids with <tt>mod</tt> time > the last
		 * sync time. If the last sync time is 0, no changed entries are
		 * returned (this would happen on the first sync).
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
		 * This command is sent in response to a <tt>kDAddEntry</tt> command
		 * from the desktop. It returns the ID that the entry was given when it
		 * was added to the current soup.
		 * 
		 * <pre>
		 * 'adid'
		 * length
		 * id
		 * </pre>
		 */
		public static final String kDAddedID = "adid";
		/**
		 * This command is sent in response to a <tt>kDReturnEntry</tt> command.
		 * The entry in the current soup specified by the ID in the
		 * <tt>kDReturnEntry</tt> command is returned.
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
		 * This command sends a package to the desktop. It's issued repeatedly
		 * in response to a <tt>kDBackupPackages</tt> message.
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
		 * Inheritance.
		 * 
		 * <pre>
		 * 'dinh'
		 * length
		 * array of class, superclass pairs
		 * </pre>
		 */
		public static final String kDInheritance = "dinh";
		/**
		 * Patches
		 * 
		 * <pre>
		 * 'patc'
		 * length = 0
		 * </pre>
		 */
		public static final String kDPatches = "patc";
	}

	/**
	 * This command is sent by the Newton in response to a
	 * <tt>kDReturnChangedEntry</tt> command from the desktop. It can also be
	 * sent by the desktop.
	 * 
	 * <pre>
	 * 'cent'
	 * length
	 * entry
	 * </pre>
	 */
	public static final String kDChangedEntry = "cent";
	/**
	 * Test.
	 * 
	 * <pre>
	 * 'test'
	 * length
	 * data
	 * </pre>
	 */
	public static final String kDTest = "test";
	/**
	 * This command is used to send a soup info frame. When received the info
	 * for the current soup is set to the specified frame.
	 * 
	 * <pre>
	 * 'sinf'
	 * length = 0
	 * soup info frame
	 * </pre>
	 */
	public static final String kDSoupInfo = "sinf";

}
