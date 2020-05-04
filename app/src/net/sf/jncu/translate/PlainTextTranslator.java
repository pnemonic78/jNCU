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

import java.io.InputStream;
import java.util.Collection;

import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;

/**
 * Plain text translator.
 *
 * @author Moshe
 */
public class PlainTextTranslator extends TextTranslator {

    private static final String[] EXT = {"txt"};

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
    public PlainTextTranslator() {
        super();
    }

    @Override
    public String getName() {
        return "Plain Text";
    }

    @Override
    public String getApplicationName() {
        return "Notes";
    }

    @Override
    public Collection<Soup> translateToNewton(InputStream in) {
        // TODO Auto-generated method stub
        // NSOFBinaryObject bin = new NSOFBinaryObject(null);
        // bin.setObjectClass(CLASS_PAPER);
        // return bin;
        return null;
    }

    @Override
    public InputStream translateFromNewton(SoupEntry entry) {
        // TODO Auto-generated method stub
        return null;
    }
}
