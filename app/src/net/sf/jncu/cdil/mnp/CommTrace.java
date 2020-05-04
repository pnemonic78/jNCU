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
import net.sf.jncu.io.PortInUseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * Trace serial port traffic.
 * <p>
 * The program traces traffic sent and received between two serial ports. At
 * least of the ports would be a null-port (virtual port), for example:<br>
 * <tt>device</tt> <-> <tt>COM1</tt> <-> <tt>CommTrace</tt> <->
 * <tt>COM2-COM3</tt> <-> <tt>application</tt><br>
 * or<br>
 * <tt>device</tt> <-> <tt>COM2-COM3</tt> <-> <tt>CommTrace</tt> <->
 * <tt>COM4-COM5</tt> <-> <tt>application</tt>
 *
 * @author moshew
 */
public class CommTrace {

    /**
     * Incoming bytes.
     */
    public static final char DIRECTION_FROM_NEWTON = '>';
    /**
     * Outgoing bytes.
     */
    public static final char DIRECTION_FROM_PC = '<';
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * Port attached to Newton.
     */
    private MNPSerialPort portN;
    /**
     * Port attached to PC.
     */
    private MNPSerialPort portP;
    private PrintStream logOut = System.out;
    private int logWidth = 0;
    /**
     * Newton to PC tracer.
     */
    private Tracer traceN;
    /**
     * PC to Newton tracer.
     */
    private Tracer traceP;
    private int baud;
    private boolean sout = false;

    /**
     * Creates a new trace.
     */
    @SuppressWarnings("deprecation")
    public CommTrace() {
        super();
        setBaud(SerialPort.BAUDRATE_38400);
        Runtime.runFinalizersOnExit(true);
    }

    /**
     * Main method.
     *
     * @param args the array of arguments.
     */
    public static void main(String[] args) {
        if ((args == null) || (args.length < 2)) {
            System.out.println("args: port1 port2 [baud [file]]");
            System.exit(1);
            return;
        }

        CommTrace tracer = new CommTrace();
        try {
            if (args.length > 2) {
                tracer.setBaud(Integer.parseInt(args[2]));
                if (args.length > 3) {
                    tracer.setOutput(new File(args[3]));
                }
            }
            tracer.trace(args[0], args[1]);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Set the baud rate.
     *
     * @param baud the baud.
     */
    private void setBaud(int baud) {
        this.baud = baud;
    }

    public void trace(String portNameN, String portNameP) throws NoSuchPortException, PortInUseException, SerialPortException, IOException {
        SerialPort portN = new SerialPort(portNameN);
        SerialPort portP = new SerialPort(portNameP);
        trace(portN, portP);
    }

    public void trace(SerialPort portN, SerialPort portP) throws SerialPortException, IOException {
        this.portN = new MNPSerialPort(portN, baud);
        this.portP = new MNPSerialPort(portP, baud);

        this.traceN = new Tracer(this.portN, this.portP, DIRECTION_FROM_NEWTON);
        this.traceP = new Tracer(this.portP, this.portN, DIRECTION_FROM_PC);

        traceN.start();
        traceP.start();
    }

    /**
     * Set the trace output.
     *
     * @param file the output file.
     * @throws FileNotFoundException if file is not found.
     */
    public void setOutput(File file) throws FileNotFoundException {
        setOutput(new FileOutputStream(file));
    }

    /**
     * Set the trace output.
     *
     * @param out the output.
     */
    public void setOutput(OutputStream out) {
        setOutput(new PrintStream(out));
    }

    /**
     * Set the trace output.
     *
     * @param out the output.
     */
    public void setOutput(PrintStream out) {
        this.logOut = out;
    }

    /**
     * Cancel tracing.
     */
    public void cancel() {
        try {
            finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        traceN.cancel();
        traceP.cancel();
        portN.close();
        portP.close();
        super.finalize();
    }

    /**
     * Trace the ports in blocking streams.
     */
    private class Tracer extends Thread {
        private final MNPSerialPort portSource;
        private final MNPSerialPort portSink;
        private final String portSourceName;
        private final char direction;
        private boolean running;

        /**
         * Creates a new tracer.
         *
         * @param portSource the input port.
         * @param portSink   the output port.
         * @param direction  the direction indicator.
         */
        public Tracer(MNPSerialPort portSource, MNPSerialPort portSink, char direction) {
            this.portSource = portSource;
            this.portSink = portSink;
            this.direction = direction;
            this.portSourceName = portSource.getPort().getPortName();
            setName("Tracer-" + portSourceName);
        }

        @Override
        public void run() {
            running = true;

            InputStream in;
            OutputStream out;
            int avail, count;
            byte[] buf = new byte[1024];
            int b;

            try {
                while (running && !isInterrupted()) {
                    in = portSource.getInputStream();
                    out = portSink.getOutputStream();
                    avail = in.available();
                    if (avail == 0)
                        avail = 1;
                    count = in.read(buf, 0, Math.min(avail, buf.length));
                    if (count == -1)
                        break;
                    for (int i = 0; i < count; i++) {
                        b = buf[i] & 0xFF;
                        synchronized (logOut) {
                            logOut.print(direction);
                            logOut.print(HEX[(b >> 4) & 0x0F]);
                            logOut.print(HEX[b & 0x0F]);
                            logWidth++;
                            if (sout && (logOut != System.out)) {
                                System.out.print(direction);
                                System.out.print(HEX[(b >> 4) & 0x0F]);
                                System.out.print(HEX[b & 0x0F]);
                                System.out.print(' ');
                            }
                            if (logWidth >= 32) {
                                logOut.println();
                                if (sout && (logOut != System.out)) {
                                    System.out.println();
                                }
                                logWidth = 0;
                            }
                        }
                        out.write(b);
                    }
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Stop tracing.
         */
        public void cancel() {
            running = false;
            try {
                interrupt();
                join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
}
