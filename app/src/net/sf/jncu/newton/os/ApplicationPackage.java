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

import net.sf.jncu.fdil.NSOFBinaryObject;

/**
 * Application package information.
 *
 * @author moshew
 */
public class ApplicationPackage {

    private String name;
    private NSOFBinaryObject binary;

    /**
     * Creates a new package.
     */
    public ApplicationPackage() {
        super();
    }

    /**
     * Creates a new package.
     *
     * @param name the name.
     */
    public ApplicationPackage(String name) {
        super();
        this.name = name;
    }

    /**
     * Get the package name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the package binary.
     *
     * @param binary the binary object.
     */
    public void setBinary(NSOFBinaryObject binary) {
        this.binary = binary;
    }

    /**
     * Get the package binary.
     *
     * @return the binary object.
     */
    public NSOFBinaryObject getBinary() {
        return binary;
    }
}
