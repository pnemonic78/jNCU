/**
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

/**
 * <tt>kDImportParameterSlipResult</tt><br>
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
 * 
 * @author moshew
 */
public class DImportParametersSlipResult extends DockCommandFromNewtonScript {

	public static final String COMMAND = "islr";

	/**
	 * Creates a new command.
	 */
	public DImportParametersSlipResult() {
		super(COMMAND);
	}

}
