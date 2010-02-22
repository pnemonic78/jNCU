package net.sf.jncu.cdil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

/**
 * CDIL pipe.
 * 
 * @author moshew
 */
public abstract class CDPipe {

	private CDState state = CDState.DISCONNECTED;

	/**
	 * Creates a new pipe.
	 */
	public CDPipe() {
		super();
	}

	/**
	 * Updates and returns the state of the pipe.<br>
	 * <tt>CD_State CD_GetState(CD_Handle pipe)</tt>
	 * <p>
	 * There is no guarantee that two calls to <tt>CD_GetState</tt> made one
	 * right after the other will return the same value. In particular, the
	 * state can always change from <tt>kCD_Listening</tt> to
	 * <tt>kCD_ConnectPending</tt> or <tt>kCD_DisconnectPending</tt>, or from
	 * <tt>kCD_Connected</tt> to <tt>kCD_DisconnectPending</tt>.
	 * 
	 * @return the state.
	 */
	public CDState getState() {
		return state;
	}

	/**
	 * Set the state.
	 * 
	 * @param state
	 *            the state.
	 */
	protected void setState(CDState state) {
		this.state = state;
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
	 */
	@SuppressWarnings("unused")
	public void dispose() throws CDILNotInitializedException, PlatformException {
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
	@SuppressWarnings("unused")
	public void disconnect() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
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
	@SuppressWarnings("unused")
	public void startListening() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
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
	@SuppressWarnings("unused")
	public void accept() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
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
	@SuppressWarnings("unused")
	public InputStream read() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		return null;
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
	@SuppressWarnings("unused")
	public OutputStream write() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
		return null;
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
	@SuppressWarnings("unused")
	public void idle() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
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
	@SuppressWarnings("unused")
	public void setTimeout(int timeoutInSecs) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException,
			TimeoutException {
	}
}
