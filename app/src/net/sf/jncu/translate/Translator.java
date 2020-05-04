/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * http://sourceforge.net/projects/jncu
 *
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
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