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

import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

/**
 * Timeout when listening to Newton.
 *
 * @author moshew
 */
public class CDTimeout extends TimerTask {

    private final CDPipe pipe;

    /**
     * Creates a new timeout.
     *
     * @param pipe the pipe.
     */
    public CDTimeout(CDPipe pipe) {
        super();
        if (pipe == null)
            throw new NullPointerException("pipe required");
        this.pipe = pipe;
    }

    @Override
    public void run() {
        pipe.notifyTimeout(new TimeoutException());
    }

}
