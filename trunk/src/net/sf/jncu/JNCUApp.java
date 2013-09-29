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
package net.sf.jncu;

import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.util.concurrent.TimeoutException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import jssc.SerialPortException;
import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDPipeListener;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.cdil.mnp.MNPPacket;
import net.sf.jncu.cdil.mnp.MNPPacketLayer;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.app.DLoadPackageFile;
import net.sf.jncu.protocol.v2_0.app.LoadPackage;
import net.sf.jncu.protocol.v2_0.data.DImportFile;
import net.sf.jncu.protocol.v2_0.io.DKeyboardPassthrough;
import net.sf.jncu.protocol.v2_0.io.DRequestToBrowse;
import net.sf.jncu.protocol.v2_0.io.FileChooser;
import net.sf.jncu.protocol.v2_0.io.FileChooser.FileChooserListener;
import net.sf.jncu.protocol.v2_0.io.KeyboardInput;
import net.sf.jncu.protocol.v2_0.io.unix.UnixFileChooser;
import net.sf.jncu.protocol.v2_0.io.win.WindowsFileChooser;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.protocol.v2_0.session.DWhichIcons;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;
import net.sf.jncu.protocol.v2_0.sync.BackupDialog;
import net.sf.jncu.protocol.v2_0.sync.DRestoreFile;
import net.sf.jncu.ui.JNCUDeviceDialog;
import net.sf.jncu.ui.JNCUFrame;
import net.sf.jncu.ui.JNCUSettingsDialog;

/**
 * jNCU main application.
 * 
 * @author Moshe
 */
public class JNCUApp implements CDPipeListener<MNPPacket, MNPPacketLayer>, DockCommandListener, FileChooserListener, JNCUController {

	private JNCUController controller;
	private final JNCUFrame frame;
	private CDLayer layer;
	private CDPipe pipe;
	private Settings settings;
	private KeyboardInput keyboardInput;
	private BackupDialog backupDialog;
	private LoadPackage packageLoader;
	private JNCUSettingsDialog settingsDialog;
	private JNCUDeviceDialog deviceDialog;
	private FileChooser chooser;

	/**
	 * Constructs a new application.
	 * 
	 * @throws PlatformException
	 *             if a platform error occurs.
	 */
	public JNCUApp() throws PlatformException {
		super();
		this.frame = new JNCUFrame();
		this.controller = this;
		frame.setController(controller);
		this.layer = CDLayer.getInstance();
		layer.startUp();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		try {
			JNCUApp ncu = new JNCUApp();
			ncu.run();
		} catch (Exception e) {
			showError(null, "main", e);
		}
	}

