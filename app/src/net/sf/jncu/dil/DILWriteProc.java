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
package net.sf.jncu.dil;

import java.io.IOException;
import java.io.InputStream;

/**
 * <tt>DIL_WriteProc</tt>
 *
 * @author moshe
 */
public interface DILWriteProc {

    /**
     * A function called to write data.<br>
     * <tt>typedef DIL_Error (*DIL_WriteProc) (const void *buf, long amt, void *userData)</tt>
     *
     * @param buf      A pointer to the data to be written.
     * @param amt      How many bytes to write. Note that the PDIL calls your
     *                 <tt>DIL_WriteProc</tt> with a value of {@code -1} for this
     *                 parameter, to signal that no more data is to be sent, and you
     *                 should flush the buffer.
     * @param userData A pointer to data you provided to the function that calls your
     *                 writing procedure. For instance, it can contain a
     *                 <tt>FILE*</tt> if the <tt>DIL_WriteProc</tt> writes data to
     *                 disk, or a <tt>CD_Handle</tt> if the <tt>DIL_WriteProc</tt>
     *                 sends data to a Newton device, or <tt>NULL</tt> if no extra
     *                 data is needed.
     * @throws IOException if an I/O error occurs.
     */
    public void write(InputStream buf, int amt, Object userData) throws IOException;

}
