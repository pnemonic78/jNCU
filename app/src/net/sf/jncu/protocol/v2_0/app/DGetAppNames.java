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
package net.sf.jncu.protocol.v2_0.app;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command asks the Newton to send information about the applications
 * installed on the Newton. See the <tt>kDAppNames</tt> description above for
 * details of the information returned. The <tt>return what</tt> parameter
 * determines what information is returned. Here are the choices:
 * <ul>
 * <li>0: return names and soups for all stores
 * <li>1: return names and soups for current store
 * <li>2: return just names for all stores
 * <li>3: return just names for current store
 * </ul>
 *
 * <pre>
 * 'gapp'
 * length
 * return what
 * </pre>
 *
 * @author Moshe
 * @see DAppNames
 */
public class DGetAppNames extends DockCommandToNewtonLong {

    /**
     * <tt>kDGetAppNames</tt>
     */
    public static final String COMMAND = "gapp";

    /**
     * Return names and soups for all stores.<br>
     * <tt>kNamesAndSoupsForAllStores</tt>
     */
    public static final int ALL_STORES_NAMES_SOUPS = 0;
    /**
     * Return names and soups for current store.<br>
     * <tt>kNamesAndSoupsForThisStore</tt>
     */
    public static final int CURRENT_STORE_NAMES_SOUPS = 1;
    /**
     * Return just names for all stores.<br>
     * <tt>kNamesForAllStores</tt>
     */
    public static final int ALL_STORES_NAMES = 2;
    /**
     * Return just names for current store.<br>
     * <tt>kNamesForThisStore</tt>
     */
    public static final int CURRENT_STORE_NAMES = 3;

    /**
     * Constructs a new command.
     */
    public DGetAppNames() {
        super(COMMAND);
        setWhat(ALL_STORES_NAMES_SOUPS);
    }

    /**
     * Set what to return.
     *
     * @param what return what?
     */
    public void setWhat(int what) {
        setValue(what);
    }

    /**
     * Get what to return.
     *
     * @return what?
     */
    public int getWhat() {
        return getValue();
    }
}
