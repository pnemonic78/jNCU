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
package net.sf.jncu.protocol.v1_0.app;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command sends a package to the desktop. It's issued repeatedly in
 * response to a <tt>kDBackupPackages</tt> message.
 *
 * <pre>
 * 'apkg'
 * length
 * package id
 * package data
 * </pre>
 */
public class DPackage extends BaseDockCommandFromNewton {

    /**
     * <tt>kDPackage</tt>
     */
    public static final String COMMAND = "apkg";

    private int id;
    private byte[] data;

    /**
     * Creates a new command.
     */
    public DPackage() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setId(ntohl(data));
        byte[] b = new byte[getLength() - 4];
        readAll(data, b);
        setData(b);
    }

    /**
     * Get the package id.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the package id.
     *
     * @param id the id.
     */
    protected void setId(int id) {
        this.id = id;
    }

    /**
     * Get the data.
     *
     * @return the data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Set the data.
     *
     * @param data the data.
     */
    protected void setData(byte[] data) {
        this.data = data;
    }

}
