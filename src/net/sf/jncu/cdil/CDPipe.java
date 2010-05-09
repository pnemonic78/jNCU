package net.sf.jncu.cdil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.TimeoutException;

import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;

/**
 * CDIL pipe.
 * 
 * @author moshew
 */
public abstract class CDPipe extends Thread {

	protected final CDLayer layer;
	private PipedOutputStream pipeSource;
	private PipedInputStream pipeSink;
	private int timeout;
	protected DockingProtocol docking;

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
		if (getCDState() == CDState.DISCONNECTED) {
			throw new PipeDisconnectedException();
		}
		layer.setState(CDState.DISCONNECT_PENDING);
		disconnectImpl();
		layer.setState(CDState.DISCONNECTED);
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
		layer.checkInitialized();
		if ((getCDState() != CDState.CONNECTED) && (getCDState() != CDState.DISCONNECT_PENDING)) {
			throw new BadPipeStateException();
		}
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
		layer.checkInitialized();
		if (getCDState() != CDState.CONNECTED) {
			throw new BadPipeStateException();
		}
	}

	/**
	 * Sends the given command to the Newton device.<br>
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
	public void write(DockCommandToNewton cmd) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
		write(cmd.getPayload());
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
		this.timeout = timeoutInSecs;
	}

	/**
	 * Get the timeout period.
	 * 
	 * @return the timeout in seconds.
	 */
	public int getTimeout() {
		return timeout;
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
	protected void notifyDisconnect() throws BadPipeStateException, CDILNotInitializedException, PlatformException, TimeoutException {
		if (getCDState() == CDState.CONNECTED) {
			layer.setState(CDState.DISCONNECT_PENDING);
		} else {
			try {
				disconnect();
			} catch (PipeDisconnectedException pde) {
				// OK, we are already disconnected.
			}
		}
	}

	/**
	 * Newton device connects.
	 * 
	 * @throws TimeoutException
	 *             if timeout occurs.
	 * @throws PipeDisconnectedException
	 *             if the pipe is disconnected.
	 * @throws PlatformException
	 *             if a platform error occurs.
	 * @throws CDILNotInitializedException
	 *             if CDIL is not initialised.
	 * @throws BadPipeStateException
	 *             if pipe is in an incorrect state.
	 */
	protected void notifyConnect() throws BadPipeStateException, CDILNotInitializedException, PlatformException, TimeoutException {
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
}
