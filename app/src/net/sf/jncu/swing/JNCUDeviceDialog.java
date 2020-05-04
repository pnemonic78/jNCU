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
package net.sf.jncu.swing;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.util.NumberUtils;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * jNCU Newton device information dialog.
 *
 * @author moshew
 */
public class JNCUDeviceDialog extends JNCUDialog {

    private static final int INSET_PANEL = 10;
    private static final int INSET_CELL_Y = 2;
    private static final int INSET_CELL_X = 5;

    private JPanel contentPane;
    private JPanel infoPane;
    private JPanel buttons;
    private JButton okButton;
    private JLabel nameLabel;
    private JLabel newtonIdLabel;
    private JLabel manufacturerIdLabel;
    private JLabel machineTypeLabel;
    private JLabel romVersionLabel;
    private JLabel romStageLabel;
    private JLabel ramSizeLabel;
    private JLabel screenHeightLabel;
    private JLabel screenWidthLabel;
    private JLabel patchVersionLabel;
    private JLabel objectSystemVersionLabel;
    private JLabel internalStoreSignatureLabel;
    private JLabel screenResolutionYLabel;
    private JLabel screenResolutionXLabel;
    private JLabel screenDepthLabel;
    private JLabel serialNumberLabel;

    /**
     * Create an info dialog.
     *
     * @param owner the owner.
     */
    public JNCUDeviceDialog(Window owner) {
        super(owner);
        init();
    }

