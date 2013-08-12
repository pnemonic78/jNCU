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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import net.sf.swing.SwingUtils;

/**
 * Backup progress.
 * 
 * @author Moshe
 */
public class BackupProgressDialog extends JDialog {

	static {
		SwingUtils.init();
	}

	private JLabel lblNote;

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
	 */
	public BackupProgressDialog(Frame owner) {
		super(owner);
		init();
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 */
	public BackupProgressDialog(Dialog owner) {
		super(owner);
		init();
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 */
	public BackupProgressDialog(Window owner) {
		super(owner);
		init();
	}

	private void init() {
		setTitle("Backup Progress");
		setModalityType(ModalityType.APPLICATION_MODAL);

		JPanel panelMain = new JPanel();
		panelMain.setBorder(new EmptyBorder(10, 5, 5, 5));
		panelMain.setOpaque(false);
		panelMain.setLayout(new BorderLayout(10, 10));
		setContentPane(panelMain);

		JPanel panelButtons = new JPanel();
		panelButtons.setOpaque(false);
		FlowLayout flowLayout = (FlowLayout) panelButtons.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		panelMain.add(panelButtons, BorderLayout.SOUTH);

		JButton btnCancel = new JButton(Toolkit.getProperty("AWT.cancel", "Cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BackupProgressDialog.this.setVisible(false);
			}
		});
		panelButtons.add(btnCancel);

		JProgressBar progressBar = new JProgressBar();
		panelMain.add(progressBar, BorderLayout.NORTH);
		progressBar.setIndeterminate(true);

		lblNote = new JLabel("Note");
		panelMain.add(lblNote, BorderLayout.CENTER);

		setMinimumSize(new Dimension(320, 140));
		SwingUtils.centreInOwner(this);
	}

	protected JLabel getNote() {
		return lblNote;
	}

	public void setNote(String text) {
		getNote().setText(text);
	}
}
