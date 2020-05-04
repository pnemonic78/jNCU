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

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton Streamed Object Format - Literals.
 *
 * @author Moshe
 */
public class NSOFLiterals extends NSOFArray {

    /**
     * Default literals class.
     */
    public static final NSOFSymbol CLASS_LITERALS = new NSOFSymbol("literals");

    /**
     * Constructs a new literals array.
     */
    private NSOFLiterals() {
        super();
        setObjectClass(CLASS_LITERALS);
    }

    /**
     * Constructs a new literals array.
     *
     * @param value the source value array.
     */
    public NSOFLiterals(NSOFObject[] value) {
        this();
        setValue(value);
    }

    /**
     * Constructs a new literals array.
     *
     * @param arr the source value array.
     */
    public NSOFLiterals(NSOFArray arr) {
        this(arr.getValue());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        NSOFLiterals copy = new NSOFLiterals();
        copy.setValue(this.getValue());
        return copy;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        NSOFLiterals copy = new NSOFLiterals();
        int length = this.length();
        for (int i = 0; i < length; i++)
            copy.add(this.get(i).deepClone());
        return copy;
    }
}
