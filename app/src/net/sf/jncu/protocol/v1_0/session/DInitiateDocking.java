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
package net.sf.jncu.protocol.v1_0.session;

import net.sf.jncu.protocol.DockCommandToNewtonLong;

/**
 * This command is sent to the newt in response to a <tt>kDRequestToDock</tt>
 * command. Session type can be one of
 * <tt>{none, settingUp, synchronize, restore, loadPackage, testComm, loadPatch, updatingStores}</tt>
 *
 * <pre>
 * 'dock'
 * length
 * session type
 * </pre>
 */
public class DInitiateDocking extends DockCommandToNewtonLong {

    /**
     * <tt>kDInitiateDocking</tt>
     */
    public static final String COMMAND = "dock";

    /**
     * <tt>kNoSession</tt>
     */
    public static final int SESSION_NONE = 0;
    /**
     * <tt>kSettingUpSession</tt>
     */
    public static final int SESSION_SETTING_UP = 1;
    /**
     * <tt>kSynchronizeSession</tt>
     */
    public static final int SESSION_SYNCHRONIZE = 2;
    /**
     * <tt>kRestoreSession</tt>
     */
    public static final int SESSION_RESTORE = 3;
    /**
     * <tt>kLoadPackageSession</tt>
     */
    public static final int SESSION_LOAD_PACKAGE = 4;
    /**
     * <tt>kTestCommSession</tt>
     */
    public static final int SESSION_TEST_COMM = 5;
    /**
     * <tt>kLoadPatchSession</tt>
     */
    public static final int SESSION_LOAD_PATCH = 6;
    /**
     * <tt>kUpdatingStoresSession</tt>
     */
    public static final int SESSION_UPDATING_STORES = 7;

    /**
     * Creates a new command.
     */
    public DInitiateDocking() {
        super(COMMAND);
        setSession(SESSION_NONE);
    }

    /**
     * Get the session type.
     *
     * @return the session.
     */
    public int getSession() {
        return getValue();
    }

    /**
     * Set the session type.
     *
     * @param session the session.
     */
    public void setSession(int session) {
        setValue(session);
    }
}
