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
package net.sf.jncu.protocol.sync;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.ui.JNCUDialog;

/**
 * Backup progress dialog.
 * 
 * @author Moshe
 */
public class BackupProgressDialog extends JNCUDialog {

	private JLabel messageLabel;
	private JButton cancelButton;

	/**
	 * Constructs a new dialog.
	 */
	public BackupProgressDialog() {
		super();
		init();
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public BackupProgressDialog(Window owner) {
		super(owner);
		init();
	}

	/**
	 * Initialise.
	 */
	private void init() {
		setTitle(JNCUResources.getString("backupProgress", "Backup Progress"));

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);

		JPanel panelButtons = createButtonsPanel();
		panelButtons.add(getCancelButton());

		JPanel panelMain = new JPanel();
		panelMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelMain.setOpaque(false);
		panelMain.setLayout(new BorderLayout(10, 10));
		panelMain.add(progressBar, BorderLayout.NORTH);
		panelMain.add(getNote(), BorderLayout.CENTER);
		panelMain.add(panelButtons, BorderLayout.SOUTH);
		setContentPane(panelMain);

		setMinimumSize(new Dimension(320, 140));
		pack();
		setLocationRelativeTo(getOwner());
	}

	/**
	 * Get the "cancel" button.
	 * 
	 * @return the button.
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = createCancelButton();
		}
		return cancelButton;
	}

	/**
	 * Get the message label.
	 * 
	 * @return the label.
	 */
	protected JLabel getNote() {
		if (messageLabel == null) {
			messageLabel = new JLabel();
			messageLabel.setText(JNCUResources.getString("note", "Note"));
		}
		return messageLabel;
	}

	/**
	 * Set the message label text.
	 * 
	 * @param text
	 *            the text.
	 */
	public void setNote(String text) {
		getNote().setText(text);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		final Object src = event.getSource();

		if (src == cancelButton) {
			close();
		}
	}
}
