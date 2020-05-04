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
