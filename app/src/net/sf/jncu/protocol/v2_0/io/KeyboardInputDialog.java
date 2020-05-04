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
package net.sf.jncu.protocol.v2_0.io;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.swing.JNCUDialog;

/**
 * Keyboard input dialog.
 * <p>
 * Provides a UI for the user to type text.
 *
 * @author Moshe
 */
public class KeyboardInputDialog extends JNCUDialog implements KeyListener {

    private JTextArea input;
    private JButton pasteButton;
    private JButton cancelButton;
    private final List<KeyboardInputListener> listeners = new ArrayList<KeyboardInputListener>();

    /**
     * Constructs a new dialog.
     */
    public KeyboardInputDialog() {
        super();
        init();
    }

    /**
     * Constructs a new dialog.
     *
     * @param owner the owner.
     */
    public KeyboardInputDialog(Window owner) {
        super(owner);
        init();
    }

    /**
     * Initialise.
     */
    private void init() {
        setTitle(JNCUResources.getString("keyboard", super.getTitle()));

        JPanel panelContents = new JPanel(new BorderLayout(5, 5));
        panelContents.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelContents.setOpaque(false);
        setContentPane(panelContents);

        JLabel inputLabel = new JLabel();
        inputLabel.setText(JNCUResources.getString("textTyped"));
        panelContents.add(inputLabel, BorderLayout.NORTH);

        panelContents.add(new JScrollPane(getTextInput()), BorderLayout.CENTER);
        getTextInput().requestFocus();
        addKeyListener(this);

        JPanel panelButtons = createButtonsPanel();
        panelButtons.add(getPasteButton());
        panelButtons.add(getCancelButton());
        panelContents.add(panelButtons, BorderLayout.SOUTH);

        setSize(320, 160);
        setLocationRelativeTo(getOwner());
    }

    /**
     * Get the text summary input.
     *
     * @return the text summary.
     */
    private JTextArea getTextInput() {
        if (input == null) {
            input = new JTextArea();
            input.setEditable(false);
            input.setAlignmentX(CENTER_ALIGNMENT);
            input.addKeyListener(this);
            input.setFocusTraversalKeysEnabled(false);

            Font font = input.getFont();
            font = font.deriveFont(font.getStyle(), 14);
            input.setFont(font);
        }
        return input;
    }

    /**
     * Get the "paste" button. Pastes text from the clipboard.
     *
     * @return the button.
     */
    private JButton getPasteButton() {
        if (pasteButton == null) {
            JButton button = createButton();
            button.setText(JNCUResources.getString("paste"));
            button.setMnemonic(JNCUResources.getChar("pasteMnemonic", KeyEvent.VK_P));
            button.setIcon(JNCUResources.getIcon("/dialog/paste.png"));
            pasteButton = button;
        }
        return pasteButton;
    }

    /**
     * Get the "cancel" button. Closes the window.
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
     * Paste text from the clipboard.
     */
    protected void paste() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor flavor = DataFlavor.getTextPlainUnicodeFlavor();
        if (clipboard.isDataFlavorAvailable(flavor)) {
            Reader reader = null;
            try {
                Transferable trans = clipboard.getContents(this);
                reader = flavor.getReaderForText(trans);
                StringBuffer s = new StringBuffer();
                int c = reader.read();
                while (c != -1) {
                    s.append((char) c);
                    c = reader.read();
                }
                String text = s.toString();
                getTextInput().setText(text);
                fireStringTyped(text);
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        getTextInput().requestFocus();
    }

    /**
     * Add a keyboard input listener.
     *
     * @param listener the listener to add.
     */
    public void addInputListener(KeyboardInputListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a keyboard input listener.
     *
     * @param listener the listener to remove.
     */
    public void removeInputListener(KeyboardInputListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify all the listeners that a character has been pressed.
     *
     * @param event the key event.
     */
    protected void fireCharTyped(KeyEvent event) {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<KeyboardInputListener> listenersCopy = new ArrayList<KeyboardInputListener>(listeners);
        for (KeyboardInputListener listener : listenersCopy) {
            listener.charTyped(event);
        }
    }

    /**
     * Notify all the listeners that text has been typed.
     *
     * @param text the text.
     */
    protected void fireStringTyped(String text) {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<KeyboardInputListener> listenersCopy = new ArrayList<KeyboardInputListener>(listeners);
        for (KeyboardInputListener listener : listenersCopy) {
            listener.stringTyped(text);
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        String text = KeyEvent.getKeyText(event.getKeyCode());
        getTextInput().setText(text);
        fireCharTyped(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        fireCharTyped(event);
    }

    @Override
    public void keyTyped(KeyEvent event) {
        fireCharTyped(event);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        final Object src = event.getSource();

        if (src == cancelButton) {
            close();
        } else if (src == pasteButton) {
            paste();
        }
    }
}
