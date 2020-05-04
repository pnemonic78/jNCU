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
package net.sf.jncu.fdil.contrib;

import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;

/**
 * Rich strings extend the string object class by embedding ink information
 * within the object. Within the unicode, a special character <tt>kInkChar</tt>
 * is used to mark the position of an ink word. The ink data is stored after the
 * null termination character. Ink size varies depending on stroke complexity.
 *
 * @author Moshe
 */
public class NSOFRichString extends NSOFString {

    /**
     * Constructs a new string.
     *
     * @param value the value.
     */
    public NSOFRichString(String value) {
        super(value);
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFRichString(this.getValue());
    }
}
