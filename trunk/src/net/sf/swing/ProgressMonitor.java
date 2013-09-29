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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import net.sf.jncu.JNCUResources;

/**
 * A class to monitor the progress of some operation.
 * 
 * @author Moshe
 * @see javax.swing.ProgressMonitor
 */
public class ProgressMonitor {

	static {
		SwingUtils.init();
	}

	/** Minimum button size. */
	private Dimension buttonMinimumSize;

	private JDialog dialog;
	private ProgressOptionPane pane;
	private JProgressBar bar;
	private JLabel noteLabel;
	private Component parentComponent;
	private String note;
	private JButton cancelOption = null;
	private Object message;
	private int min;
	private int max;
	private boolean canceled;
	private ProgressMonitorListener listener;
	private Runnable runnerShow;
	private int defaultCloseOperation;

	/**
	 * Constructs a new progress monitor.
	 * 
	 * @param parentComponent
	 *            the parent component for the dialog box
	 * @param message
	 *            a descriptive message that will be shown to the user to
	 *            indicate what operation is being monitored. This does not
	 *            change as the operation progresses. See the message parameters
	 *            to methods in {@link JOptionPane#message} for the range of
	 *            values.
	 * @param note
	 *            a short note describing the state of the operation. As the
	 *            operation progresses, you can call setNote to change the note
	 *            displayed. This is used, for example, in operations that
	 *            iterate through a list of files to show the name of the file
	 *            being processes. If note is initially null, there will be no
	 *            note line in the dialog box and setNote will be ineffective.
	 * @param min
	 *            the lower bound of the range.
	 * @param max
	 *            the upper bound of the range.
	 */
	public ProgressMonitor(Component parentComponent, Object message, String note, int min, int max) {
		this(parentComponent, message, note, min, max, WindowConstants.HIDE_ON_CLOSE);
	}

	/**
	 * Constructs a new progress monitor.
	 * 
	 * @param parentComponent
	 *            the parent component for the dialog box
	 * @param message
	 *            a descriptive message that will be shown to the user to
	 *            indicate what operation is being monitored. This does not
	 *            change as the operation progresses. See the message parameters
	 *            to methods in {@link JOptionPane#message} for the range of
	 *            values.
	 * @param note
	 *            a short note describing the state of the operation. As the
	 *            operation progresses, you can call setNote to change the note
	 *            displayed. This is used, for example, in operations that
	 *            iterate through a list of files to show the name of the file
	 *            being processes. If note is initially null, there will be no
	 *            note line in the dialog box and setNote will be ineffective.
	 * @param min
	 *            the lower bound of the range.
	 * @param max
	 *            the upper bound of the range.
	 * @param defaultCloseOperation
	 *            the default close operation.
	 */
	public ProgressMonitor(Component parentComponent, Object message, String note, int min, int max, int defaultCloseOperation) {
		this(parentComponent, message, note, min, max, defaultCloseOperation, null);
	}

	/**
	 * Constructs a new progress monitor.
	 * 
	 * @param parentComponent
	 *            the parent component for the dialog box
	 * @param message
	 *            a descriptive message that will be shown to the user to
	 *            indicate what operation is being monitored. This does not
	 *            change as the operation progresses. See the message parameters
	 *            to methods in {@link JOptionPane#message} for the range of
	 *            values.
	 * @param note
	 *            a short note describing the state of the operation. As the
	 *            operation progresses, you can call setNote to change the note
	 *            displayed. This is used, for example, in operations that
	 *            iterate through a list of files to show the name of the file
	 *            being processes. If note is initially null, there will be no
	 *            note line in the dialog box and setNote will be ineffective.
	 * @param min
	 *            the lower bound of the range.
	 * @param max
	 *            the upper bound of the range.
	 * @param listener
	 *            the listener.
	 */
	public ProgressMonitor(Component parentComponent, Object message, String note, int min, int max, ProgressMonitorListener listener) {
		this(parentComponent, message, note, min, max, WindowConstants.HIDE_ON_CLOSE, listener);
	}

	/**
	 * Constructs a new progress monitor.
	 * 
	 * @param parentComponent
	 *            the parent component for the dialog box
	 * @param message
	 *            a descriptive message that will be shown to the user to
	 *            indicate what operation is being monitored. This does not
	 *            change as the operation progresses. See the message parameters
	 *            to methods in {@link JOptionPane#message} for the range of
	 *            values.
	 * @param note
	 *            a short note describing the state of the operation. As the
	 *            operation progresses, you can call setNote to change the note
	 *            displayed. This is used, for example, in operations that
	 *            iterate through a list of files to show the name of the file
	 *            being processes. If note is initially null, there will be no
	 *            note line in the dialog box and setNote will be ineffective.
	 * @param min
	 *            the lower bound of the range.
	 * @param max
	 *            the upper bound of the range.
	 * @param defaultCloseOperation
	 *            the default close operation.
	 * @param listener
	 *            the listener.
	 */
	public ProgressMonitor(Component parentComponent, Object message, String note, int min, int max, int defaultCloseOperation, ProgressMonitorListener listener) {
		super();
		this.parentComponent = parentComponent;
		this.message = message;
		this.note = note;
		this.min = min;
		this.max = max;
		this.defaultCloseOperation = defaultCloseOperation;
		this.listener = listener;

		int buttonMinimumWidth = UIManager.getInt("OptionPane.buttonMinimumWidth");
		this.buttonMinimumSize = new Dimension(buttonMinimumWidth, 24);
	}

