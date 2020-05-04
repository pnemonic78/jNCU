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

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;

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
 * <p>
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
public class DImportParametersSlipResult extends DockCommandFromNewtonScript<NSOFFrame> {

    /**
     * <tt>kDImportParameterSlipResult</tt>
     */
    public static final String COMMAND = "islr";

    public static final NSOFSymbol SLOT_APPS = new NSOFSymbol("AppList");
    public static final NSOFSymbol SLOT_CONFLICTS = new NSOFSymbol("Conflicts");
    public static final NSOFSymbol SLOT_DATES = new NSOFSymbol("Dates");

    /**
     * Creates a new command.
     */
    public DImportParametersSlipResult() {
        super(COMMAND);
    }

}
