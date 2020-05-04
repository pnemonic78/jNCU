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
package net.sf.jncu.dil;

/**
 * DIL Error.<br>
 * <tt>DIL_Error</tt><br>
 * <tt>kDIL_ErrorBase</tt>
 *
 * @author moshew
 */
public class DILException extends Exception {

    /**
     * Creates a new DIL exception.
     */
    public DILException() {
        super();
    }

    /**
     * Creates a new DIL exception.
     *
     * @param message the detail message.
     */
    public DILException(String message) {
        super(message);
    }

    /**
     * Creates a new DIL exception.
     *
     * @param cause the cause.
     */
    public DILException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new DIL exception.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public DILException(String message, Throwable cause) {
        super(message, cause);
    }

}
