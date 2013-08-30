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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.Settings;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.swing.SwingUtils;
import net.sf.swing.text.DocumentLengthFilter;

/**
 * jNCU settings dialog.
 * 
 * @author moshew
 */
public class JNCUSettingsDialog extends JNCUDialog {

	private static final int INSET_TAB = 10;
	private static final int INSET_CELL_Y = 0;
	private static final int INSET_CELL_X = 10;
	private static final String ELLIPSIS = "...";
	/** Maximum number of password characters (64 bits). */
	private static final int PASSWORD_LENGTH = 8;

	private JPanel contentPane;
	private JPanel buttons;
	private JButton okButton;
	private JButton cancelButton;
	private Settings settings;
	private JTabbedPane tabbedPane;
	private JPanel tabComm;
	private JButton applyButton;
	private JButton buttonHelp;
	private JPanel tabSecurity;
	private JPanel tabGeneral;
	private JPanel tabDock;
	private JLabel labelPort;
	private JLabel labelSpeed;
	private JComboBox<String> listPort;
	private JComboBox<Integer> listSpeed;
	private JCheckBox checkListen;
	private JLabel labelBackupPath;
	private JButton browseButton;
	private JFileChooser browser;
	private JPasswordField passwordOld;
	private JPasswordField passwordNew;
	private JPasswordField passwordConfirm;
	private JCheckBox checkDockBackup;
	private JCheckBox checkDockBackupSelective;
	private JCheckBox checkDockSync;
	private DocumentFilter passwordFilter;

	/**
	 * Create a new settings dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public JNCUSettingsDialog(Window owner) {
		super(owner);
		init();
	}

	/**
	 * Initialise.
	 */
	private void init() {
		setContentPane(getMainContentPane());
		setSize(400, 260);
		SwingUtils.centreInOwner(this);
		getCancelButton().requestFocus();
	}

	/**
	 * Get the main content pane.
	 * 
	 * @return the panel.
	 */
	private JPanel getMainContentPane() {
		if (contentPane == null) {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(getTabbedPane(), BorderLayout.CENTER);
			panel.add(getButtons(), BorderLayout.SOUTH);
			contentPane = panel;
		}
		return contentPane;
	}

