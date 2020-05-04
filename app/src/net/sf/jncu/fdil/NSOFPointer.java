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
 * FDIL pointer object.
 * <p>
 * <em>Pointers are only used internally by the FDIL. You should never
 * see such an object.</em>
 * <p>
 * A pointer object is an object whose <tt>FD_Handle</tt> contains a reference
 * to the data comprising the object.
 *
 * @author moshew
 */
public abstract class NSOFPointer extends NSOFObject implements Precedent {

    /**
     * Creates a new pointer.
     */
    public NSOFPointer() {
        super();
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /*
     * Expose public because its object class is allowed by anything.
     */
    @Override
    public void setObjectClass(NSOFSymbol oClass) {
        super.setObjectClass(oClass);
    }
}
