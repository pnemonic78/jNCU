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
package net.sf.jncu.cdil.mnp;

import java.io.Closeable;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * MNP serial port writer.
 *
 * @author moshew
 */
public class MNPSerialPortWriter extends Thread implements Closeable {

    /**
     * System property name for a reader filter class.
     */
    public static final String PROPERTY_FILTER = "jncu.cdil.mnp.MNPSerialPortWriterFilter";

    protected final SerialPort port;
    private OutputStream out;

    /**
     * Creates a new serial port writer.
     *
     * @param port the serial port.
     */
    @SuppressWarnings("unchecked")
    public MNPSerialPortWriter(SerialPort port) {
        super();
        setName("MNPSerialPortWriter-" + getId());
        this.port = port;

        String filterClassName = System.getProperty(PROPERTY_FILTER);
        if (filterClassName != null) {
            Class<? extends FilterOutputStream> clazz;
            try {
                clazz = (Class<? extends FilterOutputStream>) Class.forName(filterClassName);
                addFilter(clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws IOException {
        // TODO flush the filters.

        getOutputStream().close();
    }

    /**
     * Write a byte to the port.
     *
     * @param b the data.
     * @throws IOException if an I/O error occurs.
     */
    public void write(int b) throws IOException {
        getOutputStream().write(b & 0xFF);
    }

    /**
     * Writes all the bytes from the specified byte array to the port.
     *
     * @param b the data.
     * @throws IOException if an I/O error occurs.
     */
    public void write(byte[] b) throws IOException {
        getOutputStream().write(b);
    }

    /**
     * Writes bytes from the specified byte array to the port.
     *
     * @param b      the data.
     * @param offset the array offset.
     * @param length the number of bytes.
     * @throws IOException if an I/O error occurs.
     */
    public void write(byte[] b, int offset, int length) throws IOException {
        getOutputStream().write(b, offset, length);
    }

    /**
     * Get the port output stream.
     *
     * @return the stream.
     */
    public OutputStream getOutputStream() {
        if (out == null)
            out = new SerialPortOutputStream(port);
        return out;
    }

    /**
     * Output stream wrapper for writing to the port.
     *
     * @author moshe
     */
    protected static class SerialPortOutputStream extends OutputStream {

        private final SerialPort port;

        public SerialPortOutputStream(SerialPort port) {
            this.port = port;
        }

        @Override
        public void write(int b) throws IOException {
            try {
                port.writeByte((byte) b);
            } catch (SerialPortException se) {
                throw new IOException(se.getCause());
            }
        }

        @Override
        public void write(byte[] b) throws IOException {
            try {
                port.writeBytes(b);
            } catch (SerialPortException se) {
                throw new IOException(se.getCause());
            }
        }
    }

    /**
     * Add a stream filter. Wraps the output stream.
     *
     * @param filterClass the filter to add.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IllegalArgumentException
     */
    public void addFilter(Class<? extends FilterOutputStream> filterClass) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Constructor<? extends FilterOutputStream> cons;
        FilterOutputStream filter;
        try {
            cons = filterClass.getConstructor(OutputStream.class);
            filter = cons.newInstance(getOutputStream());
            this.out = filter;
        } catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(nsme);
        }
    }
}
