/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * http://sourceforge.net/projects/jncu
 *
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
 */
package net.sf.jncu.fdil;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Newton Streamed Object Format encoder.
 *
 * @author moshew
 */
public class NSOFEncoder {

    private final Map<Precedent, NSOFPrecedent> precedents = new HashMap<Precedent, NSOFPrecedent>();

    /**
     * {@code 0} is a legal ID.
     */
    private int idMax = 0;
    /**
     * Written version header?
     */
    private boolean versioned;
    private boolean precedentsUse = true;

    /**
     * Creates a new encoder.
     */
    public NSOFEncoder() {
        this(false);
    }

    /**
     * Creates a new encoder.
     *
     * @param versioned append version?
     */
    public NSOFEncoder(boolean versioned) {
        super();
        this.versioned = versioned;
    }

    /**
     * Encode the NewtonScript object, recursively.<br>
     * Converts the given object into a flat stream of bytes in Newton Stream
     * Object Format (NSOF) suitable for saving to disk or for transmission to a
     * Newton device.
     *
     * @param object the object to encode.
     * @param out    the output.
     * @throws IOException if an encoding error occurs.
     */
    public void flatten(NSOFObject object, OutputStream out) throws IOException {
        if (!versioned) {
            out.write(NewtonStreamedObjectFormat.VERSION);
            versioned = true;
        }
        flattenImpl(object, out);
    }

    /**
     * Encode the NewtonScript object - implementation.
     *
     * @param object           the object to encode.
     * @param out              the output.
     * @param encodePrecedents encode precedent IDs?
     * @throws IOException if an encoding error occurs.
     */
    protected void flattenImpl(NSOFObject object, OutputStream out) throws IOException {
        if (object == null) {
            // NewtonStreamedObjectFormat.htonl(0, out);
            object = NSOFNil.NIL;
        }
        if (object instanceof NSOFPrecedent) {
            NSOFPrecedent id = (NSOFPrecedent) object;
            Precedent p = id.getReferent();
            if (p != null)
                object = (NSOFObject) p;
        }
        if (precedentsUse && (object instanceof Precedent)) {
            Precedent p = (Precedent) object;
            NSOFPrecedent id = precedents.get(p);
            if (id == null) {
                id = new NSOFPrecedent(this.idMax++);
                precedents.put(p, id);
            } else {
                object = id;
            }
        }
        object.flatten(out, this);
    }

    /**
     * Convert the Java object to an FDIL Newton Script object.
     *
     * @param o the object.
     * @return the NewtonScript object - {@code null} otherwise.
     */
    public static NSOFObject toNS(Object o) {
        if (o == null) {
            return NSOFNil.NIL;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue() ? NSOFBoolean.TRUE : NSOFBoolean.FALSE;
        }
        if (o instanceof Character) {
            return new NSOFUnicodeCharacter((Character) o);
        }
        if (o instanceof Double) {
            return new NSOFReal((Double) o);
        }
        if (o instanceof Float) {
            return new NSOFReal(((Float) o).doubleValue());
        }
        if (o instanceof Integer) {
            return new NSOFInteger((Integer) o);
        }
        if (o instanceof Number) {
            return new NSOFImmediate(((Number) o).intValue(), NSOFImmediate.IMMEDIATE_INTEGER);
        }
        if (o instanceof CharSequence) {
            return new NSOFString(o.toString());
        }
        if (o instanceof Rectangle) {
            Rectangle rect = (Rectangle) o;
            return new NSOFSmallRect(rect.y, rect.x, rect.y + rect.height, rect.x + rect.width);
        }
        if (o.getClass().isArray()) {
            if (o instanceof byte[]) {
                return new NSOFBinaryObject((byte[]) o);
            }
            if (o instanceof Object[]) {
                Object[] arr = (Object[]) o;
                NSOFObject[] entries = new NSOFObject[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    entries[i] = toNS(arr[i]);
                }
                return new NSOFArray(entries);
            }
        }
        if (o instanceof Collection<?>) {
            Collection<?> coll = (Collection<?>) o;
            NSOFObject[] entries = new NSOFObject[coll.size()];
            int i = 0;
            for (Object entry : coll) {
                entries[i++] = toNS(entry);
            }
            return new NSOFArray(entries);
        }
        return null;
    }

    /**
     * Use precedents for encoding duplicate objects?
     *
     * @param use {@code true} to encode precedents - {@code false} to ignore
     *            precedents.
     */
    public void setPrecedents(boolean use) {
        this.precedentsUse = use;
    }
}
