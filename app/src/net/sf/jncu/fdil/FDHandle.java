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
 * FDIL object handle.
 *
 * @author Moshe
 */
public class FDHandle extends Object implements Comparable<FDHandle>, FDConstants {

    public static final int TYPE_INTEGER = 0x00;
    public static final int TYPE_POINTER = 0x01;
    public static final int TYPE_IMMEDIATE = 0x02;
    public static final int TYPE_MAGIC = 0x03;
    public static final int TYPE_IMMEDIATE_SPECIAL = (FD_IMMED_SPECIAL << 2) | TYPE_IMMEDIATE;
    public static final int TYPE_IMMEDIATE_CHARACTER = (FD_IMMED_CHARACTER << 2) | TYPE_IMMEDIATE;
    public static final int TYPE_IMMEDIATE_BOOLEAN = (FD_IMMED_BOOLEAN << 2) | TYPE_IMMEDIATE;
    public static final int TYPE_IMMEDIATE_RESERVED = (FD_IMMED_RESERVED << 2) | TYPE_IMMEDIATE;

    public static final int FLAG_BINARY = 0x00;
    public static final int FLAG_ARRAY = 0x01;
    public static final int FLAG_BLOB = 0x02;
    public static final int FLAG_FRAME = 0x03;

    public static final int MASK_TYPE = 0x03;
    public static final int MASK_TYPE_IMMEDIATE = 0x0F;
    public static final int MASK_FLAG_TYPE = 0x03;

    protected static final int MASK_28 = 0x0FFFFFFF;
    protected static final int MASK_30 = 0x3FFFFFFF;

    /**
     * The {@code ref} field is the same long as in the non-debug
     * <tt>FD_Handle</tt>.
     * <p>
     * The lowest two bits determine the object’s basic type, as follows:<br>
     * {@code 00} = integer<br>
     * {@code 01} = pointer object<br>
     * {@code 10} = immediate object<br>
     * {@code 11} = magic pointer
     * <p>
     * If the {@code ref} is an integer, the value is contained in the upper 30
     * bits. If the object is an immediate, the next two low order bits
     * represent the object’s type:<br>
     * {@code 0010} = special immediate<br>
     * {@code 0110} = character immediate<br>
     * {@code 1010} = Boolean immediate<br>
     * {@code 1110} = reserved immediate
     * <p>
     * In an immediate object, the upper 28 bits contain the object’s value. For
     * example, these upper 28 bits hold the 16-bit Unicode character in a
     * character object.<br>
     * A pointer’s upper 30 bits contain an index into an internal object table.
     * <br>
     * A magic pointer object’s upper 30 bits contain the magic pointer value.
     */
    private final int ref;
    /**
     * The flags field contains a bit field describing the object. The lowest 2
     * bits of this field specifies the object’s type, as follows:<br>
     * {@code 00} = raw binary object<br>
     * {@code 01} = array<br>
     * {@code 10} = large binary object<br>
     * {@code 11} = frame
     */
    private final int flags;
    /**
     * The {@code size} field specifies the object’s size. For binary objects,
     * this is the number of user bytes in the object. For arrays and frames,
     * this is the number of elements in the object times
     * <tt>sizeof(FD_Handle)</tt>. For large binary objects, this is
     * <tt>sizeof(FD_LargeBinaryData)</tt>.
     */
    private int size;
//	/**
//	 * The next field is the object’s class, {@code oClass}, if the object is
//	 * anything but a frame, or the frame map, {@code map}, if the object is a
//	 * frame. If the object is a frame it’s class is stored in a slot named "
//	 * <tt>class</tt>" containing a symbol. A frame’s map is simply an array of
//	 * symbols containing the slot names used in the frame. There is one
//	 * difference between a frame map and a regular array. A frame map contains
//	 * the value zero in its {@code oClass} field.
//	 */
//	private final FDHandle oClass;

    private int pointer;

    /**
     * Creates a new handle.
     *
     * @param obj the object to handle.
     */
    public FDHandle(NSOFObject obj) {
        super();
        this.ref = generateRef(obj);
        this.flags = generateFlags(obj);
//		this.oClass = generateClass(obj);
        this.size = calculateSize(obj);
    }

    /**
     * Get the handle reference.
     *
     * @return the reference.
     */
    public int getReference() {
        return ref;
    }

