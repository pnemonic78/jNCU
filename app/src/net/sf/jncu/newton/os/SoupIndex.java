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
package net.sf.jncu.newton.os;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Soup index.
 * <code>{structure='slot, index=159384445, path='mtgStartDate, type='int}</code>
 *
 * @author mwaisberg
 */
public class SoupIndex {

    public static final NSOFSymbol SLOT_STRUCTURE = new NSOFSymbol("structure");
    public static final NSOFSymbol SLOT_INDEX = new NSOFSymbol("index");
    public static final NSOFSymbol SLOT_PATH = new NSOFSymbol("path");
    public static final NSOFSymbol SLOT_TYPE = new NSOFSymbol("type");

    private final NSOFFrame frame = new NSOFFrame();

    /**
     * Creates a new soup index.
     */
    public SoupIndex() {
        super();
    }

    /**
     * Decode the index frame.
     *
     * @param frame the frame.
     */
    public void fromFrame(NSOFFrame frame) {
        this.frame.putAll(frame);
    }

    /**
     * Get the index frame.
     *
     * @return the frame.
     */
    public NSOFFrame toFrame() {
        return frame;
    }

    /**
     * Get the index structure.
     *
     * @return the structure.
     */
    public NSOFSymbol getStructure() {
        return (NSOFSymbol) frame.get(SLOT_STRUCTURE);
    }

    /**
     * Set the index structure.
     *
     * @param structure the structure.
     */
    public void setStructure(NSOFSymbol structure) {
        frame.put(SLOT_STRUCTURE, structure);
    }

    /**
     * Get the index structure.
     *
     * @return the structure.
     */
    public int getIndex() {
        NSOFImmediate imm = (NSOFImmediate) frame.get(SLOT_INDEX);
        if (imm != null) {
            return imm.getValue();
        }
        return 0;
    }

    /**
     * Set the index.
     *
     * @param index the index.
     */
    public void setIndex(int index) {
        setIndex(new NSOFInteger(index));
    }

    /**
     * Set the index.
     *
     * @param index the index.
     */
    public void setIndex(NSOFInteger index) {
        frame.put(SLOT_INDEX, index);
    }

    /**
     * Get the index path.
     *
     * @return the path.
     */
    public NSOFSymbol getPath() {
        return (NSOFSymbol) frame.get(SLOT_PATH);
    }

    /**
     * Set the index path.
     *
     * @param path the path.
     */
    public void setPath(NSOFSymbol path) {
        frame.put(SLOT_PATH, path);
    }

    /**
     * Get the index type.
     *
     * @return the type.
     */
    public NSOFSymbol getType() {
        return (NSOFSymbol) frame.get(SLOT_TYPE);
    }

    /**
     * Set the index type.
     *
     * @param type the type.
     */
    public void setType(NSOFSymbol type) {
        frame.put(SLOT_TYPE, type);
    }
}
