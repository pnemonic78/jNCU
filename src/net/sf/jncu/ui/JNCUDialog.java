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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.sf.jncu.JNCUResources;
import net.sf.swing.SwingUtils;

/**
 * jNCU bae dialog.
 * 
 * @author Moshe
 */
public class JNCUDialog extends JDialog implements ActionListener {

	static {
		SwingUtils.init();
	}

	/** Minimum button size. */
	private Dimension buttonMinimumSize;

	/**
	 * Create a new dialog.
	 */
	public JNCUDialog() {
		super();
		init();
	}

	/**
	 * Create a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 */
	public JNCUDialog(Window owner) {
		super(owner);
		init();
	}

	/**
	 * Initialize.
	 */
	private void init() {
		setModalityType(ModalityType.APPLICATION_MODAL);

		int buttonMinimumWidth = UIManager.getInt("OptionPane.buttonMinimumWidth");
		this.buttonMinimumSize = new Dimension(buttonMinimumWidth, 24);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	/**
	 * Create a new button.
	 * 
	 * @return the button.
	 */
	protected JButton createButton() {
		JButton button = new JButton();
		button.setMinimumSize(buttonMinimumSize);
		button.addActionListener(this);
		return button;
	}

	/**
	 * Create a "cancel" button.
	 * 
	 * @return the button.
	 */
	protected JButton createCancelButton() {
		URL url = getClass().getResource("/dialog-cancel.png");
		Icon icon = new ImageIcon(url);

		JButton button = createButton();
		button.setText(JNCUResources.getString("JNCU.cancel", "Cancel"));
		button.setMnemonic(JNCUResources.getChar("JNCU.cancelMnemonic", KeyEvent.VK_C));
		button.setIcon(icon);

		return button;
	}

	/**
	 * Create an "OK" button.
	 * 
	 * @return the button.
	 */
	protected JButton createOkButton() {
		URL url = getClass().getResource("/dialog-ok.png");
		Icon icon = new ImageIcon(url);

		JButton button = createButton();
		button.setText(JNCUResources.getString("JNCU.ok", "OK"));
		button.setMnemonic(JNCUResources.getChar("JNCU.okMnemonic", KeyEvent.VK_O));
		button.setIcon(icon);

		return button;
	}

	/**
	 * Create a new panel for buttons.
	 * 
	 * @return the panel.
	 */
	protected JPanel createButtonsPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));
		return panel;
	}
}
