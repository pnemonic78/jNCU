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
import java.io.OutputStream;

/**
 * <tt>DIL_ReadProc</tt>
 *
 * @author moshe
 */
public interface DILReadProc {

    /**
     * A function called to read data.<br>
     * <tt>typedef DIL_Error (*DIL_ReadProc) (void *buf, long amt, void* userData)</tt>
     *
     * @param buf      A pointer to the buffer for data that you have read.
     * @param amt      How many bytes to read.
     * @param userData A pointer to data you provided to the function that calls your
     *                 reading procedure. For instance, it can contain a
     *                 <tt>FILE*</tt> if the <tt>DIL_ReadProc</tt> reads data from
     *                 disk, or a <tt>CD_Handle</tt> if the <tt>DIL_ReadProc</tt>
     *                 gets data from a Newton device, or <tt>NULL</tt> if no extra
     *                 data is needed.
     * @throws IOException if an I/O error occurs.
     */
    void read(OutputStream buf, int amt, Object userData) throws IOException;

}
