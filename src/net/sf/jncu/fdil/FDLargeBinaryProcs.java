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
 * A set of large binary procedures; it is a structure of the following format:<br>
 * <tt>struct FD_LargeBinaryProcs<br>
 * {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;DIL_Error (*Create) (void** cookie);<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;DIL_Error (*SetNumPages) (void** cookie, long pageCount);<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;DIL_Error (*ReadPage) (void** cookie, long pageNum, FD_PageBuff* pageBufPtr);<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;DIL_Error (*WritePage) (void** cookie, long pageNum, const FD_PageBuff* pageBufPtr);<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;DIL_Error (*Destroy) (void** cookie);<br>
 * };<br>
 * typedef struct FD_LargeBinaryProcs FD_LargeBinaryProcs;</tt>
 * <p>
 * The cookie argument is intended to allow your various functions to
 * communicate. It is a pointer to a <tt>void *</tt>. Typically, your
 * <tt>Create</tt> function allocates some memory for use by all your storage
 * functions, and sets the void * that cookie points to this memory block. This
 * memory is then usually freed by your <tt>Destroy</tt> function.
 * 
 * @author moshe
 * 
 */
public interface FDLargeBinaryProcs {

	/**
	 * Store data in RAM.<br>
	 * <tt>kFD_MemoryStoreProcs</tt>
	 */
	public static final int MEMORY_STORE_PROCS = 0;
	/**
	 * Store data on disk.<br>
	 * <tt>kFD_DiskStoreProcs</tt>
	 */
	public static final int DISK_STORE_PROCS = 1;
	/**
	 * Discards data.<br>
	 * <tt>kFD_NullStoreProcs</tt>
	 */
	public static final int NULL_STORE_PROCS = 2;

	/**
	 * Don’t compress data.<br>
	 * <tt>kFD_NoCompression</tt>
	 */
	public static final int NO_COMPRESSION = 0;
	/**
	 * Use Lempel-Ziv (LZ) compression. This is the only type of compression you
	 * should use when calling <tt>FD_MakeLargeBinary</tt>. <br>
	 * <tt>kFD_LZCompression</tt>
	 */
	public static final int LZ_COMPRESSION = 1;
	/**
	 * Use Zippy compression. You should never use this value.<br>
	 * <tt>kFD_ZippyCompression</tt>
	 */
	public static final int ZIPPY_COMPRESSION = 2;

	/**
	 * The <tt>Create</tt> function is called when a large binary object is
	 * being created, in response to a call to <tt>FD_MakeLargeBinary</tt> or
	 * <tt>FD_Unflatten</tt>.<br>
	 * <tt>DIL_Error (*Create) (void** cookie);</tt>
	 * 
	 * @param cookie
	 */
	public void create(Object cookie);

	/**
	 * <br>
	 * <tt>DIL_Error (*SetNumPages) (void** cookie, long pageCount);</tt>
	 * 
	 * @param cookie
	 * @param pageCount
	 */
	public void setNumPages(Object cookie, int pageCount);

	/**
	 * Your <tt>ReadPage</tt> function should copy over the required page to the
	 * buffer located at {@code pageBufPtr} ->fData and set the
	 * {@code pageBufPtr} ->fLength field to the number of bytes copied over.
	 * The contents of the page, and its length, should be the same as those
	 * specified in the call to your <tt>WritePage</tt> function when it stored
	 * this page. If no WritePage call had ever been made for the requested
	 * page, <tt>ReadPage</tt> should return
	 * <tt>kFD_LBReadingFromUnwrittenPage</tt>. If any other error occurs while
	 * trying to retrieve the page, it should return a non-zero value not equal
	 * to <tt>kFD_LBReadingFromUnwrittenPage</tt>. Otherwise, it should return
	 * <tt>kDIL_NoError</tt>. <tt>ReadPage</tt> is never called with a page
	 * number larger than, or equal to, that specified in a previous call to
	 * <tt>SetNumPages</tt>.<br>
	 * <tt>DIL_Error (*ReadPage) (void** cookie, long pageNum, FD_PageBuff* pageBufPtr);</tt>
	 * 
	 * @param cookie
	 * @param pageCount
	 * @param pageBufPtr
	 */
	public void readPage(Object cookie, int pageCount, Object pageBufPtr);

	/**
	 * Your <tt>WritePage</tt> function is passed the page number to write and
	 * pointer to an <tt>FD_PageBuff</tt> object describing the buffered data to
	 * store. It should store {@code pageBufPtr} ->fLength bytes starting at
	 * {@code pageBufPtr} ->fData. If an error occurs while saving the data,
	 * <tt>WritePage</tt> should return a non-zero value. Otherwise, it should
	 * return <tt>kDIL_NoError</tt>.
	 * <tt>WritePage is never called with a page number larger than, or equal to, that specified in a
	 * previous call to <tt>SetNumPages</tt>.<br>
	 * <tt>DIL_Error (*WritePage) (void** cookie, long pageNum, const FD_PageBuff* pageBufPtr);</tt>
	 * 
	 * @param cookie
	 * @param pageCount
	 * @param pageBufPtr
	 */
	public void writePage(Object cookie, int pageCount, final Object pageBufPtr);

	/**
	 * The <tt>Destroy</tt> function is called when the object is no longer
	 * needed. <br>
	 * <tt>DIL_Error (*Destroy) (void** cookie);</tt>
	 * 
	 * @param cookie
	 */
	public void destroy(Object cookie);

}