	/**
	 * Get the buttons pane.
	 * 
	 * @return the panel.
	 */
	private JPanel getButtons() {
		if (buttons == null) {
			buttons = createButtonsPanel();
			buttons.add(getOkButton());
			buttons.add(getCancelButton());
			buttons.add(getApplyButton());
			buttons.add(getHelpButton());
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
	 * Get the settings to populate.
	 * 
	 * @return the settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Set the settings with updated values.
	 * 
	 * @param settings
	 *            the settings to set.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;

		JComboBox<String> portNames = getListPorts();
		portNames.removeAllItems();
		String portSet = settings.getCommunications().getPortIdentifier();
		int portIndex = 0;
		int i = 0;
		for (String portId : settings.getCommunications().getPorts()) {
			portNames.addItem(portId);
			if (portId.equals(portSet)) {
				portIndex = i;
			}
			i++;
		}
		portNames.setSelectedIndex(portIndex);
		JComboBox<Integer> speeds = getListSpeeds();
		speeds.setSelectedItem(settings.getCommunications().getPortSpeed());
		getCheckListen().setSelected(settings.getCommunications().isListen());

		getBackupPath().setText(settings.getGeneral().getBackupFolder().getPath());

		getCheckDockBackup().setSelected(settings.getAutoDock().isBackup());
		getCheckDockBackupSelective().setSelected(settings.getAutoDock().isBackupSelective());
		getCheckDockSync().setSelected(settings.getAutoDock().isSync());

		getPasswordOld().setText(null);
		getPasswordNew().setText(null);
		getPasswordConfirm().setText(null);
	}

	/**
	 * Get the categories tabs.
	 * 
	 * @return the tabs.
	 */
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab(JNCUResources.getString("communications", "Communications"), getTabComm());
			tabbedPane.addTab(JNCUResources.getString("security", "Security"), getTabSecurity());
			tabbedPane.addTab(JNCUResources.getString("general", "General"), getTabGeneral());
			tabbedPane.addTab(JNCUResources.getString("autoDock", "Auto Dock"), getTabDock());
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(1, false);
			tabbedPane.setEnabledAt(3, false);
		}
		return tabbedPane;
	}

	/**
	 * Get the communications category tab.
	 * 
	 * @return the tab.
	 */
	private JPanel getTabComm() {
		if (tabComm == null) {
			GridBagConstraints gbcListen = new GridBagConstraints();
			gbcListen.gridx = 0;
			gbcListen.gridy = 0;
			gbcListen.gridwidth = 2;
			gbcListen.weightx = 1.0;
			gbcListen.weighty = 1.0;
			gbcListen.anchor = GridBagConstraints.WEST;
			gbcListen.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);

			labelPort = new JLabel();
			labelPort.setText(JNCUResources.getString("serialPortName", "Port:"));
			GridBagConstraints gbcLabelPort = new GridBagConstraints();
			gbcLabelPort.gridx = 0;
			gbcLabelPort.gridy = 1;
			gbcLabelPort.weightx = 1.0;
			gbcLabelPort.weighty = 1.0;
			gbcLabelPort.anchor = GridBagConstraints.EAST;
			gbcLabelPort.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);

			GridBagConstraints gbcListPort = new GridBagConstraints();
			gbcListPort.gridx = 1;
			gbcListPort.gridy = 1;
			gbcListPort.weightx = 1.0;
			gbcListPort.anchor = GridBagConstraints.WEST;
			gbcListPort.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);

			labelSpeed = new JLabel();
			labelSpeed.setText(JNCUResources.getString("serialPortSpeed", "Speed:"));
			GridBagConstraints gbcLabelSpeed = new GridBagConstraints();
			gbcLabelSpeed.gridx = 0;
			gbcLabelSpeed.gridy = 2;
			gbcLabelSpeed.weightx = 1.0;
			gbcLabelSpeed.weighty = 1.0;
			gbcLabelSpeed.anchor = GridBagConstraints.EAST;
			gbcLabelSpeed.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);

			GridBagConstraints gbcListSpeed = new GridBagConstraints();
			gbcListSpeed.gridx = 1;
			gbcListSpeed.gridy = 2;
			gbcListSpeed.weightx = 1.0;
			gbcListSpeed.anchor = GridBagConstraints.WEST;
			gbcListSpeed.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);

			JPanel tab = new JPanel();
			tab.setLayout(new GridBagLayout());
			tab.add(getCheckListen(), gbcListen);
			tab.add(labelPort, gbcLabelPort);
			tab.add(getListPorts(), gbcListPort);
			tab.add(labelSpeed, gbcLabelSpeed);
			tab.add(getListSpeeds(), gbcListSpeed);
			tab.setBorder(BorderFactory.createEmptyBorder(INSET_TAB, INSET_TAB, INSET_TAB, INSET_TAB));
			tabComm = tab;
		}
		return tabComm;
	}

	/**
	 * Get the" apply" button.
	 * 
	 * @return the button.
	 */
	private JButton getApplyButton() {
		if (applyButton == null) {
			URL url = getClass().getResource("/dialog-apply.png");
			Icon icon = new ImageIcon(url);

			JButton button = createButton();
			button.setText(JNCUResources.getString("apply", "Apply"));
			button.setMnemonic(JNCUResources.getChar("applyMnemonic", KeyEvent.VK_A));
			button.setIcon(icon);
			button.setEnabled(false);
			applyButton = button;
		}
		return applyButton;
	}

	/**
	 * Get the "help" button.
	 * 
	 * @return the button.
	 */
	private JButton getHelpButton() {
		if (buttonHelp == null) {
			URL url = getClass().getResource("/dialog-help.png");
			Icon icon = new ImageIcon(url);

			JButton button = createButton();
			button.setText(JNCUResources.getString("help", "Help"));
			button.setMnemonic(JNCUResources.getChar("helpMnemonic", KeyEvent.VK_H));
			button.setIcon(icon);
			button.setEnabled(false);
			buttonHelp = button;
		}
		return buttonHelp;
	}

	/**
	 * Get the security category tab.
	 * 
	 * @return the tab.
	 */
	private JPanel getTabSecurity() {
		if (tabSecurity == null) {
			JLabel labelOld = new JLabel();
			labelOld.setText(JNCUResources.getString("passwordOld", "Old Password:"));
			GridBagConstraints gbcLabelOld = new GridBagConstraints();
			gbcLabelOld.gridx = 0;
			gbcLabelOld.gridy = 0;
			gbcLabelOld.anchor = GridBagConstraints.EAST;
			gbcLabelOld.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcLabelOld.weightx = 1.0;
			gbcLabelOld.weighty = 1.0;
			GridBagConstraints gbcPassOld = new GridBagConstraints();
			gbcPassOld.gridx = 1;
			gbcPassOld.gridy = 0;
			gbcPassOld.anchor = GridBagConstraints.WEST;
			gbcPassOld.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcPassOld.weightx = 1.0;
			gbcPassOld.weighty = 1.0;

			JLabel labelNew = new JLabel();
			labelNew.setText(JNCUResources.getString("passwordNew", "New Password:"));
			GridBagConstraints gbcLabelNew = new GridBagConstraints();
			gbcLabelNew.gridx = 0;
			gbcLabelNew.gridy = 1;
			gbcLabelNew.anchor = GridBagConstraints.EAST;
			gbcLabelNew.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcLabelNew.weightx = 1.0;
			gbcLabelNew.weighty = 1.0;
			GridBagConstraints gbcPassNew = new GridBagConstraints();
			gbcPassNew.gridx = 1;
			gbcPassNew.gridy = 1;
			gbcPassNew.anchor = GridBagConstraints.WEST;
			gbcPassNew.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcPassNew.weightx = 1.0;
			gbcPassNew.weighty = 1.0;

			JLabel labelConfirm = new JLabel();
			labelConfirm.setText(JNCUResources.getString("passwordConfirm", "Confirm Password:"));
			GridBagConstraints gbcLabelConfirm = new GridBagConstraints();
			gbcLabelConfirm.gridx = 0;
			gbcLabelConfirm.gridy = 2;
			gbcLabelConfirm.anchor = GridBagConstraints.EAST;
			gbcLabelConfirm.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcLabelConfirm.weightx = 1.0;
			gbcLabelConfirm.weighty = 1.0;
			GridBagConstraints gbcPassConfirm = new GridBagConstraints();
			gbcPassConfirm.gridx = 1;
			gbcPassConfirm.gridy = 2;
			gbcPassConfirm.anchor = GridBagConstraints.WEST;
			gbcPassConfirm.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcPassConfirm.weightx = 1.0;
			gbcPassConfirm.weighty = 1.0;

			JPanel tab = new JPanel();
			tab.setLayout(new GridBagLayout());
			tab.setBorder(BorderFactory.createEmptyBorder(INSET_TAB, INSET_TAB, INSET_TAB, INSET_TAB));
			tab.add(labelOld, gbcLabelOld);
			tab.add(getPasswordOld(), gbcPassOld);
			tab.add(labelNew, gbcLabelNew);
			tab.add(getPasswordNew(), gbcPassNew);
			tab.add(labelConfirm, gbcLabelConfirm);
			tab.add(getPasswordConfirm(), gbcPassConfirm);
			tabSecurity = tab;
		}
		return tabSecurity;
	}

	/**
	 * Get the general category tab.
	 * 
	 * @return the tab.
	 */
	private JPanel getTabGeneral() {
		if (tabGeneral == null) {
			JLabel labelFolder = new JLabel();
			labelFolder.setText(JNCUResources.getString("backupFolder", "Folder:"));
			GridBagConstraints gbcLabelFolder = new GridBagConstraints();
			gbcLabelFolder.gridx = 0;
			gbcLabelFolder.gridy = 0;
			gbcLabelFolder.anchor = GridBagConstraints.WEST;
			gbcLabelFolder.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcLabelFolder.weightx = 1.0;
			gbcLabelFolder.weighty = 1.0;

			GridBagConstraints gbcValueFolder = new GridBagConstraints();
			gbcValueFolder.gridx = 0;
			gbcValueFolder.gridy = 1;
			gbcValueFolder.anchor = GridBagConstraints.WEST;
			gbcValueFolder.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcValueFolder.weightx = 1.0;
			gbcValueFolder.weighty = 1.0;
			gbcValueFolder.fill = GridBagConstraints.HORIZONTAL;

			GridBagConstraints gbcBrowseFolder = new GridBagConstraints();
			gbcBrowseFolder.gridx = 0;
			gbcBrowseFolder.gridy = 2;
			gbcBrowseFolder.anchor = GridBagConstraints.EAST;
			gbcBrowseFolder.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcBrowseFolder.weightx = 1.0;
			gbcBrowseFolder.weighty = 1.0;

			JPanel tab = new JPanel();
			tab.setLayout(new GridBagLayout());
			tab.add(labelFolder, gbcLabelFolder);
			tab.add(getBackupPath(), gbcValueFolder);
			tab.add(getBrowseButton(), gbcBrowseFolder);
			tab.setBorder(BorderFactory.createEmptyBorder(INSET_TAB, INSET_TAB, INSET_TAB, INSET_TAB));
			tabGeneral = tab;
		}
		return tabGeneral;
	}

	/**
	 * Get the auto-dock category tab.
	 * 
	 * @return the tab.
	 */
	private JPanel getTabDock() {
		if (tabDock == null) {
			GridBagConstraints gbcBackup = new GridBagConstraints();
			gbcBackup.gridx = 0;
			gbcBackup.gridy = 0;
			gbcBackup.gridwidth = 2;
			gbcBackup.anchor = GridBagConstraints.WEST;
			gbcBackup.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcBackup.weightx = 1.0;
			gbcBackup.weighty = 1.0;

			GridBagConstraints gbcBackupSel = new GridBagConstraints();
			gbcBackupSel.gridx = 1;
			gbcBackupSel.gridy = 1;
			gbcBackupSel.anchor = GridBagConstraints.WEST;
			gbcBackupSel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X << 2, INSET_CELL_Y, INSET_CELL_X);
			gbcBackupSel.weightx = 1.0;
			gbcBackupSel.weighty = 1.0;

			GridBagConstraints gbcSync = new GridBagConstraints();
			gbcSync.gridx = 0;
			gbcSync.gridy = 2;
			gbcSync.gridwidth = 2;
			gbcSync.anchor = GridBagConstraints.WEST;
			gbcSync.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
			gbcSync.weightx = 1.0;
			gbcSync.weighty = 1.0;

			JPanel tab = new JPanel();
			tab.setLayout(new GridBagLayout());
			tab.setBorder(BorderFactory.createEmptyBorder(INSET_TAB, INSET_TAB, INSET_TAB, INSET_TAB));
			tab.add(getCheckDockBackup(), gbcBackup);
			tab.add(getCheckDockBackupSelective(), gbcBackupSel);
			tab.add(getCheckDockSync(), gbcSync);
			tabDock = tab;
		}
		return tabDock;
	}

	/**
	 * Get the list of port names.
	 * 
	 * @return the list.
	 */
	private JComboBox<String> getListPorts() {
		if (listPort == null) {
			JComboBox<String> list = new JComboBox<String>();
			listPort = list;
		}
		return listPort;
	}

	/**
	 * Get the list of port speeds.
	 * 
	 * @return the list.
	 */
	private JComboBox<Integer> getListSpeeds() {
		if (listSpeed == null) {
			JComboBox<Integer> list = new JComboBox<Integer>();
			list.addItem(MNPSerialPort.BAUD_2400);
			list.addItem(MNPSerialPort.BAUD_4800);
			list.addItem(MNPSerialPort.BAUD_9600);
			list.addItem(MNPSerialPort.BAUD_38400);
			list.addItem(MNPSerialPort.BAUD_57600);
			listSpeed = list;
		}
		return listSpeed;
	}

	/**
	 * Get the check box to enable listening for Newton.
	 * 
	 * @return the check box.
	 */
	private JCheckBox getCheckListen() {
		if (checkListen == null) {
			checkListen = new JCheckBox();
			checkListen.setText(JNCUResources.getString("listen", "Listen"));
			checkListen.setOpaque(false);
		}
		return checkListen;
	}

	/**
	 * GEt the button to browse for backup folder.
	 * 
	 * @return the button.
	 */
	private JButton getBrowseButton() {
		if (browseButton == null) {
			URL url = getClass().getResource("/browse.png");
			Icon icon = new ImageIcon(url);

			JButton button = createButton();
			button.setText(JNCUResources.getString("browse", "Browse")+ ELLIPSIS);
			button.setMnemonic(JNCUResources.getChar("browseMnemonic", KeyEvent.VK_B));
			button.setIcon(icon);
			browseButton = button;
		}
		return browseButton;
	}

	/**
	 * Get the browser for backup folder.
	 * 
	 * @return the chooser.
	 */
	private JFileChooser getBrowser() {
		if (browser == null) {
			browser = new JFileChooser();
			browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		return browser;
	}

	/**
	 * Save the settings.
	 */
	public void save() {
		Settings settings = getSettings();

		settings.getCommunications().setListen(getCheckListen().isSelected());
		settings.getCommunications().setPortIdentifier((String) getListPorts().getSelectedItem());
		settings.getCommunications().setPortSpeed((Integer) getListSpeeds().getSelectedItem());

		settings.getAutoDock().setBackup(getCheckDockBackup().isSelected());
		settings.getAutoDock().setBackupSelective(getCheckDockBackupSelective().isSelected());
		settings.getAutoDock().setSync(getCheckDockSync().isSelected());

		String passwordOld = String.valueOf(getPasswordOld().getPassword());
		String passwordNew = String.valueOf(getPasswordNew().getPassword());
		String passwordConfirm = String.valueOf(getPasswordConfirm().getPassword());
		if (passwordNew.equals(passwordConfirm)) {
			settings.getSecurity().setPasswordOld(passwordOld);
			settings.getSecurity().setPasswordNew(passwordNew);
		}

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
			JFileChooser browser = getBrowser();
			browser.setSelectedFile(settings.getGeneral().getBackupFolder());
			int ret = browser.showOpenDialog(getBrowseButton());
			if (ret == JFileChooser.APPROVE_OPTION) {
				File selectedFile = browser.getSelectedFile();
				getBackupPath().setText(selectedFile.getPath());
				settings.getGeneral().setBackupFolder(selectedFile);
			}
		} else if (src == checkDockBackup) {
			getCheckDockBackupSelective().setEnabled(checkDockBackup.isSelected());
		}
	}

	/**
	 * Get the confirmation password.
	 * 
	 * @return the password.
	 */
	private JPasswordField getPasswordConfirm() {
		if (passwordConfirm == null) {
			JPasswordField password = new JPasswordField(PASSWORD_LENGTH + 4);
			((AbstractDocument) password.getDocument()).setDocumentFilter(getPasswordFilter());
			passwordConfirm = password;
		}
		return passwordConfirm;
	}

	/**
	 * Get the new password.
	 * 
	 * @return the password.
	 */
	private JPasswordField getPasswordNew() {
		if (passwordNew == null) {
			JPasswordField password = new JPasswordField(PASSWORD_LENGTH + 4);
			((AbstractDocument) password.getDocument()).setDocumentFilter(getPasswordFilter());
			passwordNew = password;
		}
		return passwordNew;
	}

	/**
	 * Get the old password.
	 * 
	 * @return the password.
	 */
	private JPasswordField getPasswordOld() {
		if (passwordOld == null) {
			JPasswordField password = new JPasswordField(PASSWORD_LENGTH + 4);
			((AbstractDocument) password.getDocument()).setDocumentFilter(getPasswordFilter());
			passwordOld = password;
		}
		return passwordOld;
	}

	/**
	 * Get the check box to backup on auto-dock.
	 * 
	 * @return the check box.
	 */
	private JCheckBox getCheckDockBackup() {
		if (checkDockBackup == null) {
			JCheckBox check = new JCheckBox();
			check.setText(JNCUResources.getString("backup", "Backup"));
			check.setOpaque(false);
			check.addActionListener(this);
			checkDockBackup = check;
		}
		return checkDockBackup;
	}

	/**
	 * Get the check box to backup selectively on auto-dock.
	 * 
	 * @return the check box.
	 */
	private JCheckBox getCheckDockBackupSelective() {
		if (checkDockBackupSelective == null) {
			JCheckBox check = new JCheckBox();
			check.setText(JNCUResources.getString("backupSelective", "Selective"));
			check.setOpaque(false);
			check.setEnabled(false);
			checkDockBackupSelective = check;
		}
		return checkDockBackupSelective;
	}

	/**
	 * Get the check box to sync on auto-dock.
	 * 
	 * @return the check box.
	 */
	private JCheckBox getCheckDockSync() {
		if (checkDockSync == null) {
			JCheckBox check = new JCheckBox();
			check.setText(JNCUResources.getString("sync", "Synchronize"));
			check.setOpaque(false);
			checkDockSync = check;
		}
		return checkDockSync;
	}

	/**
	 * Get the password filter.
	 * 
	 * @return the filter.
	 */
	private DocumentFilter getPasswordFilter() {
		if (passwordFilter == null) {
			passwordFilter = new DocumentLengthFilter(PASSWORD_LENGTH);
		}
		return passwordFilter;
	}

	/**
	 * Get the backup path label.
	 * 
	 * @return the label.
	 */
	private JLabel getBackupPath() {
		if (labelBackupPath == null) {
			labelBackupPath = new JLabel();
			labelBackupPath.setText(".");
		}
		return labelBackupPath;
	}
}
