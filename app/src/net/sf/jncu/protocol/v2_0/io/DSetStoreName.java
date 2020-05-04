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

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This command requests that the name of the current store be set to the
 * specified name.
 *
 * <pre>
 * 'ssna'
 * length
 * name ref
 * </pre>
 *
 * @author moshew
 */
public class DSetStoreName extends BaseDockCommandToNewton {

    /**
     * <tt>kDSetStoreName</tt>
     */
    public static final String COMMAND = "ssna";

    private String name;

    /**
     * Creates a new command.
     */
    public DSetStoreName() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        NSOFString name = new NSOFString(getName());
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(name, data);
    }

    /**
     * Get the store name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the store name.
     *
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }

}
