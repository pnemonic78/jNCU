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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.sf.swing.SwingUtils;

/**
 * jNCU about dialog.
 * 
 * @author moshew
 */
public class NCUAbout extends JDialog implements ActionListener {

	private static final String TITLE = "jNewton Connection Utility";

	private JPanel contentPane;
	private JPanel buttons;
	private JButton okButton;
	private Dimension buttonMinimumSize;
	private JTextArea description;

	/**
	 * @param owner
	 */
	public NCUAbout(Frame owner) {
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
		setSize(400, 260);
		SwingUtils.centreInOwner(this);
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

	private JTextArea getDescription() {
		if (description == null) {
			StringBuilder buf = new StringBuilder();
			buf.append("Copyright (c) 2010-2013. All Rights Reserved.").append(
					'\n');
			buf.append("http://sourceforge.net/projects/jncu").append('\n');
			buf.append("http://jncu.sourceforge.net").append('\n');
			buf.append("Contributor(s):").append('\n');
			buf.append("    Moshe Waisberg").append('\n');

			description = new JTextArea(buf.toString());
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
			okButton = new JButton();
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.setText("OK");
			okButton.setMinimumSize(buttonMinimumSize);
			okButton.addActionListener(this);
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
}
