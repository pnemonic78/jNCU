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
import java.awt.Desktop.Action;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLEditorKit;

import net.sf.jncu.JNCUResources;

/**
 * jNCU "about" dialog.
 * 
 * @author moshew
 */
public class JNCUAboutDialog extends JNCUDialog implements HyperlinkListener {

	private JPanel contentPane;
	private JPanel buttons;
	private JButton okButton;
	private JTextComponent description;
	private Desktop desktop;

	/**
	 * Create an about dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public JNCUAboutDialog(Window owner) {
		super(owner);
		init();
	}

	/**
	 * Initialise.
	 */
	private void init() {
		setTitle(JNCUResources.getString("jncuAbout", super.getTitle()));
		setContentPane(getMainContentPane());
		pack();
		setLocationRelativeTo(getOwner());
		getOkButton().requestFocus();
	}

	/**
	 * Get the main content pane.
	 * 
	 * @return the panel.
	 */
	private JPanel getMainContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(getDescription(), BorderLayout.CENTER);
			contentPane.add(getButtons(), BorderLayout.SOUTH);
		}
		return contentPane;
	}

	/**
	 * Get the description.
	 * 
	 * @return the description text.
	 */
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
	 * Get the buttons panel.
	 * 
	 * @return the panel.
	 */
	private JPanel getButtons() {
		if (buttons == null) {
			buttons = createButtonsPanel();
			buttons.add(getOkButton());
		}
		return buttons;
	}

	/**
	 * Get the "OK" button.
	 * 
	 * @return the button.
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = createOkButton();
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

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			if (Desktop.isDesktopSupported()) {
				// open link in browser.
				URL url = event.getURL();
				if (desktop == null)
					desktop = Desktop.getDesktop();
				if (desktop.isSupported(Action.BROWSE)) {
					try {
						desktop.browse(url.toURI());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
