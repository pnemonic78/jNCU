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

import net.sf.util.zip.ChecksumException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.concurrent.TimeoutException;

/**
 * CD packet layer.
 *
 * @author Moshe
 */
public abstract class CDPacketLayer<P extends CDPacket> extends Thread {

    /**
     * System property name for a filter class.
     */
    public static final String PROPERTY_FILTER = "jncu.cdil.filterClass";
    protected final CDPipe<P, ? extends CDPacketLayer<P>> pipe;
    private boolean running;
    private int timeout;
    private final Timer timer = new Timer();
    private CDTimeout timeoutTask;
    /**
     * List of packet listeners.
     */
    private final Collection<CDPacketListener<P>> listeners = new ArrayList<CDPacketListener<P>>();
    /**
     * List of packet filters.
     */
    private final Collection<CDPacketFilter<P>> filters = new ArrayList<CDPacketFilter<P>>();

    /**
     * Constructs a new packet layer.
     *
     * @param pipe the pipe.
     */
    @SuppressWarnings("unchecked")
    public CDPacketLayer(CDPipe<P, ? extends CDPacketLayer<P>> pipe) {
        super();
        setName("CDPacketLayer-" + getId());
        this.pipe = pipe;
        setTimeout(30);

        String filterClassName = System.getProperty(PROPERTY_FILTER);
        if (filterClassName != null) {
            Class<?> clazz;
            CDPacketFilter<P> filter;
            try {
                clazz = Class.forName(filterClassName);
                filter = (CDPacketFilter<P>) clazz.newInstance();
                addPacketFilter(filter);
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            } catch (InstantiationException ie) {
                ie.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }
        }
    }

    /**
     * Get the output stream for packets.
     *
     * @return the stream.
     * @throws IOException if an I/O error occurs.
     */
    protected abstract OutputStream getOutput() throws IOException;

    /**
     * Get the input stream for packets.
     *
     * @return the stream.
     * @throws IOException if an I/O error occurs.
     */
    protected abstract InputStream getInput() throws IOException;

    /**
     * Is finished?
     *
     * @return finished.
     */
    protected boolean isFinished() {
        return !running;
    }

