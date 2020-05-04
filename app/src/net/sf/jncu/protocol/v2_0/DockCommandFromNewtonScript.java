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
package net.sf.jncu.protocol.v2_0;

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * Command from the Newton with a single NewtonScript object result.
 *
 * @author moshew
 */
public abstract class DockCommandFromNewtonScript<T extends NSOFObject> extends BaseDockCommandFromNewton {

    private T result;

    /**
     * Creates a new command.
     *
     * @param cmd the command.
     */
    public DockCommandFromNewtonScript(String cmd) {
        super(cmd);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        NSOFDecoder decoder = new NSOFDecoder();
        NSOFObject o = decoder.inflate(data);
        T result = null;
        if (!NSOFImmediate.isNil(o))
            result = (T) o;
        setResult(result);
    }

    /**
     * Set the result.
     *
     * @param result the result.
     */
    protected void setResult(T result) {
        this.result = result;
    }

    /**
     * Get the result.
     *
     * @return the result.
     */
    public T getResult() {
        return result;
    }

    @Override
    public String toString() {
        T result = getResult();
        StringBuffer s = new StringBuffer();
        s.append(super.toString());
        s.append(':');
        if (result == null) {
            s.append((String) null);
        } else {
            s.append(result.getObjectClass());
            s.append('=');
            s.append(result.toString());
        }
        return s.toString();
    }
}
