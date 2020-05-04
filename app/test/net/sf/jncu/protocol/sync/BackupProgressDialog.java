/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jncu.protocol.sync;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.swing.JNCUDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

/**
 * Backup progress dialog.
 *
 * @author Moshe
 */
public class BackupProgressDialog extends JNCUDialog {

    private JLabel messageLabel;
    private JButton cancelButton;

    /**
     * Constructs a new dialog.
     */
    public BackupProgressDialog() {
        super();
        init();
    }

    /**
     * Constructs a new dialog.
     *
     * @param owner the owner.
     */
    public BackupProgressDialog(Window owner) {
        super(owner);
        init();
    }

    /**
     * Initialise.
     */
    private void init() {
        setTitle(JNCUResources.getString("backupProgress", super.getTitle()));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        JPanel panelButtons = createButtonsPanel();
        panelButtons.add(getCancelButton());

        JPanel panelMain = new JPanel();
        panelMain.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelMain.setOpaque(false);
        panelMain.setLayout(new BorderLayout(10, 10));
        panelMain.add(progressBar, BorderLayout.NORTH);
        panelMain.add(getNote(), BorderLayout.CENTER);
        panelMain.add(panelButtons, BorderLayout.SOUTH);
        setContentPane(panelMain);

        setMinimumSize(new Dimension(320, 140));
        pack();
        setLocationRelativeTo(getOwner());
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
     * Get the message label.
     *
     * @return the label.
     */
    protected JLabel getNote() {
        if (messageLabel == null) {
            messageLabel = new JLabel();
            messageLabel.setText(JNCUResources.getString("note"));
        }
        return messageLabel;
    }

    /**
     * Set the message label text.
     *
     * @param text the text.
     */
    public void setNote(String text) {
        getNote().setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        final Object src = event.getSource();

        if (src == cancelButton) {
            close();
        }
    }
}
