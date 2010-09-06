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
	 * @param port
	 *            the serial port.
	 */
	public MNPSerialPacketLayer(MNPSerialPort port) {
		super();
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

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.mnp.MNPPacketLayer#listen(java.io.InputStream)
	 */
	public void listen() throws IOException {
		listen(port.getInputStream());
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.mnp.MNPPacketLayer#read(java.io.InputStream)
	 */
	protected byte[] read() throws IOException {
		return read(port.getInputStream());
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.mnp.MNPPacketLayer#receive(java.io.InputStream)
	 */
	public MNPPacket receive() throws IOException {
		return receive(port.getInputStream());
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.mnp.MNPPacketLayer#send(java.io.OutputStream,
	 * net.sf.jncu.cdil.mnp.MNPPacket)
	 */
	public void send(MNPPacket packet) throws IOException {
		send(port.getOutputStream(), packet);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.mnp.MNPPacketLayer#write(java.io.OutputStream,
	 * byte[], int, int)
	 */
	protected void write(byte[] payload, int offset, int length) throws IOException {
		write(port.getOutputStream(), payload, offset, length);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.mnp.MNPPacketLayer#write(java.io.OutputStream,
	 * byte[])
	 */
	protected void write(byte[] payload) throws IOException {
		write(port.getOutputStream(), payload);
	}

	@Override
	public void close() {
		port.close();
		super.close();
	}

}
