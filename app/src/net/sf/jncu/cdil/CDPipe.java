/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jncu.cdil;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v2_0.session.DockingProtocol;
import net.sf.jncu.protocol.v2_0.session.DockingState;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Timer;
import java.util.concurrent.TimeoutException;

/**
 * CDIL pipe.
 *
 * @author moshew
 */
public abstract class CDPipe<P extends CDPacket, L extends CDPacketLayer<P>> extends Thread implements CDStateListener, CDPacketListener<P>, DockCommandListener {

    protected static final long PING_TIME = 10000L;

    protected final CDLayer layer;
    /**
     * @deprecated
     */
    @Deprecated
    private PipedOutputStream pipeSource;
    /**
     * @deprecated
     */
    @Deprecated
    private InputStream pipeSink;
    private L packetLayer;
    private CDCommandLayer<P, L> cmdLayer;
    protected DockingProtocol<P, L> docking;
    private final Timer timer = new Timer();
    private CDPing pingTask;
    private boolean pingFixed;
    private CDPipeListener<P, L> listener;

    /**
     * Creates a new pipe.
     *
     * @param layer the owner layer.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public CDPipe(CDLayer layer) throws ServiceNotSupportedException {
        super();
        setName("CDPipe-" + getId());
        if (layer == null)
            throw new ServiceNotSupportedException("layer required");
        this.layer = layer;
        layer.addStateListener(this);
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
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is already disconnected.
     * @throws TimeoutException            if timeout occurs.
     */
    public void dispose() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
        layer.checkInitialized();
        if (getCDState() != CDState.DISCONNECTED) {
            disconnect();
        }
        layer.removeStateListener(this);
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
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is already disconnected.
     * @throws TimeoutException            if timeout occurs.
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
     */
    protected void disconnectImpl() {
        stopPing();
        getCommandLayer().close();
        getPacketLayer().close();
    }

    /**
     * Disconnect quietly.
     */
    public void disconnectQuiet() {
        try {
            disconnect();
        } catch (Exception e) {
            if (listener != null)
                listener.pipeDisconnectFailed(this, e);
        }
    }