    /**
     * Get the flags.
     *
     * @return the flags.
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Get the size.
     *
     * @return the size.
     */
    public int getSize() {
        return size;
    }

//	/**
//	 * Get the symbol object for the class.
//	 * 
//	 * @return the class.
//	 */
//	public FDHandle getObjectClass() {
//		return oClass;
//	}

    @Override
    public int hashCode() {
        return ref;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FDHandle) {
            return compareTo((FDHandle) obj) == 0;
        }
        return false;
    }

    @Override
    public int compareTo(FDHandle that) {
        int n = this.ref - that.ref;
        if (n == 0)
            n = this.flags - that.flags;
        if (n == 0)
            n = this.pointer - that.pointer;
        if (n == 0)
            n = this.size - that.size;
        return n;
    }

    @Override
    public String toString() {
        return String.valueOf(ref);
    }

    /**
     * Generate the handle reference.
     *
     * @param obj the object.
     * @return the reference.
     */
    private int generateRef(NSOFObject obj) {
        int value = 0;
        int type = 0;
        if (obj instanceof NSOFInteger) {
            NSOFInteger i = (NSOFInteger) obj;
            type = TYPE_INTEGER;
            value = (i.getValue() & MASK_30) << 2;
        } else if (obj instanceof NSOFMagicPointer) {
            NSOFMagicPointer m = (NSOFMagicPointer) obj;
            type = TYPE_MAGIC;
            value = (m.getValue() & MASK_30) << 2;
        } else if (obj instanceof NSOFImmediate) {
            NSOFImmediate i = (NSOFImmediate) obj;
            type = TYPE_IMMEDIATE;
            if (i.isCharacter()) {
                type = TYPE_IMMEDIATE_CHARACTER;
                value = (i.getValue() & MASK_28) << 4;
            } else if (i.isInteger()) {
                type = TYPE_INTEGER;
                value = (i.getValue() & MASK_30) << 2;
            } else if (i.isMagicPointer()) {
                type = TYPE_MAGIC;
                value = (i.getValue() & MASK_30) << 2;
            } else if (i.isNil()) {
                type = TYPE_IMMEDIATE_SPECIAL;
                value = (i.getValue() & MASK_28) << 4;
            } else if (i.isTrue()) {
                type = TYPE_IMMEDIATE_BOOLEAN;
                value = (i.getValue() & MASK_28) << 4;
            }
        } else if (obj instanceof NSOFPointer) {
            type = TYPE_POINTER;
            value = (++pointer) << 2;
        }
        return value | type;
    }

    /**
     * Generate the handle flags.
     *
     * @param obj the object.
     * @return the flags.
     */
    private int generateFlags(NSOFObject obj) {
        int flags = 0;
        if (obj instanceof NSOFArray) {
            flags |= FLAG_ARRAY;
        } else if (obj instanceof NSOFBinaryObject) {
            if (obj instanceof NSOFLargeBinary) {
                flags |= FLAG_BLOB;
            } else {
                flags |= FLAG_BINARY;
            }
        } else if (obj instanceof NSOFFrame) {
            flags |= FLAG_FRAME;
        }
        return flags;
    }

//	/**
//	 * Generate the handle object class.
//	 * 
//	 * @param obj
//	 *            the object.
//	 * @return the class handle.
//	 */
//	private FDHandle generateClass(NSOFObject obj) {
//		final NSOFSymbol cls = obj.getObjectClass();
//		if (cls == null)
//			return null;
//		FDHandle oClass = FDHandles.getInstance().find(cls);
//		if (oClass == null)
//			oClass = FDHandles.getInstance().create(cls);
//		return oClass;
//	}

    /**
     * Calculate the object size.
     *
     * @param obj the object.
     * @return the size.
     */
    private int calculateSize(NSOFObject obj) {
        if (obj instanceof NSOFImmediate) {
            return 4;
        }
        if (obj instanceof NSOFArray) {
            return ((NSOFArray) obj).length();
        }
        if (obj instanceof NSOFFrame) {
            return ((NSOFFrame) obj).size();
        }
        if (obj instanceof NSOFBinaryObject) {
            byte[] data = ((NSOFBinaryObject) obj).getValue();
            if (data != null)
                return data.length;
        }
        if (obj instanceof NSOFString) {
            String s = ((NSOFString) obj).getValue();
            return (s == null) ? 0 : (s.length() << 1);
        }
        return 0;
    }
}
