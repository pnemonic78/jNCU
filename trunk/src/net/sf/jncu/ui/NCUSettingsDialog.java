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
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
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
public class NCUSettingsDialog extends JDialog implements ActionListener {

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
	private JPanel tabGeneral;
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
	public NCUSettingsDialog(Frame owner) {
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
		getCancelButton().requestFocus();
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
			contentPane.add(getTabbedPane(), BorderLayout.CENTER);
			contentPane.add(getButtons(), BorderLayout.SOUTH);
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
			buttons.add(getHelpButton(), null);
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

	/**
	 * This method initializes buttonCancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			URL url = getClass().getResource("/dialog-cancel.png");
			Icon icon = new ImageIcon(url);

			JButton button = new JButton();
			button.setMnemonic(KeyEvent.VK_C);
			button.setText(Toolkit.getProperty("AWT.cancel", "Cancel"));
			button.setIcon(icon);
			button.setMinimumSize(buttonMinimumSize);
			button.addActionListener(this);
			cancelButton = button;
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

		JComboBox/* <String> */ports = getListPorts();
		ports.removeAllItems();
		for (String port : settings.getPorts()) {
			ports.addItem(port);
		}
		ports.setSelectedItem(settings.getPortIdentifier());
		JComboBox/* <Integer> */speeds = getListSpeeds();
		speeds.setSelectedItem(settings.getPortSpeed());
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
			tabbedPane.addTab("General", getTabGeneral());
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
			labelSpeed = new JLabel();
			labelSpeed.setText("Speed:");
			GridBagConstraints gbcListSpeed = new GridBagConstraints();
			gbcListSpeed.fill = GridBagConstraints.NONE;
			gbcListSpeed.gridy = 1;
			gbcListSpeed.gridx = 1;
			gbcListSpeed.weightx = 1.0;
			gbcListSpeed.anchor = GridBagConstraints.WEST;
			labelPort = new JLabel();
			labelPort.setText("Serial Port:");
			GridBagConstraints gbcLabelPort = new GridBagConstraints();
			gbcLabelPort.gridx = 0;
			gbcLabelPort.gridy = 0;
			GridBagConstraints gbcListPort = new GridBagConstraints();
			gbcListPort.fill = GridBagConstraints.NONE;
			gbcListPort.gridx = 1;
			gbcListPort.gridy = 0;
			gbcListPort.weightx = 1.0;
			gbcListPort.anchor = GridBagConstraints.WEST;
			GridBagConstraints gbcLabelSpeed = new GridBagConstraints();
			gbcLabelSpeed.gridx = 0;
			gbcLabelSpeed.gridy = 1;

			JPanel tab = new JPanel();
			tab.setLayout(new GridBagLayout());
			tab.add(labelPort, gbcLabelPort);
			tab.add(getListPorts(), gbcListPort);
			tab.add(labelSpeed, gbcLabelSpeed);
			tab.add(getListSpeeds(), gbcListSpeed);
			tabComm = tab;
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
			URL url = getClass().getResource("/dialog-apply.png");
			Icon icon = new ImageIcon(url);

			JButton button = new JButton();
			button.setMnemonic(KeyEvent.VK_A);
			button.setText("Apply");
			button.setIcon(icon);
			button.setMinimumSize(buttonMinimumSize);
			button.addActionListener(this);
			button.setEnabled(false);
			applyButton = button;
		}
		return applyButton;
	}

	/**
	 * This method initializes buttonHelp
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getHelpButton() {
		if (buttonHelp == null) {
			URL url = getClass().getResource("/dialog-help.png");
			Icon icon = new ImageIcon(url);

			JButton button = new JButton();
			button.setMnemonic(KeyEvent.VK_H);
			button.setText("Help");
			button.setIcon(icon);
			button.setMinimumSize(buttonMinimumSize);
			button.addActionListener(this);
			button.setEnabled(false);
			buttonHelp = button;
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
	 * This method initializes tabPassword
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTabGeneral() {
		if (tabGeneral == null) {
			GridBagConstraints gbcListen = new GridBagConstraints();
			gbcListen.gridx = 0;
			gbcListen.gridwidth = 2;
			gbcListen.anchor = GridBagConstraints.WEST;
			gbcListen.gridy = 2;
			labelFolder = new JLabel();
			labelFolder.setText("Default folder for backup files:");
			GridBagConstraints gbcLabelFolder = new GridBagConstraints();
			gbcLabelFolder.gridx = 0;
			gbcLabelFolder.gridwidth = 2;
			gbcLabelFolder.anchor = GridBagConstraints.WEST;
			gbcLabelFolder.gridy = 3;
			labelFolderPath = new JLabel();
			labelFolderPath.setText(".");
			GridBagConstraints gbcValueFolder = new GridBagConstraints();
			gbcValueFolder.gridx = 0;
			gbcValueFolder.gridwidth = 2;
			gbcValueFolder.anchor = GridBagConstraints.WEST;
			gbcValueFolder.gridy = 4;
			GridBagConstraints gbcBrowseFolder = new GridBagConstraints();
			gbcBrowseFolder.gridx = 2;
			gbcBrowseFolder.gridy = 4;

			JPanel tab = new JPanel();
			tab.setLayout(new GridBagLayout());
			tab.add(getCheckListen(), gbcListen);
			tab.add(labelFolder, gbcLabelFolder);
			tab.add(labelFolderPath, gbcValueFolder);
			tab.add(getBrowseButton(), gbcBrowseFolder);

			tabGeneral = tab;
		}
		return tabGeneral;
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
	private JComboBox/* <String> */getListPorts() {
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
	private JComboBox/* <Integer> */getListSpeeds() {
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
		settings.setPortIdentifier((String) getListPorts().getSelectedItem());
		settings.setPortSpeed((Integer) getListSpeeds().getSelectedItem());
		settings.setListen(getCheckListen().isSelected());
		settings.setBackupFolder(labelFolderPath.getText());
		settings.save();
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
