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

import net.sf.jncu.cdil.adsp.ADSPPipe;
import net.sf.jncu.cdil.ctb.CTBPipe;
import net.sf.jncu.cdil.mnp.CommPorts;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.tcp.TCPPipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * CDIL layer. Manages CDIL connections.
 *
 * @author moshew
 */
public class CDLayer {

    private static CDLayer instance;
    private final List<String> serialPorts = new ArrayList<String>();
    private CDState state = CDState.UNKNOWN;
    private final List<CDStateListener> stateListeners = new ArrayList<CDStateListener>();

    /**
     * Creates a new CDIL layer.
     */
    protected CDLayer() {
        super();
        setState(CDState.UNINITIALIZED);
    }

    /**
     * Get the CDIL instance.
     *
     * @return the factory.
     */
    public static CDLayer getInstance() {
        if (instance == null) {
            instance = new CDLayer();
        }
        return instance;
    }

    /**
     * Initialises the CDIL library.<br>
     * <tt>DIL_Error CD_Startup()</tt>
     * <p>
     * This call makes sure that any low-level transport layers (for example:
     * ADSP, TCP/IP, MNP) are available and properly initialised. If none are
     * available or none can be initialised, this function returns an error.
     * This function is usually called once at the start of your program.
     * However, you can call it as many times as you want as long as you call
     * <tt>CD_Shutdown</tt> an equal number of times.
     *
     * @throws PlatformException if a platform error occurs.
     */
    public void startUp() throws PlatformException {
        initADSP();
        initCTB();
        initMNP();
        initTCP();
        stateListeners.clear();
        setState(CDState.DISCONNECTED);
    }

    /**
     * Closes any transport layers opened and initialised in startup, and closes
     * and disposes of all open pipes.<br>
     * <tt>DIL_Error CD_Shutdown()</tt>
     * <p>
     * This function must be called once for every time you called
     * <tt>CD_Startup</tt>. Usually, you just call it once at the end of your
     * program. However, you can call it as many times as you want, as long as
     * you don't call it more times that you've called <tt>CD_Startup</tt>. If
     * this is the last call to <tt>CD_Shutdown</tt>, then all memory allocated
     * by the CDIL since <tt>CD_Startup</tt> was called is deallocated.
     *
     * @throws CDILNotInitializedException if CDIL was not initialised.
     * @throws PlatformException           if a platform error occurs.
     */
    public void shutDown() throws CDILNotInitializedException, PlatformException {
        checkInitialized();
        disposeADSP();
        disposeCTB();
        disposeMNP();
        disposeTCP();
        instance = null;
    }

    /**
     * Determines whether the ADSP service is available.<br>
     * <tt>DIL_Error CD_CheckADSP()</tt>
     *
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws PlatformException            if a platform error occurs.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public void checkADSP() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
        checkInitialized();
        throw new ServiceNotSupportedException();
    }

    /**
     * Determines whether the MNP service is available.<br>
     * <tt>DIL_Error CD_CheckMNPSerial()</tt>
     *
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws PlatformException            if a platform error occurs.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public void checkMNPSerial() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
        checkInitialized();
        if (serialPorts.isEmpty()) {
            throw new ServiceNotSupportedException();
        }
    }

    /**
     * Determines whether the TCP service is available.<br>
     * <tt>DIL_Error CD_CheckTCP()</tt>
     *
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws PlatformException            if a platform error occurs.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public void checkTCP() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
        checkInitialized();
        throw new ServiceNotSupportedException();
    }

    /**
     * Determines whether the CTB service is available.<br>
     * <tt>DIL_Error CD_CheckCTB(const char* toolName)</tt>
     *
     * @param toolName the name of the tool.
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws PlatformException            if a platform error occurs.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public void checkCTB(String toolName) throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
        checkInitialized();
        throw new ServiceNotSupportedException();
    }

    /**
     * Returns a user-displayable string containing the name of a selectable
     * serial port.<br>
     * <tt>DIL_Error CD_GetSerialPortName(long index, char* buffer, long* bufLen)</tt>
     * <p>
     * Normal usage of this function is to start with zero, incrementing index,
     * until the function returns <tt>kCD_IndexOutOfRange</tt>.
     *
     * @param index a zero-based value indicating the port for which you want a
     *              string.
     * @return the port name.
     */
    public String getSerialPortName(int index) {
        return serialPorts.get(index);
    }

