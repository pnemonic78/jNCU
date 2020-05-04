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
import net.sf.jncu.fdil.NSOFNil;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.util.NewtonDateUtils;

/**
 * Soup entry.
 *
 * @author moshe
 */
public class SoupEntry extends NSOFFrame implements Comparable<SoupEntry> {

    public static final NSOFSymbol SLOT_ID = new NSOFSymbol("_uniqueID");
    public static final NSOFSymbol SLOT_MODIFIED = new NSOFSymbol("_modTime");

    /**
     * Creates a new entry.
     */
    public SoupEntry() {
        super();
        init();
    }

    /**
     * Creates a new entry.
     *
     * @param frame the source entry frame.
     */
    public SoupEntry(NSOFFrame frame) {
        super();
        init();
        this.putAll(frame);
    }

    private void init() {
        put(SLOT_ID, NSOFNil.NIL);
        put(SLOT_MODIFIED, NewtonDateUtils.toMinutes(System.currentTimeMillis()));
    }

    /**
     * Get the entry ID.
     *
     * @return the unique ID.
     */
    public int getId() {
        return ((NSOFImmediate) get(SLOT_ID)).getValue();
    }

    /**
     * Set the entry ID.
     *
     * @param id the unique ID.
     */
    public void setId(int id) {
        put(SLOT_ID, new NSOFInteger(id));
    }

    /**
     * Get the modified time.
     *
     * @return the time in milliseconds.
     */
    public long getModifiedTime() {
        NSOFImmediate imm = (NSOFImmediate) get(SLOT_MODIFIED);
        return (imm == null) ? 0 : NewtonDateUtils.fromMinutes(imm.getValue());
    }

    /**
     * Set the modified time.
     *
     * @param time the time in milliseconds.
     */
    public void setModifiedTime(long time) {
        put(SLOT_MODIFIED, NewtonDateUtils.toMinutes(time));
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public int compareTo(SoupEntry that) {
        return this.getId() - that.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof SoupEntry) {
            return compareTo((SoupEntry) obj) == 0;
        }
        return super.equals(obj);
    }
}
