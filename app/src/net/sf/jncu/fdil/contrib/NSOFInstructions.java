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

import net.sf.jncu.fdil.NSOFBinaryObject;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton Streamed Object Format - Instructions.
 *
 * @author Moshe
 */
public class NSOFInstructions extends NSOFBinaryObject {

    /**
     * Default instructions class.
     */
    public static final NSOFSymbol CLASS_INSTRUCTIONS = new NSOFSymbol("instructions");

    /**
     * Constructs new instructions.
     */
    public NSOFInstructions() {
        super();
        setObjectClass(CLASS_INSTRUCTIONS);
    }

    /**
     * Constructs new instructions.
     *
     * @param bin the source binary object.
     */
    public NSOFInstructions(NSOFBinaryObject bin) {
        this();
        setObjectClass(bin.getObjectClass());
        setValue(bin.getValue());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        NSOFInstructions copy = new NSOFInstructions();
        copy.setValue(this.getValue());
        return copy;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        NSOFInstructions copy = new NSOFInstructions();
        byte[] value = this.getValue();
        if (value != null) {
            byte[] value2 = new byte[value.length];
            System.arraycopy(value, 0, value2, 0, value.length);
            copy.setValue(value2);
        }
        return copy;
    }
}
