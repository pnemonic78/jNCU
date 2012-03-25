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
import java.util.concurrent.TimeoutException;

/**
 * Timeout when listening to Newton.
 * 
 * @author moshew
 */
public class CDTimeout extends TimerTask {

	private final CDPipe<? extends CDPacket> pipe;

	/**
	 * Creates a new timeout.
	 * 
	 * @param pipe
	 *            the pipe.
	 */
	public CDTimeout(CDPipe<? extends CDPacket> pipe) {
		super();
		if (pipe == null)
			throw new NullPointerException("pipe required");
		this.pipe = pipe;
	}

	@Override
	public void run() {
		pipe.notifyTimeout(new TimeoutException());
	}

}
