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
package net.sf.jncu.translate;

import java.io.IOException;

/**
 * Translator exception.
 *
 * @author Moshe
 */
public class TranslationException extends IOException {

    /**
     * Constructs a new exception.
     */
    public TranslationException() {
        super();
    }

    /**
     * Constructs a new exception.
     *
     * @param message the detail message.
     */
    public TranslationException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception.
     *
     * @param cause the cause.
     */
    public TranslationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public TranslationException(String message, Throwable cause) {
        super(message, cause);
    }

}
