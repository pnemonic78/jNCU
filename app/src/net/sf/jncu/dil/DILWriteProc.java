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
    void write(InputStream buf, int amt, Object userData) throws IOException;

}
