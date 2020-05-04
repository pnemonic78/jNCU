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

import java.util.TimerTask;

/**
 * @author Moshe
 */
public class KeyboardTask extends TimerTask {

    private final KeyboardInput input;

    /**
     * Constructs a new task.
     *
     * @param input the owner input.
     */
    public KeyboardTask(KeyboardInput input) {
        super();
        if (input == null)
            throw new IllegalArgumentException("(Keyboard input required");
        this.input = input;
    }

    @Override
    public void run() {
        // Write the buffer (either char or string) to sender.
        input.flush();
    }

}