    /**
     * Add a packet listener.
     *
     * @param listener the listener to add.
     */
    public void addPacketListener(CDPacketListener<P> listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a packet listener.
     *
     * @param listener the listener to remove.
     */
    public void removePacketListener(CDPacketListener<P> listener) {
        listeners.remove(listener);
    }

    /**
     * Notify all the listeners that a packet has been received.
     *
     * @param packet the received packet.
     */
    protected void firePacketReceived(P packet) {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<CDPacketListener<P>> listenersCopy = new ArrayList<CDPacketListener<P>>(listeners);
        for (CDPacketListener<P> listener : listenersCopy) {
            listener.packetReceived(packet);
        }
    }

    /**
     * Notify all the listeners that a packet is being sent.
     *
     * @param packet the sending packet.
     */
    protected void firePacketSending(P packet) {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<CDPacketListener<P>> listenersCopy = new ArrayList<CDPacketListener<P>>(listeners);
        for (CDPacketListener<P> listener : listenersCopy) {
            listener.packetSending(packet);
        }
    }

    /**
     * Notify all the listeners that a packet has been sent.
     *
     * @param packet the sent packet.
     */
    protected void firePacketSent(P packet) {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<CDPacketListener<P>> listenersCopy = new ArrayList<CDPacketListener<P>>(listeners);
        for (CDPacketListener<P> listener : listenersCopy) {
            listener.packetSent(packet);
        }
    }

    /**
     * Notify all the listeners that a packet has been acknowledged.
     *
     * @param packet the sent packet.
     */
    protected void firePacketAcknowledged(P packet) {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<CDPacketListener<P>> listenersCopy = new ArrayList<CDPacketListener<P>>(listeners);
        for (CDPacketListener<P> listener : listenersCopy) {
            listener.packetAcknowledged(packet);
        }
    }

    /**
     * Notify all the listeners that no more packets will be available.
     */
    protected void firePacketEOF() {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<CDPacketListener<P>> listenersCopy = new ArrayList<CDPacketListener<P>>(listeners);
        for (CDPacketListener<P> listener : listenersCopy) {
            listener.packetEOF();
        }
    }

    /**
     * Close the layer and release resources.
     */
    public void close() {
        running = false;
        timer.cancel();
        listeners.clear();
    }

    /**
     * Restart the timeout.
     */
    protected void restartTimeout() {
        if (timeoutTask != null)
            timeoutTask.cancel();
        if (pipe == null)
            return;
        this.timeoutTask = new CDTimeout(pipe);
        timer.schedule(timeoutTask, timeout * 1000L);
    }

    /**
     * Receive a packet.
     *
     * @return the packet.
     * @throws IOException if an I/O error occurs.
     */
    public P receive() throws IOException {
        if (isFinished())
            return null;
        P packet = null;
        byte[] payload;
        try {
            payload = read();
            packet = createPacket(payload);
            if (packet != null) {
                restartTimeout();

                packet = filterPacket(packet);

                if (packet != null)
                    firePacketReceived(packet);
            }
        } catch (ChecksumException ce) {
            ce.printStackTrace();
        } catch (EOFException eofe) {
            firePacketEOF();
        }
        return packet;
    }

    /**
     * Receive a packet payload.
     *
     * @return the payload - {@code null} otherwise.
     * @throws EOFException if end of stream is reached.
     * @throws IOException  if an I/O error occurs.
     */
    protected abstract byte[] read() throws EOFException, IOException;

    /**
     * Read a byte from the default input.
     *
     * @return the byte value.
     * @throws EOFException if end of stream is reached.
     * @throws IOException  if an I/O error occurs.
     */
    protected int readByte() throws IOException {
        return readByte(getInput());
    }

    /**
     * Read a byte.
     *
     * @param in the input.
     * @return the byte value.
     * @throws EOFException if end of stream is reached.
     * @throws IOException  if an I/O error occurs.
     */
    protected int readByte(InputStream in) throws IOException {
        int b;
        try {
            b = in.read();
        } catch (IOException ioe) {
            // PipedInputStream throws IOException instead of returning -1.
            if ((in.available() == 0) && (in instanceof PipedInputStream))
                throw new EOFException(ioe.getMessage());
            throw ioe;
        }
        if (b == -1)
            throw new EOFException();
        return b;
    }

    /**
     * Create a link packet, and decode.
     *
     * @param payload the payload.
     * @return the packet.
     */
    protected abstract P createPacket(byte[] payload);

    /**
     * Listen for incoming packets until no more packets are available.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void listen() throws IOException {
        P packet;
        do {
            packet = receive();
            yield();
        } while (packet != null);
    }

    /**
     * Send a packet.
     *
     * @param packet the packet.
     * @throws IOException      if an I/O error occurs.
     * @throws TimeoutException if a timeout occurs.
     */
    public void send(P packet) throws IOException, TimeoutException {
        if (isFinished())
            return;
        byte[] payload = packet.serialize();
        try {
            firePacketSending(packet);
            write(payload);
            firePacketSent(packet);
        } catch (EOFException eof) {
            firePacketEOF();
        }
    }

    /**
     * Send a packet.
     *
     * @param payload the payload.
     * @throws IOException if an I/O error occurs.
     */
    protected final void write(byte[] payload) throws IOException {
        write(payload, 0, payload.length);
    }

    /**
     * Send a packet.
     *
     * @param payload the payload.
     * @param offset  the frame offset.
     * @param length  the frame length.
     * @throws IOException if an I/O error occurs.
     */
    protected void write(byte[] payload, int offset, int length) throws IOException {
        OutputStream out = getOutput();
        if (out == null)
            return;
        out.write(payload, offset, length);
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
     */
    public void setTimeout(int timeoutInSecs) {
        this.timeout = timeoutInSecs;
        restartTimeout();
    }

    /**
     * Get the timeout period.
     *
     * @return the timeout in seconds.
     */
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void run() {
        running = true;
        do {
            try {
                listen();
            } catch (EOFException eofe) {
                firePacketEOF();
            } catch (IOException ioe) {
                if (isConnected()) {
                    ioe.printStackTrace();
                }
            }
        } while (running && !isInterrupted() && isConnected());
        running = false;
    }

    /**
     * Is the pipe connected?
     *
     * @return {@code true} if connecting or connected.
     */
    protected boolean isConnected() {
        if (pipe == null)
            return true;
        return pipe.isConnected();
    }

    /**
     * Add a packet filter.
     *
     * @param filter the filter to add.
     */
    public void addPacketFilter(CDPacketFilter<P> filter) {
        if (!filters.contains(filter)) {
            filters.add(filter);
        }
    }

    /**
     * Remove a packet filter.
     *
     * @param filter the filter to remove.
     */
    public void removePacketFilter(CDPacketFilter<P> filter) {
        filters.remove(filter);
    }

    /**
     * Filter the packet.
     *
     * @param packet the packet.
     * @return the packet - {@code null} otherwise.
     */
    protected P filterPacket(P packet) {
        for (CDPacketFilter<P> filter : filters) {
            packet = filter.filterPacket(packet);
            if (packet == null)
                return null;
        }
        return packet;
    }

    /**
     * Clear the sending queue.
     */
    public void clearSend() {
    }
}