    /**
     * Makes the pipe start listening for a connection for a Newton device.<br>
     * <tt>DIL_Error CD_StartListening(CD_Handle pipe)</tt>
     * <p>
     * After the successful completion of this call, the pipe is put in the
     * <tt>kCD_Listening</tt> state.
     *
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is disconnected.
     * @throws TimeoutException            if timeout occurs.
     */
    public void startListening() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
        layer.checkInitialized();
        if (getCDState() != CDState.DISCONNECTED) {
            throw new BadPipeStateException("pipe connected");
        }
        this.docking = createDockingProtocol();
        start();
        layer.setState(CDState.LISTENING);
    }

    /**
     * Makes the pipe start listening for a connection for a Newton device.<br>
     * <tt>DIL_Error CD_StartListening(CD_Handle pipe)</tt>
     * <p>
     * After the successful completion of this call, the pipe is put in the
     * <tt>kCD_Connected</tt> state.
     *
     * @param listener the listener.
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is disconnected.
     * @throws TimeoutException            if timeout occurs.
     * @see #startListening()
     */
    public void startListening(CDPipeListener<P, L> listener) throws BadPipeStateException, CDILNotInitializedException, PlatformException, PipeDisconnectedException,
            TimeoutException {
        this.listener = listener;
        startListening();
    }

    @Override
    public void run() {
        getPacketLayer().start();
        getCommandLayer().start();
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
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is disconnected.
     * @throws TimeoutException            if timeout occurs.
     */
    public void accept() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
        layer.checkInitialized();
        if (getCDState() != CDState.CONNECT_PENDING) {
            throw new BadPipeStateException("state " + getCDState());
        }
        acceptImpl();
    }

    /**
     * Acceptance implementation.
     *
     * @throws PlatformException if a platform error occurs.
     * @throws TimeoutException  if timeout occurs.
     */
    protected void acceptImpl() throws PlatformException, PipeDisconnectedException, TimeoutException {
    }

    /**
     * Reads bytes from a pipe.<br>
     * <tt>DIL_Error CD_Read(CD_Handle pipe, void* p, long count)</tt>
     * <p>
     * Note that a pipe need not be connected in order for bytes to be read from
     * it. It is possible for a pipe to have buffered data received from a
     * Newton OS device before the connection was broken. As long as the pipe's
     * state is <tt>kCD_Connected</tt> or <tt>kCD_DisconnectPending</tt>,
     * clients of the CDIL are still able to retrieve these bytes.
     *
     * @return the input stream.
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is disconnected.
     * @throws TimeoutException            if timeout occurs.
     * @deprecated
     */
    @Deprecated
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
     * Sends the given bytes to the Newton device.<br>
     * <tt>DIL_Error CD_Write(CD_Handle pipe, const void* p, long count)</tt>
     * <p>
     * The data is not actually sent each time <tt>CD_Write</tt> is called. It
     * is buffered until either the buffer is full, or a non-CD_Write call is
     * executed: <tt>CD_Idle</tt>, <tt>CD_Read</tt>, <tt>CD_Disconnect</tt>, or
     * <tt>CD_BytesAvailable</tt>.
     *
     * @param b the data.
     * @return the output.
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is disconnected.
     * @throws TimeoutException            if timeout occurs.
     */
    public final void write(byte[] b) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
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
     * @param b      the data.
     * @param offset the array offset.
     * @param count  the number of bytes to write to the pipe.
     * @return the output.
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is disconnected.
     * @throws TimeoutException            if timeout occurs.
     */
    public void write(byte[] b, int offset, int count) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
        layer.checkConnected();
    }

    /**
     * Sends the given command to the Newton device.
     *
     * @param cmd the command.
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is disconnected.
     * @throws TimeoutException            if timeout occurs.
     */
    public void write(DockCommandToNewton cmd) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
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
     * sized one. If you don't call <tt>CD_Idle</tt> frequently enough, you may
     * lose data. On the other hand, you unnecessarily slow down your
     * application if you call this function too frequently. Frequencies on the
     * order of a tenth of second should be adequate. In general you calling
     * this function once each time through the main event loop, is sufficient.
     *
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is disconnected.
     * @throws TimeoutException            if timeout occurs.
     */
    public void idle() throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
        layer.checkInitialized();
        idleImpl();
        Thread.yield();
    }

    /**
     * Idle implementation.
     *
     * @throws PlatformException if a platform error occurs.
     * @throws TimeoutException  if timeout occurs.
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
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     * @throws PipeDisconnectedException   if the pipe is disconnected.
     * @throws TimeoutException            if timeout occurs.
     */
    public void setTimeout(int timeoutInSecs) throws CDILNotInitializedException, PlatformException, BadPipeStateException, PipeDisconnectedException, TimeoutException {
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
     * @throws TimeoutException            if timeout occurs.
     * @throws PlatformException           if a platform error occurs.
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws BadPipeStateException       if pipe is in an incorrect state.
     */
    protected void notifyDisconnected() throws BadPipeStateException, CDILNotInitializedException, PlatformException, TimeoutException {
    }

    /**
     * Newton device connected.
     *
     * @throws BadPipeStateException if pipe is in an incorrect state.
     */
    public void notifyConnected() throws BadPipeStateException {
        if (getCDState() != CDState.LISTENING) {
            throw new BadPipeStateException();
        }
        layer.setState(CDState.CONNECT_PENDING);
        removeCommandListener(docking);
    }

    /**
     * Get the CD state.
     *
     * @return the state.
     */
    protected CDState getCDState() {
        return layer.getState();
    }

    /**
     * Set the CD state.
     *
     * @param state the state.
     */
    protected void setCDState(CDState state) {
        layer.setState(this, state);
    }

    /**
     * Get the docking state.
     *
     * @return the state.
     */
    public DockingState getDockingState() {
        return docking.getState();
    }

    @Override
    public void packetAcknowledged(P packet) {
    }

    @Override
    public void packetEOF() {
        disconnectQuiet();
    }

    @Override
    public void packetReceived(P packet) {
    }

    @Override
    public void packetSending(P packet) {
    }

    @Override
    public void packetSent(P packet) {
    }

    @Override
    public void commandReceiving(DockCommandFromNewton command, int progress, int total) {
    }

    @Override
    public void commandReceived(DockCommandFromNewton command) {
        // We keep connection alive either by pinging.
        if (pingTask != null)
            restartPing();
    }

    @Override
    public void commandSending(DockCommandToNewton command, int progress, int total) {
    }

    @Override
    public void commandSent(DockCommandToNewton command) {
        // We keep connection alive either by pinging.
        if (pingTask != null)
            restartPing();
    }

    @Override
    public void commandEOF() {
        disconnectQuiet();
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
     * Create a packet layer.
     *
     * @return the command layer.
     */
    protected abstract L createPacketLayer();

    /**
     * Create a command layer.
     *
     * @param packetLayer the packet layer.
     * @return the command layer.
     */
    protected abstract CDCommandLayer<P, L> createCommandLayer(L packetLayer);

    /**
     * Can send packets to Newton?
     *
     * @return {@code true} if connected or connection pending.
     */
    public boolean allowSend() {
        CDState stateCD = getCDState();
        return (stateCD == CDState.CONNECT_PENDING) || (stateCD == CDState.CONNECTED) || (stateCD == CDState.LISTENING);
    }

    /**
     * Get the packet layer. Creates the layer if {@code null}.
     *
     * @return the packet layer.
     */
    public L getPacketLayer() {
        if (packetLayer == null) {
            packetLayer = createPacketLayer();
            packetLayer.addPacketListener(this);
        }
        return packetLayer;
    }

    /**
     * Get the command layer. Creates layer if {@code null}.
     *
     * @return the command layer.
     */
    public CDCommandLayer<P, L> getCommandLayer() {
        if (cmdLayer == null) {
            cmdLayer = createCommandLayer(getPacketLayer());
            cmdLayer.addCommandListener(this);
        }
        return cmdLayer;
    }

    /**
     * Add a packet listener.
     *
     * @param listener the listener to add.
     */
    public void addPacketListener(CDPacketListener<P> listener) {
        getPacketLayer().addPacketListener(listener);
    }

    /**
     * Remove a packet listener.
     *
     * @param listener the listener to remove.
     */
    public void removePacketListener(CDPacketListener<P> listener) {
        getPacketLayer().removePacketListener(listener);
    }

    /**
     * Add a command listener.
     *
     * @param listener the listener to add.
     */
    public void addCommandListener(DockCommandListener listener) {
        getCommandLayer().addCommandListener(listener);
    }

    /**
     * Remove a command listener.
     *
     * @param listener the listener to remove.
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
     * @param fixed ping at fixed intervals, even though connection is busy?
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

    /**
     * Create a docking protocol handler.
     *
     * @return the docker.
     */
    protected DockingProtocol<P, L> createDockingProtocol() {
        return new DockingProtocol<P, L>(this);
    }

    /**
     * Process the command.
     *
     * @param command the received command.
     */
    protected void processCommand(DockCommandFromNewton command) {
    }

    /**
     * Is the pipe connected?
     *
     * @return {@code true} if connecting or connected.
     */
    public boolean isConnected() {
        final CDState state = getLayer().getState();
        if ((state == CDState.CONNECT_PENDING) || (state == CDState.CONNECTED))
            return true;
        final DockingState dockingState = getDockingState();
        return (dockingState != DockingState.DISCONNECTING) && (dockingState != DockingState.DISCONNECTED);
    }

    /**
     * Notify this pipe that a timeout error occurred.
     *
     * @param te the timeout error.
     */
    public void notifyTimeout(TimeoutException te) {
        disconnectQuiet();
    }

    @Override
    public void stateChanged(CDLayer layer, CDState newState) {
        final CDPipeListener<P, L> listener = this.listener;
        if (listener == null)
            return;

        switch (newState) {
            case CONNECT_PENDING:
                try {
                    listener.pipeConnectionPending(this);
                    accept();
                } catch (Exception e) {
                    listener.pipeConnectionPendingFailed(this, e);
                }
                break;
            case CONNECTED:
                try {
                    idle();
                    listener.pipeConnected(this);
                } catch (Exception e) {
                    listener.pipeConnectionFailed(this, e);
                }
                break;
            case DISCONNECT_PENDING:
                break;
            case DISCONNECTED:
                listener.pipeDisconnected(this);
                break;
            case LISTENING:
                listener.pipeConnectionListening(this);
                break;
            case UNINITIALIZED:
                break;
            case UNKNOWN:
                break;
        }
    }

    /**
     * Clear the commands queue.
     */
    public void clearCommands() {
        getCommandLayer().clearQueue();
    }
}
