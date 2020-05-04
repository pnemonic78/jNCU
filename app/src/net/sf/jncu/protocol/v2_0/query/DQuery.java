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
package net.sf.jncu.protocol.v2_0.query;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.protocol.v2_0.DockCommandToNewtonScript;

/**
 * The parameter frame must contain a <tt>queryspec</tt> slot and may contain a
 * <tt>soupname</tt> slot. Performs the specified query on the current store.
 * The <tt>queryspec</tt> is a full <tt>queryspec</tt> including valid test,
 * etc. functions. Soup name is a string that's used to find a soup in the
 * current store to query. If the soup name is an empty string or a
 * <tt>NILREF</tt> the query is done on the current soup. A <tt>kDLongData</tt>
 * is returned with a cursor ID that should be used with the rest of the remote
 * query commands.
 *
 * <pre>
 * 'qury'
 * length
 * parameter frame
 * </pre>
 *
 * @author moshew
 */
public class DQuery extends DockCommandToNewtonScript<NSOFFrame> {

    /**
     * <tt>kDQuery</tt>
     */
    public static final String COMMAND = "qury";

    public static final NSOFSymbol SLOT_QUERYSPEC = new NSOFSymbol("queryspec");
    public static final NSOFSymbol SLOT_SOUP = new NSOFSymbol("soupname");

    private final NSOFFrame parameters = new NSOFFrame();

    /**
     * Creates a new command.
     */
    public DQuery() {
        super(COMMAND);
        setObject(parameters);
    }

    /**
     * Get the parameter frame.
     *
     * @return the frame.
     */
    public NSOFFrame getParameters() {
        return parameters;
    }

    /**
     * Set the parameter frame.
     *
     * @param parameters the frame.
     */
    public void setParameters(NSOFFrame parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
    }

    /**
     * Set the soup name.
     *
     * @param name the name.
     */
    public void setSoup(String name) {
        setSoup(new NSOFString(name));
    }

    /**
     * Set the soup name.
     *
     * @param name the name.
     */
    public void setSoup(NSOFString name) {
        parameters.put(SLOT_SOUP, name);
    }

    /**
     * Set the soup name.
     *
     * @param soup the soup.
     */
    public void setSoup(Soup soup) {
        setSoup(soup.getName());
    }

    /**
     * Set the query specification.
     *
     * @param querySpec the query specification.
     */
    public void setQuerySpec(NSOFObject querySpec) {
        parameters.put(SLOT_QUERYSPEC, querySpec);
    }
}
