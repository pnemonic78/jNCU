package net.sf.jncu;

import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 * NCU settings.
 * 
 * @author moshew
 */
public class NCUSettings extends JDialog {

	private JPanel jContentPane = null;
	private JPanel buttons = null;
	private JButton buttonOk = null;
	private JButton buttonCancel = null;
	private Dimension buttonMinimumSize; // @jve:decl-index=0:
	private Settings settings; // @jve:decl-index=0:
	private JTabbedPane tabbedPane = null;
	private JPanel tabComm = null; // @jve:decl-index=0:visual-constraint="507,150"
	private JButton buttonApply = null;
	private JButton buttonHelp = null;
	private JPanel tabPassword = null; // @jve:decl-index=0:visual-constraint="532,140"
	private JPanel tabDock = null;
	private JLabel labelPort = null;
	private JLabel labelSpeed = null;
	private JComboBox listPort = null;
	private JComboBox listSpeed = null;
	private JCheckBox checkListen = null;
	private JLabel labelFolder = null;
	private JLabel labelFolderPath = null;
	private JButton buttonBrowse = null;
	private JFileChooser browser = null;

	/**
	 * @param owner
	 */
	public NCUSettings(Frame owner) {
		super(owner, true);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		int buttonMinimumWidth = UIManager
				.getInt("OptionPane.buttonMinimumWidth");
		buttonMinimumSize = new Dimension(buttonMinimumWidth, 24);
		this.setSize(400, 260);
		this.setTitle("jNewton Connection Utility");
		this.setContentPane(getJContentPane());
		this.setResizable(false);
		Container parent = getParent();
		Point locParent = parent.getLocation();
		Dimension sizeParent = parent.getSize();
		this.setLocation(locParent.x
				+ ((sizeParent.width - this.getWidth()) >> 1), locParent.y
				+ ((sizeParent.height - this.getHeight()) >> 1));
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
			jContentPane.add(getButtons(), BorderLayout.SOUTH);
			jContentPane.add(getTabbedPane(), BorderLayout.CENTER);
		}
		return jContentPane;
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
			buttons.add(getButtonOk(), null);
			buttons.add(getButtonCancel(), null);
			buttons.add(getButtonApply(), null);
			buttons.add(getButtonHelp(), null);
		}
		return buttons;
	}

	/**
	 * This method initializes buttonOk
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getButtonOk() {
		if (buttonOk == null) {
			buttonOk = new JButton();
			buttonOk.setMnemonic(KeyEvent.VK_O);
			buttonOk.setText("OK");
			buttonOk.setName("buttonOk");
			buttonOk.setMinimumSize(buttonMinimumSize);
			buttonOk.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					save();
					close();
				}
			});
		}
		return buttonOk;
	}

	/**
	 * This method initializes buttonCancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getButtonCancel() {
		if (buttonCancel == null) {
			buttonCancel = new JButton();
			buttonCancel.setText("Cancel");
			buttonCancel.setMnemonic(KeyEvent.VK_C);
			buttonCancel.setName("buttonCancel");
			buttonCancel.setMinimumSize(buttonMinimumSize);
			buttonCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					close();
				}
			});
		}
		return buttonCancel;
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

		JComboBox ports = getListPort();
		ports.removeAllItems();
		for (CommPortIdentifier port : settings.getPorts()) {
			ports.addItem(port.getName());
		}
		JComboBox speed = getListSpeed();
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
			tabComm.add(getButtonBrowse(), gridBagConstraints31);
		}
		return tabComm;
	}

	/**
	 * This method initializes buttonApply
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getButtonApply() {
		if (buttonApply == null) {
			buttonApply = new JButton();
			buttonApply.setText("Apply");
			buttonApply.setMnemonic(KeyEvent.VK_A);
			buttonApply.setMinimumSize(buttonMinimumSize);
			buttonApply.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					save();
				}
			});
		}
		return buttonApply;
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
	private JComboBox getListPort() {
		if (listPort == null) {
			listPort = new JComboBox();
		}
		return listPort;
	}

	/**
	 * This method initializes listSpeed
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getListSpeed() {
		if (listSpeed == null) {
			listSpeed = new JComboBox();
			listSpeed.addItem(NCUComm.BAUD_2400);
			listSpeed.addItem(NCUComm.BAUD_4800);
			listSpeed.addItem(NCUComm.BAUD_9600);
			listSpeed.addItem(NCUComm.BAUD_38400);
			listSpeed.addItem(NCUComm.BAUD_57600);
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
	private JButton getButtonBrowse() {
		if (buttonBrowse == null) {
			buttonBrowse = new JButton();
			buttonBrowse.setText("Browse...");
			buttonBrowse.setMnemonic(KeyEvent.VK_B);
			buttonBrowse.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int ret = getBrowser().showOpenDialog(getButtonBrowse());
					if (ret == JFileChooser.APPROVE_OPTION) {
						labelFolderPath.setText(getBrowser().getSelectedFile()
								.getPath());
					}
				}
			});
		}
		return buttonBrowse;
	}

	public JFileChooser getBrowser() {
		if (browser == null) {
			browser = new JFileChooser();
			browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		return browser;
	}

	public void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public void save() {
		Settings settings = getSettings();
		settings.setPortIdentifier((String) getListPort().getSelectedItem());
		settings.setPortSpeed((Integer) getListSpeed().getSelectedItem());
		settings.setListen(getCheckListen().isSelected());
		settings.setBackupFolder(labelFolderPath.getText());
	}
}
