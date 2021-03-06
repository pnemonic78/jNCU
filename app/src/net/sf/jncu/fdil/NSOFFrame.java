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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Newton Streamed Object Format - Frame.
 * <p>
 * A frame is an aggregate object where each element, called a "slot," contains
 * any FDIL object, and is indexed by name. The slot name itself is a symbol.
 * Rather than using an integer index to retrieve a value that's been added to a
 * frame (as you would with an array), you specify the slot name to get the slot
 * value.
 *
 * @author Moshe
 */
public class NSOFFrame extends NSOFPointer implements NSOFCollection {

    /**
     * Default frame class.<br>
     * <tt>kFD_SymFrame</tt>
     */
    public static final NSOFSymbol CLASS_FRAME = new NSOFSymbol("frame");
    public static final NSOFSymbol SLOT_CLASS = new NSOFSymbol("class");

    private final Map<NSOFSymbol, NSOFObject> slots = new LinkedHashMap<NSOFSymbol, NSOFObject>();

    /**
     * Constructs a new frame.
     */
    public NSOFFrame() {
        super();
        setObjectClass(CLASS_FRAME);
    }

    @Override
    public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
        this.slots.clear();

        // Number of slots (xlong)
        final int size = XLong.decodeValue(in);
        NSOFSymbol[] keys = new NSOFSymbol[size];
        NSOFString str;

        // Slot tags in ascending order (symbol objects)
        for (int i = 0; i < size; i++) {
            str = (NSOFString) decoder.inflate(in);
            if (str instanceof NSOFSymbol) {
                keys[i] = (NSOFSymbol) str;
            } else {
                keys[i] = new NSOFSymbol(str.getValue());
            }
        }

