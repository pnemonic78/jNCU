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
package net.sf.jncu.fdil;

/**
 * Newton Streamed Object Format - True.
 *
 * @author moshew
 */
public class NSOFTrue extends NSOFBoolean {

    /**
     * Default True boolean class.
     */
    public static final NSOFSymbol CLASS_TRUE = new NSOFSymbol("TRUE");

    /**
     * Creates a new True.
     */
    public NSOFTrue() {
        super(true);
        setObjectClass(CLASS_TRUE);
    }

    @Override
    public String toString() {
        return Boolean.TRUE.toString();
    }

    @Override
    public boolean isTrue() {
        return true;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFTrue();
    }
}
