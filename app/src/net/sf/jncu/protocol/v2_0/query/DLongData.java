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

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * Newton returns a long value. The interpretation of the data depends on the
 * command which prompted the return of the long value.
 *
 * <pre>
 * 'ldta'
 * length
 * data
 * </pre>
 *
 * @author moshew
 */
public class DLongData extends BaseDockCommandFromNewton {

    /**
     * <tt>kDLongData</tt>
     */
    public static final String COMMAND = "ldta";

    private int data;

    /**
     * Creates a new command.
     */
    public DLongData() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setData(ntohl(data));
    }

    /**
     * Get the data.
     *
     * @return the data.
     */
    public int getData() {
        return data;
    }

    /**
     * Set the data.
     *
     * @param data the data.
     */
    public void setData(int data) {
        this.data = data;
    }

}
