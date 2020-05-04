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
package net.sf.jncu.protocol.v2_0.sync;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;

/**
 * This command returns information about what was imported into the Newton.
 * Each array element specifies a soup and how many entries were added to it.
 * There will typically be only one frame in the array. The frame will look like
 * this:<br>
 * <code>[{soupName: "Notes", count: 7}, {soupName: "Names", count: 3}]</code>
 *
 * <pre>
 * 'schg'
 * length
 * array
 * </pre>
 *
 * @author moshew
 */
public class DSoupsChanged extends BaseDockCommandToNewton {

    /**
     * <tt>kDSoupsChanged</tt>
     */
    public static final String COMMAND = "schg";

    private final Set<SoupChanged> soups = new TreeSet<SoupChanged>();

    /**
     * Constructs a new command.
     */
    public DSoupsChanged() {
        super(COMMAND);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        NSOFObject[] items = new NSOFObject[soups.size()];
        int i = 0;
        for (SoupChanged soup : soups) {
            items[i++] = soup.toFrame();
        }
        NSOFArray arr = new NSOFPlainArray(items);
        NSOFEncoder encoder = new NSOFEncoder();
        encoder.flatten(arr, data);
    }

    /**
     * Add a changed soup.
     *
     * @param soup the soup.
     */
    public void addSoup(SoupChanged soup) {
        soups.add(soup);
    }

    /**
     * Create a changed soup.
     *
     * @param soupName the soup name.
     * @param count    the count.
     * @return the soup.
     */
    public static SoupChanged createSoup(String soupName, int count) {
        SoupChanged soup = new SoupChanged();
        soup.setSoupName(soupName);
        soup.setCount(count);
        return soup;
    }

    /**
     * Soup that has changed.
     */
    public static class SoupChanged implements Comparable<SoupChanged> {

        public static final NSOFSymbol SLOT_SOUP = new NSOFSymbol("soupName");
        public static final NSOFSymbol SLOT_COUNT = new NSOFSymbol("count");

        private String soupName;
        private int count;

        /**
         * Creates a new soup frame.
         */
        protected SoupChanged() {
            super();
        }

        /**
         * Get the soup name.
         *
         * @return the soup name.
         */
        public String getSoupName() {
            return soupName;
        }

        /**
         * Set the soup name.
         *
         * @param soupName the soup name.
         */
        public void setSoupName(String soupName) {
            this.soupName = soupName;
        }

        /**
         * Get the count.
         *
         * @return the count.
         */
        public int getCount() {
            return count;
        }

        /**
         * Set the count.
         *
         * @param count the count.
         */
        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public int hashCode() {
            return getSoupName().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            SoupChanged that = (SoupChanged) obj;
            return compareTo(that) == 0;
        }

        @Override
        public int compareTo(SoupChanged that) {
            if (this == that) {
                return 0;
            }
            if (that == null) {
                return 1;
            }
            return this.getSoupName().compareTo(that.getSoupName());
        }

        /**
         * Get the frame.
         *
         * @return the frame.
         */
        public NSOFFrame toFrame() {
            NSOFFrame frame = new NSOFFrame();
            frame.put(SLOT_SOUP, new NSOFString(getSoupName()));
            frame.put(SLOT_COUNT, new NSOFInteger(getCount()));
            return frame;
        }
    }
}
