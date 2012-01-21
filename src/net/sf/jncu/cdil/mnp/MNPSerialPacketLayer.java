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
package net.sf.jncu.cdil.mnp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

/**
 * MNP packet layer for serial port.
 * 
 * @author moshew
 */
public class MNPSerialPacketLayer extends MNPPacketLayer {

	protected final MNPSerialPort port;

	/**
	 * Creates a new packet layer.
	 * 
	 * @param pipe
	 *            the pipe.
	 * @param port
	 *            the serial port.
	 */
	public MNPSerialPacketLayer(MNPPipe pipe, MNPSerialPort port) {
		super(pipe);
		this.port = port;
	}

	/**
	 * Get the serial port.
	 * 
	 * @return the port.
	 */
	public MNPSerialPort getPort() {
		return port;
	}

	@Override
	protected OutputStream getOutput() throws IOException {
		return port.getOutputStream();
	}

	@Override
	protected InputStream getInput() throws IOException {
		return port.getInputStream();
	}

	@Override
	public void close() {
		super.close();
		port.close();
	}

	public void sendAndAcknowledge(MNPPacket packet) throws TimeoutException {
		sender.sendAndAcknowledge(packet);
	}

}
