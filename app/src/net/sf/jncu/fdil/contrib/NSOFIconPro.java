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
package net.sf.jncu.fdil.contrib;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Newton Streamed Object Format - "Professional" icon object.
 * <p>
 * <tt>{<br>
 * &nbsp;&nbsp;unhilited={<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;bounds={top: 0, left: 0, bottom: 24, right: 27},<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;colordata={bitdepth=1, cbits='bits},<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;bits='bits<br>
 * &nbsp;&nbsp;},<br>
 * &nbsp;&nbsp;hilited={<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;bounds={top: 0, left: 0, bottom: 24, right: 27},<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;bits='bits}<br>
 * }</tt>
 *
 * @author mwaisberg
 */
public class NSOFIconPro extends NSOFFrame {

    /**
     * Default icon class.
     */
    public static final NSOFSymbol CLASS_ICON_PRO = new NSOFSymbol("iconPro");

    public static final NSOFSymbol SLOT_UNHILITED = new NSOFSymbol("unhilited");
    public static final NSOFSymbol SLOT_HILITED = new NSOFSymbol("hilited");

    /**
     * Creates a new icon.
     */
    public NSOFIconPro() {
        super();
        setObjectClass(CLASS_ICON_PRO);
    }

    /**
     * Get the unhighlighted icon.
     *
     * @return the icon - {@code null} otherwise.
     */
    public NSOFIcon getUnhilited() {
        NSOFObject slot = get(SLOT_UNHILITED);
        if (NSOFImmediate.isNil(slot))
            return null;
        NSOFFrame frame = (NSOFFrame) slot;
        if (frame instanceof NSOFIcon) {
            return (NSOFIcon) frame;
        }
        NSOFIcon icon = new NSOFIcon();
        icon.putAll(frame);
        setUnhilited(icon);
        return icon;
    }

    /**
     * Set the unhighlighted icon.
     *
     * @param icon the icon.
     */
    public void setUnhilited(NSOFIcon icon) {
        put(SLOT_UNHILITED, icon);
    }

    /**
     * Get the highlighted icon.
     *
     * @return the icon - {@code null} otherwise.
     */
    public NSOFIcon getHilited() {
        NSOFObject slot = get(SLOT_HILITED);
        if (NSOFImmediate.isNil(slot))
            return null;
        NSOFFrame frame = (NSOFFrame) slot;
        if (frame instanceof NSOFIcon) {
            return (NSOFIcon) frame;
        }
        NSOFIcon icon = new NSOFIcon();
        icon.putAll(frame);
        setHilited(icon);
        return icon;
    }

    /**
     * Set the highlighted icon.
     *
     * @param icon the icon.
     */
    public void setHilited(NSOFIcon icon) {
        put(SLOT_HILITED, icon);
    }
}
