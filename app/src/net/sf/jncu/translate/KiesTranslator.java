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
package net.sf.jncu.translate;

import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;

import java.io.InputStream;
import java.util.Collection;

/**
 * Samsung Kies contacts translator.
 *
 * @author Moshe
 */
public class KiesTranslator extends NamesTranslator {

    private static final String[] EXT = {"spb"};

    /**
     * Get the file filter extensions.
     *
     * @return the array of extensions.
     */
    public static String[] getFilterExtensions() {
        return EXT;
    }

    /**
     * Constructs a new translator.
     */
    public KiesTranslator() {
        super();
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getApplicationName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Soup> translateToNewton(InputStream in) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream translateFromNewton(SoupEntry entry) {
        // TODO Auto-generated method stub
        return null;
    }

}
