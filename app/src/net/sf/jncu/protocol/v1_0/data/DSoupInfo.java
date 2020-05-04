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

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.protocol.DockCommandBidi;
import net.sf.jncu.protocol.v2_0.DockCommandFromNewtonScript;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command is used to send a soup info frame. When received the info for
 * the current soup is set to the specified frame.
 *
 * <pre>
 * 'sinf'
 * length
 * soup info frame
 * </pre>
 */
public class DSoupInfo extends DockCommandFromNewtonScript<NSOFFrame> implements DockCommandBidi {

    /**
     * <tt>kDSoupInfo</tt><br>
     * <tt>kDSetSoupInfo</tt>
     */
    public static final String COMMAND = "sinf";

    private Soup soup;
    private DockCommandToNewtonScript<NSOFFrame> to;

    /**
     * Creates a new command.
     */
    public DSoupInfo() {
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
    protected void setSoup(Soup soup) {
        this.soup = soup;
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        super.decodeCommandData(data);
        Soup soup = null;
        NSOFFrame frame = getResult();
        if (frame != null) {
            soup = getSoup();
            if (soup == null)
                soup = new Soup("");
            soup.fromFrame(frame);
        }
        setSoup(soup);
    }

    @Override
    public InputStream getCommandPayload() throws IOException {
        if (to == null) {
            to = new DockCommandToNewtonScript<NSOFFrame>(COMMAND) {
            };
        }
        return to.getCommandPayload();
    }

    @Override
    public int getCommandPayloadLength() throws IOException {
        return to.getCommandPayloadLength();
    }

}
