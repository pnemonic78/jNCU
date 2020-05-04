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
 * FDIL not initialized error.<br>
 * <tt>kFD_FDILNotInitialized (kFD_ErrorBase - 19)</tt>
 *
 * @author moshe
 */
public class FDILNotInitializedException extends FDILException {

    public FDILNotInitializedException() {
        super();
    }

    /**
     * @param message
     */
    public FDILNotInitializedException(String message) {
        super(message);
    }

    public FDILNotInitializedException(Throwable cause) {
        super(cause);
    }

    public FDILNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

}
