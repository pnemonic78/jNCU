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

import java.nio.charset.UnsupportedCharsetException;

/**
 * Illegal character in symbol error.<br>
 * <tt>kFD_IllegalCharInSymbol (kFD_ErrorBase - 45)</tt>
 *
 * @author moshe
 */
public class IllegalCharInSymbolException extends UnsupportedCharsetException {

    private final char c;

    public IllegalCharInSymbolException(char c) {
        super("ASCII");
        this.c = c;
    }

    public char getCharacter() {
        return c;
    }

    @Override
    public String getMessage() {
        return "Bad character = 0x" + Integer.toHexString(c);
    }
}
