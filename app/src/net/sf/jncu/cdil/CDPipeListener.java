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
 * NCU Connection CDIL pipe listener.
 *
 * @author Moshe
 */
public interface CDPipeListener<P extends CDPacket, L extends CDPacketLayer<P>> {

    /**
     * Notification that the CDIL pipe is disconnected.
     *
     * @param pipe the CDIL pipe.
     */
    void pipeDisconnected(CDPipe<P, L> pipe);

    /**
     * Notification that the CDIL pipe failed to disconnect.
     *
     * @param pipe the CDIL pipe.
     * @param e    the error.
     */
    void pipeDisconnectFailed(CDPipe<P, L> pipe, Exception e);

    /**
     * Notification that the CDIL pipe is listening for a connection.
     *
     * @param pipe the CDIL pipe.
     */
    void pipeConnectionListening(CDPipe<P, L> pipe);

    /**
     * Notification that the CDIL pipe failed to listen for a connection.
     *
     * @param pipe the CDIL pipe.
     * @param e    the error.
     */
    void pipeConnectionListenFailed(CDPipe<P, L> pipe, Exception e);

    /**
     * Notification that the CDIL pipe has a connection pending.
     *
     * @param pipe the CDIL pipe.
     */
    void pipeConnectionPending(CDPipe<P, L> pipe);

    /**
     * Notification that the CDIL pipe failed to pend a connection.
     *
     * @param pipe the CDIL pipe.
     * @param e    the error.
     */
    void pipeConnectionPendingFailed(CDPipe<P, L> pipe, Exception e);

    /**
     * Notification that the CDIL pipe is connected.
     *
     * @param pipe the CDIL pipe.
     */
    void pipeConnected(CDPipe<P, L> pipe);

    /**
     * Notification that the CDIL pipe failed to connect.
     *
     * @param pipe the CDIL pipe.
     * @param e    the error.
     */
    void pipeConnectionFailed(CDPipe<P, L> pipe, Exception e);

}
