/**
 * 
 */
package net.sf.jncu.ui;

import javax.swing.Icon;
import javax.swing.WindowConstants;

import net.sf.jncu.JNCUResources;

/**
 * Test the dialog to monitor the progress of a module's operations.
 * 
 * @author Moshe
 */
public class JNCUModuleDialogTester {

	public static void main(String[] args) {
		Icon icon = JNCUResources.getIcon("/dialog/play.png");

		JNCUModuleDialog d = new JNCUModuleDialog(icon, "msg", "note", 0, 0);
		d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		d.setVisible(true);
	}

}
