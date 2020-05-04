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
package net.sf.jncu.cdil;

/**
 * NCU Connection CDIL pipe listener.
 * 
 * @author Moshe
 */
public interface CDPipeListener<P extends CDPacket, L extends CDPacketLayer<P>> {

	/**
	 * Notification that the CDIL pipe is disconnected.
	 * 
	 * @param pipe
	 *            the CDIL pipe.
	 */
	public void pipeDisconnected(CDPipe<P, L> pipe);

	/**
	 * Notification that the CDIL pipe failed to disconnect.
	 * 
	 * @param pipe
	 *            the CDIL pipe.
	 * @param e
	 *            the error.
	 */
	public void pipeDisconnectFailed(CDPipe<P, L> pipe, Exception e);

	/**
	 * Notification that the CDIL pipe is listening for a connection.
	 * 
	 * @param pipe
	 *            the CDIL pipe.
	 */
	public void pipeConnectionListening(CDPipe<P, L> pipe);

	/**
	 * Notification that the CDIL pipe failed to listen for a connection.
	 * 
	 * @param pipe
	 *            the CDIL pipe.
	 * @param e
	 *            the error.
	 */
	public void pipeConnectionListenFailed(CDPipe<P, L> pipe, Exception e);

	/**
	 * Notification that the CDIL pipe has a connection pending.
	 * 
	 * @param pipe
	 *            the CDIL pipe.
	 */
	public void pipeConnectionPending(CDPipe<P, L> pipe);

	/**
	 * Notification that the CDIL pipe failed to pend a connection.
	 * 
	 * @param pipe
	 *            the CDIL pipe.
	 * @param e
	 *            the error.
	 */
	public void pipeConnectionPendingFailed(CDPipe<P, L> pipe, Exception e);

	/**
	 * Notification that the CDIL pipe is connected.
	 * 
	 * @param pipe
	 *            the CDIL pipe.
	 */
	public void pipeConnected(CDPipe<P, L> pipe);

	/**
	 * Notification that the CDIL pipe failed to connect.
	 * 
	 * @param pipe
	 *            the CDIL pipe.
	 * @param e
	 *            the error.
	 */
	public void pipeConnectionFailed(CDPipe<P, L> pipe, Exception e);

}
