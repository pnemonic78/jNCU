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
package net.sf.util.zip;

import java.util.zip.ZipException;

/**
 * There was a problem with the checksum.
 *
 * @author mwaisberg
 */
public class ChecksumException extends ZipException {

    /**
     * Creates a new checksum exception.
     */
    public ChecksumException() {
    }

    /**
     * Creates a new checksum exception.
     *
     * @param s the message.
     */
    public ChecksumException(String s) {
        super(s);
    }

}
