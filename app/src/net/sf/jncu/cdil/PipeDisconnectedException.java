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
package net.sf.jncu.cdil;

import java.io.IOException;

/**
 * Pipe disconnected - <tt>kCD_PipeDisconnected</tt>.
 *
 * @author moshew
 */
public class PipeDisconnectedException extends IOException {

    public PipeDisconnectedException() {
        super();
    }

    public PipeDisconnectedException(String message) {
        super(message);
    }

    public PipeDisconnectedException(Throwable cause) {
        super(cause);
    }

}
