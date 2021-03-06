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

import net.sf.jncu.fdil.contrib.NSOFSoupName;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This command is used by restore to remove all entries from a soup before the
 * soup data is restored.
 *
 * <pre>
 * 'dsou'
 * length
 * soup name	// C string
 * </pre>
 *
 * @author moshew
 */
public class DEmptySoup extends BaseDockCommandToNewton {

    /**
     * <tt>kDEmptySoup</tt>
     */
    public static final String COMMAND = "esou";

    private String name;

    /**
     * Creates a new command.
     */
    public DEmptySoup() {
        super(COMMAND);
    }

    /**
     * Get the soup name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the soup name.
     *
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the soup name.
     *
     * @param soup the soup.
     */
    public void setSoup(Soup soup) {
        setName(soup.getName());
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        NSOFSoupName.flatten(getName(), data);
    }

}
