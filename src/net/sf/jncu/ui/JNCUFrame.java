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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import net.sf.jncu.JNCUApp;
import net.sf.jncu.JNCUController;
import net.sf.jncu.JNCUResources;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.protocol.v2_0.session.DWhichIcons;
import net.sf.swing.SwingUtils;

/**
 * jNCU main frame.
 * 
 * @author moshew
 */
public class JNCUFrame extends JFrame implements ActionListener, WindowListener {

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
	private JNCUController controller;
	private Icon statusDisconnectedIcon;
	private Icon statusConnectedIcon;

	/**
	 * Create a new frame.
	 */
	public JNCUFrame() {
		super();
		this.frame = this;
		getController();
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
			JMenu menu = new JMenu();
			menu.setText(JNCUResources.getString("file"));
			menu.setMnemonic(JNCUResources.getChar("fileMnemonic", KeyEvent.VK_F));
			menu.add(getMenuImport());
			menu.add(getMenuExport());
			menu.addSeparator();
			menu.add(getMenuExit());
			menuFile = menu;
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
			menuItem.setText(JNCUResources.getString("import") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("importMnemonic", KeyEvent.VK_I));
			menuItem.setIcon(JNCUResources.getIcon("/menu/import.png"));
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
			menuItem.setText(JNCUResources.getString("export") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("exportMnemonic", KeyEvent.VK_E));
			menuItem.setIcon(JNCUResources.getIcon("/menu/export.png"));
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
			menuItem.setText(JNCUResources.getString("sync") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("syncMnemonic", KeyEvent.VK_S));
			menuItem.setIcon(JNCUResources.getIcon("/menu/sync.png"));
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
			menuItem.setText(JNCUResources.getString("synSettings") + ELLIPSIS);
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
			menu.setText(JNCUResources.getString("newton"));
			menu.setMnemonic(JNCUResources.getChar("newtonMnemonic", KeyEvent.VK_N));
			menu.add(getMenuSync());
			menu.add(getMenuBackup());
			menu.add(getMenuRestore());
			menu.addSeparator();
			menu.add(getMenuInstall());
			menu.add(getMenuKeyboard());
			menu.addSeparator();
			menu.add(getMenuDeviceInfo());
			menu.addSeparator();
			menu.add(getMenuSettings());
			menu.add(getMenuSyncSettings());
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
			menu.setText(JNCUResources.getString("help"));
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
			menuItem.setText(JNCUResources.getString("exit"));
			menuItem.setMnemonic(JNCUResources.getChar("exitMnemonic", KeyEvent.VK_X));
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
			menuItem.setIcon(JNCUResources.getIcon("/menu/exit.png"));
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
			menuItem.setText(JNCUResources.getString("settings") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("settingsMnemonic", KeyEvent.VK_S));
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			menuItem.setIcon(JNCUResources.getIcon("/menu/settings.png"));
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
			menuItem.setText(JNCUResources.getString("backup") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("backupMnemonic", KeyEvent.VK_B));
			menuItem.setIcon(JNCUResources.getIcon("/menu/backup.png"));
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
			menuItem.setText(JNCUResources.getString("restore") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("restoreMnemonic", KeyEvent.VK_R));
			menuItem.setIcon(JNCUResources.getIcon("/menu/restore.png"));
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
			menuItem.setText(JNCUResources.getString("install") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("installMnemonic", KeyEvent.VK_I));
			menuItem.setIcon(JNCUResources.getIcon("/menu/pkg.png"));
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
			menuItem.setText(JNCUResources.getString("keyboard") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("keyboardMnemonic", KeyEvent.VK_K));
			menuItem.setIcon(JNCUResources.getIcon("/menu/keyboard.png"));
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
			menuItem.setText(JNCUResources.getString("deviceInfo") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("deviceInfoMnemonic", KeyEvent.VK_D));
			menuItem.setIcon(JNCUResources.getIcon("/menu/pda.png"));
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
			menuItem.setText(JNCUResources.getString("about") + ELLIPSIS);
			menuItem.setMnemonic(JNCUResources.getChar("aboutMnemonic", KeyEvent.VK_A));
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.ALT_MASK));
			menuItem.setIcon(JNCUResources.getIcon("/menu/about.png"));
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
			panel.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("connectStatus")));
			panel.setOpaque(false);
			panel.add(statusConnection, BorderLayout.CENTER);
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
	public void setConnected(boolean connected) {
		getMenuDeviceInfo().setEnabled(connected);
		if (connected) {
			if (statusConnectedIcon == null) {
				statusConnectedIcon = JNCUResources.getIcon("/connected.png");
			}

			NewtonInfo info = getController().getDeviceInformation();
			String deviceName = info.getName();
			statusConnection.setIcon(statusConnectedIcon);
			statusConnection.setText(String.format(JNCUResources.getString("connectConnected"), deviceName));
		} else {
			if (statusDisconnectedIcon == null) {
				statusDisconnectedIcon = JNCUResources.getIcon("/disconnected.png");
			}

			statusConnection.setIcon(statusDisconnectedIcon);
			statusConnection.setText(JNCUResources.getString("connectPlease"));
			setIcons(DWhichIcons.NONE);
		}
	}

	/**
	 * Initialize.
	 */
	private void init() {
		setTitle(JNCUResources.getString("jncu"));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(this);

		List<Image> images = new ArrayList<Image>();
		URL url;
		Image image;
		try {
			url = getClass().getResource("/icon/64.png");
			image = ImageIO.read(url);
			images.add(image);
			url = getClass().getResource("/icon/48.png");
			image = ImageIO.read(url);
			images.add(image);
			url = getClass().getResource("/icon/32.png");
			image = ImageIO.read(url);
			images.add(image);
			url = getClass().getResource("/icon/16.png");
			image = ImageIO.read(url);
			images.add(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setIconImages(images);

		setJMenuBar(getMainMenu());
		setContentPane(getMainContentPane());
		setResizable(false);
		setMinimumSize(new Dimension(450, 440));
		pack();
		setLocationRelativeTo(getOwner());

		setConnected(false);
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
			SwingUtils.postWindowClosing(frame);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		final Object src = event.getSource();

		if (src == menuExit) {
			close();
		} else if (src == menuSettings) {
			showSettings();
		} else if (src == menuKeyboard) {
			keyboard();
		} else if (src == menuAbout) {
			about();
		} else if (src == syncButton) {
			sync();
		} else if (src == installButton) {
			installPackage();
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
			utilsGroup.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("basicUtilities")));
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
			exportGroup.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("moveToDesktop")));
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
			importGroup.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("moveToDevice")));
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
			JButton button = new JButton();
			button.setIcon(JNCUResources.getIcon("/sync.png"));
			button.setToolTipText(JNCUResources.getString("sync"));
			button.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			button.addActionListener(this);
			button.setEnabled(false);
			syncButton = button;
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
			JButton button = new JButton();
			button.setIcon(JNCUResources.getIcon("/pkg.png"));
			button.setToolTipText(JNCUResources.getString("install"));
			button.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			button.addActionListener(this);
			button.setEnabled(false);
			installButton = button;
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
			JButton button = new JButton();
			button.setIcon(JNCUResources.getIcon("/keyboard.png"));
			button.setToolTipText(JNCUResources.getString("keyboard"));
			button.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			button.addActionListener(this);
			button.setEnabled(false);
			keyboardButton = button;
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
			JButton button = new JButton();
			button.setIcon(JNCUResources.getIcon("/backup.png"));
			button.setToolTipText(JNCUResources.getString("backup"));
			button.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			button.addActionListener(this);
			button.setEnabled(false);
			backupButton = button;
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
			JButton button = new JButton();
			button.setIcon(JNCUResources.getIcon("/restore.png"));
			button.setToolTipText(JNCUResources.getString("restore"));
			button.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			button.addActionListener(this);
			button.setEnabled(false);
			restoreButton = button;
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
			JButton button = new JButton();
			button.setIcon(JNCUResources.getIcon("/import.png"));
			button.setToolTipText(JNCUResources.getString("import"));
			button.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			button.addActionListener(this);
			button.setEnabled(false);
			importButton = button;
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
			JButton button = new JButton();
			button.setIcon(JNCUResources.getIcon("/export.png"));
			button.setToolTipText(JNCUResources.getString("export"));
			button.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON, INSET_BUTTON, INSET_BUTTON));
			button.addActionListener(this);
			button.setEnabled(false);
			exportButton = button;
		}
		return exportButton;
	}

	/**
	 * Synchronize.
	 */
	private void sync() {
		getController().sync();
	}

	/**
	 * Install package.
	 */
	private void installPackage() {
		getController().installPackage();
	}

	/**
	 * Use keyboard.
	 */
	private void keyboard() {
		getController().keyboard();
	}

	/**
	 * Backup.
	 */
	private void backupToDesktop() {
		getController().backupToDesktop();
	}

	/**
	 * Export.
	 */
	private void exportToDesktop() {
		getController().exportToDesktop();
	}

	/**
	 * Restore.
	 */
	private void restoreToNewton() {
		getController().restoreToNewton();
	}

	/**
	 * Import.
	 */
	private void importToNewton() {
		getController().importToNewton();
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
	private void showSettings() {
		try {
			getController().showSettings();
		} catch (Exception e) {
			showError("Show settings", e);
		}
	}

	/**
	 * Set the controller.
	 * 
	 * @param control
	 *            the controller.
	 */
	public void setController(JNCUController control) {
		this.controller = control;
	}

	/**
	 * Get the controller.
	 * 
	 * @return the controller.
	 */
	protected JNCUController getController() {
		return controller;
	}

	/**
	 * Show an error to the user.
	 * 
	 * @param message
	 *            the message.
	 * @param e
	 *            the error.
	 */
	protected void showError(String message, Throwable e) {
		JNCUApp.showError(this, message, e);
	}

	/**
	 * Show the device information.
	 */
	private void showDevice() {
		getController().showDevice();
	}

	/**
	 * Set which icons to enable.
	 * 
	 * @param icons
	 *            the icons.
	 * @see DWhichIcons
	 */
	public void setIcons(int icons) {
		final boolean backupIcon = (icons & DWhichIcons.BACKUP) == DWhichIcons.BACKUP;
		final boolean importIcon = (icons & DWhichIcons.IMPORT) == DWhichIcons.IMPORT;
		final boolean installIcon = (icons & DWhichIcons.INSTALL) == DWhichIcons.INSTALL;
		final boolean keyboardIcon = (icons & DWhichIcons.KEYBOARD) == DWhichIcons.KEYBOARD;
		final boolean restoreIcon = (icons & DWhichIcons.RESTORE) == DWhichIcons.RESTORE;
		final boolean syncIcon = (icons & DWhichIcons.SYNC) == DWhichIcons.SYNC;

		getMenuBackup().setEnabled(backupIcon);
		getBackupButton().setEnabled(backupIcon);

		getMenuExport().setEnabled(importIcon);
		getExportButton().setEnabled(importIcon);

		getMenuImport().setEnabled(importIcon);
		getImportButton().setEnabled(importIcon);

		getMenuInstall().setEnabled(installIcon);
		getInstallButton().setEnabled(installIcon);

		getMenuKeyboard().setEnabled(keyboardIcon);
		getKeyboardButton().setEnabled(keyboardIcon);

		getMenuRestore().setEnabled(restoreIcon);
		getRestoreButton().setEnabled(restoreIcon);

		getMenuSync().setEnabled(syncIcon);
		getMenuSyncSettings().setEnabled(syncIcon);
		getSyncButton().setEnabled(syncIcon);
	}

	@Override
	public void windowOpened(WindowEvent event) {
	}

	@Override
	public void windowClosing(WindowEvent event) {
		getController().exit();
	}

	@Override
	public void windowClosed(WindowEvent event) {
	}

	@Override
	public void windowIconified(WindowEvent event) {
	}

	@Override
	public void windowDeiconified(WindowEvent event) {
	}

	@Override
	public void windowActivated(WindowEvent event) {
	}

	@Override
	public void windowDeactivated(WindowEvent event) {
	}
}
