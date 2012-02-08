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
package net.sf.jncu.cdil;

import java.util.TimerTask;

import net.sf.jncu.protocol.v1_0.session.DHello;

/**
 * Ping the Newton.
 * 
 * @author moshew
 */
public class CDPing extends TimerTask {

	private final CDPipe<? extends CDPacket> pipe;

	/**
	 * Creates a new ping.
	 * 
	 * @param pipe
	 *            the pipe.
	 */
	public CDPing(CDPipe<? extends CDPacket> pipe) {
		super();
		if (pipe == null)
			throw new IllegalArgumentException("pipe required");
		this.pipe = pipe;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		DHello hello = new DHello();
		try {
			pipe.write(hello);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
