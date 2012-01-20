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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Timer;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;

/**
 * CDIL pipe.
 * 
 * @author moshew
 */
public abstract class CDPipe<P extends CDPacket> extends Thread implements DockCommandListener {

	protected static final long PING_TIME = 10000L;

	protected final CDLayer layer;
	private PipedOutputStream pipeSource;
	private PipedInputStream pipeSink;
	private CDCommandLayer<P> cmdLayer;
	protected DockingProtocol docking;
	private final Timer timer = new Timer();
	private CDPing pingTask;
	private boolean pingFixed;

	/**
	 * Creates a new pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	public CDPipe(CDLayer layer) throws ServiceNotSupportedException {
		super();
		setName("Pipe-" + getId());
		if (layer == null) {
			throw new ServiceNotSupportedException();
		}
		this.layer = layer;
		this.pipeSource = new PipedOutputStream();
		layer.setState(CDState.DISCONNECTED);
	}

	/**
	 * Disposes of the communications pipe.<br>
	 * <tt>DIL_Error CD_Dispose(CD_Handle pipe)</tt>
	 * <p>
	 * The pipe passed to <tt>CD_Dispose</tt> can be in any state. If
	 * appropriate, the pipe is disconnected or removed from a listening state
	 * before it is deleted.<br>
	 * After this call, the reference to the pipe is invalid and should no
	 * longer be used.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is already disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void dispose() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		layer.checkInitialized();
		if (getCDState() != CDState.DISCONNECTED) {
			disconnect();
		}
		timer.cancel();
	}

	/**
	 * Puts the specified pipe in the <tt>kCD_Disconnected</tt> state.<br>
	 * <tt>DIL_Error CD_Disconnect(CD_Handle pipe)</tt>
	 * <p>
	 * If the pipe is listening, it stops listening. If the pipe is connected,
	 * it is disconnected. In all cases, the state of the pipe after making this
	 * call is <tt>kCD_Disconnected</tt>. Any internally buffered data is
	 * flushed and can no longer be read with <tt>CD_Read</tt>.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is already disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void disconnect() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		layer.checkInitialized();
		if (getCDState() != CDState.DISCONNECTED) {
			layer.setState(CDState.DISCONNECT_PENDING);
			disconnectImpl();
			layer.setState(CDState.DISCONNECTED);
			notifyDisconnected();
		}
	}

	/**
	 * Disconnection implementation.
	 * 
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	protected void disconnectImpl() throws PlatformException, TimeoutException {
		stopPing();
		getCommandLayer().close();
	}

	public void disconnectQuiet() {
		try {
			disconnect();
		} catch (BadPipeStateException bpse) {
			// ignore
		} catch (CDILNotInitializedException cnie) {
			// ignore
		} catch (PlatformException pe) {
			// ignore
		} catch (PipeDisconnectedException pde) {
			// ignore
		} catch (TimeoutException te) {
			// ignore
		}
	}

	/**
	 * Makes the pipe start listening for a connection for a Newton device.<br>
	 * <tt>DIL_Error CD_StartListening(CD_Handle pipe)</tt>
	 * <p>
	 * After the successful completion of this call, the pipe is put in the
	 * <tt>kCD_Listening</tt> state.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void startListening() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		layer.checkInitialized();
		if (getCDState() != CDState.DISCONNECTED) {
			throw new BadPipeStateException();
		}
		getCommandLayer();
		this.docking = new DockingProtocol(this);
		start();
		layer.setState(CDState.LISTENING);
	}

	/**
	 * Makes the pipe accept a pending connection.<br>
	 * <tt>DIL_Error CD_Accept(CD_Handle pipe)</tt>
	 * <p>
	 * This pipe should be in the <tt>kCD_ConnectPending</tt> state.<br>
	 * After the successful completion of this call, the pipe is fully
	 * connected, its state will be <tt>kCD_Connected</tt>, and it can be used
	 * to exchange data with a Newton OS application.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void accept() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		layer.checkInitialized();
		if (getCDState() != CDState.CONNECT_PENDING) {
			throw new BadPipeStateException();
		}
		acceptImpl();
	}

	/**
	 * Acceptance implementation.
	 * 
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	protected void acceptImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
	}

	/**
	 * Reads bytes from a pipe.<br>
	 * <tt>DIL_Error CD_Read(CD_Handle pipe, void* p, long count)</tt>
	 * <p>
	 * Note that a pipe need not be connected in order for bytes to be read from
	 * it. It is possible for a pipe to have buffered data received from a
	 * Newton OS device before the connection was broken. As long as the pipe’s
	 * state is <tt>kCD_Connected</tt> or <tt>kCD_DisconnectPending</tt>,
	 * clients of the CDIL are still able to retrieve these bytes.
	 * 
	 * @return the input stream.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public InputStream getInput() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		layer.checkConnected();
		if (pipeSink == null) {
			try {
				this.pipeSink = new PipedInputStream(pipeSource);
			} catch (IOException ioe) {
				throw new PipeDisconnectedException(ioe.getMessage());
			}
		}
		return pipeSink;
	}

	/**
	 * Get the output stream to write actual data that was received from the
	 * Newton. This is the pipe source that is fed to the sink and read using
	 * {@link #getInput()}.
	 * 
	 * @return the source.
	 */
	protected OutputStream getOutput() {
		return pipeSource;
	}

