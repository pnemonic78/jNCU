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
package net.sf.jncu.protocol.v2_0;

import java.util.Map;

import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.v2_0.app.DAppNames;
import net.sf.jncu.protocol.v2_0.app.DLoadPackageFile;
import net.sf.jncu.protocol.v2_0.app.DPackageInfo;
import net.sf.jncu.protocol.v2_0.data.DBackupIDs;
import net.sf.jncu.protocol.v2_0.data.DBackupSoupDone;
import net.sf.jncu.protocol.v2_0.data.DGetRestoreOptions;
import net.sf.jncu.protocol.v2_0.data.DImportParametersSlipResult;
import net.sf.jncu.protocol.v2_0.data.DRestoreAll;
import net.sf.jncu.protocol.v2_0.data.DRestoreFile;
import net.sf.jncu.protocol.v2_0.data.DSetBaseID;
import net.sf.jncu.protocol.v2_0.data.DSoupNotDirty;
import net.sf.jncu.protocol.v2_0.io.DDefaultStore;
import net.sf.jncu.protocol.v2_0.io.DGetDefaultPath;
import net.sf.jncu.protocol.v2_0.io.DGetFileInfo;
import net.sf.jncu.protocol.v2_0.io.DGetFilesAndFolders;
import net.sf.jncu.protocol.v2_0.io.DImportFile;
import net.sf.jncu.protocol.v2_0.io.DInternalStore;
import net.sf.jncu.protocol.v2_0.io.DKeyboardPassthrough;
import net.sf.jncu.protocol.v2_0.io.DRequestToBrowse;
import net.sf.jncu.protocol.v2_0.io.DResolveAlias;
import net.sf.jncu.protocol.v2_0.io.DSetPath;
import net.sf.jncu.protocol.v2_0.io.DSetTranslator;
import net.sf.jncu.protocol.v2_0.io.win.DGetDevices;
import net.sf.jncu.protocol.v2_0.io.win.DGetFilters;
import net.sf.jncu.protocol.v2_0.io.win.DSetDrive;
import net.sf.jncu.protocol.v2_0.io.win.DSetFilter;
import net.sf.jncu.protocol.v2_0.query.DLongData;
import net.sf.jncu.protocol.v2_0.query.DRefResult;
import net.sf.jncu.protocol.v2_0.session.DCallResult;
import net.sf.jncu.protocol.v2_0.session.DInitiateDocking;
import net.sf.jncu.protocol.v2_0.session.DNewtonInfo;
import net.sf.jncu.protocol.v2_0.session.DNewtonName;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.protocol.v2_0.session.DPassword;
import net.sf.jncu.protocol.v2_0.session.DRequestToDock;
import net.sf.jncu.protocol.v2_0.session.DUnknownCommand;
import net.sf.jncu.protocol.v2_0.sync.DSyncOptions;
import net.sf.jncu.protocol.v2_0.sync.DSynchronize;

/**
 * Docking command factory.
 * 
 * @author moshew
 */
public class DockCommandFactory extends net.sf.jncu.protocol.v1_0.DockCommandFactory {

	private static DockCommandFactory instance;

	/**
	 * Creates a new command factory.
	 */
	protected DockCommandFactory() {
		super();
	}

	@Override
	protected void register(Map<String, Class<? extends DockCommand>> registry) {
		super.register(registry);
		registry.put(DInitiateDocking.COMMAND, DInitiateDocking.class);
		registry.put(DNewtonInfo.COMMAND, DNewtonInfo.class);
		registry.put(DNewtonName.COMMAND, DNewtonName.class);
		registry.put(DOperationCanceled.COMMAND, DOperationCanceled.class);
		registry.put(DRequestToDock.COMMAND, DRequestToDock.class);
		registry.put(DOperationCanceled.COMMAND, DOperationCanceled.class);
		registry.put(DOperationCanceledAck.COMMAND, DOperationCanceledAck.class);
		registry.put(DOperationDone.COMMAND, DOperationDone.class);
		registry.put(DPassword.COMMAND, DPassword.class);
		registry.put(DPackageInfo.COMMAND, DPackageInfo.class);
		registry.put(DLoadPackageFile.COMMAND, DLoadPackageFile.class);
		registry.put(DAppNames.COMMAND, DAppNames.class);
		registry.put(DSoupNotDirty.COMMAND, DSoupNotDirty.class);
		registry.put(DSetBaseID.COMMAND, DSetBaseID.class);
		registry.put(DRestoreFile.COMMAND, DRestoreFile.class);
		registry.put(DRestoreAll.COMMAND, DRestoreAll.class);
		registry.put(DImportParametersSlipResult.COMMAND, DImportParametersSlipResult.class);
		registry.put(DGetRestoreOptions.COMMAND, DGetRestoreOptions.class);
		registry.put(DBackupSoupDone.COMMAND, DBackupSoupDone.class);
		registry.put(DBackupIDs.COMMAND, DBackupIDs.class);
		registry.put(DSynchronize.COMMAND, DSynchronize.class);
		registry.put(DSyncOptions.COMMAND, DSyncOptions.class);
		registry.put(DSetTranslator.COMMAND, DSetTranslator.class);
		registry.put(DSetPath.COMMAND, DSetPath.class);
		registry.put(DResolveAlias.COMMAND, DResolveAlias.class);
		registry.put(DRequestToBrowse.COMMAND, DRequestToBrowse.class);
		registry.put(DKeyboardPassthrough.COMMAND, DKeyboardPassthrough.class);
		registry.put(DInternalStore.COMMAND, DInternalStore.class);
		registry.put(DImportFile.COMMAND, DImportFile.class);
		registry.put(DGetFilesAndFolders.COMMAND, DGetFilesAndFolders.class);
		registry.put(DGetFileInfo.COMMAND, DGetFileInfo.class);
		registry.put(DGetDefaultPath.COMMAND, DGetDefaultPath.class);
		registry.put(DDefaultStore.COMMAND, DDefaultStore.class);
		registry.put(DSetFilter.COMMAND, DSetFilter.class);
		registry.put(DGetDevices.COMMAND, DGetDevices.class);
		registry.put(DGetFilters.COMMAND, DGetFilters.class);
		registry.put(DSetDrive.COMMAND, DSetDrive.class);
		registry.put(DLongData.COMMAND, DLongData.class);
		registry.put(DRefResult.COMMAND, DRefResult.class);
		registry.put(DRequestToDock.COMMAND, DRequestToDock.class);
		registry.put(DCallResult.COMMAND, DCallResult.class);
		registry.put(DUnknownCommand.COMMAND, DUnknownCommand.class);
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
