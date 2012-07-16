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
package net.sf.jncu.protocol.v2_0.sync;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import net.sf.swing.SwingUtils;

/**
 * Backup options dialog.
 * 
 * @author mwaisberg
 */
public class BackupDialog extends JDialog {

	static {
		SwingUtils.init();
	}

	private static final String TITLE = "Backup";
	private JButton cancelButton;

	/**
	 * Creates a new dialog.
	 */
	public BackupDialog() {
		super();
		init();
	}

	/**
	 * Creates a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public BackupDialog(Frame owner) {
		super(owner, true);
		init();
	}

	/**
	 * Creates a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public BackupDialog(Dialog owner) {
		super(owner, true);
		init();
	}

	/**
	 * Creates a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public BackupDialog(Window owner) {
		super(owner, ModalityType.APPLICATION_MODAL);
		init();
	}

	/**
	 * Initialise.
	 */
	private void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(TITLE);
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);

		JPanel panelContents = new JPanel();
		panelContents.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panelContents.setOpaque(false);
		panelContents.setLayout(new BorderLayout(5, 5));
		setContentPane(panelContents);

		JPanel panelStores = new JPanel();
		panelStores.setOpaque(false);
		GridBagConstraints gbc_panelStores = new GridBagConstraints();
		gbc_panelStores.insets = new Insets(0, 0, 5, 5);
		gbc_panelStores.fill = GridBagConstraints.BOTH;
		gbc_panelStores.gridx = 0;
		gbc_panelStores.gridy = 4;
		panelContents.add(panelStores, BorderLayout.WEST);
		panelStores.setLayout(new BorderLayout(5, 5));
		panelStores.setBorder(BorderFactory.createTitledBorder("Backup From:"));

		JList listStores = new JList();
		listStores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelStores.add(listStores, BorderLayout.CENTER);
		listStores.setVisibleRowCount(5);
		listStores.setModel(new AbstractListModel() {
			String[] values = new String[] { "Internal" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});

		JPanel panelStoresButtons = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelStoresButtons.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		panelStores.add(panelStoresButtons, BorderLayout.SOUTH);

		JButton btnStoresSelectAll = new JButton("Select All");
		panelStoresButtons.add(btnStoresSelectAll);

		JButton btnStoresClearAll = new JButton("Clear All");
		panelStoresButtons.add(btnStoresClearAll);

		JPanel panelInfo = new JPanel();
		panelInfo.setOpaque(false);
		GridBagConstraints gbc_panelSoups = new GridBagConstraints();
		gbc_panelSoups.insets = new Insets(0, 0, 5, 0);
		gbc_panelSoups.fill = GridBagConstraints.BOTH;
		gbc_panelSoups.gridx = 1;
		gbc_panelSoups.gridy = 4;
		panelContents.add(panelInfo, BorderLayout.EAST);
		panelInfo.setLayout(new BorderLayout(5, 5));
		panelInfo.setBorder(BorderFactory.createTitledBorder("Information:"));

		JPanel panelInfoButtons = new JPanel();
		panelInfoButtons.setOpaque(false);
		FlowLayout flowLayout_1 = (FlowLayout) panelInfoButtons.getLayout();
		flowLayout_1.setAlignment(FlowLayout.TRAILING);
		panelInfo.add(panelInfoButtons, BorderLayout.SOUTH);

		JButton btnInfoSelectAll = new JButton("Select All");
		panelInfoButtons.add(btnInfoSelectAll);

		JButton btnInfoClearAll = new JButton("Clear All");
		panelInfoButtons.add(btnInfoClearAll);

		JPanel panelInfoCenter = new JPanel();
		panelInfoCenter.setOpaque(false);
		panelInfo.add(panelInfoCenter, BorderLayout.CENTER);
		panelInfoCenter.setLayout(new BorderLayout(0, 0));

		JList listInformation = new JList();
		listInformation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelInfoCenter.add(listInformation, BorderLayout.CENTER);
		listInformation.setModel(new AbstractListModel() {
			String[] values = new String[] { "Calls", "SBM", "System information", "TermLimit", "Time Zones", "Other information" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listInformation.setVisibleRowCount(5);

		JCheckBox checkPackages = new JCheckBox("Include All Packages");
		panelInfoCenter.add(checkPackages, BorderLayout.SOUTH);
		checkPackages.setSelected(true);

		JPanel panelButtons = new JPanel();
		panelButtons.setOpaque(false);
		panelContents.add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));

		JButton btnBackup = new JButton("Backup");
		panelButtons.add(btnBackup);

		panelButtons.add(getCancelButton());

		pack();
		SwingUtils.centreInOwner(this);
	}

	public static void main(String[] args) {
		BackupDialog dialog = new BackupDialog();
		dialog.setVisible(true);
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
	 * Close the window.
	 */
	public void close() {
		if (isVisible()) {
			setVisible(false);
			SwingUtils.postWindowClosing(this);
		}
	}

}
