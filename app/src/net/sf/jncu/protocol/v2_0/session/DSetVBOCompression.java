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
package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command controls which VBOs are sent compressed to the desktop. VBO can
 * always be sent compressed, never compressed or only package VBOs sent
 * compressed.
 *
 * <pre>
 * 'cvbo'
 * length
 * what
 * </pre>
 *
 * @author moshew
 * @see #UNCOMPRESSED
 * @see #COMPRESSED_PACKAGES
 * @see #COMPRESSED_VBO
 */
public class DSetVBOCompression extends DockCommandToNewtonLong {

    /**
     * <tt>kDSetVBOCompression</tt>
     */
    public static final String COMMAND = "cvbo";

    /**
     * <tt>eUncompressedVBOs</tt><br>
     * <tt>kUncompressedVBOs</tt><br>
     * VBO sent uncompressed.
     */
    public static final int UNCOMPRESSED = 0;
    /**
     * <tt>eCompressedPackagesOnly</tt><br>
     * <tt>kCompressedPackagesOnly</tt><br>
     * Only package VBOs sent compressed.
     */
    public static final int COMPRESSED_PACKAGES = 1;
    /**
     * <tt>eCompressedVBOs</tt><br>
     * <tt>kCompressedVBOs</tt><br>
     * VBO sent compressed.
     */
    public static final int COMPRESSED_VBO = 2;

    /**
     * Creates a new command.
     */
    public DSetVBOCompression() {
        super(COMMAND);
    }

    /**
     * Get the compression.
     *
     * @return the compression.
     */
    public int getCompression() {
        return getValue();
    }

    /**
     * Set the compression.
     *
     * @param compression the compression.
     */
    public void setCompression(int compression) {
        setValue(compression);
    }
}
