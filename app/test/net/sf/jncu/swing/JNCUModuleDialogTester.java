/**
 *
 */
package net.sf.jncu.swing;

import net.sf.jncu.JNCUResources;

import javax.swing.Icon;
import javax.swing.WindowConstants;

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
