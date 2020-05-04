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

/**
 * Unknown stream version error.<br>
 * <tt>kFD_UnknownStreamVersion (kFD_ErrorBase - 1)</tt>
 *
 * @author Moshe
 */
public class UnknownStreamVersionException extends IllegalArgumentException {

    public UnknownStreamVersionException() {
        super();
    }

    public UnknownStreamVersionException(String message) {
        super(message);
    }

    public UnknownStreamVersionException(Throwable cause) {
        super(cause);
    }

    public UnknownStreamVersionException(String message, Throwable cause) {
        super(message, cause);
    }

}
