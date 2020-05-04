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

import net.sf.jncu.io.ReplaceInputStream;
import net.sf.jncu.io.ReplaceOutputStream;
import net.sf.junit.SFTestCase;

import org.junit.Test;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class NCULinkRequestFilterTest extends SFTestCase {

    public NCULinkRequestFilterTest() {
        super("NCULinkRequestFilter");
    }

    @Test
    public void testIn() throws Exception {
        PipedInputStream in;
        PipedOutputStream out;
        ReplaceInputStream filter;
        int count;
        int availRaw;
        int availFiltered;
        byte[] find = NCULinkRequestFilter.LR_FROM_NCU_FIND;
        byte[] replace = NCULinkRequestFilter.LR_FROM_PC_REPLACE;
        int i;

        in = new PipedInputStream();
        out = new PipedOutputStream(in);
        filter = new ReplaceInputStream(in, find, replace);
        count = 0;
        availRaw = 0;
        availFiltered = 0;
        assertEquals(availRaw, in.available());
        assertEquals(availFiltered, filter.available());

        for (i = 0; i < 5; i++) {
            out.write(100 + i);
            count++;
            availRaw++;
            availFiltered++;
            assertEquals(availRaw, in.available());
            assertEquals(availFiltered, filter.available());
            availRaw = 0;
        }

        for (i = 0; i < 5; i++) {
            out.write(find[i]);
            count++;
            availRaw++;
            availFiltered += 0;
            assertEquals(availRaw, in.available());
            assertEquals(availFiltered, filter.available());
            availRaw = 0;
        }
        out.write(0xFF);
        count++;
        availRaw++;
        availFiltered = count;
        assertEquals(availRaw, in.available());
        assertEquals(availFiltered, filter.available());
        availRaw = 0;

        for (i = 0; i < find.length - 1; i++) {
            out.write(find[i]);
            count++;
            availRaw++;
            availFiltered += 0;
            assertEquals(availRaw, in.available());
            assertEquals(availFiltered, filter.available());
            availRaw = 0;
        }
        out.write(find[i]);
        count++;
        availRaw++;
        assertEquals(availRaw, in.available());
        availFiltered = count - find.length + replace.length;
        assertEquals(availFiltered, filter.available());

        out.close();
        filter.close();
    }

    @Test
    public void testOut() throws Exception {
        PipedInputStream in;
        PipedOutputStream out;
        ReplaceOutputStream filter;
        int count;
        int availFiltered;
        byte[] find = NCULinkRequestFilter.LR_FROM_NCU_FIND;
        byte[] replace = NCULinkRequestFilter.LR_FROM_PC_REPLACE;
        int i;

        in = new PipedInputStream();
        out = new PipedOutputStream(in);
        filter = new NCULinkRequestFilter(out);
        count = 0;
        availFiltered = 0;
        assertEquals(availFiltered, in.available());

        for (i = 0; i < 5; i++) {
            filter.write(100 + i);
            count++;
            filter.flush();
            availFiltered++;
            assertEquals(availFiltered, in.available());
        }

        for (i = 0; i < 5; i++) {
            filter.write(find[i]);
            count++;
            filter.flush();
            availFiltered += 0;
            assertEquals(availFiltered, in.available());
        }
        filter.write(0xFF);
        count++;
        filter.flush();
        availFiltered = count;
        assertEquals(availFiltered, in.available());

        for (i = 0; i < find.length - 1; i++) {
            filter.write(find[i]);
            count++;
            filter.flush();
            availFiltered += 0;
            assertEquals(availFiltered, in.available());
        }
        filter.write(find[i]);
        count++;
        filter.flush();
        availFiltered = count - find.length + replace.length;
        assertEquals(availFiltered, in.available());

        out.close();
        filter.close();
    }
}
