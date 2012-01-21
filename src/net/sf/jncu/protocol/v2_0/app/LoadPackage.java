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
package net.sf.jncu.protocol.v2_0.app;

import java.io.File;
import java.util.Collection;

import javax.swing.JOptionPane;

import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.app.DLoadPackage;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v2_0.io.win.FileChooser;

/**
 * Load package module.
 * 
 * @author Moshe
 */
public class LoadPackage implements DockCommandListener {

	private static final String TITLE = "Load Package";

	private final CDPipe<? extends CDPacket> pipe;

	/**
	 * Constructs a new loader.
	 * 
	 * @param pipe
	 *            the pipe.
	 */
	public LoadPackage(CDPipe<? extends CDPacket> pipe) {
		super();
		if (pipe == null)
			throw new IllegalArgumentException("pipe required");
		this.pipe = pipe;
		pipe.addCommandListener(this);
	}

	protected FileChooser createFileChooser(CDPipe<? extends CDPacket> pipe, Collection<NSOFString> types) {
		if (File.separatorChar == '\\')
			return new FileChooser(pipe, types);
		return null;
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		String cmd = command.getCommand();

		if (DResult.COMMAND.equals(cmd)) {
			DResult result = (DResult) command;
			// Upload was finished?
			if (result.getErrorCode() != DResult.OK) {
				// Show the error to the user.
				JOptionPane.showMessageDialog(null, result.getError(), TITLE, JOptionPane.ERROR_MESSAGE);
			}
			commandEOF();
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
	}

	@Override
	public void commandEOF() {
		pipe.removeCommandListener(this);
	}

	/**
	 * Upload the package to the Newton.
	 * 
	 * @param file
	 */
	public void loadPackage(File file) {
		DLoadPackage load = new DLoadPackage();
		load.setFile(file);
		try {
			pipe.write(load);
		} catch (Exception e) {
			// Show an error to the user.
			JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			commandEOF();
		}
	}
}
