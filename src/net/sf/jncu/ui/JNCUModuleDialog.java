/**
 * 
 */
package net.sf.jncu.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicOptionPaneUI;

import net.sf.jncu.JNCUResources;

/**
 * A dialog to monitor the progress of a module's operations.
 * 
 * @author Moshe
 * @see javax.swing.ProgressMonitor
 */
public class JNCUModuleDialog extends JNCUDialog implements WindowListener {

	/**
	 * Module progress listener.
	 * 
	 * @author Moshe
	 */
	public static interface ModuleProgressListener {
		/**
		 * The progress dialog is closing.<br>
		 * Provide the listener an opportunity to close the dialog after it
		 * finishes its processing.
		 * 
		 * @param dialog
		 *            the progress dialog.
		 * @return {@code true} to close the dialog.
		 */
		public boolean moduleProgressCancel(JNCUModuleDialog dialog);
	}

	private JPanel mainPane;
	private JLabel iconLabel;
	private JPanel body;
	private Object message;
	private Component messageComponent;
	private JLabel noteLabel;
	private JProgressBar bar;
	private JButton cancelOption;
	private boolean canceled;
	private ModuleProgressListener listener;

	/**
	 * Constructs a new dialog.
	 * 
	 * @param icon
	 *            the message icon.
	 * @param message
	 *            a descriptive message.
	 * @param note
	 */
	public JNCUModuleDialog(Icon icon, Object message, String note) {
		this(null, icon, message, note);
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 * @param icon
	 *            the message icon.
	 * @param message
	 *            a descriptive message.
	 * @param note
	 *            a short note describing the state of the operation.
	 */
	public JNCUModuleDialog(Window owner, Icon icon, Object message, String note) {
		this(owner, icon, message, note, 0, 0);
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param icon
	 *            the message icon.
	 * @param message
	 *            a descriptive message.
	 * @param note
	 *            a short note describing the state of the operation.
	 * @param min
	 *            the lower bound of the range.
	 * @param max
	 *            the upper bound of the range.
	 */
	public JNCUModuleDialog(Icon icon, Object message, String note, int min, int max) {
		this(null, icon, message, note, min, max);
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 * @param icon
	 *            the message icon.
	 * @param message
	 *            a descriptive message.
	 * @param note
	 *            a short note describing the state of the operation.
	 * @param min
	 *            the lower bound of the range.
	 * @param max
	 *            the upper bound of the range.
	 */
	public JNCUModuleDialog(Window owner, Icon icon, Object message, String note, int min, int max) {
		this(owner, icon, message, note, min, max, null);
	}

	/**
	 * Constructs a new dialog.
	 * 
	 * @param owner
	 *            the owner.
	 * @param icon
	 *            the message icon.
	 * @param message
	 *            a descriptive message.
	 * @param note
	 *            a short note describing the state of the operation.
	 * @param min
	 *            the lower bound of the range.
	 * @param max
	 *            the upper bound of the range.
	 * @param listener
	 *            the dialog listener.
	 */
	public JNCUModuleDialog(Window owner, Icon icon, Object message, String note, int min, int max, ModuleProgressListener listener) {
		super(owner);
		this.iconLabel = new JLabel(icon);
		this.message = message;
		this.noteLabel = new JLabel(note);
		this.bar = new JProgressBar();
		bar.setIndeterminate(min >= max);
		bar.setMinimum(min);
		bar.setMaximum(max);
		this.listener = listener;
		init();
	}

	private void init() {
		setModalityType(DEFAULT_MODALITY_TYPE);
		setTitle(UIManager.getString("ProgressMonitor.progressText"));
		setContentPane(getMainContentPane());
		setMinimumSize(new Dimension(BasicOptionPaneUI.MinimumWidth, BasicOptionPaneUI.MinimumHeight));
		pack();
		setLocationRelativeTo(getOwner());
		getCancelButton().requestFocus();
	}

	private Container getMainContentPane() {
		if (mainPane == null) {
			JPanel pane = new JPanel();
			pane.setLayout(new BorderLayout(5, 5));
			pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			pane.add(iconLabel, BorderLayout.WEST);

			body = new JPanel();
			body.setLayout(new GridBagLayout());
			body.setOpaque(false);
			body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			pane.add(body, BorderLayout.CENTER);

			setMessage(message);

			GridBagConstraints cons = new GridBagConstraints();
			cons.gridx = 0;
			cons.gridy = 1;
			cons.gridwidth = GridBagConstraints.REMAINDER;
			cons.gridheight = 1;
			cons.insets = new Insets(0, 0, 3, 0);
			cons.fill = GridBagConstraints.HORIZONTAL;
			cons.weightx = 1.0;
			body.add(noteLabel, cons);

			cons = new GridBagConstraints();
			cons.gridx = 0;
			cons.gridy = 2;
			cons.gridwidth = GridBagConstraints.REMAINDER;
			cons.gridheight = 1;
			cons.insets = new Insets(0, 0, 3, 0);
			cons.fill = GridBagConstraints.HORIZONTAL;
			cons.weightx = 1.0;
			body.add(bar, cons);

			JPanel buttons = createButtonsPanel();
			buttons.add(getCancelButton());
			pane.add(buttons, BorderLayout.SOUTH);

			mainPane = pane;
		}
		return mainPane;
	}

	private Component getCancelButton() {
		if (cancelOption == null)
			cancelOption = createCancelButton();
		return cancelOption;
	}

	/**
	 * Sets the option pane's message-object.
	 * 
	 * @param newMessage
	 *            the <code>Object</code> to display.
	 */
	public void setMessage(Object newMessage) {
		if (newMessage == null) {
			if ((body != null) && (messageComponent != null)) {
				body.remove(messageComponent);
			}
			messageComponent = null;
		} else {
			if (newMessage instanceof Component)
				messageComponent = (Component) newMessage;
			else {
				JLabel label = new JLabel(newMessage.toString());
				// Make message font bigger than the note font.
				Font font = label.getFont();
				font = font.deriveFont(font.getSize() + 2);
				font = font.deriveFont(Font.BOLD);
				label.setFont(font);
				messageComponent = label;
			}

			if (body != null) {
				GridBagConstraints cons = new GridBagConstraints();
				cons.gridx = 0;
				cons.gridy = 0;
				cons.gridwidth = GridBagConstraints.REMAINDER;
				cons.gridheight = 1;
				cons.insets = new Insets(0, 0, 3, 0);
				cons.fill = GridBagConstraints.HORIZONTAL;
				cons.weightx = 1.0;
				body.add(messageComponent, cons);
			}
		}

		message = newMessage;
		pack();
	}

	/**
	 * Specifies the additional note that is displayed along with the progress
	 * message. Used, for example, to show which file the is currently being
	 * copied during a multiple-file copy.
	 * 
	 * @param note
	 *            the note to display.
	 * @see #getNote
	 */
	public void setNote(String note) {
		noteLabel.setText(note);
		pack();
	}

	/**
	 * Specifies the additional note that is displayed along with the progress
	 * message.
	 * 
	 * @return the note to display.
	 * @see #setNote
	 */
	public String getNote() {
		return noteLabel.getText();
	}

	public static void main(String[] args) {
		Icon icon = JNCUResources.getIcon("/dialog/import.png");

		JNCUModuleDialog d = new JNCUModuleDialog(icon, "msg", "note", 0, 100);
		d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		d.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);

		final Object src = event.getSource();

		if (src == cancelOption) {
			notifyCancel();
		}
	}

	/**
	 * Notify the listener of the user's intent to cancel the operation, but
	 * without closing the dialog.
	 */
	private void notifyCancel() {
		canceled = true;
		boolean cancel = false;
		if ((cancelOption != null) && (listener != null)) {
			cancel |= listener.moduleProgressCancel(this);
		}
		if (cancel)
			close();
	}

	@Override
	public void windowOpened(WindowEvent event) {
	}

	@Override
	public void windowClosing(WindowEvent event) {
		if (getDefaultCloseOperation() != WindowConstants.DO_NOTHING_ON_CLOSE)
			notifyCancel();
	}

	@Override
	public void windowClosed(WindowEvent event) {
	}

	@Override
	public void windowIconified(WindowEvent event) {
	}

	@Override
	public void windowDeiconified(WindowEvent event) {
	}

	@Override
	public void windowActivated(WindowEvent event) {
	}

	@Override
	public void windowDeactivated(WindowEvent event) {
	}

	/**
	 * Returns true if the user hits the Cancel button in the progress dialog.
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * Returns the minimum value -- the lower end of the progress value.
	 * 
	 * @return the minimum value.
	 * @see #setMinimum
	 */
	public int getMinimum() {
		return bar.getMinimum();
	}

	/**
	 * Specifies the minimum value.
	 * 
	 * @param m
	 *            the minimum value.
	 * @see #getMinimum
	 */
	public void setMinimum(int m) {
		bar.setMinimum(m);
	}

	/**
	 * Returns the maximum value -- the higher end of the progress value.
	 * 
	 * @return the maximum value.
	 * @see #setMaximum
	 */
	public int getMaximum() {
		return bar.getMaximum();
	}

	/**
	 * Specifies the maximum value.
	 * 
	 * @param m
	 *            the maximum value.
	 * @see #getMaximum
	 */
	public void setMaximum(int m) {
		bar.setMaximum(m);
	}

	/**
	 * Indicate the progress of the operation being monitored. If the specified
	 * value is >= the maximum, the progress monitor is closed.
	 * 
	 * @param nv
	 *            an integer specifying the current value, between the maximum
	 *            and minimum specified for this component.
	 * @see #setMinimum
	 * @see #setMaximum
	 * @see #close
	 */
	public void setProgress(final int nv) {
		int min = getMinimum();
		int max = getMaximum();
		bar.setIndeterminate((nv < min) || (min == max));
		bar.setValue(nv);
		if (!isShowing()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					setVisible(true);
				}
			});
		}
	}

	/**
	 * Indicate that the operation is complete. This happens automatically when
	 * the value set by setProgress is >= max, but it may be called earlier if
	 * the operation ends early.
	 */
	public void close() {
		setVisible(false);
		dispose();
	}

	/**
	 * Set the progress dialog listener.
	 * 
	 * @param listener
	 *            the listener.
	 */
	public void setListener(ModuleProgressListener listener) {
		this.listener = listener;
	}

	/**
	 * Set the message icon.
	 * 
	 * @param icon
	 *            the icon.
	 */
	public void setIcon(Icon icon) {
		iconLabel.setIcon(icon);
	}

	/**
	 * Allow the dialog to be cancelled?
	 * 
	 * @param cancel
	 *            allow cancel?
	 */
	public void setCancelOption(boolean cancel) {
		if (cancel) {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getCancelButton().setVisible(true);
		} else {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			getCancelButton().setVisible(false);
		}
	}
}