    /**
     * Initialise.
     */
    private void init() {
        setContentPane(getMainContentPane());
        pack();
        setLocationRelativeTo(getOwner());
        getOkButton().requestFocus();
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
            contentPane.add(getInfoPane(), BorderLayout.CENTER);
            contentPane.add(getButtons(), BorderLayout.SOUTH);
        }
        return contentPane;
    }

    /**
     * Get the information content pane.
     *
     * @return the panel.
     */
    private JPanel getInfoPane() {
        if (infoPane == null) {
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(INSET_PANEL, INSET_PANEL, INSET_PANEL, INSET_PANEL));

            int y = 0;
            JLabel label;
            GridBagConstraints gbcLabel;
            GridBagConstraints gbcValue;

            label = new JLabel(JNCUResources.getString("device.name"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.WEST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getNameLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.id"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getNewtonIdLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.serial"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getSerialNumberLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.store"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getInternalStoreSignatureLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.manufacturer"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getManufacturerIdLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.machine"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.WEST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getMachineTypeLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.ROMVersion"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getRomVersionLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.ROMStage"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getRomStageLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.patch"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getPatchVersionLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.object"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getObjectSystemVersionLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.RAM"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getRamSizeLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.screenWidth"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getScreenWidthLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.screenHeight"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getScreenHeightLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.screenDepth"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getScreenDepthLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.screenResX"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getScreenResolutionXLabel(), gbcValue);

            label = new JLabel(JNCUResources.getString("device.screenResY"));
            gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = y;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(label, gbcLabel);
            gbcValue = new GridBagConstraints();
            gbcValue.gridx = 1;
            gbcValue.gridy = y++;
            gbcValue.anchor = GridBagConstraints.EAST;
            gbcValue.insets = new Insets(INSET_CELL_Y, INSET_CELL_X, INSET_CELL_Y, INSET_CELL_X);
            panel.add(getScreenResolutionYLabel(), gbcValue);

            infoPane = panel;
        }
        return infoPane;
    }

    /**
     * Get the buttons panel.
     *
     * @return the panel.
     */
    private JPanel getButtons() {
        if (buttons == null) {
            buttons = createButtonsPanel();
            buttons.add(getOkButton());
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

    @Override
    public void actionPerformed(ActionEvent event) {
        final Object src = event.getSource();

        if (src == okButton) {
            close();
        }
    }

    /**
     * Set the Newton device information.
     *
     * @param info the device information.
     */
    public void setDeviceInfo(NewtonInfo info) {
        String serialNumber = Long.toString(info.getSerialNumber(), 16).toUpperCase(Locale.US);
        while (serialNumber.length() < 16) {
            serialNumber = "0" + serialNumber;
        }

        String romMajor = Integer.toString(info.getMajorVersion());
        String romMinor = Integer.toString(info.getMinorVersion());
        String romVersion = romMajor + "." + romMinor;

        String romLang = "";
        Locale locale;
        switch (info.getLanguage()) {
            case NewtonInfo.ENGLISH:
                locale = Locale.ENGLISH;
                romLang = locale.getDisplayLanguage();
                break;
            case NewtonInfo.FRENCH:
                locale = Locale.FRENCH;
                romLang = locale.getDisplayLanguage();
                break;
            case NewtonInfo.GERMAN:
                locale = Locale.GERMAN;
                romLang = locale.getDisplayLanguage();
                break;
        }

        getInternalStoreSignatureLabel().setText(Integer.toString(info.getInternalStoreSignature()));
        getMachineTypeLabel().setText(getMachineValue(info.getMachineType()));
        getManufacturerIdLabel().setText(Integer.toString(info.getManufacturerId()));
        getNameLabel().setText(info.getName());
        getNewtonIdLabel().setText(Integer.toString(info.getNewtonId()));
        getObjectSystemVersionLabel().setText(Integer.toString(info.getObjectSystemVersion()));
        getPatchVersionLabel().setText(Integer.toString(info.getPatchVersion()));
        getRamSizeLabel().setText(NumberUtils.formatFileSize(info.getRAMSize()));
        getRomStageLabel().setText(Integer.toString(info.getStage()) + " " + romLang);
        getRomVersionLabel().setText(romVersion);
        getScreenDepthLabel().setText(Integer.toString(info.getScreenDepth()) + " bits per pixel");
        getScreenHeightLabel().setText(Integer.toString(info.getScreenHeight()) + " pixels");
        getScreenResolutionXLabel().setText(Integer.toString(info.getScreenResolutionHorizontal()) + " pixels per inch");
        getScreenResolutionYLabel().setText(Integer.toString(info.getScreenResolutionVertical()) + " pixels per inch");
        getScreenWidthLabel().setText(Integer.toString(info.getScreenWidth()) + " pixels");
        getSerialNumberLabel().setText(serialNumber);
        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Get the nameLabel.
     *
     * @return the nameLabel
     */
    private JLabel getNameLabel() {
        if (nameLabel == null) {
            nameLabel = new JLabel();
        }
        return nameLabel;
    }

    /**
     * Get the newtonIdLabel.
     *
     * @return the newtonIdLabel
     */
    private JLabel getNewtonIdLabel() {
        if (newtonIdLabel == null) {
            newtonIdLabel = new JLabel();
        }
        return newtonIdLabel;
    }

    /**
     * Get the manufacturerIdLabel.
     *
     * @return the manufacturerIdLabel
     */
    private JLabel getManufacturerIdLabel() {
        if (manufacturerIdLabel == null) {
            manufacturerIdLabel = new JLabel();
        }
        return manufacturerIdLabel;
    }

    /**
     * Get the machineTypeLabel.
     *
     * @return the machineTypeLabel
     */
    private JLabel getMachineTypeLabel() {
        if (machineTypeLabel == null) {
            machineTypeLabel = new JLabel();
        }
        return machineTypeLabel;
    }

    /**
     * Get the romVersionLabel.
     *
     * @return the romVersionLabel
     */
    private JLabel getRomVersionLabel() {
        if (romVersionLabel == null) {
            romVersionLabel = new JLabel();
        }
        return romVersionLabel;
    }

    /**
     * Get the romStageLabel.
     *
     * @return the romStageLabel
     */
    private JLabel getRomStageLabel() {
        if (romStageLabel == null) {
            romStageLabel = new JLabel();
        }
        return romStageLabel;
    }

    /**
     * Get the ramSizeLabel.
     *
     * @return the ramSizeLabel
     */
    private JLabel getRamSizeLabel() {
        if (ramSizeLabel == null) {
            ramSizeLabel = new JLabel();
        }
        return ramSizeLabel;
    }

    /**
     * Get the screenHeightLabel.
     *
     * @return the screenHeightLabel
     */
    private JLabel getScreenHeightLabel() {
        if (screenHeightLabel == null) {
            screenHeightLabel = new JLabel();
        }
        return screenHeightLabel;
    }

    /**
     * Get the screenWidthLabel.
     *
     * @return the screenWidthLabel
     */
    private JLabel getScreenWidthLabel() {
        if (screenWidthLabel == null) {
            screenWidthLabel = new JLabel();
        }
        return screenWidthLabel;
    }

    /**
     * Get the patchVersionLabel.
     *
     * @return the patchVersionLabel
     */
    private JLabel getPatchVersionLabel() {
        if (patchVersionLabel == null) {
            patchVersionLabel = new JLabel();
        }
        return patchVersionLabel;
    }

    /**
     * Get the objectSystemVersionLabel.
     *
     * @return the objectSystemVersionLabel
     */
    private JLabel getObjectSystemVersionLabel() {
        if (objectSystemVersionLabel == null) {
            objectSystemVersionLabel = new JLabel();
        }
        return objectSystemVersionLabel;
    }

    /**
     * Get the internalStoreSignatureLabel.
     *
     * @return the internalStoreSignatureLabel
     */
    private JLabel getInternalStoreSignatureLabel() {
        if (internalStoreSignatureLabel == null) {
            internalStoreSignatureLabel = new JLabel();
        }
        return internalStoreSignatureLabel;
    }

    /**
     * Get the screenResolutionYLabel.
     *
     * @return the screenResolutionYLabel
     */
    private JLabel getScreenResolutionYLabel() {
        if (screenResolutionYLabel == null) {
            screenResolutionYLabel = new JLabel();
        }
        return screenResolutionYLabel;
    }

    /**
     * Get the screenResolutionXLabel.
     *
     * @return the screenResolutionXLabel
     */
    private JLabel getScreenResolutionXLabel() {
        if (screenResolutionXLabel == null) {
            screenResolutionXLabel = new JLabel();
        }
        return screenResolutionXLabel;
    }

    /**
     * Get the screenDepthLabel.
     *
     * @return the screenDepthLabel
     */
    private JLabel getScreenDepthLabel() {
        if (screenDepthLabel == null) {
            screenDepthLabel = new JLabel();
        }
        return screenDepthLabel;
    }

    /**
     * Get the serialNumberLabel.
     *
     * @return the serialNumberLabel
     */
    private JLabel getSerialNumberLabel() {
        if (serialNumberLabel == null) {
            serialNumberLabel = new JLabel();
        }
        return serialNumberLabel;
    }

    private String getMachineValue(int machineType) {
        switch (machineType) {
            case 0:
                return "Newton MessagePad (OMP), 20 MHz ARM 610";
            case 1:
                return "Newton MessagePad 100, 20 MHz ARM 610";
            case 2:
                return "Newton MessagePad 110, 20 MHz ARM 610";
            case 3:
                return "Newton MessagePad 120, 20 MHz ARM 610";
            case 4:
                return "Newton MessagePad 130, 20 MHz ARM 610";
            case 0x10003000:// 268447744
                return "Newton MessagePad 2000, 162 MHz StrongARM 110";
            case 5:
                return "Newton MessagePad 2100, 162 MHz StrongARM 110";
            case 6:
                return "Newton eMate 300, 25 MHz ARM 710a";
        }

        return "0x" + Integer.toString(machineType, 16);
    }
}
