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
package net.sf.jncu.fdil;

/**
 * Compression types for <tt>FD_MakeLargeBinary</tt>.<br>
 * <tt>FD_CompressionType</tt>
 *
 * @author Moshe
 */
public enum FDCompressionType {

    /**
     * No compression.<br>
     * <tt>kFD_NoCompression</tt>
     */
    NO_COMPRESSION,
    /**
     * Lempel-Ziv compression.<br>
     * <tt>kFD_LZCompression</tt>
     */
    LZ_COMPRESSION,
    /**
     * Zippy compression.<br>
     * <tt>kFD_ZippyCompression</tt>
     */
    ZIPPY_COMPRESSION

}
