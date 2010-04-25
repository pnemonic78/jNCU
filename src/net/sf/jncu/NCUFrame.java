package net.sf.jncu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * Main NCU frame.
 * 
 * @author moshew
 */
public class NCUFrame extends JFrame {

	private final JFrame jFrame;
	private JPanel jContentPane = null;
	private JMenuBar mainMenu = null;
	private JMenu menuFile = null; // @jve:decl-index=0:visual-constraint="264,82"
	private JMenu menuNewton = null;
	private JMenu menuHelp = null;
	private JMenuItem menuExit = null;
	private JMenuItem menuSettings = null;
	private JMenuItem menuBackup = null;
	private JMenuItem menuRestore = null;
	private JMenuItem menuInstall = null;
	private JMenuItem menuKeyboard = null;
	private JMenuItem menuAbout = null;
	private NCUSettings settingsDialog = null;
	private Settings settings = null;
	private JPanel panelStatus = null;
	private JLabel labelStatus = null; // @jve:decl-index=0:

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
			menuHelp.setText("Help");
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
			menuExit = new JMenuItem();
			menuExit.setText("Exit");
			menuExit.setMnemonic(KeyEvent.VK_X);
			menuExit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					close();
				}
			});
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
			menuSettings = new JMenuItem();
			menuSettings.setMnemonic(KeyEvent.VK_S);
			menuSettings.setText("Settings...");
			menuSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			menuSettings.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Settings settings = getSettings();
					// comm.stopListenForNewton();
					getSettingsDialog().setVisible(true);
					if (settings.isListen()) {
						// comm.startListenForNewton(settings.getPortIdentifier(),
						// settings.getPortSpeed());
					}
				}
			});
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
			menuBackup = new JMenuItem();
			menuBackup.setText("Backup...");
			menuBackup.setEnabled(false);
			menuBackup.setMnemonic(KeyEvent.VK_B);
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
			menuRestore = new JMenuItem();
			menuRestore.setText("Restore...");
			menuRestore.setEnabled(false);
			menuRestore.setMnemonic(KeyEvent.VK_R);
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
			menuInstall = new JMenuItem();
			menuInstall.setMnemonic(KeyEvent.VK_I);
			menuInstall.setEnabled(false);
			menuInstall.setText("Install Packages...");
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
			menuKeyboard = new JMenuItem();
			menuKeyboard.setText("Keyboard Passthrough");
			menuKeyboard.setEnabled(false);
			menuKeyboard.setMnemonic(KeyEvent.VK_K);
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
			menuAbout = new JMenuItem();
			menuAbout.setText("About...");
			menuAbout.setMnemonic(KeyEvent.VK_A);
		}
		return menuAbout;
	}

	/**
	 * This method initializes panelStatus
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanelStatus() {
		if (panelStatus == null) {
			labelStatus = new JLabel();
			labelStatus.setText("For help, press F1");
			panelStatus = new JPanel();
			panelStatus.setLayout(new BoxLayout(getPanelStatus(), BoxLayout.X_AXIS));
			panelStatus.setPreferredSize(new Dimension(0, 24));
			panelStatus.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
			panelStatus.add(labelStatus, null);
		}
		return panelStatus;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NCUFrame thisClass = new NCUFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public NCUFrame() {
		super();
		this.jFrame = this;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(450, 440);
		this.setJMenuBar(getMainMenu());
		this.setContentPane(getJContentPane());
		this.setTitle("jNewton Connection Utility");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		this.setLocation((screenSize.width - this.getWidth()) >> 1, (screenSize.height - this.getHeight()) >> 1);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPanelStatus(), BorderLayout.SOUTH);
		}
		return jContentPane;
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

	public void close() {
		// comm.stopListenForNewton();
		dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
	}
}
