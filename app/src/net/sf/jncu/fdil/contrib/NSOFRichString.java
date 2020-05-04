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