	/**
	 * Indicate that the operation is complete. This happens automatically when
	 * the value set by setProgress is >= max, but it may be called earlier if
	 * the operation ends early.
	 */
	public void close() {
		if (dialog != null) {
			dialog.setVisible(false);
			dialog.dispose();
			dialog = null;
			bar = null;
		}
	}

	/**
	 * Returns the minimum value -- the lower end of the progress value.
	 * 
	 * @return an int representing the minimum value
	 * @see #setMinimum
	 */
	public int getMinimum() {
		return min;
	}

	/**
	 * Specifies the minimum value.
	 * 
	 * @param m
	 *            an int specifying the minimum value
	 * @see #getMinimum
	 */
	public void setMinimum(int m) {
		if (bar != null) {
			bar.setMinimum(m);
		}
		min = m;
	}

	/**
	 * Returns the maximum value -- the higher end of the progress value.
	 * 
	 * @return an int representing the maximum value
	 * @see #setMaximum
	 */
	public int getMaximum() {
		return max;
	}

	/**
	 * Specifies the maximum value.
	 * 
	 * @param m
	 *            an int specifying the maximum value
	 * @see #getMaximum
	 */
	public void setMaximum(int m) {
		if (bar != null) {
			bar.setMaximum(m);
		}
		max = m;
	}

	/**
	 * Returns true if the user hits the Cancel button in the progress dialog.
	 */
	public boolean isCanceled() {
		return canceled;
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
		if (nv >= max) {
			close();
			return;
		}
		if (bar != null) {
			bar.setIndeterminate(nv < min);
			bar.setValue(nv);
			return;
		}

		bar = new JProgressBar();
		bar.setIndeterminate(nv < min);
		bar.setMinimum(min);
		bar.setMaximum(max);
		bar.setValue(nv);
		if (note != null)
			noteLabel = new JLabel(note);
		if (defaultCloseOperation == WindowConstants.DO_NOTHING_ON_CLOSE) {
			pane = new ProgressOptionPane(new Object[] { message, noteLabel, bar });
		} else {
			JPanel buttons = createButtonsPanel();
			if (listener == null)
				cancelOption = createOkButton();
			else
				cancelOption = createCancelButton();
			buttons.add(cancelOption);
			pane = new ProgressOptionPane(new Object[] { message, noteLabel, bar, buttons });
			cancelOption.addActionListener(pane);
		}

		dialog = pane.createDialog(parentComponent, UIManager.getString("ProgressMonitor.progressText"));
		dialog.setDefaultCloseOperation(defaultCloseOperation);
		dialog.addWindowListener(pane);
		if (runnerShow == null) {
			runnerShow = new Runnable() {
				@Override
				public void run() {
					dialog.setVisible(true);
				}
			};
			SwingUtilities.invokeLater(runnerShow);
		}
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
		this.note = note;
		if (noteLabel != null) {
			noteLabel.setText(note);
		}
	}

	/**
	 * Specifies the additional note that is displayed along with the progress
	 * message.
	 * 
	 * @return the note to display.
	 * @see #setNote
	 */
	public String getNote() {
		return note;
	}

	private class ProgressOptionPane extends JOptionPane implements ActionListener, WindowListener {

		ProgressOptionPane(Object messageList) {
			super(messageList, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);
		}

		@Override
		public int getMaxCharactersPerLineCount() {
			return 60;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			notifyCancel();
		}

		@Override
		public void windowOpened(WindowEvent event) {
		}

		@Override
		public void windowClosing(WindowEvent event) {
			if (defaultCloseOperation != WindowConstants.DO_NOTHING_ON_CLOSE)
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
		 * Notify the listener of the user's intent to cancel the operation, but
		 * without closing the dialog.
		 */
		private void notifyCancel() {
			canceled = true;
			if ((cancelOption != null) && (listener != null))
				listener.proressMonitorCancel(ProgressMonitor.this);
		}
	}

	/**
	 * Create a new button.
	 * 
	 * @return the button.
	 */
	protected JButton createButton() {
		JButton button = new JButton();
		button.setMinimumSize(buttonMinimumSize);
		return button;
	}

	/**
	 * Create a "cancel" button.
	 * 
	 * @return the button.
	 */
	protected JButton createCancelButton() {
		JButton button = createButton();
		button.setText(JNCUResources.getString("cancel", "Cancel"));
		button.setMnemonic(JNCUResources.getChar("cancelMnemonic", KeyEvent.VK_C));
		button.setIcon(JNCUResources.getIcon("/dialog/cancel.png"));

		return button;
	}

	/**
	 * Create an "OK" button.
	 * 
	 * @return the button.
	 */
	protected JButton createOkButton() {
		JButton button = createButton();
		button.setText(JNCUResources.getString("ok", "OK"));
		button.setMnemonic(JNCUResources.getChar("okMnemonic", KeyEvent.VK_O));
		button.setIcon(JNCUResources.getIcon("/dialog/ok.png"));

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

	/**
	 * Progress monitor listener.
	 * 
	 * @author Moshe
	 */
	public static interface ProgressMonitorListener {
		/**
		 * The progress monitor's is closing.<br>
		 * Provide the listener an opportunity to close the dialog after it
		 * finishes its processing.
		 * 
		 * @param monitor
		 *            the progress monitor.
		 */
		public void proressMonitorCancel(ProgressMonitor monitor);
	}
}
