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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.TreeSet;

/**
 * This command is sent to a desktop that the Newton wishes to browse files on.
 * File types can be 'import', 'packages', 'syncFiles' or an array of strings to
 * use for filtering.
 *
 * <pre>
 * 'rtbr'
 * length
 * file types
 * </pre>
 *
 * @author moshew
 */
public class DRequestToBrowse extends BaseDockCommandFromNewton {

    /**
     * <tt>kDRequestToBrowse</tt>
     */
    public static final String COMMAND = "rtbr";

    /**
     * List of files to import.
     */
    public static final NSOFSymbol IMPORT = new NSOFSymbol("Import");
    /**
     * List of packages to install.
     */
    public static final NSOFSymbol PACKAGES = new NSOFSymbol("packages");
    /**
     * List of files to synchronise.
     */
    public static final NSOFSymbol SYNC_FILES = new NSOFSymbol("syncFiles");

    private final Collection<NSOFString> types = new TreeSet<NSOFString>();

    /**
     * Constructs a new command.
     */
    public DRequestToBrowse() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        types.clear();
        NSOFDecoder decoder = new NSOFDecoder();

        int length = getLength();
        byte[] buf = new byte[length];
        readAll(data, buf);
        ByteArrayInputStream in = new ByteArrayInputStream(buf);
        int start, end;
        NSOFObject next;

        while (length > 0) {
            start = in.available();
            next = decoder.inflate(in);
            end = in.available();
            length -= (start - end);

            if (next instanceof NSOFString) {
                NSOFString filter = (NSOFString) next;
                types.add(filter);
            } else {
                throw new ClassCastException("invalid filter: " + next);
            }
        }
    }

    /**
     * Get the file types for filtering.
     *
     * @return the list of types.
     */
    public Collection<NSOFString> getTypes() {
        return types;
    }

}
