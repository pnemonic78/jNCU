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

import net.sf.jncu.protocol.DockCommandToNewtonLong;
import net.sf.jncu.util.NewtonDateUtils;

/**
 * The time of the last sync.
 *
 * <pre>
 * 'stme'
 * length
 * time in minutes
 * </pre>
 *
 * @author moshew
 */
public class DLastSyncTime extends DockCommandToNewtonLong {

    /**
     * <tt>kDLastSyncTime</tt>
     */
    public static final String COMMAND = "stme";

    /**
     * Creates a new command.
     */
    public DLastSyncTime() {
        this(0);
    }

    /**
     * Creates a new command.
     *
     * @param time the time in milliseconds.
     */
    public DLastSyncTime(long time) {
        super(COMMAND);
        setLastSyncTime(time);
    }

    /**
     * Get time of the last sync.
     *
     * @return the time in milliseconds.
     */
    public long getLastSyncTime() {
        return NewtonDateUtils.fromMinutes(getValue());
    }

    /**
     * Set time of the last sync.
     *
     * @param time the time in milliseconds.
     */
    public void setLastSyncTime(long time) {
        setValue(NewtonDateUtils.getMinutes(time));
    }
}
