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
package net.sf.jncu.protocol.v2_0.io.win;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

/**
 * This command returns an array of filters to the Newton. It's sent in response
 * to a <tt>kDGetFilters</tt> command. The filter should be an array of strings
 * which are displayed in the filter pop-up. If the filter array is {@code null}
 * no pop-up is displayed. Windows only.
 *
 * <pre>
 * 'filt'
 * length
 * filter array
 * </pre>
 *
 * @author moshew
 */
public class DFilters extends BaseDockCommandToNewton {

    /**
     * <tt>kDFilters</tt>
     */
    public static final String COMMAND = "filt";

    private final List<FileFilter> filters = new ArrayList<FileFilter>();

    /**
     * Creates a new command.
     */
    public DFilters() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        if (filters.isEmpty())
            return;
        NSOFString[] entries = new NSOFString[filters.size()];
        int i = 0;
        for (FileFilter filter : filters) {
            entries[i++] = new NSOFString(filter.getDescription());
        }
        NSOFArray arr = new NSOFPlainArray(entries);
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(arr, data);
    }

    /**
     * Get the filters.
     *
     * @return the list of filters.
     */
    public List<FileFilter> getFilters() {
        return filters;
    }

    /**
     * Set the filters.
     *
     * @return the list of filters.
     */
    public void setFilters(Collection<FileFilter> filters) {
        this.filters.clear();
        this.filters.addAll(filters);
    }

    /**
     * Add a filter.
     *
     * @param extension the filter extension.
     */
    public void addExtensionFilter(String extension) {
        addFilter(new FileNameExtensionFilter("*." + extension, extension));
    }

    /**
     * Add a filter.
     *
     * @param filter the filter.
     */
    public void addFilter(FileFilter filter) {
        filters.add(filter);
    }

    /**
     * Add filters.
     *
     * @param filters the array of filters.
     */
    public void addFilters(FileFilter[] filters) {
        if (filters != null)
            for (FileFilter filter : filters)
                addFilter(filter);
    }

    /**
     * Add filters.
     *
     * @param filters the list of filters.
     */
    public void addFilters(Collection<FileFilter> filters) {
        if (filters != null)
            this.filters.addAll(filters);
    }
}
