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
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.v2_0.app.AppName;
import net.sf.jncu.ui.NCUDialog;
import net.sf.swing.CheckListCellRenderer;
import net.sf.swing.SwingUtils;

/**
 * Backup options dialog.
 * 
 * @author mwaisberg
 */
public class BackupDialog extends NCUDialog {

	private static final String TITLE = "Backup";

	private JButton cancelButton;
	private JButton backupButton;
	private JList listStores;
	private JList listInformation;
	private JButton selectAllStoresButton;
	private JButton clearAllStoresButton;
	private JButton selectAllInfoButton;
	private JButton clearAllInfoButton;

	private final MouseListener listMouseClicked = new MouseAdapter() {
		public void mouseClicked(MouseEvent event) {
			JList list = (JList) event.getSource();

			// Get index of item clicked
			int index = list.locationToIndex(event.getPoint());
			if (index < 0)
				return;
			JCheckBox item = (JCheckBox) list.getModel().getElementAt(index);
			if (item == null)
				return;

			// Toggle selected state
			item.setSelected(!item.isSelected());

			// Repaint cell
			list.repaint(list.getCellBounds(index, index));
		}
	};

	private final KeyListener listkeyClicked = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent event) {
			int key = event.getKeyCode();
			if ((key != KeyEvent.VK_SPACE) && (key != KeyEvent.VK_ENTER))
				return;

			JList list = (JList) event.getSource();

			// Get index of item clicked
			int index = list.getSelectedIndex();
			if (index < 0)
				return;
			JCheckBox item = (JCheckBox) list.getModel().getElementAt(index);
			if (item == null)
				return;

			// Toggle selected state
			item.setSelected(!item.isSelected());

			// Repaint cell
			list.repaint(list.getCellBounds(index, index));
		}
	};

	private final Map<String, Store> stores = new TreeMap<String, Store>();
	private final Map<String, AppName> apps = new TreeMap<String, AppName>();
	private boolean success;

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
		super(owner);
		init();
	}

	/**
	 * Creates a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public BackupDialog(Dialog owner) {
		super(owner);
		init();
	}

	/**
	 * Creates a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public BackupDialog(Window owner) {
		super(owner);
		init();
	}

	/**
	 * Initialise.
	 */
	private void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(TITLE);
		setResizable(false);

		JPanel panelContents = new JPanel();
		panelContents.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panelContents.setOpaque(false);
		panelContents.setLayout(new BorderLayout(5, 5));
		setContentPane(panelContents);

		JPanel panelStores = new JPanel();
		panelStores.setOpaque(false);
		panelContents.add(panelStores, BorderLayout.WEST);
		panelStores.setLayout(new BorderLayout(5, 5));
		panelStores.setBorder(BorderFactory.createTitledBorder("Backup From Stores"));

		JScrollPane scrollStores = new JScrollPane(getListStores());
		panelStores.add(scrollStores, BorderLayout.CENTER);

		JPanel panelStoresButtons = createButtonsPanel();
		panelStoresButtons.add(getSelectAllStoresButton());
		panelStoresButtons.add(getClearAllStoresButton());
		panelStores.add(panelStoresButtons, BorderLayout.SOUTH);

		JPanel panelInfo = new JPanel();
		panelInfo.setOpaque(false);
		panelContents.add(panelInfo, BorderLayout.CENTER);
		panelInfo.setLayout(new BorderLayout(5, 5));
		panelInfo.setBorder(BorderFactory.createTitledBorder("Information"));

		JScrollPane scrollInfo = new JScrollPane(getListInformation());
		panelInfo.add(scrollInfo, BorderLayout.CENTER);

		JPanel panelInfoButtons = createButtonsPanel();
		panelInfoButtons.add(getSelectAllInfoButton());
		panelInfoButtons.add(getClearAllInfoButton());
		panelInfo.add(panelInfoButtons, BorderLayout.SOUTH);

		JPanel panelButtons = createButtonsPanel();
		panelButtons.add(getBackupButton());
		panelButtons.add(getCancelButton());
		panelContents.add(panelButtons, BorderLayout.SOUTH);

		setSize(380, 300);
		SwingUtils.centreInOwner(this);
		getBackupButton().requestFocus();
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

	/**
	 * Get the list of stores.
	 * 
	 * @return the list.
	 */
	private JList getListStores() {
		if (listStores == null) {
			JList list = new JList();
			list.setCellRenderer(new CheckListCellRenderer());
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setVisibleRowCount(5);
			list.addMouseListener(listMouseClicked);
			list.addKeyListener(listkeyClicked);
			listStores = list;
		}
		return listStores;
	}

	/**
	 * Get the list of information soups.
	 * 
	 * @return the list.
	 */
	private JList getListInformation() {
		if (listInformation == null) {
			JList list = new JList();
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setVisibleRowCount(5);
			list.setCellRenderer(new CheckListCellRenderer());
			list.addMouseListener(listMouseClicked);
			list.addKeyListener(listkeyClicked);
			listInformation = list;
		}
		return listInformation;
	}

	/**
	 * Populate the list of stores.
	 * 
	 * @param stores
	 *            the stores.
	 */
	public void setStores(Collection<Store> stores) {
		this.stores.clear();

		if (stores == null) {
			getListStores().removeAll();
		} else {
			DefaultListModel modelStores = new DefaultListModel();
			for (Store store : stores)
				this.stores.put(store.getName(), store);
			for (String name : this.stores.keySet())
				modelStores.addElement(new JCheckBox(name, true));
			getListStores().setModel(modelStores);
		}

		pack();
		SwingUtils.centreInOwner(this);
	}

	/**
	 * Set the list of application names.
	 * 
	 * @param names
	 *            the application names.
	 */
	public void setApplications(Collection<AppName> names) {
		this.apps.clear();

		if (names == null) {
			getListInformation().removeAll();
		} else {
			for (AppName name : names)
				this.apps.put(name.getName(), name);
			DefaultListModel modelApps = new DefaultListModel();
			for (String name : this.apps.keySet()) {
				modelApps.addElement(new JCheckBox(name, true));
			}
			getListInformation().setModel(modelApps);
		}

		pack();
		SwingUtils.centreInOwner(this);
	}

	/**
	 * Check all items.
	 * 
	 * @param list
	 *            the list.
	 */
	private void selectAll(JList list) {
		ListModel model = list.getModel();
		int size = model.getSize();
		JCheckBox check;
		for (int i = 0; i < size; i++) {
			check = (JCheckBox) model.getElementAt(i);
			check.setSelected(true);
		}
		list.repaint();
	}

	/**
	 * Un-check all items.
	 * 
	 * @param list
	 *            the list.
	 */
	private void clearAll(JList list) {
		ListModel model = list.getModel();
		int size = model.getSize();
		JCheckBox check;
		for (int i = 0; i < size; i++) {
			check = (JCheckBox) model.getElementAt(i);
			check.setSelected(false);
		}
		list.repaint();
	}

	/**
	 * Get the sync options.
	 * 
	 * @return the options - {@code null} if cancelled.
	 */
	public SyncOptions getSyncOptions() {
		if (!success)
			return null;

		Collection<Store> storesSelected = new ArrayList<Store>();
		ListModel modelStores = getListStores().getModel();
		ListModel modelApps = getListInformation().getModel();
		int sizeStores = modelStores.getSize();
		int sizeApps = modelApps.getSize();
		String storeName, appName;
		JCheckBox check;
		Store store;
		AppName app;
		NSOFArray soups;
		NSOFString soupName;
		int countStores = 0;
		int countApps = 0;
		boolean packages = false;

		for (int i = 0; i < sizeStores; i++) {
			check = (JCheckBox) modelStores.getElementAt(i);
			if (check.isSelected()) {
				countStores++;
				storeName = check.getText();
				store = new Store(storeName);
				storesSelected.add(store);

				countApps = 0;
				for (int a = 0; a < sizeApps; a++) {
					check = (JCheckBox) modelApps.getElementAt(a);
					if (check.isSelected()) {
						countApps++;
						appName = check.getText();

						if (!packages && AppName.NAME_PACKAGES.equals(appName))
							packages = true;

						app = apps.get(appName);
						soups = app.getSoups();
						if (soups == null)
							continue;
						for (NSOFObject o : soups.toList()) {
							soupName = (NSOFString) o;
							store.addSoup(new Soup(soupName.getValue()));
						}
					}
				}
			}
		}

		if (countStores == 0)
			return null;
		if (countApps == 0)
			return null;
		SyncOptions options = new SyncOptions();
		options.setStores(storesSelected);
		options.setPackages(packages);
		if (countApps == sizeApps)
			options.setSyncAll(true);

		return options;
	}

	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = createCancelButton();
		}
		return cancelButton;
	}

	private JButton getBackupButton() {
		if (backupButton == null) {
			URL url = getClass().getResource("/dialog-play.png");
			Icon icon = new ImageIcon(url);

			JButton button = createButton();
			button.setText("Backup");
			button.setMnemonic(KeyEvent.VK_B);
			button.setIcon(icon);
			backupButton = button;
		}
		return backupButton;
	}

	/**
	 * @return the selectAllStoresButton
	 */
	private JButton getSelectAllStoresButton() {
		if (selectAllStoresButton == null) {
			URL url = getClass().getResource("/dialog-select-all.png");
			Icon icon = new ImageIcon(url);

			JButton button = createButton();
			button.setText("Select All");
			button.setIcon(icon);
			selectAllStoresButton = button;
		}
		return selectAllStoresButton;
	}

	/**
	 * @return the clearAllStoresButton
	 */
	private JButton getClearAllStoresButton() {
		if (clearAllStoresButton == null) {
			URL url = getClass().getResource("/dialog-clear-all.png");
			Icon icon = new ImageIcon(url);

			JButton button = createButton();
			button.setText("Clear All");
			button.setIcon(icon);
			clearAllStoresButton = button;
		}
		return clearAllStoresButton;
	}

	/**
	 * @return the selectAllInfoButton
	 */
	private JButton getSelectAllInfoButton() {
		if (selectAllInfoButton == null) {
			URL url = getClass().getResource("/dialog-select-all.png");
			Icon icon = new ImageIcon(url);

			JButton button = createButton();
			button.setText("Select All");
			button.setIcon(icon);
			selectAllInfoButton = button;
		}
		return selectAllInfoButton;
	}

	/**
	 * @return the clearAllInfoButton
	 */
	private JButton getClearAllInfoButton() {
		if (clearAllInfoButton == null) {
			URL url = getClass().getResource("/dialog-clear-all.png");
			Icon icon = new ImageIcon(url);

			JButton button = createButton();
			button.setText("Clear All");
			button.setIcon(icon);
			clearAllInfoButton = button;
		}
		return clearAllInfoButton;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		final Object src = event.getSource();

		if (src == cancelButton) {
			success = false;
			close();
		} else if (src == backupButton) {
			success = true;
			close();
		} else if (src == clearAllInfoButton) {
			clearAll(getListInformation());
		} else if (src == clearAllStoresButton) {
			clearAll(getListStores());
		} else if (src == selectAllInfoButton) {
			selectAll(getListInformation());
		} else if (src == selectAllStoresButton) {
			selectAll(getListStores());
		}
	}
}
