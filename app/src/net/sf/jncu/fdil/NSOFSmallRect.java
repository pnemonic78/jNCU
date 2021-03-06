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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Newton Streamed Object Format - Small Rectangle.
 *
 * @author Moshe
 */
public class NSOFSmallRect extends NSOFFrame {

    /**
     * Default small rectangle class.
     */
    public static final NSOFSymbol CLASS_SMALL_RECT = new NSOFSymbol("smallRect");

    protected static final NSOFSymbol SLOT_TOP = new NSOFSymbol("top");
    protected static final NSOFSymbol SLOT_LEFT = new NSOFSymbol("left");
    protected static final NSOFSymbol SLOT_BOTTOM = new NSOFSymbol("bottom");
    protected static final NSOFSymbol SLOT_RIGHT = new NSOFSymbol("right");

    /**
     * Constructs a new small rectangle.
     */
    public NSOFSmallRect() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructs a new small rectangle.
     *
     * @param top    the top co-ordinate.
     * @param left   the left co-ordinate.
     * @param bottom the bottom co-ordinate.
     * @param right  the right co-ordinate.
     */
    public NSOFSmallRect(int top, int left, int bottom, int right) {
        super();
        setObjectClass(CLASS_SMALL_RECT);
        setTop(top);
        setLeft(left);
        setBottom(bottom);
        setRight(right);
    }

    /**
     * Constructs a new small rectangle.
     *
     * @param top    the top co-ordinate.
     * @param left   the left co-ordinate.
     * @param bottom the bottom co-ordinate.
     * @param right  the right co-ordinate.
     */
    public NSOFSmallRect(byte top, byte left, byte bottom, byte right) {
        super();
        setObjectClass(CLASS_SMALL_RECT);
        setTop(top);
        setLeft(left);
        setBottom(bottom);
        setRight(right);
    }

    @Override
    public void inflate(InputStream in, NSOFDecoder decoder) throws IOException {
        // Top value (byte)
        int top = in.read();
        if (top == -1) {
            throw new EOFException();
        }
        setTop(top);

        // Left value (byte)
        int left = in.read();
        if (left == -1) {
            throw new EOFException();
        }
        setLeft(left);

        // Bottom value (byte)
        int bottom = in.read();
        if (bottom == -1) {
            throw new EOFException();
        }
        setBottom(bottom);

        // Right value (byte)
        int right = in.read();
        if (right == -1) {
            throw new EOFException();
        }
        setRight(right);
    }

    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        out.write(NSOF_SMALL_RECT);
        // Top value (byte)
        out.write(getTop());
        // Left value (byte)
        out.write(getLeft());
        // Bottom value (byte)
        out.write(getBottom());
        // Right value (byte)
        out.write(getRight());
    }

    /**
     * Get the bottom.
     *
     * @return the bottom
     */
    public int getBottom() {
        return ((NSOFInteger) get(SLOT_BOTTOM)).getValue();
    }

    /**
     * Set the bottom.
     *
     * @param bottom the bottom.
     */
    public void setBottom(int bottom) {
        put(SLOT_BOTTOM, new NSOFInteger(bottom & 0xFF));
    }

    /**
     * Set the bottom.
     *
     * @param bottom the bottom.
     */
    public void setBottom(byte bottom) {
        setBottom((int) bottom);
    }

    /**
     * Get the left.
     *
     * @return the left
     */
    public int getLeft() {
        return ((NSOFInteger) get(SLOT_LEFT)).getValue();
    }

    /**
     * Set the left.
     *
     * @param left the left.
     */
    public void setLeft(int left) {
        put(SLOT_LEFT, new NSOFInteger(left & 0xFF));
    }

    /**
     * Set the left.
     *
     * @param left the left.
     */
    public void setLeft(byte left) {
        setLeft((int) left);
    }

    /**
     * Get the right.
     *
     * @return the right
     */
    public int getRight() {
        return ((NSOFInteger) get(SLOT_RIGHT)).getValue();
    }

    /**
     * Set the right.
     *
     * @param right the right.
     */
    public void setRight(int right) {
        put(SLOT_RIGHT, new NSOFInteger(right & 0xFF));
    }

    /**
     * Set the right.
     *
     * @param right the right.
     */
    public void setRight(byte right) {
        setRight((int) right);
    }

    /**
     * Get the top.
     *
     * @return the top
     */
    public int getTop() {
        return ((NSOFInteger) get(SLOT_TOP)).getValue();
    }

    /**
     * Set the top.
     *
     * @param top the top.
     */
    public void setTop(int top) {
        put(SLOT_TOP, new NSOFInteger(top & 0xFF));
    }

    /**
     * Set the top.
     *
     * @param top the top.
     */
    public void setTop(byte top) {
        setTop((int) top);
    }

    @Override
    public int hashCode() {
        return (getTop() << 24) | (getLeft() << 16) | (getBottom() << 8) | (getRight() << 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof NSOFSmallRect) {
            NSOFSmallRect that = (NSOFSmallRect) obj;
            return (this.getBottom() == that.getBottom()) && (this.getLeft() == that.getLeft()) && (this.getRight() == that.getRight()) && (this.getTop() == that.getTop());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "{top: " + getTop() + ", left: " + getLeft() + ", bottom: " + getBottom() + ", right: " + getRight() + "}";
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFSmallRect(getTop(), getLeft(), getBottom(), getRight());
    }

    @Override
    public NSOFObject put(NSOFSymbol name, NSOFObject value) {
        if (SLOT_BOTTOM.equals(name))
            return super.put(name, value);
        if (SLOT_LEFT.equals(name))
            return super.put(name, value);
        if (SLOT_RIGHT.equals(name))
            return super.put(name, value);
        if (SLOT_TOP.equals(name))
            return super.put(name, value);
        if (SLOT_CLASS.equals(name))
            return super.put(name, value);
        throw new UnsupportedOperationException("invalid slot: " + name);
    }
}
