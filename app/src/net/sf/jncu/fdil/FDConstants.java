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
package net.sf.jncu.fdil;

/**
 * FDIL constants.
 *
 * @author moshe
 */
public interface FDConstants {

    /**
     * MacRoman character encoding.<br>
     * <tt><tt>kFD_MacEncoding</tt>
     */
    int FD_MAC_ENCODING = 0;
    /**
     * Windows character encoding.<br>
     * <tt><tt>kFD_WindowsEncoding</tt>
     */
    int FD_WINDOWS_ENCODING = 1;

    /**
     * A special immediate.<br>
     * <tt>kImmedSpecial</tt>
     */
    int FD_IMMED_SPECIAL = 0x00;
    /**
     * A character.<br>
     * <tt>kImmedCharacter</tt>
     */
    int FD_IMMED_CHARACTER = 0x01;
    /**
     * A Boolean.<br>
     * <tt>kImmedBoolean</tt>
     */
    int FD_IMMED_BOOLEAN = 0x02;
    /**
     * A reserved immediate.<br>
     * <tt>kImmedReserved</tt>
     */
    int FD_IMMED_RESERVED = 0x03;

    /**
     * Store data in RAM.<br>
     * <tt>kFD_MemoryStoreProcs</tt>
     */
    int FD_MEMORY_STORE_PROCS = 0;
    /**
     * Store data on disk.<br>
     * <tt>kFD_DiskStoreProcs</tt>
     */
    int FD_DISK_STORE_PROCS = 1;
    /**
     * Discards data.<br>
     * <tt>kFD_NullStoreProcs</tt>
     */
    int FD_NULL_STORE_PROCS = 2;

    /**
     * Don’t compress data.<br>
     * <tt>kFD_NoCompression</tt>
     */
    int FD_NO_COMPRESSION = 0;
    /**
     * Use Lempel-Ziv (LZ) compression. This is the only type of compression you
     * should use when calling <tt>FD_MakeLargeBinary</tt>. <br>
     * <tt>kFD_LZCompression</tt>
     */
    int FD_LZ_COMPRESSION = 1;
    /**
     * Use Zippy compression. You should never use this value.<br>
     * <tt>kFD_ZippyCompression</tt>
     */
    int FD_ZIPPY_COMPRESSION = 2;

}
