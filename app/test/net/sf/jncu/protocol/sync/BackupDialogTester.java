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
package net.sf.jncu.protocol.sync;

import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.v2_0.app.AppName;
import net.sf.jncu.protocol.v2_0.sync.BackupDialog;
import net.sf.jncu.protocol.v2_0.sync.SyncOptions;

import java.util.ArrayList;
import java.util.List;

public class BackupDialogTester {

    public static void main(String[] args) {
        BackupDialog dialog = new BackupDialog();

        List<Store> stores = new ArrayList<Store>();
        stores.add(new Store("Internal"));
        stores.add(new Store("Store #2"));
        stores.add(new Store("Store #3"));
        stores.add(new Store("Store #4"));
        stores.add(new Store("Store #5"));
        stores.add(new Store("Store #6"));
        stores.add(new Store("Store #7"));
        stores.add(new Store("Store #8"));
        stores.add(new Store("Store #9"));
        stores.add(new Store("Store #10"));
        stores.add(new Store("Store #11"));
        stores.add(new Store("Store #12"));
        dialog.setStores(stores);

        List<AppName> apps = new ArrayList<AppName>();
        AppName app;
        apps.add(new AppName(AppName.NAME_BACKUP));
        apps.add(new AppName(AppName.NAME_OTHER));
        app = new AppName(AppName.NAME_PACKAGES);
        app.put(AppName.SLOT_SOUPS, new NSOFPlainArray(new NSOFString[]{new NSOFString("Packages")}));
        apps.add(app);
        apps.add(new AppName(AppName.NAME_SYSTEM));
        dialog.setApplications(apps);

        dialog.setVisible(true);

        SyncOptions options = dialog.getSyncOptions();
        System.out.println("options=" + ((options == null) ? null : options.toFrame()));
    }
}
