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
package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.JNCUResources;
import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFBoolean;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Application name.
 *
 * @author mwaisberg
 */
public class AppName extends NSOFFrame implements Comparable<AppName> {

    public static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
    public static final NSOFSymbol SLOT_SOUPS = new NSOFSymbol("soups");
    public static final NSOFSymbol SLOT_PACKAGES = new NSOFSymbol("packages");

    /**
     * If there are packages installed, a "Packages" item is listed.
     *
     * @see JNCUResources#getString(String)
     * @see #CLASS_PACKAGES
     */
    public static final String NAME_PACKAGES = "appName.package";
    /**
     * Application name for soups that don't have an associated application,
     * there's an "Other information" entry.
     *
     * @see JNCUResources#getString(String)
     * @see #CLASS_OTHER
     */
    public static final String NAME_OTHER = "appName.other";
    /**
     * "System information" includes the system and directory soups.
     *
     * @see JNCUResources#getString(String)
     * @see #CLASS_SYSTEM
     */
    public static final String NAME_SYSTEM = "appName.system";
    /**
     * If a card is present and has a backup there will be a "Card backup" item.
     *
     * @see JNCUResources#getString(String)
     * @see #CLASS_BACKUP
     */
    public static final String NAME_BACKUP = "appName.backup";

    /**
     * Object class slot to indicate that this array entry is for the packages.
     *
     * @see #NAME_PACKAGES
     */
    public static final NSOFSymbol CLASS_PACKAGES = new NSOFSymbol("packageFrame");
    /**
     * Object class slot to indicate that this array entry is for other
     * information.
     *
     * @see #NAME_OTHER
     */
    public static final NSOFSymbol CLASS_OTHER = new NSOFSymbol("otherFrame");
    /**
     * Object class slot to indicate that this array entry is for the system
     * information.
     *
     * @see #NAME_SYSTEM
     */
    public static final NSOFSymbol CLASS_SYSTEM = new NSOFSymbol("systemFrame");
    /**
     * Object class slot to indicate that this array entry is for the card
     * backup.
     *
     * @see #NAME_BACKUP
     */
    public static final NSOFSymbol CLASS_BACKUP = new NSOFSymbol("backupFrame");

    /**
     * Creates a new name.
     */
    public AppName() {
        super();
    }

    /**
     * Creates a new name.
     *
     * @param frame the source frame.
     */
    public AppName(NSOFFrame frame) {
        super();
        this.putAll(frame);
    }

    /**
     * Creates a new name.
     *
     * @param name the name.
     */
    public AppName(String name) {
        super();
        setName(name);
    }

    /**
     * Get the application name.
     *
     * @return the name - {@code null} otherwise.
     */
    public String getName() {
        NSOFObject o = get(SLOT_NAME);
        if (!NSOFImmediate.isNil(o))
            return ((NSOFString) o).getValue();
        return null;
    }

    /**
     * Set the application name.
     *
     * @param name the name.
     */
    public void setName(String name) {
        put(SLOT_NAME, new NSOFString(name));
    }

    /**
     * Get the application soup names.
     *
     * @return the array of {@code NSOFString} names - {@code null} otherwise.
     */
    public NSOFArray getSoups() {
        NSOFObject o = get(SLOT_SOUPS);
        if (!NSOFImmediate.isNil(o))
            return (NSOFArray) o;
        return null;
    }

    /**
     * Are packages installed?
     *
     * @return {@code true} if there are packages installed.
     */
    public boolean hasPackages() {
        NSOFObject o = get(SLOT_PACKAGES);
        if (!NSOFImmediate.isNil(o))
            return ((NSOFImmediate) o).isTrue();
        return false;
    }

    /**
     * Set installed packages.
     *
     * @param packages {@code true} if there are packages installed.
     */
    public void setPackages(boolean packages) {
        put(SLOT_PACKAGES, NSOFBoolean.valueOf(packages));
    }

    @Override
    public int compareTo(AppName that) {
        String thisName = this.getName();
        String thatName = that.getName();
        if (thisName == null) {
            if (thatName != null) {
                return -1;
            }
        } else if (thatName == null) {
            return 1;
        }
        int c = thisName.compareTo(thatName);
        if (c != 0)
            return c;
        NSOFArray thisSoups = this.getSoups();
        NSOFArray thatSoups = that.getSoups();
        if (thisSoups == null) {
            if (thatSoups != null) {
                return -1;
            }
        } else if (thatSoups == null) {
            return 1;
        } else {
            c = thisSoups.length() - thatSoups.length();
        }
        return c;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof AppName) {
            AppName that = (AppName) obj;
            return compareTo(that) == 0;
        }
        return super.equals(obj);
    }
}
