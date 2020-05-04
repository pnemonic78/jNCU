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
package net.sf.jncu.protocol.v2_0.query;

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Applies the specified function to each of the cursor's entries in turn and
 * returns an array of the results. A <tt>kDRefResult</tt> is returned. See
 * MapCursor in NPG.
 *
 * <pre>
 * 'cmap'
 * length
 * cursor id
 * function
 * </pre>
 *
 * @author moshew
 */
public class DCursorMap extends DCursor {

    /**
     * <tt>kDCursorMap</tt>
     */
    public static final String COMMAND = "cmap";

    private NSOFObject function;

    /**
     * Creates a new command.
     */
    public DCursorMap() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        super.writeCommandData(data);
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(getFunction(), data);
    }

    /**
     * Get the function.
     *
     * @return the function.
     */
    public NSOFObject getFunction() {
        return function;
    }

    /**
     * Set the function.
     *
     * @param function the function.
     */
    public void setFunction(NSOFObject function) {
        this.function = function;
    }

}
