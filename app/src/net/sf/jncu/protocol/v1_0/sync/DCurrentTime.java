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
package net.sf.jncu.protocol.v1_0.sync;

import net.sf.jncu.protocol.BaseDockCommandFromNewton;

import java.io.IOException;
import java.io.InputStream;

/**
 * The current time on the Newton.<br>
 * The time granularity is minutes.
 * <p>
 * In response to a <tt>kDLastSyncTime</tt> command.
 *
 * <pre>
 * 'time'
 * length = 4
 * time
 * </pre>
 *
 * @author moshew
 */
public class DCurrentTime extends BaseDockCommandFromNewton {

    /**
     * <tt>kDCurrentTime</tt>
     */
    public static final String COMMAND = "time";

    private int time;

    /**
     * Creates a new command.
     */
    public DCurrentTime() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setTime(ntohl(data));
    }

    /**
     * Set the time.
     *
     * @param time the time.
     */
    protected void setTime(int time) {
        this.time = time;
    }

    /**
     * Get the time.
     *
     * @return the time.
     */
    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return super.toString() + ":" + getTime();
    }
}
