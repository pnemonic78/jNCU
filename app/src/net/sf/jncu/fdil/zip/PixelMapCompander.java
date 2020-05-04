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
package net.sf.jncu.fdil.zip;

/**
 * A compressor-expander (compander) specialized for pixel map data. (A bitmap
 * is a pixel map having a bit depth of 1.) This compander assumes that the data
 * in the VBO is a pixel map and that the pixel map data is 32-bit aligned; that
 * is, the length of the rows in the pixel map is an even multiple of 4 bytes.
 *
 * @author mwaisberg
 */
public class PixelMapCompander extends StoreCompander {

    /**
     * Creates a new compander.
     */
    public PixelMapCompander() {
    }

}
