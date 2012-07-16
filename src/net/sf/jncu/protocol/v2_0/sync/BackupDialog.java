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
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
	private JCheckBox checkPackages;
	private SyncOptions options;
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

		JPanel panelInfoCenter = new JPanel();
		panelInfoCenter.setOpaque(false);
		panelInfo.add(panelInfoCenter, BorderLayout.CENTER);
		panelInfoCenter.setLayout(new BorderLayout(0, 0));

		listInformation = new JList();
		listInformation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listInformation.setVisibleRowCount(5);
		listInformation.setCellRenderer(new CheckListCellRenderer());
		listInformation.addMouseListener(listMouseClicked);
		listInformation.addKeyListener(listkeyClicked);
		JScrollPane scrollInfo = new JScrollPane(listInformation);
		panelInfoCenter.add(scrollInfo, BorderLayout.CENTER);

		JPanel panelInfoButtons = new JPanel();
		panelInfoCenter.add(panelInfoButtons, BorderLayout.SOUTH);
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

		checkPackages = new JCheckBox("Include All Packages");
		panelInfo.add(checkPackages, BorderLayout.SOUTH);
		checkPackages.setSelected(true);

		JPanel panelButtons = new JPanel();
		panelButtons.setOpaque(false);
		panelContents.add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));

		buttonBackup = new JButton("Backup");
		buttonBackup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				close();
				// TODO Start backup...
			}
		});
		panelButtons.add(buttonBackup);

		buttonCancel = new JButton(Toolkit.getProperty("AWT.cancel", "Cancel"));
		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				close();
			}
		});
		panelButtons.add(buttonCancel);

		setSize(380, 300);
		SwingUtils.centreInOwner(this);
	}

	public static void main(String[] args) {
		SyncOptions options = new SyncOptions();
		options.setPackages(true);

		List<Store> stores = new ArrayList<Store>();
		stores.add(new Store("Internal"));
		stores.add(new Store("Store #2"));
		stores.add(new Store("Store #3"));
		stores.add(new Store("Store #4"));
		stores.add(new Store("Store #5"));
		stores.add(new Store("Store #6"));
		stores.add(new Store("Store #7"));
		stores.add(new Store("Store #8"));
		stores.add(new Store("Store #9"));
		stores.add(new Store("Store #10"));
		stores.add(new Store("Store #11"));
		stores.add(new Store("Store #12"));
		options.setStores(stores);

		List<Soup> soups = new ArrayList<Soup>();
		soups.add(new Soup("Soup #1"));
		soups.add(new Soup("Soup #3"));
		soups.add(new Soup("Soup #5"));
		soups.add(new Soup("Soup #7"));
		soups.add(new Soup("Soup #9"));
		soups.add(new Soup("Soup #11"));
		stores.get(0).setSoups(soups);

		soups = new ArrayList<Soup>();
		soups.add(new Soup("Soup #2"));
		soups.add(new Soup("Soup #4"));
		soups.add(new Soup("Soup #6"));
		soups.add(new Soup("Soup #8"));
		soups.add(new Soup("Soup #10"));
		soups.add(new Soup("Soup #12"));
		stores.get(1).setSoups(soups);

		soups = new ArrayList<Soup>();
		soups.add(new Soup(AppName.NAME_BACKUP));
		soups.add(new Soup(AppName.NAME_OTHER));
		soups.add(new Soup(AppName.NAME_PACKAGES));
		soups.add(new Soup(AppName.NAME_SYSTEM));
		stores.get(2).setSoups(soups);

		BackupDialog dialog = new BackupDialog();
		dialog.setSyncOptions(options);
		dialog.setVisible(true);
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
	 * Get the checkbox to include all packages.
	 * 
	 * @return the checkbox
	 */
	private JCheckBox getCheckPackages() {
		return checkPackages;
	}

	/**
	 * Get the sync options.
	 * 
	 * @return the options.
	 */
	public SyncOptions getSyncOptions() {
		return options;
	}

	/**
	 * Set the sync options.
	 * 
	 * @param options
	 *            the options.
	 */
	public void setSyncOptions(SyncOptions options) {
		this.options = options;

		DefaultListModel modelStores = new DefaultListModel();
		DefaultListModel modelSoups = new DefaultListModel();
		SortedSet<Soup> soups = new TreeSet<Soup>();

		for (Store store : options.getStores()) {
			modelStores.addElement(new JCheckBox(store.getName(), true));
			soups.addAll(store.getSoups());
		}
		String soupName;
		for (Soup soup : soups) {
			soupName = soup.getName();
			if (AppName.NAME_BACKUP.equals(soupName))
				continue;
			if (AppName.NAME_PACKAGES.equals(soupName))
				continue;
			modelSoups.addElement(new JCheckBox(soupName, true));
		}
		getListStores().setModel(modelStores);
		getListInformation().setModel(modelSoups);

		getCheckPackages().setSelected(options.isPackages());

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

}
