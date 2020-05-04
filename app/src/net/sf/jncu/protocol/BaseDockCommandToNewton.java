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
package net.sf.jncu.protocol;

import net.sf.jncu.fdil.NSOFString;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.Vector;

/**
 * Docking command from desktop to Newton.
 *
 * @author moshew
 */
public abstract class BaseDockCommandToNewton extends BaseDockCommand implements DockCommandToNewton {

    private ByteArrayInputStream commandPayloadHeader;
    private ByteArrayInputStream commandPayloadFooter;

    /**
     * Creates a new docking command from Newton.
     *
     * @param cmd the command.
     */
    protected BaseDockCommandToNewton(String cmd) {
        super(cmd);
    }

    /**
     * Creates a new docking command from Newton.
     *
     * @param cmd the command.
     */
    protected BaseDockCommandToNewton(byte[] cmd) {
        super(cmd);
    }

    @Override
    public InputStream getCommandPayload() throws IOException {
        // Get this first so that we can calculate our data length.
        InputStream data = getCommandData();

        Vector<InputStream> v = new Vector<InputStream>();
        v.add(getCommandPayloadHeader());
        v.add(data);
        v.add(getCommandPayloadFooter());
        return new SequenceInputStream(v.elements());
    }

    @Override
    public int getCommandPayloadLength() throws IOException {
        return COMMAND_HEADER_LENGTH + getLength() + getCommandPayloadFooter().available();
    }

    /**
     * Encode the command data to write.
     *
     * @param data the data output.
     * @throws IOException if an I/O error occurs.
     */
    protected void writeCommandData(OutputStream data) throws IOException {
    }

    /**
     * Get the command data to write.<br>
     * <em>The stream should be non-blocking.</em>
     *
     * @return the command data.
     * @throws IOException if an I/O error occurs.
     */
    protected InputStream getCommandData() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeCommandData(out);
        out.flush();
        out.close();
        if (getLength() == 0)
            setLength(out.size());
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * Host-to-network long.<br>
     * Write 4 bytes as an unsigned integer in network byte order (Big Endian).
     *
     * @param out   the output.
     * @param frame the frame data.
     * @throws IOException if an I/O error occurs.
     */
    public static void htonl(int n, OutputStream out) throws IOException {
        out.write((n >> 24) & 0xFF);
        out.write((n >> 16) & 0xFF);
        out.write((n >> 8) & 0xFF);
        out.write((n >> 0) & 0xFF);
    }

    /**
     * Host-to-network long.<br>
     * Write 8 bytes as an unsigned integer in network byte order (Big Endian).
     *
     * @param out   the output.
     * @param frame the frame data.
     * @throws IOException if an I/O error occurs.
     */
    public static void htonl(long n, OutputStream out) throws IOException {
        htonl((int) ((n >> 32) & 0xFFFFFFFFL), out);
        htonl((int) ((n >> 0) & 0xFFFFFFFFL), out);
    }

    /**
     * Write a <tt>C</tt>-style Unicode string (UTF-16, null-terminated).
     *
     * @param s   the string.
     * @param out the output.
     * @throws IOException if an I/O error occurs.
     */
    public static void writeString(String s, OutputStream out) throws IOException {
        if ((s != null) && (s.length() > 0)) {
            byte[] utf16 = s.getBytes(NSOFString.CHARSET_UTF16);
            // The 1st and 2nd bytes are UTF-16 header 0xFE and 0xFF.
            out.write(utf16, 2, utf16.length - 2);
        }
        // Null-terminator.
        out.write(0);
        out.write(0);
    }

    /**
     * Get the command payload header.
     *
     * @return the command payload prefix.
     * @throws IOException if an I/O error occurs.
     */
    private ByteArrayInputStream getCommandPayloadHeader() throws IOException {
        if (commandPayloadHeader == null) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream(COMMAND_HEADER_LENGTH);
            buf.write(COMMAND_PREFIX_BYTES);
            buf.write(commandBytes);
            htonl(getLength(), buf);
            commandPayloadHeader = new ByteArrayInputStream(buf.toByteArray());
        } else {
            commandPayloadHeader.reset();
        }
        return commandPayloadHeader;
    }

    /**
     * Get the command payload footer.
     *
     * @return the command payload footer.
     * @throws IOException if an I/O error occurs.
     */
    private ByteArrayInputStream getCommandPayloadFooter() throws IOException {
        if (commandPayloadFooter == null) {
            int length = getLength();
            ByteArrayOutputStream footer = new ByteArrayOutputStream(3);
            // 4-byte align
            switch (length & 3) {
                case 1:
                    footer.write(0);
                case 2:
                    footer.write(0);
                case 3:
                    footer.write(0);
                    break;
            }
            commandPayloadFooter = new ByteArrayInputStream(footer.toByteArray());
        } else {
            commandPayloadFooter.reset();
        }
        return commandPayloadFooter;
    }
}
