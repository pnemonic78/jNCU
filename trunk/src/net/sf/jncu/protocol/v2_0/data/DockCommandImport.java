package net.sf.jncu.protocol.v2_0.data;

public class DockCommandImport extends DockCommandData {

	/** Desktop to Newton. */
	public static class DesktopToNewton extends DockCommandData.DesktopToNewton {
		/**
		 * The following is a possible example of what would be displayed on the
		 * Newton following the <tt>kDImportParametersSlip</tt> command:
		 * <p>
		 * The slip will, at minimum, display 2 text string fields corresponding
		 * to the slip title and a filename. Up to 5 additional fields, plus the
		 * <tt>CloseBox</tt>, could be displayed. While the slip is displayed,
		 * "helos" are sent to the desktop. When the user taps on the "Import"
		 * button or the <tt>CloseBox</tt>, a
		 * <tt>kDImportParameterSlipResult</tt> is sent to the desktop. Each of
		 * the other 5 fields is shown if the slot defining it exists in the
		 * frame parameter.
		 * <p>
		 * The frame contains the following slots used to configure the display
		 * of the slip:<br>
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
	}

	/** Newton to Desktop. */
	public static class NewtonToDesktop extends DockCommandData.NewtonToDesktop {
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
	}

}
