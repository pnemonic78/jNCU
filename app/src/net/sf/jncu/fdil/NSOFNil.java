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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - NIL.
 * <p>
 * There is only one special FDIL immediate object that you encounter, the nil
 * object. This object, which you can refer to with the constant
 * <tt>kFD_NIL</tt>, is used to signify the lack of information.
 *
 * @author Moshe
 */
public class NSOFNil extends NSOFImmediate {

    /**
     * Default nil class.
     */
    public static final NSOFSymbol CLASS_NIL = new NSOFSymbol("NIL");

    /**
     * The nil object.<br>
     * <tt>kFD_NIL</tt>
     */
    public static final NSOFNil NIL = new NSOFNil();

    /**
     * Constructs a new Nil.
     */
    public NSOFNil() {
        super(0, IMMEDIATE_NIL);
        setObjectClass(CLASS_NIL);
    }

    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        out.write(NSOF_NIL);
    }

    @Override
    public String toString() {
        return "nil";
    }

    @Override
    public boolean isNil() {
        return true;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return NIL;
    }
}
