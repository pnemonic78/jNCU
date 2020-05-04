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

/**
 * <tt>DIL_StatusProc</tt>
 *
 * @author moshe
 */
public interface DILStatusProc {

    /**
     * A function called to retrieve the number of bytes available to be read.<br>
     * <tt>typedef DIL_Error (*DIL_StatusProc) (long *bytesAvailable, void *userData)</tt>
     *
     * @param userData A pointer to data you provided to the function that calls your
     *                 reading procedure. For instance, it can contain a
     *                 <tt>FILE*</tt> if the <tt>DIL_ReadProc</tt> reads data from
     *                 disk, or a <tt>CD_Handle</tt> if the <tt>DIL_ReadProc</tt>
     *                 gets data from a Newton device, or <tt>NULL</tt> if no extra
     *                 data is needed.
     * @return the number of bytes available.
     * @throws IOException if an I/O error occurs.
     */
    public int status(Object userData) throws IOException;

}
