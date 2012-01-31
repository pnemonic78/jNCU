/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * The following is a possible example of what would be displayed on the Newton
 * following the <tt>kDImportParametersSlip</tt> command:
 * <p>
 * The slip will, at minimum, display 2 text string fields corresponding to the
 * slip title and a filename. Up to 5 additional fields, plus the
 * <tt>CloseBox</tt>, could be displayed. While the slip is displayed, "helos"
 * are sent to the desktop. When the user taps on the "Import" button or the
 * <tt>CloseBox</tt>, a <tt>kDImportParameterSlipResult</tt> is sent to the
 * desktop. Each of the other 5 fields is shown if the slot defining it exists
 * in the frame parameter.
 * <p>
 * The frame contains the following slots used to configure the display of the
 * slip:<br>
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
 * 
 * @author moshew
 */
public class DImportParametersSlip extends DockCommandToNewtonScript<NSOFFrame> {

	/** <tt>kDImportParametersSlip</tt> */
	public static final String COMMAND = "islp";

	/**
	 * Creates a new command.
	 */
	public DImportParametersSlip() {
		super(COMMAND);
	}

}
