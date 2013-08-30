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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.sf.jncu.Controller;
import net.sf.jncu.JNCUApp;
import net.sf.jncu.JNCUResources;
import net.sf.swing.SwingUtils;

/**
 * jNCU main frame.
 * 
 * @author moshew
 */
public class JNCUFrame extends JFrame implements ActionListener {

	static {
		SwingUtils.init();
	}

	private static final int INSET_X = 20;
	private static final int INSET_Y = 10;
	private static final int INSET_BUTTON = 10;
	private static final String ELLIPSIS = "...";

	private final JNCUFrame frame;
	private JPanel contentPane;
	private JMenuBar mainMenu;
	private JMenu menuFile;
	private JMenu menuNewton;
	private JMenu menuHelp;
	private JMenuItem menuExit;
	private JMenuItem menuSettings;
	private JMenuItem menuBackup;
	private JMenuItem menuRestore;
	private JMenuItem menuInstall;
	private JMenuItem menuKeyboard;
	private JMenuItem menuAbout;
	private JMenuItem menuImport;
	private JMenuItem menuExport;
	private JMenuItem menuSync;
	private JMenuItem menuSyncSettings;
	private JMenuItem menuDeviceInfo;
	private JPanel statusPanel;
	private JLabel statusLabel;
	private JLabel statusConnection;
	private JPanel quickMenuPane;
	private JButton syncButton;
	private JButton installButton;
	private JButton keyboardButton;
	private JButton backupButton;
	private JButton restoreButton;
	private JButton importButton;
	private JButton exportButton;
	private JNCUAboutDialog aboutDialog;
	private Controller control;
	private Icon statusDisconnected;
	private Icon statusConnected;

	/**
	 * Create a new frame.
	 */
	public JNCUFrame() {
		super();
		this.frame = this;
		getControl();
		init();
	}

	/**
	 * Get the main menu.
	 * 
	 * @return the menu bar.
	 */
	private JMenuBar getMainMenu() {
		if (mainMenu == null) {
			mainMenu = new JMenuBar();
			mainMenu.add(getMenuFile());
			mainMenu.add(getMenuNewton());
			mainMenu.add(getMenuHelp());
		}
		return mainMenu;
	}

	/**
	 * Get the "File" menu.
	 * 
	 * @return the menu.
	 */
	private JMenu getMenuFile() {
		if (menuFile == null) {
			menuFile = new JMenu();
			menuFile.setText(JNCUResources.getString("file", "File"));
			menuFile.setMnemonic(JNCUResources.getChar("fileMnemonic", KeyEvent.VK_F));
			menuFile.add(getMenuImport());
			menuFile.add(getMenuExport());
			menuFile.addSeparator();
			menuFile.add(getMenuSync());
			menuFile.add(getMenuSyncSettings());
			menuFile.addSeparator();
			menuFile.add(getMenuExit());
		}
		return menuFile;
	}

	/**
	 * Get the "File | Import" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuImport() {
		if (menuImport == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("import", "Import") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("importMnemonic", KeyEvent.VK_I));
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuImport = menuItem;
		}
		return menuImport;
	}

	/**
	 * Get the "File | Export" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuExport() {
		if (menuExport == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("export", "Export") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("exportMnemonic", KeyEvent.VK_E));
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuExport = menuItem;
		}
		return menuExport;
	}

	/**
	 * Get the "File | Sync" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuSync() {
		if (menuSync == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("sync", "Synchronize") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("syncMnemonic", KeyEvent.VK_S));
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuSync = menuItem;
		}
		return menuSync;
	}

	/**
	 * Get the "File | Sync Settings" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuSyncSettings() {
		if (menuSyncSettings == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("synSettings", "Synchronization Settings") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("synSettingsMnemonic", KeyEvent.VK_T));
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuSyncSettings = menuItem;
		}
		return menuSyncSettings;
	}

	/**
	 * Get the "Newton" menu.
	 * 
	 * @return the menu.
	 */
	private JMenu getMenuNewton() {
		if (menuNewton == null) {
			JMenu menu = new JMenu();
			menu.setText(JNCUResources.getString("newton", "Newton"));
			menu.setMnemonic(JNCUResources.getChar("newtonMnemonic", KeyEvent.VK_N));
			menu.add(getMenuBackup());
			menu.add(getMenuRestore());
			menu.addSeparator();
			menu.add(getMenuInstall());
			menu.add(getMenuKeyboard());
			menu.addSeparator();
			menu.add(getMenuDeviceInfo());
			menu.addSeparator();
			menu.add(getMenuSettings());
			menuNewton = menu;
		}
		return menuNewton;
	}

