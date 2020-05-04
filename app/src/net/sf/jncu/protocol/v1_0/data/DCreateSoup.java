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

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.contrib.NSOFSoupName;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Create a soup.
 *
 * <pre>
 * 'csop'
 * length
 * soup name
 * index description
 * </pre>
 *
 * @author moshew
 */
public class DCreateSoup extends BaseDockCommandToNewton {

    /**
     * <tt>kDCreateSoup</tt>
     */
    public static final String COMMAND = "csop";

    private Soup soup;

    /**
     * Creates a new command.
     */
    public DCreateSoup() {
        super(COMMAND);

    }

    /**
     * Get the soup.
     *
     * @return the soup.
     */
    public Soup getSoup() {
        return soup;
    }

    /**
     * Set the soup.
     *
     * @param soup the soup.
     */
    public void setSoup(Soup soup) {
        this.soup = soup;
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        NSOFSoupName.flatten(getSoup().getName(), data);
        NSOFEncoder encoder = new NSOFEncoder();

        encoder.flatten(getSoup().getDefinition().get(Soup.SLOT_INDEXES), data);
    }

}
