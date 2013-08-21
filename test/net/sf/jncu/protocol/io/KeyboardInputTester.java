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

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.mnp.EmptyPipe;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.protocol.v2_0.io.DKeyboardChar;
import net.sf.jncu.protocol.v2_0.io.KeyboardInput;
import net.sf.jncu.protocol.v2_0.io.KeyboardInputListener;

public class KeyboardInputTester implements WindowListener,
		KeyboardInputListener {

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
			if (portName == null) {
				this.pipe = new EmptyPipe(layer);
			} else {
				layer.startUp();
				this.pipe = layer.createMNPSerial(portName,
						MNPSerialPort.BAUD_38400);
			}
			pipe.startListening();
			while (layer.getState() == CDState.LISTENING) {
				Thread.yield();
			}
			if (portName == null)
				layer.setState(pipe, CDState.CONNECT_PENDING);
			pipe.accept();

			this.input = new KeyboardInput(pipe, null);
			input.getDialog().addWindowListener(this);
			input.getDialog().addInputListener(this);
			input.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void charTyped(KeyEvent ke) {
		if (ke.getID() != KeyEvent.KEY_PRESSED)
			return;
		char keyChar = DKeyboardChar.toNewtonChar(ke.getKeyChar(),
				ke.getKeyCode());
		// Ignore unknown characters.
		if (keyChar == 0)
			return;
		int keyFlags = DKeyboardChar.toNewtonState(ke.getModifiers());
		System.out.println("charTyped keyChar=" + keyChar + " keyFlags="
				+ keyFlags);
	}

	@Override
	public void stringTyped(String text) {
		System.out.println("stringTyped [" + text + "]");
	}
}
