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
package net.sf.jncu.io;

import jssc.SerialPortException;

/**
 * The port requested is currently in use.
 *
 * @author moshe
 */
public class PortInUseException extends SerialPortException {

    /**
     * Create a new serial port exception.
     *
     * @param name the port name.
     */
    public PortInUseException(String name) {
        super(name, null, TYPE_PORT_BUSY);
    }
}