	/**
	 * Get the "Help" menu.
	 * 
	 * @return the menu.
	 */
	private JMenu getMenuHelp() {
		if (menuHelp == null) {
			JMenu menu = new JMenu();
			menu.setText(JNCUResources.getString("help", "Help"));
			menu.setMnemonic(JNCUResources.getChar("helpMnemonic", KeyEvent.VK_H));
			menu.add(getMenuAbout());
			menuHelp = menu;
		}
		return menuHelp;
	}

	/**
	 * Get the "File | Exit" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuExit() {
		if (menuExit == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("exit", "Exit"));
			menuItem.setMnemonic(JNCUResources.getChar("exitMnemonic", KeyEvent.VK_X));
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			menuExit = menuItem;
		}
		return menuExit;
	}

	/**
	 * Get the "Newton | Settings" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuSettings() {
		if (menuSettings == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("settings", "Settings") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("settingsMnemonic", KeyEvent.VK_S));
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			menuSettings = menuItem;
		}
		return menuSettings;
	}

	/**
	 * Get the "Newton | Backup" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuBackup() {
		if (menuBackup == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("backup", "Backup") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("backupMnemonic", KeyEvent.VK_B));
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuBackup = menuItem;
		}
		return menuBackup;
	}

	/**
	 * Get the "Newton | Restore" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuRestore() {
		if (menuRestore == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("restore", "Restore") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("restoreMnemonic", KeyEvent.VK_R));
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuRestore = menuItem;
		}
		return menuRestore;
	}

	/**
	 * Get the "Newton | Install Package" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuInstall() {
		if (menuInstall == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("install", "Install Packages") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("installMnemonic", KeyEvent.VK_I));
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuInstall = menuItem;
		}
		return menuInstall;
	}

	/**
	 * Get the "Newton | Keyboard" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuKeyboard() {
		if (menuKeyboard == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("keyboard", "Keyboard Passthrough") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("keyboardMnemonic", KeyEvent.VK_K));
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuKeyboard = menuItem;
		}
		return menuKeyboard;
	}

	/**
	 * Get the "Newton | Device Info" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuDeviceInfo() {
		if (menuDeviceInfo == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("devieInfo", "Device") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("devieInfoMnemonic", KeyEvent.VK_D));
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuDeviceInfo = menuItem;
		}
		return menuDeviceInfo;
	}

	/**
	 * Get the "Help | About" menu.
	 * 
	 * @return the menu item.
	 */
	private JMenuItem getMenuAbout() {
		if (menuAbout == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText(JNCUResources.getString("about", "About") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("aboutMnemonic", KeyEvent.VK_A));
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.ALT_MASK));
			menuItem.addActionListener(this);
			menuAbout = menuItem;

		}
		return menuAbout;
	}

	/**
	 * Get the status panel.
	 * 
	 * @return the panel.
	 */
	private JPanel getStatusPanel() {
		if (statusPanel == null) {
			JLabel label = new JLabel();
			label.setMinimumSize(new Dimension(0, 24));
			label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			statusConnection = label;

			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("connectStatus", "Status")));
			panel.setOpaque(false);
			panel.add(statusConnection, BorderLayout.CENTER);
			JPanel statusConnectionPanel = panel;

			label = new JLabel();
			label.setMinimumSize(new Dimension(0, 24));
			label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			statusLabel = label;

			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.setOpaque(false);
			panel.add(statusConnectionPanel, BorderLayout.CENTER);
			panel.add(statusLabel, BorderLayout.SOUTH);
			statusPanel = panel;
		}
		return statusPanel;
	}

	/**
	 * Set the connection status.
	 * 
	 * @param connected
	 *            is connected?
	 */
	private void setStatusConnected(boolean connected) {
		if (connected) {
			if (statusConnected == null) {
				URL url = getClass().getResource("/connected.png");
				statusConnected = new ImageIcon(url);
			}

			statusConnection.setIcon(statusConnected);
			statusConnection.setText(JNCUResources.getString("connectConnected", "Connected to Newton device."));
		} else {
			if (statusDisconnected == null) {
				URL url = getClass().getResource("/disconnected.png");
				statusDisconnected = new ImageIcon(url);
			}

			statusConnection.setIcon(statusDisconnected);
			statusConnection.setText(JNCUResources.getString("connectPlease", "Connect your Newton device."));
		}
	}

	/**
	 * Initialize.
	 */
	private void init() {
		setTitle(JNCUResources.getString("jncu", null));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		List<Image> images = new ArrayList<Image>();
		URL url;
		Image image;
		try {
			url = getClass().getResource("/icon-64.png");
			image = ImageIO.read(url);
			images.add(image);
			url = getClass().getResource("/icon-48.png");
			image = ImageIO.read(url);
			images.add(image);
			url = getClass().getResource("/icon-32.png");
			image = ImageIO.read(url);
			images.add(image);
			url = getClass().getResource("/icon-16.png");
			image = ImageIO.read(url);
			images.add(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setIconImages(images);

		setJMenuBar(getMainMenu());
		setContentPane(getMainContentPane());
		setMinimumSize(new Dimension(450, 440));
		pack();
		setResizable(false);
		SwingUtils.centreInOwner(this);

		setStatusConnected(false);
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
			contentPane.add(getQuickMenuPane(), BorderLayout.CENTER);
			contentPane.add(getStatusPanel(), BorderLayout.SOUTH);
		}
		return contentPane;
	}

	/**
	 * Close the frame.
	 */
	public void close() {
		if (isShowing()) {
			getControl().close();
			SwingUtils.postWindowClosing(frame);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		final Object src = event.getSource();

		if (src == menuExit) {
			close();
		} else if (src == menuSettings) {
			settings();
		} else if (src == menuKeyboard) {
			keyboard();
		} else if (src == menuAbout) {
			about();
		} else if (src == syncButton) {
			sync();
		} else if (src == installButton) {
			install();
		} else if (src == keyboardButton) {
			keyboard();
		} else if (src == backupButton) {
			backupToDesktop();
		} else if (src == exportButton) {
			exportToDesktop();
		} else if (src == restoreButton) {
			restoreToNewton();
		} else if (src == importButton) {
			importToNewton();
		} else if (src == menuDeviceInfo) {
			showDevice();
		}
	}

	/**
	 * Get the quick menu pane with big icon buttons.
	 * 
	 * @return the panel.
	 */
	private JPanel getQuickMenuPane() {
		if (quickMenuPane == null) {
			JPanel utilsGroup = new JPanel();
			utilsGroup.setOpaque(false);
			utilsGroup.setLayout(new GridBagLayout());
			utilsGroup.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("basicUtilities", "Basic Utilities")));
			GridBagConstraints gbcSync = new GridBagConstraints();
			gbcSync.gridx = 0;
			gbcSync.insets = new Insets(INSET_Y, INSET_X, INSET_Y, INSET_X);
			utilsGroup.add(getSyncButton(), gbcSync);
			GridBagConstraints gbcInstall = new GridBagConstraints();
			gbcInstall.gridx = 1;
			gbcInstall.insets = new Insets(INSET_Y, INSET_X, INSET_Y, INSET_X);
			utilsGroup.add(getInstallButton(), gbcInstall);
			GridBagConstraints gbcKeyboard = new GridBagConstraints();
			gbcKeyboard.gridx = 2;
			gbcKeyboard.insets = new Insets(INSET_Y, INSET_X, INSET_Y, INSET_X);
			utilsGroup.add(getKeyboardButton(), gbcKeyboard);

			JPanel exportGroup = new JPanel();
			exportGroup.setOpaque(false);
			exportGroup.setLayout(new GridBagLayout());
			exportGroup.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("moveToDesktop", "Move to PC")));
			GridBagConstraints gbcBackup = new GridBagConstraints();
			gbcBackup.gridx = 0;
			gbcBackup.insets = new Insets(INSET_Y, INSET_X, INSET_Y, INSET_X);
			exportGroup.add(getBackupButton(), gbcBackup);
			GridBagConstraints gbcExport = new GridBagConstraints();
			gbcExport.gridx = 1;
			gbcExport.insets = new Insets(INSET_Y, INSET_X, INSET_Y, INSET_X);
			exportGroup.add(getExportButton(), gbcExport);

			JPanel importGroup = new JPanel();
			importGroup.setOpaque(false);
			importGroup.setLayout(new GridBagLayout());
			importGroup.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("moveToDevice", "Move to device")));
			GridBagConstraints gbcRestore = new GridBagConstraints();
			gbcRestore.gridx = 0;
			gbcRestore.insets = new Insets(INSET_Y, INSET_X, INSET_Y, INSET_X);
			importGroup.add(getRestoreButton(), gbcRestore);
			GridBagConstraints gbcImport = new GridBagConstraints();
			gbcImport.gridx = 1;
			gbcImport.insets = new Insets(INSET_Y, INSET_X, INSET_Y, INSET_X);
			importGroup.add(getImportButton(), gbcImport);

			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(3, 1));
			panel.add(utilsGroup);
			panel.add(exportGroup);
			panel.add(importGroup);
			quickMenuPane = panel;
		}
		return quickMenuPane;
	}

	/**
	 * Get the "Sync" icon button.
	 * 
	 * @return the button.
	 */
	private JButton getSyncButton() {
		if (syncButton == null) {
			URL url = getClass().getResource("/sync.png");
			Icon icon = new ImageIcon(url);

			syncButton = new JButton(icon);
			syncButton.setToolTipText(JNCUResources.getString("sync", "Synchronize"));
			syncButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			syncButton.addActionListener(this);
			syncButton.setEnabled(false);
		}
		return syncButton;
	}

	/**
	 * Get the "Install Package" icon button.
	 * 
	 * @return the button.
	 */
	private JButton getInstallButton() {
		if (installButton == null) {
			URL url = getClass().getResource("/pkg.png");
			Icon icon = new ImageIcon(url);

			installButton = new JButton(icon);
			installButton.setToolTipText(JNCUResources.getString("install", "Install"));
			installButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			installButton.addActionListener(this);
			installButton.setEnabled(false);
		}
		return installButton;
	}

	/**
	 * Get the "Use Keyboard" icon button.
	 * 
	 * @return the button.
	 */
	private JButton getKeyboardButton() {
		if (keyboardButton == null) {
			URL url = getClass().getResource("/kbd.png");
			Icon icon = new ImageIcon(url);

			keyboardButton = new JButton(icon);
			keyboardButton.setToolTipText(JNCUResources.getString("keyboard", "Use Keyboard"));
			keyboardButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			keyboardButton.addActionListener(this);
			keyboardButton.setEnabled(false);
		}
		return keyboardButton;
	}

	/**
	 * Get the "Backup" icon button.
	 * 
	 * @return the button.
	 */
	private JButton getBackupButton() {
		if (backupButton == null) {
			URL url = getClass().getResource("/backup.png");
			Icon icon = new ImageIcon(url);

			backupButton = new JButton(icon);
			backupButton.setToolTipText(JNCUResources.getString("backup", "Backup"));
			backupButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			backupButton.addActionListener(this);
			backupButton.setEnabled(false);
		}
		return backupButton;
	}

	/**
	 * Get the "Restore" icon button.
	 * 
	 * @return the button.
	 */
	private JButton getRestoreButton() {
		if (restoreButton == null) {
			URL url = getClass().getResource("/restore.png");
			Icon icon = new ImageIcon(url);

			restoreButton = new JButton(icon);
			restoreButton.setToolTipText(JNCUResources.getString("restore", "Restore"));
			restoreButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			restoreButton.addActionListener(this);
			restoreButton.setEnabled(false);
		}
		return restoreButton;
	}

	/**
	 * Get the "Import" icon button.
	 * 
	 * @return the button.
	 */
	private JButton getImportButton() {
		if (importButton == null) {
			URL url = getClass().getResource("/import.png");
			Icon icon = new ImageIcon(url);

			importButton = new JButton(icon);
			importButton.setToolTipText(JNCUResources.getString("import", "Import"));
			importButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			importButton.addActionListener(this);
			importButton.setEnabled(false);
		}
		return importButton;
	}

	/**
	 * Get the "Export" icon button.
	 * 
	 * @return the button.
	 */
	private JButton getExportButton() {
		if (exportButton == null) {
			URL url = getClass().getResource("/export.png");
			Icon icon = new ImageIcon(url);

			exportButton = new JButton(icon);
			exportButton.setToolTipText(JNCUResources.getString("export", "Export"));
			exportButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			exportButton.addActionListener(this);
			exportButton.setEnabled(false);
		}
		return exportButton;
	}

	/**
	 * Synchronize.
	 */
	private void sync() {
		getControl().sync();
	}

	/**
	 * Install package.
	 */
	private void install() {
		getControl().install();
	}

	/**
	 * Use keyboard.
	 */
	private void keyboard() {
		getControl().keyboard();
	}

	/**
	 * Backup.
	 */
	private void backupToDesktop() {
		getControl().backupToDesktop();
	}

	/**
	 * Export.
	 */
	private void exportToDesktop() {
		getControl().exportToDesktop();
	}

	/**
	 * Restore.
	 */
	private void restoreToNewton() {
		getControl().restoreToNewton();
	}

	/**
	 * Import.
	 */
	private void importToNewton() {
		getControl().importToNewton();
	}

	/**
	 * Get the "about jNCU" dialog.
	 * 
	 * @return the dialog.
	 */
	private JNCUAboutDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new JNCUAboutDialog(this);
		}
		return aboutDialog;
	}

	/**
	 * Show the "about jNCU" dialog.
	 */
	private void about() {
		getAboutDialog().setVisible(true);
	}

	/**
	 * Show the settings dialog.
	 */
	private void settings() {
		try {
			getControl().showSettings();
		} catch (Exception e) {
			showError(e);
		}
	}

	/**
	 * Set the controller.
	 * 
	 * @param control
	 *            the controller.
	 */
	public void setController(Controller control) {
		this.control = control;
	}

	/**
	 * Get the controller.
	 * 
	 * @return the controller.
	 */
	protected Controller getControl() {
		return control;
	}

	/**
	 * Show an error to the user.
	 * 
	 * @param e
	 *            the error.
	 */
	protected void showError(Exception e) {
		JNCUApp.showError(this, e);
	}

	/**
	 * Set the status label.
	 * 
	 * @param status
	 *            the status text.
	 */
	public void setStatus(String status) {
		statusLabel.setText(status);
	}

	/**
	 * Show the device information.
	 */
	private void showDevice() {
		getControl().showDevice();
	}
}
