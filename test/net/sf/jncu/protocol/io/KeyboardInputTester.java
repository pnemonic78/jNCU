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
package net.sf.jncu.protocol.io;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.SwingUtilities;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDPipeListener;
import net.sf.jncu.cdil.mnp.EmptyPipe;
import net.sf.jncu.cdil.mnp.MNPPacket;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v2_0.io.DKeyboardChar;
import net.sf.jncu.protocol.v2_0.io.KeyboardInput;
import net.sf.jncu.protocol.v2_0.io.KeyboardInputListener;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;

public class KeyboardInputTester implements WindowListener, KeyboardInputListener, DockCommandListener, CDPipeListener<MNPPacket> {

	private String portName;
	private CDLayer layer;
	private MNPPipe pipe;
	private KeyboardInput input;

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		KeyboardInputTester tester = new KeyboardInputTester();
		if (args.length > 0) {
			tester.setPortName(args[0]);
		}
		tester.run();
	}

	public KeyboardInputTester() {
		super();
		this.layer = CDLayer.getInstance();
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public void run() {
		try {
			layer.startUp();
			if (portName == null) {
				this.pipe = new EmptyPipe(layer);
			} else {
				this.pipe = layer.createMNPSerial(portName, MNPSerialPort.BAUD_38400);
			}
			pipe.addCommandListener(this);
			pipe.startListening(this);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void windowOpened(WindowEvent event) {
	}

	@Override
	public void windowClosing(WindowEvent event) {
		if (portName == null)
			input.getDialog().dispose();
	}

	@Override
	public void windowClosed(WindowEvent event) {
		try {
			pipe.disconnect();
			layer.shutDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent event) {
	}

	@Override
	public void windowDeiconified(WindowEvent event) {
	}

	@Override
	public void windowActivated(WindowEvent event) {
	}

	@Override
	public void windowDeactivated(WindowEvent event) {
	}

	@Override
	public void charTyped(KeyEvent event) {
		if (event.getID() != KeyEvent.KEY_PRESSED)
			return;
		char keyChar = DKeyboardChar.toNewtonChar(event.getKeyChar(), event.getKeyCode());
		// Ignore unknown characters.
		if (keyChar == 0)
			return;
		int keyFlags = DKeyboardChar.toNewtonState(event.getModifiers());
		System.out.println("charTyped keyChar=" + keyChar + " keyFlags=" + keyFlags);
	}

	@Override
	public void stringTyped(String text) {
		System.out.println("stringTyped [" + text + "]");
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
	}

	@Override
	public void commandReceiving(IDockCommandFromNewton command, int progress, int total) {
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (DOperationDone.COMMAND.equals(command.getCommand())) {
			input.getDialog().dispose();
		}
	}

	@Override
	public void commandSending(IDockCommandToNewton command, int progress, int total) {
	}

	@Override
	public void commandEOF() {
	}

	@Override
	public void pipeDisconnected(CDPipe<MNPPacket> pipe) {
	}

	@Override
	public void pipeDisconnectFailed(CDPipe<MNPPacket> pipe, Exception e) {
		System.exit(100);
	}

	@Override
	public void pipeConnectionListening(CDPipe<MNPPacket> pipe) {
	}

	@Override
	public void pipeConnectionListenFailed(CDPipe<MNPPacket> pipe, Exception e) {
		System.exit(101);
	}

	@Override
	public void pipeConnectionPending(CDPipe<MNPPacket> pipe) {
	}

	@Override
	public void pipeConnectionPendingFailed(CDPipe<MNPPacket> pipe, Exception e) {
		System.exit(102);
	}

	@Override
	public void pipeConnected(final CDPipe<MNPPacket> pipe) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				input = new KeyboardInput(pipe, null);
				input.initiate();
				input.getDialog().addWindowListener(KeyboardInputTester.this);
				input.getDialog().addInputListener(KeyboardInputTester.this);
				input.start();
			}
		});
	}

	@Override
	public void pipeConnectionFailed(CDPipe<MNPPacket> pipe, Exception e) {
		System.exit(103);
	}
}
