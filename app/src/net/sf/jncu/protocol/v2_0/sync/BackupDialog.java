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

import net.sf.jncu.JNCUResources;
import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.v2_0.app.AppName;
import net.sf.jncu.swing.JNCUDialog;
import net.sf.swing.CheckListCellRenderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 * Backup options dialog.
 *
 * @author mwaisberg
 */
public class BackupDialog extends JNCUDialog {

    private JButton cancelButton;
    private JButton backupButton;
    private JList<JCheckBox> listStores;
    private JList<JCheckBox> listInformation;
    private JButton selectAllStoresButton;
    private JButton clearAllStoresButton;
    private JButton selectAllInfoButton;
    private JButton clearAllInfoButton;

    private final MouseListener listMouseClicked = new MouseAdapter() {
        public void mouseClicked(MouseEvent event) {
            @SuppressWarnings("unchecked")
            JList<JCheckBox> list = (JList<JCheckBox>) event.getSource();

            // Get index of item clicked
            int index = list.locationToIndex(event.getPoint());
            if (index < 0)
                return;
            JCheckBox item = list.getModel().getElementAt(index);
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

            @SuppressWarnings("unchecked")
            JList<JCheckBox> list = (JList<JCheckBox>) event.getSource();

            // Get index of item clicked
            int index = list.getSelectedIndex();
            if (index < 0)
                return;
            JCheckBox item = list.getModel().getElementAt(index);
            if (item == null)
                return;

            // Toggle selected state
            item.setSelected(!item.isSelected());

            // Repaint cell
            list.repaint(list.getCellBounds(index, index));
        }
    };

    private final Map<JCheckBox, Store> stores = new HashMap<JCheckBox, Store>();
    private final Map<JCheckBox, AppName> apps = new HashMap<JCheckBox, AppName>();
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
     * @param owner the owner.
     */
    public BackupDialog(Window owner) {
        super(owner);
        init();
    }

