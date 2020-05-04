/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * http://sourceforge.net/projects/jncu
 *
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
 */
package net.sf.jncu.cdil.mnp;

import net.sf.jncu.io.NoSuchPortException;
import net.sf.junit.SFTestCase;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import jssc.SerialPort;

/**
 * Use Virtual COM ports for testing. COM2 attaches to NCU. COM3 attaches to
 * this tester.
 *
 * @author moshew
 */
public class NullModem extends SFTestCase {

    @Test
    public void testNModem() throws Exception {
        CommPorts commPorts = new CommPorts();
        Collection<SerialPort> ports = commPorts.getPorts();
        if (ports.size() == 0) {
            throw new NoSuchPortException(null);
        }
        SerialPort sport = null;
        for (SerialPort p : ports) {
            String name = p.getPortName();
            if ("COM1".equals(name) || "/dev/ttyUSB0".equals(name)) {
                sport = p;
                break;
            }
        }
        if (sport == null) {
            throw new NoSuchPortException("COM1");
        }
        MNPSerialPort port = new MNPSerialPort(sport, MNPSerialPort.BAUD_38400);
        Reader reader = new Reader(port);
        reader.start();

        Thread.sleep(10000);
        reader.close();
        port.close();
    }

    /**
     * Read bytes and populates the queue.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected void poll(MNPSerialPort port) throws IOException {
        InputStream in = port.getInputStream();
        int i = 0;
        int b;
        do {
            b = in.read();
            if (b == -1) {
                throw new EOFException();
            }
            if ((i & 15) == 0) {
                System.out.println();
            } else {
                System.out.print(' ');
            }
            System.out.print("0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
            i++;
        } while (true);
    }

    private class Reader extends Thread {
        private boolean running;
        private final InputStream in;

        Reader(MNPSerialPort port) {
            super();
            this.in = port.getInputStream();
        }

        @Override
        public void run() {
            try {
                this.running = true;

                int b;
                do {
                    b = in.read();
                    if (b == -1) {
                        break;
                    }
                    logRead(b);
                } while (running && !isInterrupted());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        public void close() {
            running = false;
            try {
                in.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private int r = 0;
    private int w = 0;

    protected void logRead(int b) {
        if (((r & 15) == 0) || (w > 0)) {
            System.out.println();
            System.out.println('<');
            r = 0;
            w = 0;
        } else {
            System.out.print(',');
        }
        System.out.print("0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
        r++;
    }

    protected void logWrite(int b) {
        if (((w & 15) == 0) || (r > 0)) {
            System.out.println();
            System.out.println('>');
            r = 0;
            w = 0;
        } else {
            System.out.print(',');
        }
        System.out.print("0x" + (b < 0x10 ? "0" : "") + Integer.toHexString(b));
        w++;
    }
}
