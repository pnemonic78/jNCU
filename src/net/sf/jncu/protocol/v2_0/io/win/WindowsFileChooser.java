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
package net.sf.jncu.protocol.v2_0.io.win;

import java.io.File;
import java.util.Collection;

import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.v2_0.io.DPath;
import net.sf.jncu.protocol.v2_0.io.FileChooser;

/**
 * Data source for interacting with the Newton file browser for Windows
 * operating systems.
 * 
 * @author Moshe
 */
public class WindowsFileChooser extends FileChooser {

	/**
	 * Constructs a new file chooser.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param types
	 *            the chooser types.
	 */
	public WindowsFileChooser(CDPipe<? extends CDPacket> pipe, Collection<NSOFString> types) {
		super(pipe, types);
		init();
	}

	/**
	 * Constructs a new file chooser.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param type
	 *            the chooser type.
	 */
	public WindowsFileChooser(CDPipe<? extends CDPacket> pipe, NSOFString type) {
		super(pipe, type);
		init();
	}

	/**
	 * Initialise.
	 */
	private void init() {
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (!isEnabled())
			return;

		super.commandReceived(command);

		String cmd = command.getCommand();

		if (DGetDevices.COMMAND.equals(cmd)) {
			DDevices cmdDevices = new DDevices();
			write(cmdDevices);
		} else if (DGetFilters.COMMAND.equals(cmd)) {
			DFilters cmdFilters = new DFilters();
			cmdFilters.addFilters(getFilters());
			write(cmdFilters);
		} else if (DSetDrive.COMMAND.equals(cmd)) {
			DSetDrive cmdSet = (DSetDrive) command;
			setPath(new File(cmdSet.getDrive()));
			DPath cmdPath = new DPath();
			cmdPath.setPath(getPath());
			write(cmdPath);
		} else if (DSetFilter.COMMAND.equals(cmd)) {
			DSetFilter cmdSet = (DSetFilter) command;
			int index = cmdSet.getIndex();
			setFilter(getFilters().get(index));
			sendFiles();
		}
	}

}