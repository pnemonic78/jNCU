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
 * Newton Streamed Object Format - Boolean.
 * <p>
 * There are two Boolean objects: the true object, and the false object. The nil
 * object <tt>kFD_NIL</tt> is not the same as the false object.
 *
 * @author moshew
 */
public class NSOFBoolean extends NSOFImmediate {

    /**
     * Default boolean class.<br>
     * <tt>kFD_SymBoolean</tt>
     */
    public static final NSOFSymbol CLASS_BOOLEAN = new NSOFSymbol("boolean");

    /**
     * The true object.<br>
     * <tt>kFD_True</tt>
     */
    public static final NSOFBoolean TRUE = new NSOFBoolean(true);
    /**
     * The false object.<br>
     * <tt>kFD_False</tt>
     */
    public static final NSOFBoolean FALSE = new NSOFBoolean(false);

    /**
     * Creates a new boolean.
     *
     * @param value the value.
     */
    protected NSOFBoolean(boolean value) {
        super(value ? 0x1A : 0, value ? IMMEDIATE_TRUE : IMMEDIATE_NIL);
        setObjectClass(CLASS_BOOLEAN);
    }

    @Override
    public String toString() {
        return isTrue() ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
    }

    @Override
    public boolean isNil() {
        return getValue() == 0;
    }

    @Override
    public boolean isTrue() {
        return getValue() != 0;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return isTrue() ? TRUE : FALSE;
    }

    /**
     * Get the boolean object.
     *
     * @param value the boolean value.
     * @return
     */
    public static NSOFBoolean valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }
}
