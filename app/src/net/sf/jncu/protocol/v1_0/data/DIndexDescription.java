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
package net.sf.jncu.protocol.v1_0.data;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.newton.os.SoupIndex;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This command specifies the indexes that should be created for the current
 * soup.
 *
 * <pre>
 * 'indx'
 * length
 * indexes
 * </pre>
 */
public class DIndexDescription extends BaseDockCommandFromNewton {

    /**
     * <tt>kDIndexDescription</tt>
     */
    public static final String COMMAND = "indx";

    private final List<SoupIndex> indexes = new ArrayList<SoupIndex>();

    /**
     * Creates a new command.
     */
    public DIndexDescription() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setIndexes(null);

        NSOFDecoder decoder = new NSOFDecoder();
        NSOFObject o = decoder.inflate(data);

        if (!NSOFImmediate.isNil(o)) {
            NSOFArray arr = (NSOFArray) o;
            int size = arr.length();
            SoupIndex index;
            NSOFFrame frame;

            for (int i = 0; i < size; i++) {
                frame = (NSOFFrame) arr.get(i);
                index = new SoupIndex();
                index.fromFrame(frame);
                indexes.add(index);
            }
        }
    }

    /**
     * Get the soup indexes.
     *
     * @return the array of indexes.
     */
    public List<SoupIndex> getIndexes() {
        return indexes;
    }

    /**
     * Set the soup indexes.
     *
     * @param indexes the array of indexes.
     */
    protected void setIndexes(List<SoupIndex> indexes) {
        this.indexes.clear();
        if (indexes != null)
            this.indexes.addAll(indexes);
    }
}
