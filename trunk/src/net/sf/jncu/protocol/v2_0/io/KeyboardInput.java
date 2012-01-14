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

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;

/**
 * Keyboard input for pass-through mode.
 * 
 * @author Moshe
 */
public class KeyboardInput extends Thread implements DockCommandListener, WindowListener, KeyboardInputListener {

	/** Windows EOL. */
	protected static final String CRLF = "\r\n";
	/** Macintosh EOL. */
	protected static final String CR = "\r";
	/** Unix EOL. */
	protected static final char LF = '\n';

	private static enum State {
		None, Initialised, Input, Cancelled, Finished
	}

	private final CDPipe pipe;
	private State state = State.None;
	private KeyboardInputDialog dialog;

	/**
	 * Constructs a new object.
	 */
	public KeyboardInput(CDPipe pipe) {
		super();
		if (pipe == null)
			throw new IllegalArgumentException("pipe required");
		this.pipe = pipe;

		pipe.addCommandListener(this);

		state = State.Initialised;

		dialog = new KeyboardInputDialog();
		dialog.addInputListener(this);
		dialog.addWindowListener(this);
	}

	/**
	 * Show the input window.
	 */
	@Override
	public void run() {
		pipe.ping();
		state = State.Input;
		dialog.setVisible(true);
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (state == State.Cancelled)
			return;
		if (state == State.Finished)
			return;

		String cmd = command.getCommand();

		if (DOperationCanceled.COMMAND.equals(cmd)) {
			DOperationCanceledAck ack = new DOperationCanceledAck();
			send(ack);
			commandEOF();
			state = State.Cancelled;
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
			state = State.Finished;
		} else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
			state = State.Cancelled;
		}
	}

	@Override
	public void commandEOF() {
		pipe.cancelPing();
		pipe.removeCommandListener(this);
		if (state == State.Input) {
			dialog.close();
		}
		dialog.removeInputListener(this);
	}

	/**
	 * Send a command.
	 * 
	 * @param command
	 *            the command.
	 */
	protected void send(IDockCommandToNewton command) {
		try {
			if (pipe.getLayer().getState() == CDState.CONNECTED)
				pipe.write(command);
		} catch (Exception e) {
			e.printStackTrace();
			commandEOF();
		}
	}

	/**
	 * Write the character to the Newton.
	 * 
	 * @param c
	 *            the character.
	 * @param flags
	 *            the state flags.
	 */
	protected void writeChar(char c, int flags) {
		if (state != State.Input)
			return;

		DKeyboardChar cmd = new DKeyboardChar();
		cmd.setCharacter(c);
		cmd.setState(flags);
		send(cmd);
	}

	/**
	 * Write the string to the Newton.
	 * 
	 * @param s
	 *            the string.
	 */
	protected void writeString(String s) {
		if (state != State.Input)
			return;

		DKeyboardString cmd = new DKeyboardString();
		cmd.setString(s);
		send(cmd);
	}

	@Override
	public void windowOpened(WindowEvent we) {
	}

	@Override
	public void windowClosing(WindowEvent we) {
		if (state == State.Input) {
			DOperationDone done = new DOperationDone();
			try {
				if (pipe.getLayer().getState() == CDState.CONNECTED)
					pipe.write(done);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		commandEOF();
	}

	@Override
	public void windowClosed(WindowEvent we) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent we) {
	}

	@Override
	public void windowActivated(WindowEvent we) {
	}

	@Override
	public void windowDeactivated(WindowEvent we) {
	}

	@Override
	public void charTyped(KeyEvent ke) {
		if (ke.getID() != KeyEvent.KEY_PRESSED)
			return;
		char keyChar = DKeyboardChar.toNewtonChar(ke.getKeyChar(), ke.getKeyCode());
		// Ignore unknown characters.
		if (keyChar == 0)
			return;

		int keyFlags = DKeyboardChar.toNewtonState(ke.getModifiers());

		writeChar(keyChar, keyFlags);
	}

	@Override
	public void stringTyped(String text) {
		int length = text.length();
		if (length == 0)
			return;

		// Replace with Macintosh EOL.
		text = text.replaceAll(CRLF, CR);
		text = text.replace(LF, DKeyboardChar.RETURN);
		writeString(text);
	}
}
