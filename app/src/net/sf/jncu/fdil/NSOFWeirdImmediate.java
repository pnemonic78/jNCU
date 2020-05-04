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
package net.sf.jncu.fdil;

/**
 * Newton Streamed Object Format - Weird Immediate.
 *
 * @author moshew
 */
public class NSOFWeirdImmediate extends NSOFImmediate {

    /**
     * Default weird immediate class.<br>
     * <tt>kFD_SymWeird_Immediate</tt>
     */
    public static final NSOFSymbol CLASS_WEIRD_IMMEDIATE = new NSOFSymbol("weird_immediate");

    /**
     * Creates a new weird immediate.
     *
     * @param value the value.
     */
    public NSOFWeirdImmediate(int value) {
        super(value, IMMEDIATE_WEIRD);
        setObjectClass(CLASS_WEIRD_IMMEDIATE);
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFWeirdImmediate(this.getValue());
    }
}
