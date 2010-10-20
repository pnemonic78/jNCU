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
package net.sf.jncu.protocol.v1_0.sync;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;

/**
 * This command is sent by the Newton in response to a
 * <tt>kDReturnChangedEntry</tt> command from the desktop. It can also be sent
 * by the desktop.
 * 
 * <pre>
 * 'cent'
 * length
 * entry
 * </pre>
 */
public class DChangedEntry extends DockCommandFromNewton implements IDockCommandToNewton {

	/** <tt>kDChangedEntry</tt> */
	public static final String COMMAND = "cent";

	private NSOFObject entry;

	/**
	 * Creates a new command.
	 */
	public DChangedEntry() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		setEntry(decoder.decode(data));
	}

	@Override
	public byte[] getPayload() {
		IDockCommandToNewton cmd = new DockCommandToNewton(COMMAND) {

			@Override
			protected void writeCommandData(OutputStream data) throws IOException {
				NSOFEncoder encoder = new NSOFEncoder();
				encoder.encode(getEntry(), data);
			}
		};
		return cmd.getPayload();
	}

	/**
	 * Get the entry.
	 * 
	 * @return the entry.
	 */
	public NSOFObject getEntry() {
		return entry;
	}

	/**
	 * Set the entry.
	 * 
	 * @param entry
	 *            the entry.
	 */
	public void setEntry(NSOFObject entry) {
		this.entry = entry;
	}
}
