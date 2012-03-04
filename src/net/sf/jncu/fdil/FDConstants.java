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
 * FDIL constants.
 * 
 * @author moshe
 */
public interface FDConstants {

	/**
	 * MacRoman character encoding.<br>
	 * <tt><tt>kFD_MacEncoding</tt>
	 */
	public static final int FD_MAC_ENCODING = 0;
	/**
	 * Windows character encoding.<br>
	 * <tt><tt>kFD_WindowsEncoding</tt>
	 */
	public static final int FD_WINDOWS_ENCODING = 1;

	/**
	 * A special immediate.<br>
	 * <tt>kImmedSpecial</tt>
	 */
	public static final int FD_IMMED_SPECIAL = 0x00;
	/**
	 * A character.<br>
	 * <tt>kImmedCharacter</tt>
	 */
	public static final int FD_IMMED_CHARACTER = 0x01;
	/**
	 * A Boolean.<br>
	 * <tt>kImmedBoolean</tt>
	 */
	public static final int FD_IMMED_BOOLEAN = 0x02;
	/**
	 * A reserved immediate.<br>
	 * <tt>kImmedReserved</tt>
	 */
	public static final int FD_IMMED_RESERVED = 0x03;

	/**
	 * Store data in RAM.<br>
	 * <tt>kFD_MemoryStoreProcs</tt>
	 */
	public static final int FD_MEMORY_STORE_PROCS = 0;
	/**
	 * Store data on disk.<br>
	 * <tt>kFD_DiskStoreProcs</tt>
	 */
	public static final int FD_DISK_STORE_PROCS = 1;
	/**
	 * Discards data.<br>
	 * <tt>kFD_NullStoreProcs</tt>
	 */
	public static final int FD_NULL_STORE_PROCS = 2;

	/**
	 * Don’t compress data.<br>
	 * <tt>kFD_NoCompression</tt>
	 */
	public static final int FD_NO_COMPRESSION = 0;
	/**
	 * Use Lempel-Ziv (LZ) compression. This is the only type of compression you
	 * should use when calling <tt>FD_MakeLargeBinary</tt>. <br>
	 * <tt>kFD_LZCompression</tt>
	 */
	public static final int FD_LZ_COMPRESSION = 1;
	/**
	 * Use Zippy compression. You should never use this value.<br>
	 * <tt>kFD_ZippyCompression</tt>
	 */
	public static final int FD_ZIPPY_COMPRESSION = 2;

}
