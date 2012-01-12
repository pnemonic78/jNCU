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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sf.swing.SwingUtils;

/**
 * @author Moshe
 */
public class KeyboardInputDialog extends JDialog implements DocumentListener, KeyListener {

	static {
		SwingUtils.init();
	}

	private KeyListener keyListener;
	private DocumentListener docListener;
	private JTextArea text;
	private JButton pasteButton;
	private JButton clearButton;
	private JButton cancelButton;

	public static void main(String[] args) {
		KeyboardInputDialog dlg = new KeyboardInputDialog();
		dlg.setVisible(true);
	}

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
	 */
	public KeyboardInputDialog(Frame owner) {
		super(owner);
		init();
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 */
	public KeyboardInputDialog(Dialog owner) {
		super(owner);
		init();
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 */
	public KeyboardInputDialog(Window owner) {
		super(owner);
		init();
	}

	private void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setTitle("Keyboard Passthrough");
		final int w = 320;
		final int h = 200;
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
		BoxLayout layoutButtons = new BoxLayout(buttons, BoxLayout.X_AXIS);
		buttons.setLayout(layoutButtons);
		buttons.add(getPasteButton());
		buttons.add(getClearButton());
		buttons.add(getCancelButton());

		setLayout(new BorderLayout(10, 10));
		add(new JScrollPane(getTextArea()), BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		getTextArea().requestFocus();
	}

	/**
	 * Get the text area.
	 * 
	 * @return the text area.
	 */
	private JTextArea getTextArea() {
		if (text == null) {
			text = new JTextArea();
			Font font = text.getFont();
			font = new Font(font.getFontName(), font.getStyle(), 14);
			text.setFont(font);
			text.addKeyListener(this);
			text.getDocument().addDocumentListener(this);
		}
		return text;
	}

	/**
	 * Get the "paste" button. Pastes text from the clipboard.
	 * 
	 * @return the button.
	 */
	private JButton getPasteButton() {
		if (pasteButton == null) {
			pasteButton = new JButton("Paste");
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
	 * Get the "clear" button. Clears the text.
	 * 
	 * @return the button.
	 */
	private JButton getClearButton() {
		if (clearButton == null) {
			clearButton = new JButton("Clear");
			clearButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					getTextArea().setText(null);
				}
			});
		}
		return clearButton;
	}

	/**
	 * Get the "cancel" button. Closes the window.
	 * 
	 * @return the button.
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton("Cancel");
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
				getTextArea().append(s.toString());
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

	/**
	 * Set the key listener.
	 * 
	 * @param keyListener
	 *            the listener.
	 */
	public void setKeyListener(KeyListener keyListener) {
		this.keyListener = keyListener;
	}

	/**
	 * Set the document listener.
	 * 
	 * @param docListener
	 *            the listener.
	 */
	public void setDocumentListener(DocumentListener docListener) {
		this.docListener = docListener;
	}

	@Override
	public void insertUpdate(DocumentEvent de) {
		if (docListener != null)
			docListener.insertUpdate(de);
	}

	@Override
	public void removeUpdate(DocumentEvent de) {
		if (docListener != null)
			docListener.removeUpdate(de);
	}

	@Override
	public void changedUpdate(DocumentEvent de) {
		if (docListener != null)
			docListener.changedUpdate(de);
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		if (keyListener != null)
			keyListener.keyTyped(ke);
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (keyListener != null)
			keyListener.keyPressed(ke);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (keyListener != null)
			keyListener.keyReleased(ke);
	}
}
