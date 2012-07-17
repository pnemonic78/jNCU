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
package net.sf.jncu.protocol.v2_0.sync;

import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled2;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;

/**
 * Backup module.
 * 
 * @author mwaisberg
 * 
 */
public class BackupModule extends IconModule {

	protected enum State {
		None, Initialised, Cancelled, Finished
	}

	protected static final String TITLE = "Backup";

	private State state = State.None;

	/**
	 * Creates a new module.
	 * 
	 * @param pipe
	 *            the pipe.
	 */
	public BackupModule(CDPipe<? extends CDPacket> pipe) {
		super(TITLE, pipe);
		setName("BackupModule-" + getId());
		state = State.Initialised;
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (state == State.Cancelled)
			return;
		if (state == State.Finished)
			return;

		String cmd = command.getCommand();

		if (DResult.COMMAND.equals(cmd)) {
			DResult result = (DResult) command;
			int code = result.getErrorCode();

			if (code == DResult.OK) {
				switch (state) {
				case Initialised:
					// TODO state = State.Backing;
					break;
				}
			} else {
				commandEOF();
				state = State.Cancelled;
				showError(result.getError().getMessage() + "\nCode: " + code);
			}
		} else if (DOperationCanceled.COMMAND.equals(cmd)) {
			// TODO Stop backup.
			// pipe.cancel(backup);
			// backup.kill();
			DOperationCanceledAck ack = new DOperationCanceledAck();
			write(ack);
		} else if (DOperationCanceled2.COMMAND.equals(cmd)) {
			// TODO Stop backup.
			// pipe.cancel(backup);
			// backup.kill();
			DOperationCanceledAck ack = new DOperationCanceledAck();
			write(ack);
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (state == State.Cancelled)
			return;
		if (state == State.Finished)
			return;

		String cmd = command.getCommand();

		if (DOperationDone.COMMAND.equals(cmd)) {
			commandEOF();
			state = State.Finished;
		} else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
			commandEOF();
			state = State.Cancelled;
		}
	}
}
