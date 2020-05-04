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
package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This commands sets the signature of the current store to the specified value.
 * A <tt>kDResult</tt> with value {@code 0} (or the error value if an error
 * occurred) is sent to the desktop in response.
 *
 * <pre>
 * 'ssig'
 * length
 * new signature
 * </pre>
 *
 * @author moshew
 */
public class DSetStoreSignature extends DockCommandToNewtonLong {

    /**
     * <tt>kDSetStoreSignature</tt>
     */
    public static final String COMMAND = "ssig";

    /**
     * Creates a new command.
     */
    public DSetStoreSignature() {
        super(COMMAND);
    }

    /**
     * Get the soup signature.
     *
     * @return the signature.
     */
    public int getSignature() {
        return getValue();
    }

    /**
     * Set the soup signature.
     *
     * @param signature the signature.
     */
    public void setSignature(int signature) {
        setValue(signature);
    }
}
