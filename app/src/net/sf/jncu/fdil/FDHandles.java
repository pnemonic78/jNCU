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

import java.util.HashMap;
import java.util.Map;

/**
 * FDIL handles manager.
 *
 * @author moshe
 */
public class FDHandles {

    private static FDHandles instance;

    private final Map<FDHandle, NSOFObject> cache = new HashMap<FDHandle, NSOFObject>();

    /**
     * Creates a new handles pool.
     */
    public FDHandles() {
        super();
    }

    /**
     * Get the instance.
     *
     * @return the instance.
     */
    public static FDHandles getInstance() {
        if (instance == null) {
            instance = new FDHandles();
        }
        return instance;
    }

    public void clear() {
        cache.clear();
    }

    /**
     * Get the FDIL object.
     *
     * @param obj the FDIL object handle.
     * @return the FDIL object - {@code null} otherwise.
     */
    public NSOFObject get(FDHandle obj) {
        return cache.get(obj);
    }

    /**
     * Create an FDIL object handle.<br>
     * <em>If another object has the same handle, then it is written over.</em>
     *
     * @param o the FDIL object.
     * @return the handle.
     */
    public FDHandle create(NSOFObject o) {
        FDHandle obj = new FDHandle(o);
        cache.put(obj, o);
        return obj;
    }

    /**
     * Delete the FDIL object handle.
     *
     * @param obj the FDIL object handle.
     */
    public void remove(FDHandle obj) {
        cache.remove(obj);
    }

    /**
     * Find a FDIL handle for an object (that supposedly exists in the cache).
     *
     * @param o the FDIL object.
     * @return the handle - {@code null} otherwise.
     */
    public FDHandle find(NSOFObject o) {
        NSOFObject val;
        for (FDHandle obj : cache.keySet()) {
            val = cache.get(obj);
            if (o == val)
                return obj;
        }
        return null;
    }

    /**
     * Disposes of an object’s allocated memory.
     *
     * @param obj the FDIL object handle.
     * @throws PointerObjectIsFreeException if the FDIL object is invalid.
     */
    public void dispose(FDHandle obj) throws PointerObjectIsFreeException {
        NSOFObject o = get(obj);
        if (o == null)
            throw new PointerObjectIsFreeException();
        remove(obj);
        System.gc();
    }
}
