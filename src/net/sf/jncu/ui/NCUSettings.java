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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import net.sf.jncu.Settings;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.swing.SwingUtils;

/**
 * jNCU settings dialog.
 * 
 * @author moshew
 */
public class NCUSettings extends JDialog implements ActionListener {

	private static final String TITLE = "jNewton Connection Utility";

	private JPanel contentPane;
	private JPanel buttons;
	private JButton okButton;
	private JButton cancelButton;
	private Dimension buttonMinimumSize;
	private Settings settings;
	private JTabbedPane tabbedPane;
	private JPanel tabComm;
	private JButton applyButton;
	private JButton buttonHelp;
	private JPanel tabPassword;
	private JPanel tabDock;
	private JLabel labelPort;
	private JLabel labelSpeed;
	private JComboBox/* <String> */listPort;
	private JComboBox/* <Integer> */listSpeed;
	private JCheckBox checkListen;
	private JLabel labelFolder;
	private JLabel labelFolderPath;
	private JButton browseButton;
	private JFileChooser browser;

	/**
	 * @param owner
	 */
	public NCUSettings(Frame owner) {
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
			contentPane.add(getTabbedPane(), BorderLayout.CENTER);
		}
		return contentPane;
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
			buttons.add(getCancelButton(), null);
			buttons.add(getApplyButton(), null);
			buttons.add(getButtonHelp(), null);
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

	/**
	 * This method initializes buttonCancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton(Toolkit.getProperty("AWT.cancel",
					"Cancel"));
			cancelButton.addActionListener(this);
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.setMinimumSize(buttonMinimumSize);
		}
		return cancelButton;
	}

	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * @param settings
	 *            the settings to set
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;

		JComboBox/* <String> */ports = getListPort();
		ports.removeAllItems();
		for (String port : settings.getPorts()) {
			ports.addItem(port);
		}
		JComboBox/* <Integer> */speed = getListSpeed();
		speed.setSelectedItem(settings.getPortSpeed());
		getCheckListen().setSelected(settings.isListen());

		labelFolderPath.setText(settings.getBackupFolder().getPath());
	}

	/**
	 * This method initializes tabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Communications", getTabComm());
			tabbedPane.addTab("Password", getTabPassword());
			tabbedPane.addTab("Auto Dock", getTabDock());
			tabbedPane.setSelectedIndex(0);
		}
		return tabbedPane;
	}

	/**
	 * This method initializes tabComm
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTabComm() {
		if (tabComm == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 2;
			gridBagConstraints31.gridy = 4;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.gridy = 4;
			labelFolderPath = new JLabel();
			labelFolderPath.setText("c:\\");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridy = 3;
			labelFolder = new JLabel();
			labelFolder.setText("Default folder for backup files:");
			labelSpeed = new JLabel();
			labelSpeed.setText("Speed:");
			labelPort = new JLabel();
			labelPort.setText("Port:");

			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.NONE;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			tabComm = new JPanel();
			tabComm.setLayout(new GridBagLayout());
			tabComm.add(labelPort, gridBagConstraints);
			tabComm.add(labelSpeed, gridBagConstraints1);
			tabComm.add(getListPort(), gridBagConstraints2);
			tabComm.add(getListSpeed(), gridBagConstraints3);
			tabComm.add(getCheckListen(), gridBagConstraints4);
			tabComm.add(labelFolder, gridBagConstraints11);
			tabComm.add(labelFolderPath, gridBagConstraints21);
			tabComm.add(getBrowseButton(), gridBagConstraints31);
		}
		return tabComm;
	}

	/**
	 * This method initializes buttonApply
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getApplyButton() {
		if (applyButton == null) {
			applyButton = new JButton();
			applyButton.setText("Apply");
			applyButton.setMnemonic(KeyEvent.VK_A);
			applyButton.setMinimumSize(buttonMinimumSize);
			applyButton.addActionListener(this);
		}
		return applyButton;
	}

	/**
	 * This method initializes buttonHelp
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getButtonHelp() {
		if (buttonHelp == null) {
			buttonHelp = new JButton();
			buttonHelp.setText("Help");
			buttonHelp.setMnemonic(KeyEvent.VK_H);
			buttonHelp.setMinimumSize(buttonMinimumSize);
		}
		return buttonHelp;
	}

	/**
	 * This method initializes tabPassword
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTabPassword() {
		if (tabPassword == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			tabPassword = new JPanel();
			tabPassword.setLayout(new GridBagLayout());
		}
		return tabPassword;
	}

	/**
	 * This method initializes tabDock
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTabDock() {
		if (tabDock == null) {
			tabDock = new JPanel();
			tabDock.setLayout(new GridBagLayout());
		}
		return tabDock;
	}

	/**
	 * This method initializes listPort
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox/* <String> */getListPort() {
		if (listPort == null) {
			listPort = new JComboBox/* <String> */();
		}
		return listPort;
	}

	/**
	 * This method initializes listSpeed
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox/* <Integer> */getListSpeed() {
		if (listSpeed == null) {
			listSpeed = new JComboBox/* <Integer> */();
			listSpeed.addItem(MNPSerialPort.BAUD_2400);
			listSpeed.addItem(MNPSerialPort.BAUD_4800);
			listSpeed.addItem(MNPSerialPort.BAUD_9600);
			listSpeed.addItem(MNPSerialPort.BAUD_38400);
			listSpeed.addItem(MNPSerialPort.BAUD_57600);
		}
		return listSpeed;
	}

	/**
	 * This method initializes checkListen
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCheckListen() {
		if (checkListen == null) {
			checkListen = new JCheckBox();
			checkListen.setText("Always listen for Netwon Device");
		}
		return checkListen;
	}

	/**
	 * This method initializes buttonBrowse
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBrowseButton() {
		if (browseButton == null) {
			browseButton = new JButton();
			browseButton.setText("Browse...");
			browseButton.setMnemonic(KeyEvent.VK_B);
			browseButton.addActionListener(this);
		}
		return browseButton;
	}

	public JFileChooser getBrowser() {
		if (browser == null) {
			browser = new JFileChooser();
			browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		return browser;
	}

	public void close() {
		if (isShowing()) {
			SwingUtils.postWindowClosing(this);
		}
	}

	public void save() {
		Settings settings = getSettings();
		settings.setPortIdentifier((String) getListPort().getSelectedItem());
		settings.setPortSpeed((Integer) getListSpeed().getSelectedItem());
		settings.setListen(getCheckListen().isSelected());
		settings.setBackupFolder(labelFolderPath.getText());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		final Object src = event.getSource();

		if (src == cancelButton) {
			close();
		} else if (src == okButton) {
			save();
			close();
		} else if (src == applyButton) {
			save();
		} else if (src == browseButton) {
			int ret = getBrowser().showOpenDialog(getBrowseButton());
			if (ret == JFileChooser.APPROVE_OPTION) {
				labelFolderPath.setText(getBrowser().getSelectedFile()
						.getPath());
			}
		}
	}
}
