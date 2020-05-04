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
