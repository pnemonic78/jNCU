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

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Command to the Newton with a single NewtonScript object request.
 *
 * @author moshew
 */
public abstract class DockCommandToNewtonScript<T extends NSOFObject> extends BaseDockCommandToNewton {

    private T object;

    /**
     * Creates a new command.
     *
     * @param cmd the command.
     */
    public DockCommandToNewtonScript(String cmd) {
        super(cmd);
    }

    /**
     * Set the object.
     *
     * @param object the object.
     */
    public void setObject(T object) {
        this.object = object;
    }

    /**
     * Get the object.
     *
     * @return the object.
     */
    public T getObject() {
        return object;
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(getObject(), data);
    }
}
