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
 * Newton Streamed Object Format - Magic Pointer.
 * <p>
 * A magic pointer object contains a pointer to objects in a Newton devices ROM.
 * You should only need to create magic pointer objects if you are writing a
 * Newton development environment. The only likely way to run into a magic
 * pointer object in your code is reading an NTK stream file with the
 * <tt>FD_Unflatten</tt> function. You should never see a magic pointer object
 * from data sent from a Newton device, through a CDIL pipe. Magic pointers are
 * resolved before being sent from a Newton device.
 *
 * @author moshew
 */
public class NSOFMagicPointer extends NSOFImmediate {

    /**
     * Default magic pointer class.<br>
     * <tt>kFD_SymMagicPointer</tt>
     */
    public static final NSOFSymbol CLASS_MAGIC_POINTER = new NSOFSymbol("magicptr");

    /**
     * Creates a new magic pointer.
     *
     * @param value the value.
     */
    public NSOFMagicPointer(int value) {
        super(value, IMMEDIATE_MAGIC_POINTER);
        setObjectClass(CLASS_MAGIC_POINTER);
    }

    @Override
    public boolean isMagicPointer() {
        return true;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFMagicPointer(this.getValue());
    }
}