	/**
	 * Run.
	 * 
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void run() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		controller.start();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Show the error.
	 * 
	 * @param window
	 *            the owner window.
	 * @param message
	 *            the message.
	 * @param e
	 *            the error.
	 */
	public static void showError(final Window window, final String message, final Throwable e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String msg = (message == null ? "" : message);
				if (e != null) {
					e.printStackTrace();
					msg += "\n" + e.getLocalizedMessage();
				}
				String title = null;
				if (window != null) {
					if (window instanceof Frame) {
						title = ((Frame) window).getTitle();
					}
				}
				if (title == null)
					title = JNCUResources.getString("jncu");
				JOptionPane.showMessageDialog(window, msg, title, JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	/**
	 * Connect to MNP.
	 * 
	 * @param portId
	 *            the port name.
	 * @param portSpeed
	 *            the port speed.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 */
	private void connectMNP(String portId, int portSpeed) throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException, BadPipeStateException,
			PipeDisconnectedException, TimeoutException {
		MNPPipe pipe = layer.createMNPSerial(portId, portSpeed);
		pipe.addCommandListener(this);
		this.pipe = pipe;
		this.pipe.startListening(this);
	}

	@Override
	public void sync() {
		// TODO implement me!
	}

	@Override
	public void installPackage() {
		packageLoader = new LoadPackage(pipe, false, frame);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFileChooser packageChooser = packageLoader.getFileChooser();
				int ret = packageChooser.showOpenDialog(frame);
				if (ret == JFileChooser.APPROVE_OPTION) {
					File selectedFile = packageChooser.getSelectedFile();
					packageLoader.loadPackage(selectedFile);
				}
			}
		});
	}

	@Override
	public void keyboard() {
		write(new DKeyboardPassthrough());
	}

	@Override
	public void backupToDesktop() {
		backupDialog = new BackupDialog(frame);
		// TODO backupDialog.setStores(stores);
		// TODO backupDialog.setApplications(apps);
		// Don't block the command thread.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				backupDialog.setVisible(true);
			}
		});
	}

	@Override
	public void exportToDesktop() {
		// TODO implement me!
	}

	@Override
	public void restoreToNewton() {
		// TODO implement me!
	}

	@Override
	public void importToNewton() {
		// TODO implement me!
	}

	@Override
	public void close() {
		stop();
		try {
			layer.shutDown();
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * Get the settings.
	 * 
	 * @return the settings.
	 */
	public Settings getSettings() {
		if (settings == null) {
			settings = new Settings();
		}
		return settings;
	}

	/**
	 * Set the settings.
	 * 
	 * @param settings
	 *            the settings.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	@Override
	public void stop() {
		try {
			if (pipe != null) {
				if (pipe.isConnected()) {
					write(new DDisconnect());
					try {
						// Enough time to send the cancel and receive an
						// acknowledge.
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
				}
				pipe.disconnect();
				pipe.dispose();
			}
			layer.shutDown();
		} catch (Exception e) {
			// ignore
		}
	}

	@Override
	public void start() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		Settings settings = getSettings();
		String portName = settings.getCommunications().getPortIdentifier();
		if (portName == null) {
			settings.getCommunications().setPortIdentifier("");
			settings.save();
			showSettings();
			return;
		}
		if (portName.length() > 0) {
			int baud = settings.getCommunications().getPortSpeed();
			try {
				connectMNP(portName, baud);
			} catch (PlatformException pe) {
				Throwable cause = pe.getCause();
				// Ignore serial port errors and give user chance to change its
				// settings.
				if ((cause == null) || !(cause instanceof SerialPortException)) {
					throw pe;
				}
			}
		}
	}

	@Override
	public void showSettings() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		controller.stop();
		Settings settings = getSettings();
		getSettingsDialog().setSettings(settings);
		getSettingsDialog().setVisible(true);
		setSettings(settings);
		controller.start();
	}

	/**
	 * Get the settings dialog.
	 * 
	 * @return the dialog.
	 */
	private JNCUSettingsDialog getSettingsDialog() {
		if (settingsDialog == null) {
			settingsDialog = new JNCUSettingsDialog(frame);
			settingsDialog.setSettings(getSettings());
		}
		return settingsDialog;
	}

	/**
	 * Get the device information dialog.
	 * 
	 * @return the dialog.
	 */
	private JNCUDeviceDialog getDeviceDialog() {
		if (deviceDialog == null) {
			deviceDialog = new JNCUDeviceDialog(frame);
		}
		return deviceDialog;
	}

	@Override
	public void showDevice() {
		NewtonInfo info = DockingProtocol.getNewtonInfo();
		getDeviceDialog().setDeviceInfo(info);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getDeviceDialog().setVisible(true);
			}
		});
	}

	@Override
	public void pipeDisconnected(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
		frame.setConnected(false);
	}

	@Override
	public void pipeDisconnectFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
		frame.setConnected(false);
	}

	@Override
	public void pipeConnectionListening(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
		frame.setConnected(false);
	}

	@Override
	public void pipeConnectionListenFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
		frame.setConnected(false);
	}

	@Override
	public void pipeConnectionPending(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
		frame.setConnected(false);
	}

	@Override
	public void pipeConnectionPendingFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
		frame.setConnected(false);
	}

	@Override
	public void pipeConnected(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
		frame.setConnected(true);
		frame.setIcons(DWhichIcons.ALL);
	}

	@Override
	public void pipeConnectionFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
		frame.setConnected(false);
	}

	@Override
	public void commandReceiving(IDockCommandFromNewton command, int progress, int total) {
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		final String cmd = command.getCommand();

		if (DResult.COMMAND.equals(cmd)) {
			final DResult res = (DResult) command;
			if (res.getErrorCode() != DResult.OK) {
				JNCUApp.showError(frame, "Error: " + res.getErrorCode(), res.getError());
			}
		} else if (DKeyboardPassthrough.COMMAND.equals(cmd)) {
			keyboardInput = new KeyboardInput(pipe, frame);
			keyboardInput.addListener(this);
			// Don't block the command thread.
			keyboardInput.start();
		} else if (DRequestToBrowse.COMMAND.equals(cmd)) {
			if (File.separatorChar == '\\')
				chooser = new WindowsFileChooser(pipe, FileChooser.PACKAGES, frame);
			else
				chooser = new UnixFileChooser(pipe, FileChooser.PACKAGES, frame);
			chooser.addListener(this);
		}
	}

	@Override
	public void commandSending(IDockCommandToNewton command, int progress, int total) {
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
	}

	@Override
	public void commandEOF() {
		close();
	}

	/**
	 * Write a command to the Newton.
	 * 
	 * @param command
	 *            the command.
	 */
	protected void write(IDockCommandToNewton command) {
		if (pipe == null)
			return;
		try {
			if (pipe.allowSend())
				pipe.write(command);
		} catch (Exception e) {
			JNCUApp.showError(frame, "write command", e);
			if (!DOperationDone.COMMAND.equals(command.getCommand())) {
				write(new DOperationDone());
			}
		}
	}

	@Override
	public void successModule(IconModule module) {
	}

	@Override
	public void cancelModule(IconModule module) {
	}

	@Override
	public void approveSelection(FileChooser chooser, File file, IDockCommandFromNewton command) {
		final String cmd = command.getCommand();

		if (DImportFile.COMMAND.equals(cmd)) {
			// TODO implement me!
		} else if (DRestoreFile.COMMAND.equals(cmd)) {
			// TODO implement me!
		} else if (DLoadPackageFile.COMMAND.equals(cmd)) {
			packageLoader = new LoadPackage(pipe, true, frame);
			packageLoader.loadPackage(file);
		}
	}

	@Override
	public void cancelSelection(FileChooser chooser) {
	}

}
