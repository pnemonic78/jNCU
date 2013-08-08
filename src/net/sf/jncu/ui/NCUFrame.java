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
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import net.sf.jncu.Settings;
import net.sf.jncu.protocol.v2_0.io.KeyboardInputDialog;
import net.sf.swing.SwingUtils;

/**
 * Main jNCU frame.
 * 
 * @author moshew
 */
public class NCUFrame extends JFrame implements ActionListener {

	static {
		SwingUtils.init();
	}

	private static final String TITLE = "jNewton Connection Utility";
	private static final int INSET_X = 20;
	private static final int INSET_Y = 20;
	private static final int INSET_BUTTON = 10;

	private final NCUFrame frame;
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
	private NCUSettings settingsDialog;
	private Settings settings;
	private JPanel statusPanel;
	private JLabel statusLabel;
	private KeyboardInputDialog keyboardDialog;
	private JPanel quickMenuPane;
	private JButton syncButton;
	private JButton installButton;
	private JButton keyboardButton;
	private JButton backupButton;
	private JButton restoreButton;
	private JButton importButton;
	private JButton exportButton;
	private NCUAbout aboutDialog;

	/**
	 * This method initializes mainMenu
	 * 
	 * @return javax.swing.JMenuBar
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
	 * This method initializes menuFile
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getMenuFile() {
		if (menuFile == null) {
			menuFile = new JMenu();
			menuFile.setText("File");
			menuFile.setMnemonic(KeyEvent.VK_F);
			menuFile.add(getMenuExit());
		}
		return menuFile;
	}

	/**
	 * This method initializes menuNewton
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getMenuNewton() {
		if (menuNewton == null) {
			menuNewton = new JMenu();
			menuNewton.setText("Newton");
			menuNewton.setMnemonic(KeyEvent.VK_N);
			menuNewton.add(getMenuBackup());
			menuNewton.add(getMenuRestore());
			menuNewton.add(getMenuInstall());
			menuNewton.add(getMenuKeyboard());
			menuNewton.addSeparator();
			menuNewton.add(getMenuSettings());
		}
		return menuNewton;
	}

	/**
	 * This method initializes menuHelp
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getMenuHelp() {
		if (menuHelp == null) {
			menuHelp = new JMenu();
			menuHelp.setMnemonic(KeyEvent.VK_H);
			menuHelp.setText(Toolkit.getProperty("AWT.help", "Help"));
			menuHelp.add(getMenuAbout());
		}
		return menuHelp;
	}

	/**
	 * This method initializes menuExit
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMenuExit() {
		if (menuExit == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText("Exit");
			menuItem.setMnemonic(KeyEvent.VK_X);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
					InputEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			menuExit = menuItem;
		}
		return menuExit;
	}

	/**
	 * This method initializes menuSettings
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMenuSettings() {
		if (menuSettings == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setMnemonic(KeyEvent.VK_S);
			menuItem.setText("Settings...");
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					InputEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			menuSettings = menuItem;
		}
		return menuSettings;
	}

	/**
	 * This method initializes menuBackup
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMenuBackup() {
		if (menuBackup == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText("Backup...");
			menuItem.setMnemonic(KeyEvent.VK_B);
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuBackup = menuItem;
		}
		return menuBackup;
	}

	/**
	 * This method initializes menuRestore
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMenuRestore() {
		if (menuRestore == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText("Restore...");
			menuItem.setMnemonic(KeyEvent.VK_R);
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuRestore = menuItem;
		}
		return menuRestore;
	}

	/**
	 * This method initializes menuInstall
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMenuInstall() {
		if (menuInstall == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setMnemonic(KeyEvent.VK_I);
			menuItem.setText("Install Packages...");
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuInstall = menuItem;
		}
		return menuInstall;
	}

	/**
	 * This method initializes menuKeyboard
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMenuKeyboard() {
		if (menuKeyboard == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText("Keyboard Passthrough...");
			menuItem.setMnemonic(KeyEvent.VK_K);
			menuItem.addActionListener(this);
			menuItem.setEnabled(false);
			menuKeyboard = menuItem;
		}
		return menuKeyboard;
	}

	/**
	 * This method initializes menuAbout
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMenuAbout() {
		if (menuAbout == null) {
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText("About...");
			menuItem.setMnemonic(KeyEvent.VK_A);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
					InputEvent.ALT_MASK));
			menuItem.addActionListener(this);
			menuAbout = menuItem;

		}
		return menuAbout;
	}

	/**
	 * This method initializes panelStatus
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanelStatus() {
		if (statusPanel == null) {
			statusLabel = new JLabel();
			statusLabel.setText("For help, press F1");
			statusPanel = new JPanel();
			statusPanel.setLayout(new BoxLayout(getPanelStatus(),
					BoxLayout.X_AXIS));
			statusPanel.setPreferredSize(new Dimension(0, 24));
			statusPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
			statusPanel.add(statusLabel, null);
		}
		return statusPanel;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				NCUFrame frame = new NCUFrame();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Create a new frame.
	 */
	public NCUFrame() {
		super();
		this.frame = this;
		init();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void init() {
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setJMenuBar(getMainMenu());
		setContentPane(getMainContentPane());

		setSize(450, 440);
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
			contentPane.add(getPanelStatus(), BorderLayout.SOUTH);
			contentPane.add(getQuickMenuPane(), BorderLayout.CENTER);
		}
		return contentPane;
	}

