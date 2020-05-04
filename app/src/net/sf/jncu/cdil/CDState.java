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

/**
 * NCU Connection state.
 *
 * @author moshew
 */
public enum CDState {

    /**
     * Unknown.<br>
     * <tt>kCD_Unknown</tt>
     */
    UNKNOWN,
    /**
     * Is uninitialised.<br>
     * <tt>kCD_Uninitialized</tt>
     */
    UNINITIALIZED,
    /**
     * Is not connected.<br>
     * <tt>kCD_Disconnected</tt>
     */
    DISCONNECTED,
    /**
     * Is listening for a connection.<br>
     * <tt>kCD_Listening</tt>
     */
    LISTENING,
    /**
     * A connection is pending.<br>
     * <tt>kCD_ConnectPending</tt>
     */
    CONNECT_PENDING,
    /**
     * Is connected.<br>
     * <tt>kCD_Connected</tt>
     */
    CONNECTED,
    /**
     * A disconnection is pending.<br>
     * <tt>kCD_DisconnectPending</tt>
     */
    DISCONNECT_PENDING

}
