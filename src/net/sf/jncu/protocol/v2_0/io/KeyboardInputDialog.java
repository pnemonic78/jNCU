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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sf.swing.SwingUtils;

/**
 * Keyboard input dialog.
 * <p>
 * Provides a UI for the user to type text.
 * 
 * @author Moshe
 */
public class KeyboardInputDialog extends JDialog implements KeyListener {

	static {
		SwingUtils.init();
	}

	private JLabel inputLabel;
	private JTextArea input;
	private JButton pasteButton;
	private JButton cancelButton;
	private final List<KeyboardInputListener> listeners = new ArrayList<KeyboardInputListener>();

	/**
	 * Constructs a new dialog.
	 */
	public KeyboardInputDialog() {
		super();
		init();
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public KeyboardInputDialog(Frame owner) {
		super(owner);
		init();
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public KeyboardInputDialog(Dialog owner) {
		super(owner);
		init();
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public KeyboardInputDialog(Window owner) {
		super(owner);
		init();
	}

	private void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setTitle("Keyboard Passthrough");
		final int w = 320;
		final int h = 150;
		setSize(w, h);

		// Centre.
		Container parent = getParent();
		int x = 0;
		int y = 0;
		if ((parent == null) || (parent.getWidth() == 0) || (parent.getHeight() == 0)) {
			Toolkit tookit = Toolkit.getDefaultToolkit();
			Dimension size = tookit.getScreenSize();
			x = (size.width - w) / 2;
			y = (size.height - h) / 2;
		} else {
			x = parent.getX() + ((parent.getWidth() - w) / 2);
			y = parent.getY() + ((parent.getHeight() - h) / 2);
		}
		setLocation(x, y);

		JPanel buttons = new JPanel();
		FlowLayout layoutButtons = new FlowLayout(FlowLayout.TRAILING);
		buttons.setLayout(layoutButtons);
		buttons.add(getPasteButton());
		buttons.add(getCancelButton());

		setLayout(new BorderLayout(10, 10));
		add(getTextLabel(), BorderLayout.NORTH);
		add(new JScrollPane(getTextInput()), BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);

		getTextInput().requestFocus();

		this.addKeyListener(this);
	}

	/**
	 * Get the text label.
	 * 
	 * @return the text label.
	 */
	private JLabel getTextLabel() {
		if (inputLabel == null) {
			inputLabel = new JLabel("Text typed:");
		}
		return inputLabel;
	}

	/**
	 * Get the text summary input.
	 * 
	 * @return the text summary.
	 */
	private JTextArea getTextInput() {
		if (input == null) {
			input = new JTextArea();
			input.setEditable(false);
			input.setAlignmentX(CENTER_ALIGNMENT);
			input.addKeyListener(this);
			input.setFocusTraversalKeysEnabled(false);

			Font font = input.getFont();
			font = font.deriveFont(font.getStyle(), 14);
			input.setFont(font);
		}
		return input;
	}

	/**
	 * Get the "paste" button. Pastes text from the clipboard.
	 * 
	 * @return the button.
	 */
	private JButton getPasteButton() {
		if (pasteButton == null) {
			pasteButton = new JButton(Toolkit.getProperty("AWT.paste", "Paste"));
			pasteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					paste();
				}
			});
		}
		return pasteButton;
	}

	/**
	 * Get the "cancel" button. Closes the window.
	 * 
	 * @return the button.
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton(Toolkit.getProperty("AWT.cancel", "Cancel"));
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					close();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * Paste text from the clipboard.
	 */
	protected void paste() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		DataFlavor flavor = DataFlavor.getTextPlainUnicodeFlavor();
		if (clipboard.isDataFlavorAvailable(flavor)) {
			Reader reader = null;
			try {
				Transferable trans = clipboard.getContents(this);
				reader = flavor.getReaderForText(trans);
				StringBuffer s = new StringBuffer();
				int c = reader.read();
				while (c != -1) {
					s.append((char) c);
					c = reader.read();
				}
				String text = s.toString();
				getTextInput().setText(text);
				fireStringTyped(text);
			} catch (UnsupportedFlavorException ufe) {
				ufe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception e) {
					}
				}
			}
		}
		getTextInput().requestFocus();
	}

	/**
	 * Add a keyboard input listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addInputListener(KeyboardInputListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove a keyboard input listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeInputListener(KeyboardInputListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify all the listeners that a character has been pressed.
	 * 
	 * @param ke
	 *            the key event.
	 */
	protected void fireCharTyped(KeyEvent ke) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<KeyboardInputListener> listenersCopy = new ArrayList<KeyboardInputListener>(listeners);
		for (KeyboardInputListener listener : listenersCopy) {
			listener.charTyped(ke);
		}
	}

	/**
	 * Notify all the listeners that text has been typed.
	 * 
	 * @param text
	 *            the text.
	 */
	protected void fireStringTyped(String text) {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<KeyboardInputListener> listenersCopy = new ArrayList<KeyboardInputListener>(listeners);
		for (KeyboardInputListener listener : listenersCopy) {
			listener.stringTyped(text);
		}
	}

	/**
	 * Close the window.
	 */
	public void close() {
		if (isVisible()) {
			setVisible(false);
			WindowEvent closing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
			dispatchEvent(closing);
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		String text = KeyEvent.getKeyText(ke.getKeyCode());
		getTextInput().setText(text);
		fireCharTyped(ke);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		fireCharTyped(ke);
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		fireCharTyped(ke);
	}
}