    /**
     * These functions create a connection with the Newton device using the
     * AppleTalk communication service.<br>
     * <tt>DIL_Error CD_CreateADSP(CD_Handle* pipe, const char* name, const char* type)</tt>
     *
     * @param name the name of the ADSP connection. This string is what appears
     *             in the Chooser list on the Newton OS device. If you pass
     *             <tt>NULL</tt> for this parameter, the CDIL uses a default name
     *             based on your desktop computer's preferences (for instance, on
     *             a Macintosh, it will use the strings specified in the File
     *             Sharing control panel).
     * @param type the connection type. This is searched for by the Chooser on
     *             the Newton OS device. If you pass <tt>NULL</tt> for this
     *             parameter, the CDIL uses the type specified by the
     *             Connection/Dock application.
     * @return the connection.
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws PlatformException            if a platform error occurs.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public ADSPPipe createADSP(String name, byte type) throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
        checkInitialized();
        return new ADSPPipe(this, name, type);
    }

    /**
     * These functions create a connection with the Newton device using the MNP
     * Serial communication service.<br>
     * <tt>DIL_Error CD_CreateMNPSerial(CD_Handle* pipe, long port, long baud)</tt>
     * <p>
     * MNP is a packet-based protocol that ensures delivery of your data using
     * compression and error correction.
     *
     * @param port the serial port to use.
     * @param baud the baud rate to communicate at in bytes per second.
     * @return the connection.
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws PlatformException            if a platform error occurs.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public MNPPipe createMNPSerial(int port, int baud) throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
        return createMNPSerial(serialPorts.get(port), baud);
    }

    /**
     * These functions create a connection with the Newton device using the MNP
     * Serial communication service.<br>
     * <tt>DIL_Error CD_CreateMNPSerial(CD_Handle* pipe, long port, long baud)</tt>
     * <p>
     * MNP is a packet-based protocol that ensures delivery of your data using
     * compression and error correction.
     *
     * @param portName the serial port to use.
     * @param baud     the baud rate to communicate at in bytes per second.
     * @return the connection.
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws PlatformException            if a platform error occurs.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public MNPPipe createMNPSerial(String portName, int baud) throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
        checkInitialized();
        return new MNPPipe(this, portName, baud);
    }

    /**
     * These functions create a connection with the Newton device using the TCP
     * communication service.<br>
     * <tt>DIL_Error CD_CreateTCP(CD_Handle* pipe, long port)</tt>
     *
     * @param port the TCP port to listen on. Note that once the connection is
     *             made, data transfer actually occurs on a different, randomly
     *             chosen, port. This frees up the port specified in this
     *             parameter for future connections.
     * @return the connection.
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws PlatformException            if a platform error occurs.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public TCPPipe createTCP(int port) throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
        checkInitialized();
        return new TCPPipe(this, port);
    }

    /**
     * These functions create a connection with the Newton device using the
     * Macintosh Communication Tool communication service.<br>
     * <tt>DIL_Error CD_CreateCTB (CD_Handle* pipe, const char* toolName, const char* configString)</tt>
     * <p>
     * Comm. toolbox based pipes are only available on the Mac OS platform.
     *
     * @param toolName     the name of the communication tool.
     * @param configString a tool-dependent configuration string.
     * @return the connection.
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws PlatformException            if a platform error occurs.
     * @throws ServiceNotSupportedException if the service is not supported.
     */
    public CTBPipe createCTB(String toolName, String configString) throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException {
        checkInitialized();
        return new CTBPipe(this, toolName, configString);
    }

    /**
     * Initialise ADSP.
     */
    protected void initADSP() throws PlatformException {
    }

    /**
     * Initialise CTB.
     */
    protected void initCTB() throws PlatformException {
    }

    /**
     * Initialise MNP.
     */
    protected void initMNP() throws PlatformException {
        CommPorts commPorts = new CommPorts();
        serialPorts.clear();
        serialPorts.addAll(commPorts.getPortNames());
    }

    /**
     * Initialise TCP.
     */
    protected void initTCP() throws PlatformException {
    }

    /**
     * Dispose ADSP.
     */
    protected void disposeADSP() {
    }

    /**
     * Dispose CTB.
     */
    protected void disposeCTB() {
    }

    /**
     * Dispose MNP.
     */
    protected void disposeMNP() {
        serialPorts.clear();
    }

    /**
     * Dispose TCP.
     */
    protected void disposeTCP() {
    }

    /**
     * Check that CDIL has been initialised.
     *
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     */
    protected final void checkInitialized() throws CDILNotInitializedException, PlatformException {
        if ((state == CDState.UNINITIALIZED) && (serialPorts != null)) {
            throw new CDILNotInitializedException("CDIL not initialized");
        }
    }

    /**
     * Check that CDIL is connected.
     *
     * @throws CDILNotInitializedException if CDIL is not initialised.
     * @throws PlatformException           if a platform error occurs.
     */
    protected final void checkConnected() throws CDILNotInitializedException, PlatformException {
        checkInitialized();
        if ((state != CDState.CONNECTED) && (state != CDState.LISTENING)) {
            throw new BadPipeStateException("state " + state);
        }
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
     * @param state the state.
     */
    protected void setState(CDState state) {
        this.state = state;
        fireStateChanged(state);
    }

    /**
     * Set the state from a pipe.
     *
     * @param pipe  the pipe.
     * @param state the state.
     */
    public void setState(CDPipe pipe, CDState state) {
        if (pipe == null)
            throw new NullPointerException("pipe required");
        setState(state);
    }

    /**
     * Add the CDIL state listener.
     *
     * @param listener the listener.
     */
    public void addStateListener(CDStateListener listener) {
        if (!stateListeners.contains(listener))
            stateListeners.add(listener);
    }

    /**
     * Remove the CDIL state listener.
     *
     * @param listener the listener.
     */
    public void removeStateListener(CDStateListener listener) {
        stateListeners.remove(listener);
    }

    /**
     * Notify all the listeners that the state changed.
     *
     * @param newState the new state.
     */
    protected void fireStateChanged(CDState newState) {
        // Make copy of listeners to avoid ConcurrentModificationException.
        Collection<CDStateListener> listenersCopy = new ArrayList<CDStateListener>(stateListeners);
        for (CDStateListener listener : listenersCopy) {
            listener.stateChanged(this, newState);
        }
    }
}
