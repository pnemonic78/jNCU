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

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This command returns an array of translators that can be used with the
 * specified file. The list can include DataViz translators and tab templates.
 * The array should be in the order that the translators should be displayed in
 * the list.
 *
 * <pre>
 * 'trnl'
 * length
 * array of strings
 * </pre>
 *
 * @author moshew
 */
public class DTranslatorList extends BaseDockCommandToNewton {

    /**
     * <tt>kDTranslatorList</tt>
     */
    public static final String COMMAND = "trnl";

    private final List<String> translators = new ArrayList<String>();

    /**
     * Constructs a new command.
     */
    public DTranslatorList() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        NSOFString[] items = new NSOFString[translators.size()];
        int i = 0;
        for (String translator : translators) {
            items[i++] = new NSOFString(translator);
        }
        NSOFPlainArray arr = new NSOFPlainArray(items);
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(arr, data);
    }

    /**
     * Get the translators.
     *
     * @return the list of translators.
     */
    public List<String> getTranslators() {
        return translators;
    }

    /**
     * Set the translators.
     *
     * @param translators the list of translators.
     */
    public void setTranslators(Collection<String> translators) {
        this.translators.clear();
        this.translators.addAll(translators);
    }

    /**
     * Add a translator.
     *
     * @param translator the translator.
     */
    public void addTranslator(String translator) {
        translators.add(translator);
    }
}
