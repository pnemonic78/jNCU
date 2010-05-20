/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSourceVersion</tt><br>
 * Tells the Newton the version that the subsequent data is from. For example,
 * if a 1.x data file is being restored the desktop would tell the Newton that
 * version <tt>1</tt> data was coming. Same for importing a 1.x NTF file.
 * Otherwise, it should indicate that 2.x data is coming. When the connection is
 * first started the version defaults to 2.x. This information is necessary for
 * the Newton to know whether or not it should run the conversion scripts. A
 * <tt>kDRes</tt> command with value <tt>0</tt> is sent by the Newton in
 * response to this command. This commands affects only data added to the Newton
 * with <tt>kDAddEntry</tt> and <tt>kDAddEntryWithUniqueID</tt> commands. In
 * particular, note that data returned by <tt>kDReturnEntry</tt> isn't converted
 * if it's a different version than was set by this command.
 * <p>
 * <tt>manufacturer</tt> and <tt>machinetype</tt> tell the Newton the type of
 * Newton that's the source of the data being restored. These are sent at the
 * beginning of a connection as part of the Newton name command.
 * 
 * <pre>
 * 'sver'
 * length
 * vers
 * manufacturer
 * machineType
 * </pre>
 * 
 * @author moshew
 */
public class DSourceVersion extends DockCommandToNewton {

	public static final String COMMAND = "sver";

	/** Source versions. */
	public static enum eSourceVersion {
		/** Unknown version. */
		eNone,
		/** <tt>1.x</tt> data file. */
		eOnePointXData,
		/** <tt>2.x</tt> data file. */
		eTwoPointXData
	}

	/**
	 * Creates a new command.
	 */
	public DSourceVersion() {
		super(COMMAND);
	}

}
