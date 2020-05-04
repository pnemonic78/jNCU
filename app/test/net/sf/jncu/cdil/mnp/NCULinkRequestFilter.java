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

import net.sf.jncu.io.ReplaceOutputStream;

import java.io.OutputStream;

/**
 * NCU serial packet filter.
 *
 * @author mwaisberg
 */
public class NCULinkRequestFilter extends ReplaceOutputStream {

    /**
     * Bad LR from NCU to Newton.
     */
    protected static final byte[] LR_FROM_NCU_FIND = {0x16, 0x10, 0x02, 0x1D, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01,
            0x08, 0x04, 0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, 0x10, 0x03, (byte) 0x8A, 0x2E};

    /**
     * Normal LR that should be sent to Newton.
     */
    protected static final byte[] LR_FROM_PC_REPLACE = {0x16, 0x10, 0x02, 0x17, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01,
            0x08, 0x04, 0x02, 0x00, 0x01, 0x08, 0x01, 0x03, 0x10, 0x03, (byte) 0xC3, 0x4B};

    /**
     * Creates a new filter.
     *
     * @param out the output.
     */
    public NCULinkRequestFilter(OutputStream out) {
        super(out, LR_FROM_NCU_FIND, LR_FROM_PC_REPLACE);
    }
}