	/**
	 * Sends the given bytes to the Newton device.<br>
	 * <tt>DIL_Error CD_Write(CD_Handle pipe, const void* p, long count)</tt>
	 * <p>
	 * The data is not actually sent each time <tt>CD_Write</tt> is called. It
	 * is buffered until either the buffer is full, or a non-CD_Write call is
	 * executed: <tt>CD_Idle</tt>, <tt>CD_Read</tt>, <tt>CD_Disconnect</tt>, or
	 * <tt>CD_BytesAvailable</tt>.
	 * 
	 * @param b
	 *            the data.
	 * @return the output.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void write(byte[] b) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		write(b, 0, b.length);
	}

	/**
	 * Sends the given bytes to the Newton device.<br>
	 * <tt>DIL_Error CD_Write(CD_Handle pipe, const void* p, long count)</tt>
	 * <p>
	 * The data is not actually sent each time <tt>CD_Write</tt> is called. It
	 * is buffered until either the buffer is full, or a non-CD_Write call is
	 * executed: <tt>CD_Idle</tt>, <tt>CD_Read</tt>, <tt>CD_Disconnect</tt>, or
	 * <tt>CD_BytesAvailable</tt>.
	 * 
	 * @param b
	 *            the data.
	 * @param offset
	 *            the array offset.
	 * @param count
	 *            the number of bytes to write to the pipe.
	 * @return the output.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void write(byte[] b, int offset, int count) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		layer.checkConnected();
	}

	/**
	 * Sends the given command to the Newton device.
	 * 
	 * @param cmd
	 *            the command.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void write(IDockCommandToNewton cmd) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		layer.checkConnected();
		try {
			getCommandLayer().write(cmd);
		} catch (IOException ioe) {
			throw new PipeDisconnectedException(ioe);
		}
	}

	/**
	 * Allows the CDIL to service an open connection.<br>
	 * <tt>DIL_Error CD_Idle(CD_Handle pipe)</tt>
	 * <p>
	 * If the Newton device is sending data very rapidly, you must call this
	 * function frequently to buffer that data. The CDIL uses a dynamically
	 * sized buffer, but the underlying communication tool may use a statically
	 * sized one. If you don’t call <tt>CD_Idle</tt> frequently enough, you may
	 * lose data. On the other hand, you unnecessarily slow down your
	 * application if you call this function too frequently. Frequencies on the
	 * order of a tenth of second should be adequate. In general you calling
	 * this function once each time through the main event loop, is sufficient.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void idle() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		layer.checkInitialized();
		idleImpl();
	}

	/**
	 * Idle implementation.
	 * 
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	protected void idleImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
	}

	/**
	 * Sets the timeout period for CD_Read and CD_Write calls in a pipe.<br>
	 * <tt>DIL_Error CD_SetTimeout(CD_Handle pipe, long timeoutInSecs)</tt>
	 * <p>
	 * When the CDIL pipe is created, it is initialised with a default timeout
	 * period of 30 seconds. This timeout period is used to control
	 * <tt>CD_Read</tt> and <tt>CD_Write</tt> calls (and, indirectly, any
	 * flushing of outgoing data). Timeout values are specified on a per-pipe
	 * basis.<br>
	 * For <tt>CD_Read</tt>, if the requested number of bytes are not available
	 * after the timeout period, a <tt>kCD_Timeout</tt> error is returned and no
	 * bytes will be transferred. For <tt>CD_Write</tt>, if no data can be sent
	 * after the timeout period, a <tt>kCD_Timeout</tt> error is returned.<br>
	 * The timeout does not occur, if the data is presently being transferred.
	 * That is, a long operation does not fail due to a timeout. Note that an
	 * attempt is made to send data even if the timeout is set to zero seconds.
	 * 
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws TimeoutException
	 *             if timeout occurs.
	 */
	public void setTimeout(int timeoutInSecs) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		layer.checkInitialized();
		getCommandLayer().setTimeout(timeoutInSecs);
	}

	/**
	 * Get the timeout period.
	 * 
	 * @return the timeout in seconds.
	 */
	public int getTimeout() {
		return getCommandLayer().getTimeout();
	}

	/**
	 * Error occurs or Newton device disconnects.
	 * 
	 * @throws TimeoutException
	 *             if timeout occurs.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 */
	protected void notifyDisconnected() throws BadPipeStateException, CDILNotInitializedException, PlatformException, TimeoutException {
	}

	/**
	 * Newton device connected.
	 * 
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 */
	public void notifyConnected() throws BadPipeStateException {
		if (getCDState() != CDState.LISTENING) {
			throw new BadPipeStateException();
		}
		layer.setState(CDState.CONNECT_PENDING);
	}

	protected CDState getCDState() {
		return layer.getState();
	}

	protected void setCDState(CDState state) {
		layer.setState(this, state);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandListener#commandReceived(net.sf.jncu.
	 * protocol.IDockCommandFromNewton)
	 */
	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		// We keep connection alive either by pinging.
		restartPing();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandListener#commandSent(net.sf.jncu.protocol
	 * .IDockCommandToNewton)
	 */
	@Override
	public void commandSent(IDockCommandToNewton command) {
		// We keep connection alive either by pinging.
		restartPing();
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.DockCommandListener#commandEOF()
	 */
	@Override
	public void commandEOF() {
		stopPing();
	}

	/**
	 * Get the CD layer.
	 * 
	 * @return the layer.
	 */
	public CDLayer getLayer() {
		return layer;
	}

	/**
	 * Create a command layer.
	 * 
	 * @return the command layer.
	 */
	protected abstract CDCommandLayer<P> createCommandLayer();

	/**
	 * Can send?
	 * 
	 * @return true if connected or connection pending.
	 */
	public boolean canSend() {
		CDState stateCD = getLayer().getState();
		return (stateCD == CDState.CONNECT_PENDING) || (stateCD == CDState.CONNECTED) || (stateCD == CDState.LISTENING);
	}

	/**
	 * Get the command layer. Creates layer if {@code null}.
	 * 
	 * @return the the command layer.
	 */
	protected CDCommandLayer<P> getCommandLayer() {
		if (cmdLayer == null) {
			cmdLayer = createCommandLayer();
			cmdLayer.addCommandListener(this);
			cmdLayer.start();
		}
		return cmdLayer;
	}

	/**
	 * Add a command listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addCommandListener(DockCommandListener listener) {
		getCommandLayer().addCommandListener(listener);
	}

	/**
	 * Remove a command listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeCommandListener(DockCommandListener listener) {
		getCommandLayer().removeCommandListener(listener);
	}

	/**
	 * Start pinging the Newton every 10 seconds to maintain connection and
	 * avoid a timeout.
	 */
	public void ping() {
		ping(false);
	}

	/**
	 * Start pinging the Newton every 10 seconds to maintain connection and
	 * avoid a timeout.
	 * 
	 * @param fixed
	 *            ping at fixed intervals, even though connection is busy?
	 */
	public void ping(boolean fixed) {
		stopPing();
		this.pingFixed = fixed;
		restartPing();
	}

	/**
	 * Stop pinging the Newton. Resumes connection timeout.
	 */
	public void stopPing() {
		if (pingTask != null)
			pingTask.cancel();
		pingTask = null;
	}

	/**
	 * Restart the ping.
	 */
	protected void restartPing() {
		if (pingFixed) {
			if (pingTask == null) {
				this.pingTask = new CDPing(this);
				timer.schedule(pingTask, PING_TIME, PING_TIME);
			}
		} else {
			if (pingTask != null)
				pingTask.cancel();
			this.pingTask = new CDPing(this);
			timer.schedule(pingTask, PING_TIME);
		}
	}
}
