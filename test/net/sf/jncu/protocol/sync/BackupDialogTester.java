/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.protocol.sync;

import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.v2_0.app.AppName;
import net.sf.jncu.protocol.v2_0.sync.BackupDialog;
import net.sf.jncu.protocol.v2_0.sync.SyncOptions;

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
		app.put(AppName.SLOT_SOUPS, new NSOFPlainArray(new NSOFString[] { new NSOFString("Packages") }));
		apps.add(app);
		apps.add(new AppName(AppName.NAME_SYSTEM));
		dialog.setApplications(apps);

		dialog.setVisible(true);

		SyncOptions options = dialog.getSyncOptions();
		System.out.println("options=" + ((options == null) ? null : options.toFrame()));
	}
}
