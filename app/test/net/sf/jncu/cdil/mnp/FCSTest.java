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

import net.sf.junit.SFTestCase;
import net.sf.util.zip.CRC16;

import org.junit.Test;

public class FCSTest extends SFTestCase {

    /**
     * Test the FCS of Newton's "attempt to connect."
     */
    @Test
    public void testConnect() {
        byte[] frame = {0x16, 0x10, 0x02, 0x26, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01, 0x08, 0x04, 0x02, 0x40, 0x00,
                0x08, 0x01, 0x03, 0x09, 0x01, 0x01, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, (byte) 0xC5, 0x06, 0x01, 0x04, 0x00, 0x00, (byte) 0xE1, 0x00, 0x10, 0x03,
                (byte) 0xB9, (byte) 0xBF};
        int fcsWord = 0xBFB9;
        int dataLength = 41;
        int fcsLength = 2;
        assertEquals(MNPPacketLayer.PACKET_HEAD.length + dataLength + MNPPacketLayer.PACKET_TAIL.length + fcsLength, frame.length);

        CRC16 fcs = new CRC16();
        fcs.update(frame, MNPPacketLayer.PACKET_HEAD.length, dataLength);
        fcs.update(MNPPacketLayer.PACKET_TAIL[1]);
        assertEquals(fcsWord, fcs.getValue());
    }
}
