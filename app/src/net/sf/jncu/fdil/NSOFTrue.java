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
