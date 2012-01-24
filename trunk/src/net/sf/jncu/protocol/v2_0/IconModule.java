/**
 * 
 */
package net.sf.jncu.protocol.v2_0;

import javax.swing.JOptionPane;

import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.swing.SwingUtils;

/**
 * Module that does some function when user clicks an icon.
 * 
 * @author Moshe
 */
public abstract class IconModule implements DockCommandListener {

	static {
		SwingUtils.init();
	}

	private final String title;
	protected final CDPipe<? extends CDPacket> pipe;

	/**
	 * Constructs a new module.
	 * 
	 * @param title
	 *            the title.
	 * @param pipe
	 *            the pipe.
	 */
	public IconModule(String title, CDPipe<? extends CDPacket> pipe) {
		super();
		this.title = title;
		if (pipe == null)
			throw new IllegalArgumentException("pipe required");
		this.pipe = pipe;
		pipe.addCommandListener(this);
	}

	@Override
	public void commandEOF() {
		pipe.removeCommandListener(this);
	}

	/**
	 * Send a command.
	 * 
	 * @param command
	 *            the command.
	 */
	protected void write(IDockCommandToNewton command) {
		try {
			if (pipe.canSend())
				pipe.write(command);
		} catch (Exception e) {
			e.printStackTrace();
			if (!DOperationDone.COMMAND.equals(command.getCommand())) {
				DOperationDone cancel = new DOperationDone();
				write(cancel);
			}
			commandEOF();
			showError(e.getMessage());
		}
	}

	/**
	 * Show the error to the user.
	 * 
	 * @param msg
	 *            the error message.
	 */
	protected void showError(final String msg) {
		new Thread() {
			public void run() {
				JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
			}
		}.start();
	}
}
