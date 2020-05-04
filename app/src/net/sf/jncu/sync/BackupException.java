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
package net.sf.jncu.sync;

import java.io.IOException;

/**
 * Thrown to indicate that a backup operation could not complete.
 *
 * @author Moshe
 */
public class BackupException extends IOException {

    /**
     * Constructs a new exception.
     */
    public BackupException() {
        super();
    }

    /**
     * Constructs a new exception.
     *
     * @param message the detail message.
     */
    public BackupException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception.
     *
     * @param cause the cause.
     */
    public BackupException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public BackupException(String message, Throwable cause) {
        super(message, cause);
    }

}
