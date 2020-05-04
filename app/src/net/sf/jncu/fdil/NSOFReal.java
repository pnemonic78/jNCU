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

import net.sf.jncu.util.NumberUtils;

/**
 * Newton Streamed Object Format - Real number.
 * <p>
 * Real numbers in NewtonScript are represented as IEEE 64-bit floating point
 * numbers, which are accurate to about 15 decimal digits. They are binary
 * objects of class <tt>'real</tt>.
 *
 * @author moshew
 */
public class NSOFReal extends NSOFBinaryObject {

    /**
     * Default real number class.<br>
     * <tt>kFD_SymReal</tt>
     */
    public static final NSOFSymbol CLASS_REAL = new NSOFSymbol("real");

    private double value;

    /**
     * Creates a new real number.
     */
    private NSOFReal() {
        super();
        setObjectClass(CLASS_REAL);
    }

    /**
     * Creates a new real number.
     *
     * @param value the value.
     */
    public NSOFReal(double value) {
        this();
        setReal(value);
    }

    /**
     * Creates a new real number.
     *
     * @param value the value.
     */
    public NSOFReal(byte[] value) {
        this();
        setValue(value);
    }

    /**
     * Creates a new real number.
     *
     * @param bin the source binary object.
     */
    public NSOFReal(NSOFBinaryObject bin) {
        this(bin.getValue());
    }

    /**
     * Get the value.
     *
     * @return the value
     */
    public double getReal() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value the value.
     */
    protected void setReal(double value) {
        this.value = value;
        super.setValue(NumberUtils.toBytes(Double.doubleToRawLongBits(value)));
    }

    @Override
    public void setValue(byte[] value) {
        setReal(Double.longBitsToDouble(NumberUtils.toLong(value)));
        super.setValue(value);
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFReal(this.getReal());
    }

    @Override
    public String toString() {
        return String.valueOf(getReal());
    }
}
