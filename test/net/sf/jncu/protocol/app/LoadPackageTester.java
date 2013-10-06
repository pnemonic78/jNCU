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
package net.sf.jncu.protocol.app;

import java.io.File;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDPipeListener;
import net.sf.jncu.cdil.mnp.MNPPacket;
import net.sf.jncu.cdil.mnp.MNPPacketLayer;
import net.sf.jncu.cdil.mnp.MNPPacketListener;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.cdil.mnp.PacketLogger;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.IconModule.IconModuleListener;
import net.sf.jncu.protocol.v2_0.app.LoadPackage;

/**
 * Test to load a package into the Newton.
 * 
 * @author moshew
 */
public class LoadPackageTester implements IconModuleListener, MNPPacketListener, DockCommandListener, CDPipeListener<MNPPacket, MNPPacketLayer> {

	private String portName;
	private String pkgPath;
	private LoadPackage pkg;
	private boolean loading = false;
	private CDLayer layer;
	private MNPPipe pipe;
	private PacketLogger logger;

	public LoadPackageTester() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		LoadPackageTester tester = new LoadPackageTester();
		if (args.length < 2) {
			System.out.println("args: port file");
			System.exit(1);
			return;
		}
		tester.setPortName(args[0]);
		tester.setPath(args[1]);
		try {
			try {
				tester.init();
				tester.run();
			} finally {
				tester.done();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public void setPath(String path) {
		this.pkgPath = path;
	}

	public void init() throws Exception {
		logger = new PacketLogger('P');
		layer = CDLayer.getInstance();
		// Initialize the library
		layer.startUp();
		// Create a connection object
		pipe = layer.createMNPSerial(portName, MNPSerialPort.BAUD_38400);
		pipe.addPacketListener(this);
		pipe.addCommandListener(this);
		pipe.startListening(this);
	}

	public void run() throws Exception {
		File file = new File(pkgPath);

		loading = true;
		pkg = new LoadPackage(pipe, false, null);
		pkg.loadPackage(file);
		while (loading)
			Thread.yield();
	}

	private void done() throws Exception {
		if (pipe != null) {
			pipe.disconnect(); // Break the connection.
			pipe.dispose(); // Delete the pipe object
		}
		if (layer != null) {
			layer.shutDown(); // Close the library
		}
	}

	@Override
	public void successModule(IconModule module) {
		System.out.println("successModule");
		loading = false;
	}

	@Override
	public void cancelModule(IconModule module) {
		System.out.println("cancelModule");
		loading = false;
	}

	@Override
	public void packetAcknowledged(MNPPacket packet) {
		logger.log("A", packet, pipe.getDockingState());
	}

	@Override
	public void packetEOF() {
		logger.log("E", null, pipe.getDockingState());
	}

	@Override
	public void packetReceived(MNPPacket packet) {
		logger.log("R", packet, pipe.getDockingState());
	}

	@Override
	public void packetSending(MNPPacket packet) {
		logger.log("s", packet, pipe.getDockingState());
	}

	@Override
	public void packetSent(MNPPacket packet) {
		logger.log("S", packet, pipe.getDockingState());
	}

	@Override
	public void commandReceiving(DockCommandFromNewton command, int progress, int total) {
	}

	@Override
	public void commandReceived(DockCommandFromNewton command) {
		final String cmd = command.getCommand();

		if (DDisconnect.COMMAND.equals(cmd)) {
			loading = false;
		}
	}

	@Override
	public void commandSending(DockCommandToNewton command, int progress, int total) {
	}

	@Override
	public void commandSent(DockCommandToNewton command) {
	}

	@Override
	public void commandEOF() {
	}

	@Override
	public void pipeDisconnected(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
	}

	@Override
	public void pipeDisconnectFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
		System.exit(100);
	}

	@Override
	public void pipeConnectionListening(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
	}

	@Override
	public void pipeConnectionListenFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
		System.exit(101);
	}

	@Override
	public void pipeConnectionPending(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
	}

	@Override
	public void pipeConnectionPendingFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
		System.exit(102);
	}

	@Override
	public void pipeConnected(CDPipe<MNPPacket, MNPPacketLayer> pipe) {
	}

	@Override
	public void pipeConnectionFailed(CDPipe<MNPPacket, MNPPacketLayer> pipe, Exception e) {
		System.exit(103);
	}

}
