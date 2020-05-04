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
package net.sf.jncu.protocol.v2_0.data;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * This command specifies which translator the desktop should use to import the
 * file. The translator index is the index into the translator list sent by the
 * desktop in the <tt>kDTranslatorList</tt> command. The desktop should
 * acknowledge this command with an indication that the import is proceeding.
 *
 * <pre>
 * 'tran'
 * length
 * translator index
 * </pre>
 *
 * @author moshew
 */
public class DSetTranslator extends BaseDockCommandFromNewton {

    /**
     * <tt>kDSetTranslator</tt>
     */
    public static final String COMMAND = "tran";

    private int translatorIndex;

    public DSetTranslator() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setTranslatorIndex(ntohl(data));
    }

    /**
     * Get the translator index.
     *
     * @return the index.
     */
    public int getTranslatorIndex() {
        return translatorIndex;
    }

    /**
     * Set the translator index.
     *
     * @param translatorIndex the index.
     */
    public void setTranslatorIndex(int translatorIndex) {
        this.translatorIndex = translatorIndex;
    }

}
