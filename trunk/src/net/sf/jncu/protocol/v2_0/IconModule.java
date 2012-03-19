/**
 * 
 */
package net.sf.jncu.protocol.v2_0;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.swing.SwingUtils;

/**
 * Module that does some function when user clicks an icon.
 * 
 * @author Moshe
 */
public abstract class IconModule extends Thread implements DockCommandListener {

	static {
		SwingUtils.init();
	}

	/**
	 * Icon module event.
	 */
	public static interface IconModuleListener {
		/**
		 * The operation completed successfully.
		 * 
		 * @param module
		 *            the module.
		 */
		public void successModule(IconModule module);

		/**
		 * The operation was cancelled.
		 * 
		 * @param module
		 *            the module.
		 */
		public void cancelModule(IconModule module);
	}

	private final String title;
	protected final CDPipe<? extends CDPacket> pipe;
	private final List<IconModuleListener> listeners = new ArrayList<IconModuleListener>();

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
		setName("IconModule-" + getId());
		this.title = title;
		if (pipe == null)
			throw new NullPointerException("pipe required");
		this.pipe = pipe;
		pipe.addCommandListener(this);
	}

	@Override
	public void commandEOF() {
		done();
	}

	/**
	 * Send a command.
	 * 
	 * @param command
	 *            the command.
	 */
	protected void write(IDockCommandToNewton command) {
		try {
			if (pipe.allowSend())
				pipe.write(command);
		} catch (Exception e) {
			e.printStackTrace();
			if (!DOperationDone.COMMAND.equals(command.getCommand())) {
				DOperationDone cancel = new DOperationDone();
				write(cancel);
			}
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

	/**
	 * Module is finished.
	 */
	protected void done() {
		pipe.removeCommandListener(this);
	}

	/**
	 * Is the module enabled and active?
	 * 
	 * @return {@code true} if enabled.
	 */
	protected boolean isEnabled() {
		return true;
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (!isEnabled())
			return;

		String cmd = command.getCommand();

		if (DOperationCanceled.COMMAND.equals(cmd)) {
			DOperationCanceledAck ack = new DOperationCanceledAck();
			write(ack);
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (!isEnabled())
			return;

		String cmd = command.getCommand();

		if (DOperationDone.COMMAND.equals(cmd)) {
			fireDone();
			done();
		} else if (DOperationCanceled.COMMAND.equals(cmd)) {
			fireCancelled();
			done();
		} else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
			fireCancelled();
			done();
		}
	}

	/**
	 * Add a module listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addListener(IconModuleListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove a module listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeListener(IconModuleListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify all the listeners that the operation finished.
	 */
	protected void fireDone() {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<IconModuleListener> listenersCopy = new ArrayList<IconModuleListener>(listeners);
		for (IconModuleListener listener : listenersCopy) {
			listener.successModule(this);
		}
	}

	/**
	 * Notify all the listeners that the operation was cancelled.
	 */
	protected void fireCancelled() {
		// Make copy of listeners to avoid ConcurrentModificationException.
		Collection<IconModuleListener> listenersCopy = new ArrayList<IconModuleListener>(listeners);
		for (IconModuleListener listener : listenersCopy) {
			listener.cancelModule(this);
		}
	}
}
