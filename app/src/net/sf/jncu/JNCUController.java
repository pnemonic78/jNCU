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
package net.sf.jncu;

import net.sf.jncu.cdil.BadPipeStateException;
import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.newton.os.NewtonInfo;

import java.util.concurrent.TimeoutException;

/**
 * jNCU controller interface.
 *
 * @author Moshe
 */
public interface JNCUController {

    /**
     * Start listening for Newton.
     *
     * @throws ServiceNotSupportedException if the service is not supported.
     * @throws PlatformException            if a platform error occurs.
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws BadPipeStateException        if pipe is in an incorrect state.
     * @throws PipeDisconnectedException    if the pipe is disconnected.
     * @throws TimeoutException             if timeout occurs.
     */
    void start() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException, BadPipeStateException, PipeDisconnectedException, TimeoutException;

    /**
     * Stop listening for Newton.
     */
    void stop();

    /**
     * Shut down the controller and exit the application.
     */
    void exit();

    /**
     * Backup from Newton to desktop.
     */
    void backupToDesktop();

    /**
     * Export from Newton to desktop.
     */
    void exportToDesktop();

    /**
     * Restore from desktop to Newton.
     */
    void restoreToNewton();

    /**
     * Import from desktop to Newton.
     */
    void importToNewton();

    /**
     * Synchronize.
     */
    void sync();

    /**
     * Use keyboard passthrough.
     */
    void keyboard();

    /**
     * Install package on the Newton.
     */
    void installPackage();

    /**
     * Get the device information.
     *
     * @return the Newton information.
     */
    NewtonInfo getDeviceInformation();

    /**
     * Show the Newton information.
     */
    void showDevice();

    /**
     * Show the settings dialog.
     *
     * @throws ServiceNotSupportedException if the service is not supported.
     * @throws PlatformException            if a platform error occurs.
     * @throws CDILNotInitializedException  if CDIL is not initialised.
     * @throws BadPipeStateException        if pipe is in an incorrect state.
     * @throws PipeDisconnectedException    if the pipe is disconnected.
     * @throws TimeoutException             if timeout occurs.
     */
    void showSettings() throws CDILNotInitializedException, PlatformException, ServiceNotSupportedException, BadPipeStateException, PipeDisconnectedException,
            TimeoutException;
}