	private NCUSettings getSettingsDialog() {
		if (settingsDialog == null) {
			settingsDialog = new NCUSettings(this);
			settingsDialog.setSettings(getSettings());
		}
		return settingsDialog;
	}

	public Settings getSettings() {
		if (settings == null) {
			settings = new Settings();
		}
		return settings;
	}

	/**
	 * Close jNCU.
	 */
	public void close() {
		// comm.stopListenForNewton();
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
			settings();
		} else if (src == menuKeyboard) {
			getKeyboardDialog().setVisible(true);
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
		}
	}

	/**
	 * Get the keyboard input dialog.
	 * 
	 * @return the dialog.
	 */
	private KeyboardInputDialog getKeyboardDialog() {
		if (keyboardDialog == null) {
			keyboardDialog = new KeyboardInputDialog(frame);
		}
		return keyboardDialog;
	}

	private JPanel getQuickMenuPane() {
		if (quickMenuPane == null) {
			JPanel utilsGroup = new JPanel();
			utilsGroup.setOpaque(false);
			utilsGroup.setLayout(new GridBagLayout());
			utilsGroup.setBorder(BorderFactory
					.createTitledBorder("Basic Utilities"));
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
			exportGroup
					.setBorder(BorderFactory
							.createTitledBorder("Move information to your desktop computer"));
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
			importGroup
					.setBorder(BorderFactory
							.createTitledBorder("Move information to your Newton device"));
			GridBagConstraints gbcRestore = new GridBagConstraints();
			gbcRestore.gridx = 0;
			gbcRestore.insets = new Insets(INSET_Y, INSET_X, INSET_Y, INSET_X);
			importGroup.add(getRestoreButton(), gbcRestore);
			GridBagConstraints gbcImport = new GridBagConstraints();
			gbcImport.gridx = 1;
			gbcImport.insets = new Insets(INSET_Y, INSET_X, INSET_Y, INSET_X);
			importGroup.add(getImportButton(), gbcImport);

			quickMenuPane = new JPanel();
			quickMenuPane.setLayout(new GridLayout(3, 1));
			quickMenuPane.add(utilsGroup);
			quickMenuPane.add(exportGroup);
			quickMenuPane.add(importGroup);
		}
		return quickMenuPane;
	}

	/**
	 * @return the syncButton
	 */
	private JButton getSyncButton() {
		if (syncButton == null) {
			URL url = getClass().getResource("/sync.png");
			Icon icon = new ImageIcon(url);

			syncButton = new JButton(icon);
			syncButton.setToolTipText("Synchronize");
			syncButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON,
					INSET_BUTTON, INSET_BUTTON));
			syncButton.addActionListener(this);
			syncButton.setEnabled(false);
		}
		return syncButton;
	}

	/**
	 * @return the insallButton
	 */
	private JButton getInstallButton() {
		if (installButton == null) {
			URL url = getClass().getResource("/pkg.png");
			Icon icon = new ImageIcon(url);

			installButton = new JButton(icon);
			installButton.setToolTipText("Install Package");
			installButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON,
					INSET_BUTTON, INSET_BUTTON));
			installButton.addActionListener(this);
			installButton.setEnabled(false);
		}
		return installButton;
	}

	/**
	 * @return the keyboardButton
	 */
	private JButton getKeyboardButton() {
		if (keyboardButton == null) {
			URL url = getClass().getResource("/kbd.png");
			Icon icon = new ImageIcon(url);

			keyboardButton = new JButton(icon);
			keyboardButton.setToolTipText("Use Keyboard");
			keyboardButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON,
					INSET_BUTTON, INSET_BUTTON));
			keyboardButton.addActionListener(this);
			keyboardButton.setEnabled(false);
		}
		return keyboardButton;
	}

	/**
	 * @return the backupButton
	 */
	private JButton getBackupButton() {
		if (backupButton == null) {
			URL url = getClass().getResource("/backup.png");
			Icon icon = new ImageIcon(url);

			backupButton = new JButton(icon);
			backupButton.setToolTipText("Backup");
			backupButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON,
					INSET_BUTTON, INSET_BUTTON));
			backupButton.addActionListener(this);
			backupButton.setEnabled(false);
		}
		return backupButton;
	}

	/**
	 * @return the restoreButton
	 */
	private JButton getRestoreButton() {
		if (restoreButton == null) {
			URL url = getClass().getResource("/restore.png");
			Icon icon = new ImageIcon(url);

			restoreButton = new JButton(icon);
			restoreButton.setToolTipText("Restore");
			restoreButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON,
					INSET_BUTTON, INSET_BUTTON));
			restoreButton.addActionListener(this);
			restoreButton.setEnabled(false);
		}
		return restoreButton;
	}

	/**
	 * @return the importButton
	 */
	private JButton getImportButton() {
		if (importButton == null) {
			URL url = getClass().getResource("/import.png");
			Icon icon = new ImageIcon(url);

			importButton = new JButton(icon);
			importButton.setToolTipText("Import");
			importButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON,
					INSET_BUTTON, INSET_BUTTON));
			importButton.addActionListener(this);
			importButton.setEnabled(false);
		}
		return importButton;
	}

	/**
	 * @return the exportButton
	 */
	private JButton getExportButton() {
		if (exportButton == null) {
			URL url = getClass().getResource("/export.png");
			Icon icon = new ImageIcon(url);

			exportButton = new JButton(icon);
			exportButton.setToolTipText("Export");
			exportButton.setMargin(new Insets(INSET_BUTTON, INSET_BUTTON,
					INSET_BUTTON, INSET_BUTTON));
			exportButton.addActionListener(this);
			exportButton.setEnabled(false);
		}
		return exportButton;
	}

	/**
	 * Synchronize.
	 */
	private void sync() {
		// TODO implement me!
	}

	/**
	 * Install package.
	 */
	private void install() {
		// TODO implement me!
	}

	/**
	 * Use keyboard.
	 */
	private void keyboard() {
		getKeyboardDialog().setVisible(true);
	}

	/**
	 * Backup.
	 */
	private void backupToDesktop() {
		// TODO implement me!
	}

	/**
	 * Export.
	 */
	private void exportToDesktop() {
		// TODO implement me!
	}

	/**
	 * Restore.
	 */
	private void restoreToNewton() {
		// TODO implement me!
	}

	/**
	 * Import.
	 */
	private void importToNewton() {
		// TODO implement me!
	}

	private NCUAbout getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new NCUAbout(this);
		}
		return aboutDialog;
	}

	/**
	 * Show the 'about' dialog.
	 */
	private void about() {
		getAboutDialog().setVisible(true);
	}

	/**
	 * Show the settings dialog.
	 */
	private void settings() {
		Settings settings = getSettings();
		// comm.stopListenForNewton();
		getSettingsDialog().setVisible(true);
		if (settings.isListen()) {
			// comm.startListenForNewton(settings.getPortIdentifier(),
			// settings.getPortSpeed());
		}
	}
}
