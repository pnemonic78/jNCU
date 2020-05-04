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
 * This command sets the current soup. Most of the other commands pertain to
 * this soup so this command must precede any command that uses the current
 * soup. If the soup doesn't exist a <tt>kDSoupNotFound</tt> error is returned
 * but the connection is left alive so the desktop can create the soup if
 * necessary. Soup names must be < 25 characters.
 *
 * <pre>
 * 'ssou'
 * length
 * soup name	// C string
 * </pre>
 *
 * @author moshew
 */
public class DSetCurrentSoup extends BaseDockCommandToNewton {

    /**
     * <tt>kDSetCurrentSoup</tt>
     */
    public static final String COMMAND = "ssou";

    /**
     * Soup not found.<br>
     * <tt>kDSoupNotFound</tt>
     */
    public static final int ERROR_NOT_FOUND = -28015;

    private String name;

    /**
     * Creates a new command.
     */
    public DSetCurrentSoup() {
        super(COMMAND);
    }

    /**
     * Creates a new command.
     *
     * @param cmd the command.
     */
    protected DSetCurrentSoup(String cmd) {
        super(cmd);
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

    @Override
    public String toString() {
        return super.toString() + ":" + getName();
    }
}
