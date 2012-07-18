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
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
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
import net.sf.swing.CheckListCellRenderer;
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
	private JButton buttonCancel;
	private JButton buttonBackup;
	private JList listStores;
	private JList listInformation;
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
	private final SortedMap<String, Store> stores = new TreeMap<String, Store>();
	private final SortedMap<String, AppName> apps = new TreeMap<String, AppName>();
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
		panelContents.add(panelStores, BorderLayout.WEST);
		panelStores.setLayout(new BorderLayout(5, 5));
		panelStores.setBorder(BorderFactory.createTitledBorder("Backup From:"));

		listStores = new JList();
		listStores.setCellRenderer(new CheckListCellRenderer());
		listStores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listStores.setVisibleRowCount(5);
		listStores.addMouseListener(listMouseClicked);
		listStores.addKeyListener(listkeyClicked);
		JScrollPane scrollStores = new JScrollPane(listStores);
		panelStores.add(scrollStores, BorderLayout.CENTER);

		JPanel panelStoresButtons = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelStoresButtons.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		panelStores.add(panelStoresButtons, BorderLayout.SOUTH);

		JButton btnStoresSelectAll = new JButton("Select All");
		btnStoresSelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectAll(listStores);
			}
		});
		panelStoresButtons.add(btnStoresSelectAll);

		JButton btnStoresClearAll = new JButton("Clear All");
		btnStoresClearAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearAll(listStores);
			}
		});
		panelStoresButtons.add(btnStoresClearAll);

		JPanel panelInfo = new JPanel();
		panelInfo.setOpaque(false);
		panelContents.add(panelInfo, BorderLayout.CENTER);
		panelInfo.setLayout(new BorderLayout(5, 5));
		panelInfo.setBorder(BorderFactory.createTitledBorder("Information:"));

		listInformation = new JList();
		listInformation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listInformation.setVisibleRowCount(5);
		listInformation.setCellRenderer(new CheckListCellRenderer());
		listInformation.addMouseListener(listMouseClicked);
		listInformation.addKeyListener(listkeyClicked);
		JScrollPane scrollInfo = new JScrollPane(listInformation);
		panelInfo.add(scrollInfo, BorderLayout.CENTER);

		JPanel panelInfoButtons = new JPanel();
		panelInfo.add(panelInfoButtons, BorderLayout.SOUTH);
		panelInfoButtons.setOpaque(false);
		FlowLayout flowLayout_1 = (FlowLayout) panelInfoButtons.getLayout();
		flowLayout_1.setAlignment(FlowLayout.TRAILING);

		JButton btnInfoSelectAll = new JButton("Select All");
		btnInfoSelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectAll(listInformation);
			}
		});
		panelInfoButtons.add(btnInfoSelectAll);

		JButton btnInfoClearAll = new JButton("Clear All");
		btnInfoClearAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearAll(listInformation);
			}
		});
		panelInfoButtons.add(btnInfoClearAll);

		JPanel panelButtons = new JPanel();
		panelButtons.setOpaque(false);
		panelContents.add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));

		buttonBackup = new JButton("Backup");
		buttonBackup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				success = true;
				close();
			}
		});
		panelButtons.add(buttonBackup);

		buttonCancel = new JButton(Toolkit.getProperty("AWT.cancel", "Cancel"));
		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				success = false;
				close();
			}
		});
		panelButtons.add(buttonCancel);

		setSize(380, 300);
		SwingUtils.centreInOwner(this);
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
		return listStores;
	}

	/**
	 * Get the list of information soups.
	 * 
	 * @return the list.
	 */
	private JList getListInformation() {
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
}
