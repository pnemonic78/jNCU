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
package net.sf.jncu.protocol.v2_1;

import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.DockCommandToNewton;
import net.sf.jncu.protocol.v2_1.session.DSetStatusText;

import java.util.Map;

/**
 * Docking command factory.
 *
 * @author moshew
 */
public class DockCommandFactory extends net.sf.jncu.protocol.v2_0.DockCommandFactory {

    private static DockCommandFactory instance;

    /**
     * Creates a new command factory.
     */
    protected DockCommandFactory() {
        super();
    }

    @Override
    protected void registerFrom(Map<String, Class<? extends DockCommandFromNewton>> registry) {
        super.registerFrom(registry);
    }

    @Override
    protected void registerTo(Map<String, Class<? extends DockCommandToNewton>> registry) {
        super.registerTo(registry);
        registry.put(DSetStatusText.COMMAND, DSetStatusText.class);
    }

    /**
     * Get the factory instance.
     *
     * @return the factory.
     */
    public static DockCommandFactory getInstance() {
        if (instance == null) {
            instance = new DockCommandFactory();
        }
        return instance;
    }

}
