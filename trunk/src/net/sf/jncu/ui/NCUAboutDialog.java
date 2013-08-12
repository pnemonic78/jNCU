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
package net.sf.jncu.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLEditorKit;

import net.sf.swing.SwingUtils;

/**
 * jNCU about dialog.
 * 
 * @author moshew
 */
public class NCUAboutDialog extends JDialog implements ActionListener,
		HyperlinkListener {

	private static final String TITLE = "About jNewton Connection Utility";

	private JPanel contentPane;
	private JPanel buttons;
	private JButton okButton;
	private Dimension buttonMinimumSize;
	private JTextComponent description;

	/**
	 * @param owner
	 */
	public NCUAboutDialog(Frame owner) {
		super(owner, true);
		init();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void init() {
		int buttonMinimumWidth = UIManager
				.getInt("OptionPane.buttonMinimumWidth");
		this.buttonMinimumSize = new Dimension(buttonMinimumWidth, 24);

		setTitle(TITLE);
		setContentPane(getMainContentPane());
		setResizable(false);
		pack();
		SwingUtils.centreInOwner(this);
		getOkButton().requestFocus();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(getButtons(), BorderLayout.SOUTH);
			contentPane.add(getDescription(), BorderLayout.CENTER);
		}
		return contentPane;
	}

	private JTextComponent getDescription() {
		if (description == null) {
			JTextPane text = new JTextPane();
			text.setEditable(false);
			text.setMargin(new Insets(10, 10, 10, 10));
			text.setOpaque(false);
			text.setFont(this.getFont());
			text.setHighlighter(null);
			HTMLEditorKit editor = new HTMLEditorKit();
			text.setEditorKit(editor);
			text.addHyperlinkListener(this);
			description = text;

			URL url = getClass().getResource("/about.html");
			InputStream in = null;
			try {
				in = url.openStream();
				Document doc = text.getDocument();
				editor.read(in, doc, 0);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {
						// ignore
					}
				}
			}
		}
		return description;
	}

	/**
	 * This method initializes buttons
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtons() {
		if (buttons == null) {
			buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			buttons.setOpaque(false);
			buttons.add(getOkButton(), null);
		}
		return buttons;
	}

	/**
	 * This method initializes buttonOk
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			URL url = getClass().getResource("/dialog-ok.png");
			Icon icon = new ImageIcon(url);

			JButton button = new JButton();
			button.setMnemonic(KeyEvent.VK_O);
			button.setText("OK");
			button.setIcon(icon);
			button.setMinimumSize(buttonMinimumSize);
			button.addActionListener(this);
			okButton = button;
		}
		return okButton;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		final Object src = event.getSource();

		if (src == okButton) {
			close();
		}
	}

	public void close() {
		if (isShowing()) {
			SwingUtils.postWindowClosing(this);
		}
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			// open link in browser.
			URL url = event.getURL();
			try {
				Desktop.getDesktop().browse(url.toURI());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