        // Slot values in ascending order (objects)
        NSOFObject slot;
        for (int i = 0; i < size; i++) {
            slot = decoder.inflate(in);
            put(keys[i], slot);
        }
    }

    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        out.write(NSOF_FRAME);

        final List<NSOFSymbol> keys = new ArrayList<NSOFSymbol>(slots.keySet());

        // Number of slots (xlong)
        XLong.encode(keys.size(), out);

        // Slot tags in ascending order (symbol objects)
        for (NSOFSymbol key : keys) {
            encoder.flatten(key, out);
        }

        // Slot values in ascending order (objects)
        NSOFObject slot;
        for (NSOFSymbol key : keys) {
            slot = get(key);
            encoder.flatten(slot, out);
        }
    }

    @Override
    public int hashCode() {
        return slots.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof NSOFFrame) {
            return this.slots.equals(((NSOFFrame) obj).slots);
        }
        return super.equals(obj);
    }

    /**
     * Is the frame empty?
     *
     * @return true if this frame contains no slots.
     */
    public boolean isEmpty() {
        return slots.isEmpty();
    }

    /**
     * Removes all of the slots from this frame.
     */
    public void clear() {
        slots.clear();
    }

    /**
     * Adds a key/value pair to the frame.
     * <p>
     * If a pair with the specified key already exists in the frame, its
     * corresponding value object is replaced.
     *
     * @param slotName the slot name.
     * @param value    the slot value.
     * @return the replaced object or <tt>NIL</tt> if the slot does not exist.
     */
    public NSOFObject put(NSOFSymbol name, NSOFObject value) {
        NSOFObject old = slots.put(name, (value == null) ? NSOFNil.NIL : value);
        if ((value != null) && SLOT_CLASS.equals(name)) {
            NSOFSymbol oClassCurrent = getObjectClass();
            NSOFSymbol oClass = null;
            if (value instanceof NSOFSymbol) {
                oClass = (NSOFSymbol) value;
            } else if (value instanceof NSOFString) {
                oClass = new NSOFSymbol(((NSOFString) value).getValue());
            }
            if (!oClassCurrent.equals(oClass))
                setObjectClass(oClass);
        }
        return (old == null) ? NSOFNil.NIL : old;
    }

    /**
     * Associates the specified slot value with the specified symbol.
     *
     * @param slotName the slot name.
     * @param value    the slot value.
     * @see #put(NSOFSymbol, NSOFObject)
     */
    public void put(String slotName, NSOFObject value) {
        put(new NSOFSymbol(slotName), value);
    }

    /**
     * Get the mapped slot value for the specified key.
     *
     * @param slotName the slot name.
     * @return the slot value - {@code NIL} otherwise.
     */
    public NSOFObject get(NSOFSymbol slotName) {
        NSOFObject value = slots.get(slotName);
        return (value == null) ? NSOFNil.NIL : value;
    }

    /**
     * Get the mapped slot value for the specified key.
     *
     * @param slotName the slot name.
     * @return the slot value - {@code NIL} otherwise.
     * @see #get(NSOFSymbol)
     */
    public NSOFObject get(String slotName) {
        return get(new NSOFSymbol(slotName));
    }

    /**
     * Get the mapped slot value at the specified index.
     * <p>
     * The order in which the objects are returned is not defined. In
     * particular, you should not expect to retrieve them in the order in which
     * they were inserted.
     *
     * @param pos an index into the frame.
     * @return the slot value - {@code NIL} otherwise.
     */
    public NSOFObject get(int pos) {
        List<NSOFSymbol> names = new ArrayList<NSOFSymbol>(slots.keySet());
        NSOFSymbol key = names.get(pos);
        return get(key);
    }

    /**
     * Removes the slot/value pair.
     *
     * @param slotName the slot name.
     * @return the removed slot value - {@code NIL} otherwise.
     */
    public NSOFObject remove(NSOFSymbol slotName) {
        NSOFObject value = slots.remove(slotName);
        return (value == null) ? NSOFNil.NIL : value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        NSOFObject value;
        int i = 0;
        for (NSOFSymbol key : slots.keySet()) {
            if (i > 0)
                sb.append(", ");
            value = slots.get(key);
            sb.append(key.getValue());
            sb.append('=');
            if (value instanceof NSOFArray) {
                int size = ((NSOFArray) value).length();
                if (size == 0)
                    sb.append("[]");
                else if (size == 1)
                    sb.append("[1 Element]");
                else
                    sb.append("[" + size + " Elements]");
            } else
                sb.append(value.toString());
            i++;
        }
        sb.append('}');

        return sb.toString();
    }

    /**
     * Get the number of slots.
     *
     * @return the size.
     */
    public int size() {
        return slots.size();
    }

    /**
     * Get the set of slot names for traversal.
     *
     * @return the names.
     */
    public Set<NSOFSymbol> getNames() {
        return slots.keySet();
    }

    /**
     * Returns whether or not a slot with the given name exists in the frame.
     *
     * @param slotName the slot name.
     * @return true if slot found.
     */
    public boolean hasSlot(NSOFSymbol slotName) {
        return slots.containsKey(slotName);
    }

    /**
     * Returns whether or not a slot with the given name exists in the frame.
     *
     * @param slotName the slot name.
     * @return true if slot found.
     * @see #hasSlot(NSOFSymbol)
     */
    public boolean hasSlot(String slotName) {
        return hasSlot(new NSOFSymbol(slotName));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        NSOFFrame copy = new NSOFFrame();
        copy.slots.putAll(this.slots);
        return copy;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        NSOFFrame copy = new NSOFFrame();
        for (NSOFSymbol name : this.slots.keySet()) {
            copy.slots.put(name, this.slots.get(name).deepClone());
        }
        return copy;
    }

    /**
     * Add all the slots.
     *
     * @param frame the source frame.
     */
    public void putAll(NSOFFrame frame) {
        for (NSOFSymbol key : frame.getKeys())
            put(key, frame.get(key));
    }

    /**
     * Get the slots keys.
     *
     * @return the set of keys.
     */
    public Set<NSOFSymbol> getKeys() {
        return slots.keySet();
    }

    @Override
    public void setObjectClass(NSOFSymbol oClass) {
        super.setObjectClass(oClass);
        if (!CLASS_FRAME.equals(oClass))
            put(SLOT_CLASS, oClass);
    }
}