    /**
     * Initialise.
     */
    private void init() {
        setTitle(JNCUResources.getString("backup", super.getTitle()));

        JPanel panelContents = new JPanel();
        panelContents.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelContents.setOpaque(false);
        panelContents.setLayout(new BorderLayout(5, 5));
        setContentPane(panelContents);

        JPanel panelStores = new JPanel();
        panelStores.setOpaque(false);
        panelContents.add(panelStores, BorderLayout.WEST);
        panelStores.setLayout(new BorderLayout(5, 5));
        panelStores.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("backupStores")));

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
        panelInfo.setBorder(BorderFactory.createTitledBorder(JNCUResources.getString("backupSoups")));

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

        setMinimumSize(new Dimension(380, 300));
        pack();
        setLocationRelativeTo(getOwner());
        getCancelButton().requestFocus();
    }

    /**
     * Get the list of stores.
     *
     * @return the list.
     */
    private JList<JCheckBox> getListStores() {
        if (listStores == null) {
            JList<JCheckBox> list = new JList<JCheckBox>();
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
    private JList<JCheckBox> getListInformation() {
        if (listInformation == null) {
            JList<JCheckBox> list = new JList<JCheckBox>();
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
     * @param stores the stores.
     */
    public void setStores(Collection<Store> stores) {
        this.stores.clear();

        if (stores == null) {
            getListStores().removeAll();
        } else {
            DefaultListModel<JCheckBox> modelStores = new DefaultListModel<JCheckBox>();
            JCheckBox check;
            for (Store store : stores) {
                check = new JCheckBox(store.getName(), true);
                modelStores.addElement(check);
                this.stores.put(check, store);
            }
            getListStores().setModel(modelStores);
        }

        boolean hasStores = !this.stores.isEmpty();
        boolean hasApps = !this.apps.isEmpty();
        getSelectAllStoresButton().setEnabled(hasStores);
        getClearAllStoresButton().setEnabled(hasStores);
        if (hasStores && hasApps) {
            getBackupButton().setEnabled(true);
            getBackupButton().requestFocus();
        } else {
            getBackupButton().setEnabled(false);
            getCancelButton().requestFocus();
        }

        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Set the list of application names.
     *
     * @param appNames the application names.
     */
    public void setApplications(Collection<AppName> appNames) {
        this.apps.clear();

        if (appNames == null) {
            getListInformation().removeAll();
        } else {
            DefaultListModel<JCheckBox> modelApps = new DefaultListModel<JCheckBox>();
            JCheckBox check;
            for (AppName appName : appNames) {
                check = new JCheckBox(appName.getName(), true);
                modelApps.addElement(check);
                apps.put(check, appName);
            }
            getListInformation().setModel(modelApps);
        }

        boolean hasStores = !this.stores.isEmpty();
        boolean hasApps = !this.apps.isEmpty();
        getSelectAllInfoButton().setEnabled(hasApps);
        getClearAllInfoButton().setEnabled(hasApps);
        if (hasStores && hasApps) {
            getBackupButton().setEnabled(true);
            getBackupButton().requestFocus();
        } else {
            getBackupButton().setEnabled(false);
            getCancelButton().requestFocus();
        }

        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Check all items.
     *
     * @param list the list.
     */
    private void selectAll(JList<JCheckBox> list) {
        ListModel<JCheckBox> model = list.getModel();
        int size = model.getSize();
        JCheckBox check;
        for (int i = 0; i < size; i++) {
            check = model.getElementAt(i);
            check.setSelected(true);
        }
        list.repaint();
    }

    /**
     * Un-check all items.
     *
     * @param list the list.
     */
    private void clearAll(JList<JCheckBox> list) {
        ListModel<JCheckBox> model = list.getModel();
        int size = model.getSize();
        JCheckBox check;
        for (int i = 0; i < size; i++) {
            check = model.getElementAt(i);
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
        ListModel<JCheckBox> modelStores = getListStores().getModel();
        ListModel<JCheckBox> modelApps = getListInformation().getModel();
        int sizeStores = modelStores.getSize();
        int sizeApps = modelApps.getSize();
        JCheckBox check;
        Store storeNewton;
        Store store;
        AppName appName;
        Soup soup;
        NSOFArray soups;
        NSOFString soupName;
        List<Integer> soupSignatures;
        int countStores = 0;
        int countApps = 0;
        boolean packages = false;

        for (int i = 0; i < sizeStores; i++) {
            check = modelStores.getElementAt(i);
            if (check.isSelected()) {
                countStores++;
                storeNewton = stores.get(check);
                // We don't want to mistakenly populate the actual Newton store
                // with fake soups, so clone it.
                store = new Store(storeNewton.getName());
                store.fromFrame(storeNewton.toFrame());
                storesSelected.add(store);
                soupSignatures = new ArrayList<Integer>();

                countApps = 0;
                for (int a = 0; a < sizeApps; a++) {
                    check = modelApps.getElementAt(a);
                    if (check.isSelected()) {
                        countApps++;
                        appName = apps.get(check);
                        soups = appName.getSoups();

                        if (!packages && AppName.CLASS_PACKAGES.equals(appName.getObjectClass()))
                            packages = true;

                        if (soups == null)
                            continue;
                        for (NSOFObject o : soups.toList()) {
                            soupName = (NSOFString) o;
                            soup = new Soup(soupName.getValue());
                            store.addSoup(soup);
                            soupSignatures.add(soup.getSignature());
                        }
                    }
                }
                store.setSoupSignatures(soupSignatures);
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
     * Get the "backup" button.
     *
     * @return the button.
     */
    private JButton getBackupButton() {
        if (backupButton == null) {
            JButton button = createButton();
            button.setText(JNCUResources.getString("backup"));
            button.setMnemonic(JNCUResources.getChar("backupMnemonic", KeyEvent.VK_B));
            button.setIcon(JNCUResources.getIcon("/dialog/play.png"));
            button.setEnabled(false);
            backupButton = button;
        }
        return backupButton;
    }

    /**
     * Get the button to select all stores.
     *
     * @return the button.
     */
    private JButton getSelectAllStoresButton() {
        if (selectAllStoresButton == null) {
            JButton button = createButton();
            button.setText(JNCUResources.getString("selectAll"));
            button.setIcon(JNCUResources.getIcon("/dialog/select-all.png"));
            button.setEnabled(false);
            selectAllStoresButton = button;
        }
        return selectAllStoresButton;
    }

    /**
     * Get the button to deselect all stores.
     *
     * @return the button.
     */
    private JButton getClearAllStoresButton() {
        if (clearAllStoresButton == null) {
            JButton button = createButton();
            button.setText(JNCUResources.getString("clearAll"));
            button.setIcon(JNCUResources.getIcon("/dialog/clear-all.png"));
            button.setEnabled(false);
            clearAllStoresButton = button;
        }
        return clearAllStoresButton;
    }

    /**
     * Get the button to select all soups.
     *
     * @return the button.
     */
    private JButton getSelectAllInfoButton() {
        if (selectAllInfoButton == null) {
            JButton button = createButton();
            button.setText(JNCUResources.getString("selectAll"));
            button.setIcon(JNCUResources.getIcon("/dialog/select-all.png"));
            button.setEnabled(false);
            selectAllInfoButton = button;
        }
        return selectAllInfoButton;
    }

    /**
     * Get the button to deselect all soups.
     *
     * @return the button.
     */
    private JButton getClearAllInfoButton() {
        if (clearAllInfoButton == null) {
            JButton button = createButton();
            button.setText(JNCUResources.getString("clearAll"));
            button.setIcon(JNCUResources.getIcon("/dialog/clear-all.png"));
            button.setEnabled(false);
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
