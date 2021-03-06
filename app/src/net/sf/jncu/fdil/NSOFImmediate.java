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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Immediate.
 * <p>
 * Other than characters, the only immediate objects that you need are the true
 * object and the nil object, which are specified by the <tt>kFD_True</tt> and
 * <tt>kFD_NIL</tt> constants.
 * <p>
 * 2 bits are used for the immediate type, leaving a maximum of 30 bits for the
 * immediate value.
 *
 * @author Moshe
 */
public class NSOFImmediate extends NSOFObject {

    /**
     * Default immediate class.<br>
     * <tt>kFD_Sym_Immediate</tt>
     */
    public static final NSOFSymbol CLASS_IMMEDIATE = new NSOFSymbol("immediate");

    /**
     * Integer immediate.
     */
    public static final int IMMEDIATE_INTEGER = 0x0;
    /**
     * Character immediate.
     */
    public static final int IMMEDIATE_CHARACTER = 0x6;
    /**
     * TRUE immediate.
     */
    public static final int IMMEDIATE_TRUE = 0xA;
    /**
     * NIL immediate.
     */
    public static final int IMMEDIATE_NIL = 0x2;
    /**
     * Magic Pointer immediate.
     */
    public static final int IMMEDIATE_MAGIC_POINTER = 0x3;
    /**
     * Weird immediate.
     */
    public static final int IMMEDIATE_WEIRD = 0x1;

    private int value;
    private int type;
    private boolean valueSet;

    /**
     * Constructs a new immediate.
     *
     * @param value the value.
     * @param type  the type.
     */
    public NSOFImmediate(int value, int type) {
        this(type);
        setValue(value);
    }

    /**
     * Constructs a new immediate. The value must be set later in
     * {@link #inflate(InputStream, NSOFDecoder)}
     *
     * @param type the type.
     */
    protected NSOFImmediate(int type) {
        super();
        setObjectClass(CLASS_IMMEDIATE);
        setType(type);
    }

    @Override
    public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
        // Already decoded.
    }

    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        out.write(NSOF_IMMEDIATE);

        // Immediate Ref (xlong)
        int val = getValue();
        int ref = val;
        switch (type) {
            case IMMEDIATE_CHARACTER:
                ref = (val << 4) | 0x6;
                break;
            case IMMEDIATE_INTEGER:
                ref = val << 2;
                break;
            case IMMEDIATE_MAGIC_POINTER:
                ref = (val << 2) | 0x3;
                break;
            case IMMEDIATE_NIL:
                ref = 0x2;
                break;
            case IMMEDIATE_TRUE:
                ref = 0x1A;
                break;
            case IMMEDIATE_WEIRD:
                ref = (val << 2) | 0x2;
                break;
        }
        XLong.encode(ref, out);
    }

    /**
     * Get the value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value the value.
     */
    protected void setValue(int value) {
        if (valueSet)
            throw new IllegalArgumentException("value already set");
        this.value = value;
        this.valueSet = true;
    }

    @Override
    public int hashCode() {
        return getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof NSOFImmediate) {
            NSOFImmediate that = (NSOFImmediate) obj;
            return (this.getType() == that.getType()) && (this.getValue() == that.getValue());
        }
        return super.equals(obj);
    }

    /**
     * Get the type.
     *
     * @return the type.
     */
    public int getType() {
        return type;
    }

    /**
     * Set the type.
     *
     * @param type the type.
     */
    protected void setType(int type) {
        this.type = type;
    }

    /**
     * Decoder can test if the immediate is a character.
     *
     * @param r the Ref of an Immediate.
     * @return {@code true} if a character.
     */
    public static boolean isRefCharacter(int r) {
        return (r & 0xF) == 0x6;
    }

    /**
     * Decoder can test if the immediate is an integer.
     *
     * @param r the Ref of an Immediate.
     * @return {@code true} if an integer.
     */
    public static boolean isRefInteger(int r) {
        return (r & 0x3) == 0x0;
    }

    /**
     * Decoder can test if the immediate is an integer.
     *
     * @param r the Ref of an Immediate.
     * @return {@code true} if an integer.
     */
    public static boolean isRefMagicPointer(int r) {
        return (r & 0x3) == 0x3;
    }

    /**
     * Decoder can test if the immediate is a NIL.
     *
     * @param r the Ref of an Immediate.
     * @return {@code true} if NIL.
     */
    public static boolean isRefNil(int r) {
        return r == 0x2;
    }

    /**
     * Decoder can test if the immediate is TRUE.
     *
     * @param r the Ref of an Immediate.
     * @return {@code true} if TRUE.
     */
    public static boolean isRefTrue(int r) {
        return (r == 0x1A);
    }

    /**
     * Decoder can test if the immediate is TRUE.
     *
     * @param r the Ref of an Immediate.
     * @return {@code true} if weird.
     */
    public static boolean isRefWeird(int r) {
        return ((r & 0x2) == 0x2) && (r != 0x2);
    }

    /**
     * Decoder can test if the immediate is a character.
     *
     * @return {@code true} if a character.
     */
    public boolean isCharacter() {
        return type == IMMEDIATE_CHARACTER;
    }

    /**
     * Decoder can test if the immediate is an integer.
     *
     * @return {@code true} if an integer.
     */
    public boolean isInteger() {
        return type == IMMEDIATE_INTEGER;
    }

    /**
     * Decoder can test if the immediate is an integer.
     *
     * @return {@code true} if an integer.
     */
    public boolean isMagicPointer() {
        return type == IMMEDIATE_MAGIC_POINTER;
    }

    /**
     * Decoder can test if the immediate is a NIL.
     *
     * @return {@code true} if NIL.
     */
    public boolean isNil() {
        return type == IMMEDIATE_NIL;
    }

    /**
     * Decoder can test if the immediate is TRUE.
     *
     * @return {@code true} if TRUE.
     */
    public boolean isTrue() {
        return type == IMMEDIATE_TRUE;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFImmediate(this.getValue(), this.getType());
    }

    /**
     * Is the object a NIL?
     *
     * @param obj the object to test.
     * @return {@code true} if NIL.
     */
    public static boolean isNil(NSOFObject obj) {
        if (obj == null)
            return true;
        if (obj instanceof NSOFImmediate) {
            NSOFImmediate imm = (NSOFImmediate) obj;
            return imm.isNil();
        }
        return false;
    }
}
