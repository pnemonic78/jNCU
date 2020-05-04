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
package net.sf.swing;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Check box list item renderer.
 * 
 * @author mwaisberg
 */
public class CheckListCellRenderer extends JCheckBox implements ListCellRenderer<JCheckBox> {

	/**
	 * Creates a new check box renderer.
	 */
	public CheckListCellRenderer() {
		super();
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox value, int index, boolean selected, boolean cellHasFocus) {
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		if (selected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
			setOpaque(true);
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
			setOpaque(false);
		}
		setSelected(value.isSelected());
		setText(value.getText());

		return this;
	}

}
