package net.sf.jncu.protocol.v2_0.query;

import net.sf.jncu.protocol.v2_0.DockingEventCommands;

/**
 * Remote query commands.
 * <p>
 * All of the commands in this section are based on the NewtonScript query
 * functions. Please see the Newton Programmer's Guide for details about the
 * functions performed by the commands. The query command returns a long
 * representing the queries cursor. Each of the other commands take this cursor
 * as a parameter. Entries are returned with the kDEntry command.
 */
public class DockCommandRemoteQuery extends DockingEventCommands {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockingEventCommands.DesktopToNewton {
		/**
		 * The parameter frame must contain a <tt>queryspec</tt> slot and may
		 * contain a <tt>soupname</tt> slot. Performs the specified query on the
		 * current store. The <tt>queryspec</tt> is a full <tt>queryspec</tt>
		 * including valid test, etc. functions. Soup name is a string that's
		 * used to find a soup in the current store to query. If the soup name
		 * is an empty string or a <tt>NILREF</tt> the query is done on the
		 * current soup. A <tt>kDLongData</tt> is returned with a cursor ID that
		 * should be used with the rest of the remote query commands.
		 * 
		 * <pre>
		 * 'qury'
		 * length
		 * parameter frame
		 * </pre>
		 */
		public static final String kDQuery = "qury";
		/**
		 * The entry at the specified key location is returned. <tt>Nil</tt> is
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
		 * turn and returns an array of the results. A <tt>kDRefResult</tt> is
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
		 * Moves the cursor forward <tt>count</tt> entries from its current
		 * position and returns that entry. Returns nil if the cursor is moved
		 * past the last entry.
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
		 * Moves the cursor to the next entry in the set of entries referenced
		 * by the cursor and returns the entry. Returns nil if the cursor is
		 * moved past the last entry.
		 * 
		 * <pre>
		 * 'next'
		 * length
		 * cursor id
		 * </pre>
		 */
		public static final String kDCursorNext = "next";
		/**
		 * Moves the cursor to the previous entry in the set of entries
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
		 * Resets the cursor to its initial state. A <tt>kDRes</tt> of 0 is
		 * returned.
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
		 * <tt>kDRes</tt> of 0 is returned.
		 * 
		 * <pre>
		 * 'rend'
		 * length
		 * cursor id
		 * </pre>
		 */
		public static final String kDCursorResetToEnd = "rend";
		/**
		 * Returns the count of the entries matching the query specification. A
		 * <tt>kDLongData</tt> is returned.
		 * 
		 * <pre>
		 * 'cnt '
		 * length
		 * cursor id
		 * </pre>
		 */
		public static final String kDCursorCountEntries = "cnt ";
		/**
		 * Returns <tt>kDLongData</tt> with a 0 for unknown, 1 for start and 2
		 * for end.
		 * 
		 * <pre>
		 * 'whch'
		 * length
		 * cursor id
		 * </pre>
		 */
		public static final String kDCursorWhichEnd = "whch";
		/**
		 * Disposes the cursor and returns a <tt>kDRes</tt> with a 0 or error.
		 * 
		 * <pre>
		 * 'cfre'
		 * length
		 * cursor id
		 * </pre>
		 */
		public static final String kDCursorFree = "cfre";
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockingEventCommands.NewtonToDesktop {
		/**
		 * Newton returns a long value. The interpretation of the data depends
		 * on the command which prompted the return of the long value.
		 * 
		 * <pre>
		 * 'ldta'
		 * length
		 * data
		 * </pre>
		 */
		public static final String kDLongData = "ldta";
		/**
		 * Reference result.
		 * 
		 * <pre>
		 * 'ref '
		 * </pre>
		 */
		public static final String kDRefResult = "ref ";
	}

	/** Cursor is at unknown position. */
	public static final int eCursorWhichEndUnknown = 0;
	/** Cursor is at start position. */
	public static final int eCursorWhichEndStart = 1;
	/** Cursor is at end position. */
	public static final int eCursorWhichEndEnd = 2;

}
