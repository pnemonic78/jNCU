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
package net.sf.jncu.protocol.v2_0.io;

import java.util.TimerTask;

/**
 * @author Moshe
 */
public class KeyboardTask extends TimerTask {

	private final KeyboardInput input;

	/**
	 * Constructs a new task.
	 * 
	 * @param input
	 *            the owner input.
	 */
	public KeyboardTask(KeyboardInput input) {
		super();
		if (input == null)
			throw new IllegalArgumentException("(Keyboard input required");
		this.input = input;
	}

	@Override
	public void run() {
		// Write the buffer (either char or string) to sender.
		input.flush();
	}

}
