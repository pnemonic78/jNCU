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

import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v2_0.app.DAppNames;
import net.sf.jncu.protocol.v2_0.app.DGetAppNames;
import net.sf.jncu.protocol.v2_0.app.DGetPackageInfo;
import net.sf.jncu.protocol.v2_0.app.DLoadPackageFile;
import net.sf.jncu.protocol.v2_0.app.DPackageInfo;
import net.sf.jncu.protocol.v2_0.app.DRemovePackage;
import net.sf.jncu.protocol.v2_0.app.DRequestToInstall;
import net.sf.jncu.protocol.v2_0.app.DRestorePackage;
import net.sf.jncu.protocol.v2_0.data.DAddEntryWithUniqueID;
import net.sf.jncu.protocol.v2_0.data.DBackupIDs;
import net.sf.jncu.protocol.v2_0.data.DBackupSoup;
import net.sf.jncu.protocol.v2_0.data.DBackupSoupDone;
import net.sf.jncu.protocol.v2_0.data.DCreateDefaultSoup;
import net.sf.jncu.protocol.v2_0.data.DGetRestoreOptions;
import net.sf.jncu.protocol.v2_0.data.DImportFile;
import net.sf.jncu.protocol.v2_0.data.DImportParametersSlip;
import net.sf.jncu.protocol.v2_0.data.DImportParametersSlipResult;
import net.sf.jncu.protocol.v2_0.data.DImporting;
import net.sf.jncu.protocol.v2_0.data.DNewtonTICommand;
import net.sf.jncu.protocol.v2_0.data.DRequestToRestore;
import net.sf.jncu.protocol.v2_0.data.DRestoreAll;
import net.sf.jncu.protocol.v2_0.data.DRestoreFile;
import net.sf.jncu.protocol.v2_0.data.DRestoreOptions;
import net.sf.jncu.protocol.v2_0.data.DRestorePatch;
import net.sf.jncu.protocol.v2_0.data.DSendSoup;
import net.sf.jncu.protocol.v2_0.data.DSetBaseID;
import net.sf.jncu.protocol.v2_0.data.DSetSoupGetInfo;
import net.sf.jncu.protocol.v2_0.data.DSetSoupSignature;
import net.sf.jncu.protocol.v2_0.data.DSetTranslator;
import net.sf.jncu.protocol.v2_0.data.DSoupNotDirty;
import net.sf.jncu.protocol.v2_0.data.DSourceVersion;
import net.sf.jncu.protocol.v2_0.data.DTranslatorList;
import net.sf.jncu.protocol.v2_0.io.DAliasResolved;
import net.sf.jncu.protocol.v2_0.io.DDefaultStore;
import net.sf.jncu.protocol.v2_0.io.DFileInfo;
import net.sf.jncu.protocol.v2_0.io.DFilesAndFolders;
import net.sf.jncu.protocol.v2_0.io.DGetDefaultPath;
import net.sf.jncu.protocol.v2_0.io.DGetDefaultStore;
import net.sf.jncu.protocol.v2_0.io.DGetFileInfo;
import net.sf.jncu.protocol.v2_0.io.DGetFilesAndFolders;
import net.sf.jncu.protocol.v2_0.io.DGetInternalStore;
import net.sf.jncu.protocol.v2_0.io.DInternalStore;
import net.sf.jncu.protocol.v2_0.io.DKeyboardChar;
import net.sf.jncu.protocol.v2_0.io.DKeyboardPassthrough;
import net.sf.jncu.protocol.v2_0.io.DKeyboardString;
import net.sf.jncu.protocol.v2_0.io.DPath;
import net.sf.jncu.protocol.v2_0.io.DRequestToBrowse;
import net.sf.jncu.protocol.v2_0.io.DResolveAlias;
import net.sf.jncu.protocol.v2_0.io.DSetPath;
import net.sf.jncu.protocol.v2_0.io.DSetStoreGetNames;
import net.sf.jncu.protocol.v2_0.io.DSetStoreName;
import net.sf.jncu.protocol.v2_0.io.DSetStoreSignature;
import net.sf.jncu.protocol.v2_0.io.DSetStoreToDefault;
import net.sf.jncu.protocol.v2_0.io.win.DDevices;
import net.sf.jncu.protocol.v2_0.io.win.DFilters;
import net.sf.jncu.protocol.v2_0.io.win.DGetDevices;
import net.sf.jncu.protocol.v2_0.io.win.DGetFilters;
import net.sf.jncu.protocol.v2_0.io.win.DSetDrive;
import net.sf.jncu.protocol.v2_0.io.win.DSetFilter;
import net.sf.jncu.protocol.v2_0.query.DCursorCountEntries;
import net.sf.jncu.protocol.v2_0.query.DCursorEntry;
import net.sf.jncu.protocol.v2_0.query.DCursorFree;
import net.sf.jncu.protocol.v2_0.query.DCursorGotoKey;
import net.sf.jncu.protocol.v2_0.query.DCursorMap;
import net.sf.jncu.protocol.v2_0.query.DCursorMove;
import net.sf.jncu.protocol.v2_0.query.DCursorNext;
import net.sf.jncu.protocol.v2_0.query.DCursorPrev;
import net.sf.jncu.protocol.v2_0.query.DCursorReset;
import net.sf.jncu.protocol.v2_0.query.DCursorResetToEnd;
import net.sf.jncu.protocol.v2_0.query.DCursorWhichEnd;
import net.sf.jncu.protocol.v2_0.query.DLongData;
import net.sf.jncu.protocol.v2_0.query.DQuery;
import net.sf.jncu.protocol.v2_0.query.DRefResult;
import net.sf.jncu.protocol.v2_0.session.DCallGlobalFunction;
import net.sf.jncu.protocol.v2_0.session.DCallResult;
import net.sf.jncu.protocol.v2_0.session.DCallRootMethod;
import net.sf.jncu.protocol.v2_0.session.DDesktopInfo;
import net.sf.jncu.protocol.v2_0.session.DGetPassword;
import net.sf.jncu.protocol.v2_0.session.DInitiateDocking;
import net.sf.jncu.protocol.v2_0.session.DNewtonInfo;
import net.sf.jncu.protocol.v2_0.session.DNewtonName;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.protocol.v2_0.session.DPassword;
import net.sf.jncu.protocol.v2_0.session.DRefTest;
import net.sf.jncu.protocol.v2_0.session.DRegProtocolExtension;
import net.sf.jncu.protocol.v2_0.session.DRemoveProtocolExtension;
import net.sf.jncu.protocol.v2_0.session.DRequestToDock;
import net.sf.jncu.protocol.v2_0.session.DResultString;
import net.sf.jncu.protocol.v2_0.session.DSetTimeout;
import net.sf.jncu.protocol.v2_0.session.DSetVBOCompression;
import net.sf.jncu.protocol.v2_0.session.DShowProgress;
import net.sf.jncu.protocol.v2_0.session.DUnknownCommand;
import net.sf.jncu.protocol.v2_0.session.DWhichIcons;
import net.sf.jncu.protocol.v2_0.sync.DGetChangedIndex;
import net.sf.jncu.protocol.v2_0.sync.DGetChangedInfo;
import net.sf.jncu.protocol.v2_0.sync.DGetSyncOptions;
import net.sf.jncu.protocol.v2_0.sync.DRequestToSync;
import net.sf.jncu.protocol.v2_0.sync.DSoupsChanged;
import net.sf.jncu.protocol.v2_0.sync.DSyncOptions;
import net.sf.jncu.protocol.v2_0.sync.DSyncResults;
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

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.v1_0.DockCommandFactory#registerFrom(java.util.Map)
	 */
	@Override
	protected void registerFrom(Map<String, Class<? extends IDockCommandFromNewton>> registry) {
		super.registerFrom(registry);
		registry.put(DNewtonInfo.COMMAND, DNewtonInfo.class);
		registry.put(DNewtonName.COMMAND, DNewtonName.class);
		registry.put(DRequestToDock.COMMAND, DRequestToDock.class);
		registry.put(DOperationCanceled.COMMAND, DOperationCanceled.class);
		registry.put(DPassword.COMMAND, DPassword.class);
		registry.put(DPackageInfo.COMMAND, DPackageInfo.class);
		registry.put(DLoadPackageFile.COMMAND, DLoadPackageFile.class);
		registry.put(DAppNames.COMMAND, DAppNames.class);
		registry.put(DSoupNotDirty.COMMAND, DSoupNotDirty.class);
		registry.put(DSetBaseID.COMMAND, DSetBaseID.class);
		registry.put(DRestoreFile.COMMAND, DRestoreFile.class);
		registry.put(DRestoreAll.COMMAND, DRestoreAll.class);
		registry.put(DImportParametersSlipResult.COMMAND, DImportParametersSlipResult.class);
		registry.put(DNewtonTICommand.COMMAND, DNewtonTICommand.class);
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

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.v1_0.DockCommandFactory#registerTo(java.util.Map)
	 */
	@Override
	protected void registerTo(Map<String, Class<? extends IDockCommandToNewton>> registry) {
		super.registerTo(registry);
		registry.put(DAddEntryWithUniqueID.COMMAND, DAddEntryWithUniqueID.class);
		registry.put(DAliasResolved.COMMAND, DAliasResolved.class);
		registry.put(DBackupSoup.COMMAND, DBackupSoup.class);
		registry.put(DCallGlobalFunction.COMMAND, DCallGlobalFunction.class);
		registry.put(DCallRootMethod.COMMAND, DCallRootMethod.class);
		registry.put(DCreateDefaultSoup.COMMAND, DCreateDefaultSoup.class);
		registry.put(DCursorCountEntries.COMMAND, DCursorCountEntries.class);
		registry.put(DCursorEntry.COMMAND, DCursorEntry.class);
		registry.put(DCursorFree.COMMAND, DCursorFree.class);
		registry.put(DCursorGotoKey.COMMAND, DCursorGotoKey.class);
		registry.put(DCursorMap.COMMAND, DCursorMap.class);
		registry.put(DCursorMove.COMMAND, DCursorMove.class);
		registry.put(DCursorNext.COMMAND, DCursorNext.class);
		registry.put(DCursorPrev.COMMAND, DCursorPrev.class);
		registry.put(DCursorReset.COMMAND, DCursorReset.class);
		registry.put(DCursorResetToEnd.COMMAND, DCursorResetToEnd.class);
		registry.put(DCursorWhichEnd.COMMAND, DCursorWhichEnd.class);
		registry.put(DDesktopInfo.COMMAND, DDesktopInfo.class);
		registry.put(DDevices.COMMAND, DDevices.class);
		registry.put(DFileInfo.COMMAND, DFileInfo.class);
		registry.put(DFilesAndFolders.COMMAND, DFilesAndFolders.class);
		registry.put(DFilters.COMMAND, DFilters.class);
		registry.put(DGetAppNames.COMMAND, DGetAppNames.class);
		registry.put(DGetChangedIndex.COMMAND, DGetChangedIndex.class);
		registry.put(DGetChangedInfo.COMMAND, DGetChangedInfo.class);
		registry.put(DGetDefaultStore.COMMAND, DGetDefaultStore.class);
		registry.put(DGetInternalStore.COMMAND, DGetInternalStore.class);
		registry.put(DGetPackageInfo.COMMAND, DGetPackageInfo.class);
		registry.put(DGetPassword.COMMAND, DGetPassword.class);
		registry.put(DGetSyncOptions.COMMAND, DGetSyncOptions.class);
		registry.put(DImportParametersSlip.COMMAND, DImportParametersSlip.class);
		registry.put(DImporting.COMMAND, DImporting.class);
		registry.put(DInitiateDocking.COMMAND, DInitiateDocking.class);
		registry.put(DKeyboardChar.COMMAND, DKeyboardChar.class);
		registry.put(DKeyboardPassthrough.COMMAND, DKeyboardPassthrough.class);
		registry.put(DKeyboardString.COMMAND, DKeyboardString.class);
		registry.put(DOperationCanceled.COMMAND, DOperationCanceled.class);
		registry.put(DOperationCanceledAck.COMMAND, DOperationCanceledAck.class);
		registry.put(DOperationDone.COMMAND, DOperationDone.class);
		registry.put(DPath.COMMAND, DPath.class);
		registry.put(DQuery.COMMAND, DQuery.class);
		registry.put(DRefTest.COMMAND, DRefTest.class);
		registry.put(DRegProtocolExtension.COMMAND, DRegProtocolExtension.class);
		registry.put(DRemovePackage.COMMAND, DRemovePackage.class);
		registry.put(DRemoveProtocolExtension.COMMAND, DRemoveProtocolExtension.class);
		registry.put(DRequestToInstall.COMMAND, DRequestToInstall.class);
		registry.put(DRequestToRestore.COMMAND, DRequestToRestore.class);
		registry.put(DRequestToSync.COMMAND, DRequestToSync.class);
		registry.put(DRestoreOptions.COMMAND, DRestoreOptions.class);
		registry.put(DRestorePackage.COMMAND, DRestorePackage.class);
		registry.put(DRestorePatch.COMMAND, DRestorePatch.class);
		registry.put(DResultString.COMMAND, DResultString.class);
		registry.put(DSendSoup.COMMAND, DSendSoup.class);
		registry.put(DSetSoupGetInfo.COMMAND, DSetSoupGetInfo.class);
		registry.put(DSetSoupSignature.COMMAND, DSetSoupSignature.class);
		registry.put(DSetStoreGetNames.COMMAND, DSetStoreGetNames.class);
		registry.put(DSetStoreName.COMMAND, DSetStoreName.class);
		registry.put(DSetStoreSignature.COMMAND, DSetStoreSignature.class);
		registry.put(DSetStoreToDefault.COMMAND, DSetStoreToDefault.class);
		registry.put(DSetTimeout.COMMAND, DSetTimeout.class);
		registry.put(DSetVBOCompression.COMMAND, DSetVBOCompression.class);
		registry.put(DShowProgress.COMMAND, DShowProgress.class);
		registry.put(DSoupsChanged.COMMAND, DSoupsChanged.class);
		registry.put(DSourceVersion.COMMAND, DSourceVersion.class);
		registry.put(DSyncResults.COMMAND, DSyncResults.class);
		registry.put(DTranslatorList.COMMAND, DTranslatorList.class);
		registry.put(DUnknownCommand.COMMAND, DUnknownCommand.class);
		registry.put(DWhichIcons.COMMAND, DWhichIcons.class);
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
