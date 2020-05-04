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

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This command is sent in response to a <tt>kDGetSoupNames</tt> command. It
 * returns the names and signatures of all the soups in the current store.
 *
 * <pre>
 * 'soup'
 * length
 * array of string names
 * array of corresponding soup signatures
 * </pre>
 */
public class DSoupNames extends BaseDockCommandFromNewton {

    /**
     * <tt>kDSoupNames</tt>
     */
    public static final String COMMAND = "soup";

    private final List<Soup> soups = new ArrayList<Soup>();

    /**
     * Creates a new command.
     */
    public DSoupNames() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setSoups(null);

        NSOFDecoder decoder = new NSOFDecoder();
        NSOFArray arr = (NSOFArray) decoder.inflate(data);
        NSOFObject[] entries = arr.getValue();
        NSOFObject entry;

        final int length = entries.length;
        List<Soup> soups = new ArrayList<Soup>(length);
        Soup soup;

        for (int i = 0; i < length; i++) {
            entry = entries[i];
            soup = new Soup(((NSOFString) entry).getValue());
            soups.add(soup);
        }

        decoder = new NSOFDecoder();
        arr = (NSOFArray) decoder.inflate(data);
        entries = arr.getValue();
        for (int i = 0; i < length; i++) {
            entry = entries[i];
            soup = soups.get(i);
            soup.setSignature(((NSOFImmediate) entry).getValue());
        }

        setSoups(soups);
    }

    /**
     * Get the soups.
     *
     * @return the soups.
     */
    public List<Soup> getSoups() {
        return soups;
    }

    /**
     * Set the soup names.
     *
     * @param names the names.
     */
    protected void setSoups(List<Soup> soups) {
        this.soups.clear();
        if (soups != null)
            this.soups.addAll(soups);
    }
}
