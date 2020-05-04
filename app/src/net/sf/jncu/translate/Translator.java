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
 * Translator.<br>
 * One translator class translates only one file type.
 *
 * @author Moshe
 */
public abstract class Translator {

    /**
     * Constructs a new translator.
     */
    public Translator() {
        super();
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public abstract String getName();

    /**
     * Get the application name.
     *
     * @return the name.
     */
    public abstract String getApplicationName();

    /**
     * Transform the file to Newton format.
     *
     * @param in the input file.
     * @return the soups with entries.
     * @throws TranslationException if a translation error occurs.
     */
    public abstract Collection<Soup> translateToNewton(InputStream in) throws TranslationException;

    /**
     * Transform the Newton object to a file.
     *
     * @param entry the entry.
     * @return the translated file.
     * @throws TranslationException if a translation error occurs.
     */
    public abstract InputStream translateFromNewton(SoupEntry entry) throws TranslationException;
}