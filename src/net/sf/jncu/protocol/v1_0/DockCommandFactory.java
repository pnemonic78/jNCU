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
package net.sf.jncu.protocol.v1_0;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.IDockCommand;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.app.DBackupPackages;
import net.sf.jncu.protocol.v1_0.app.DDeleteAllPackages;
import net.sf.jncu.protocol.v1_0.app.DDeletePkgDir;
import net.sf.jncu.protocol.v1_0.app.DGetPackageIDs;
import net.sf.jncu.protocol.v1_0.app.DLoadPackage;
import net.sf.jncu.protocol.v1_0.app.DPackage;
import net.sf.jncu.protocol.v1_0.app.DPackageIDList;
import net.sf.jncu.protocol.v1_0.data.DAddEntry;
import net.sf.jncu.protocol.v1_0.data.DAddedID;
import net.sf.jncu.protocol.v1_0.data.DCreateSoup;
import net.sf.jncu.protocol.v1_0.data.DDeleteEntries;
import net.sf.jncu.protocol.v1_0.data.DDeleteSoup;
import net.sf.jncu.protocol.v1_0.data.DEmptySoup;
import net.sf.jncu.protocol.v1_0.data.DEntry;
import net.sf.jncu.protocol.v1_0.data.DGetIndexDescription;
import net.sf.jncu.protocol.v1_0.data.DGetSoupIDs;
import net.sf.jncu.protocol.v1_0.data.DGetSoupInfo;
import net.sf.jncu.protocol.v1_0.data.DGetSoupNames;
import net.sf.jncu.protocol.v1_0.data.DIndexDescription;
import net.sf.jncu.protocol.v1_0.data.DReturnEntry;
import net.sf.jncu.protocol.v1_0.data.DSetCurrentSoup;
import net.sf.jncu.protocol.v1_0.data.DSoupIDs;
import net.sf.jncu.protocol.v1_0.data.DSoupInfo;
import net.sf.jncu.protocol.v1_0.data.DSoupNames;
import net.sf.jncu.protocol.v1_0.io.DGetStoreNames;
import net.sf.jncu.protocol.v1_0.io.DSetCurrentStore;
import net.sf.jncu.protocol.v1_0.io.DStoreNames;
import net.sf.jncu.protocol.v1_0.query.DGetInheritance;
import net.sf.jncu.protocol.v1_0.query.DGetPatches;
import net.sf.jncu.protocol.v1_0.query.DInheritance;
import net.sf.jncu.protocol.v1_0.query.DPatches;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v1_0.session.DHello;
import net.sf.jncu.protocol.v1_0.session.DInitiateDocking;
import net.sf.jncu.protocol.v1_0.session.DNewtonName;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v1_0.session.DRequestToDock;
import net.sf.jncu.protocol.v1_0.session.DTest;
import net.sf.jncu.protocol.v1_0.sync.DChangedEntry;
import net.sf.jncu.protocol.v1_0.sync.DChangedIDs;
import net.sf.jncu.protocol.v1_0.sync.DCurrentTime;
import net.sf.jncu.protocol.v1_0.sync.DGetChangedIDs;
import net.sf.jncu.protocol.v1_0.sync.DLastSyncTime;
import net.sf.jncu.protocol.v1_0.sync.DReturnChangedEntry;
import net.sf.jncu.protocol.v2_0.session.DDesktopInfo;

/**
 * Docking command factory.
 * 
 * @author moshew
 */
public class DockCommandFactory {

	private static DockCommandFactory instance;
	private static final Map<String, Class<? extends IDockCommand>> registry = new HashMap<String, Class<? extends IDockCommand>>();

	/**
	 * Creates a new command factory.
	 */
	protected DockCommandFactory() {
		super();

	}

	/**
	 * Get the command registry.
	 * 
	 * @return the registry.
	 */
	protected Map<String, Class<? extends IDockCommand>> getRegistry() {
		if (registry.isEmpty()) {
			register(registry);
		}
		return registry;
	}

	/**
	 * Register dock commands.
	 * <p>
	 * It's only really necessary to register "from Newton" commands.
	 * 
	 * @param registry
	 *            the registry.
	 */
	private void register(Map<String, Class<? extends IDockCommand>> registry) {
		Map<String, Class<? extends IDockCommandFromNewton>> registryFrom = new HashMap<String, Class<? extends IDockCommandFromNewton>>();
		Map<String, Class<? extends IDockCommandToNewton>> registryTo = new HashMap<String, Class<? extends IDockCommandToNewton>>();
		registerFrom(registryFrom);
		registerTo(registryTo);
		registry.putAll(registryFrom);
		registry.putAll(registryTo);
	}

	/**
	 * Register commands from Newton.
	 * 
	 * @param registry
	 *            the registry.
	 */
	protected void registerFrom(Map<String, Class<? extends IDockCommandFromNewton>> registry) {
		registry.put(DAddedID.COMMAND, DAddedID.class);
		registry.put(DChangedEntry.COMMAND, DChangedEntry.class);
		registry.put(DChangedIDs.COMMAND, DChangedIDs.class);
		registry.put(DCurrentTime.COMMAND, DCurrentTime.class);
		registry.put(DDisconnect.COMMAND, DDisconnect.class);
		registry.put(DEntry.COMMAND, DEntry.class);
		registry.put(DHello.COMMAND, DHello.class);
		registry.put(DIndexDescription.COMMAND, DIndexDescription.class);
		registry.put(DInheritance.COMMAND, DInheritance.class);
		registry.put(DNewtonName.COMMAND, DNewtonName.class);
		registry.put(DOperationCanceled.COMMAND, DOperationCanceled.class);
		registry.put(DPackage.COMMAND, DPackage.class);
		registry.put(DPackageIDList.COMMAND, DPackageIDList.class);
		registry.put(DPatches.COMMAND, DPatches.class);
		registry.put(DRequestToDock.COMMAND, DRequestToDock.class);
		registry.put(DResult.COMMAND, DResult.class);
		registry.put(DSoupIDs.COMMAND, DSoupIDs.class);
		registry.put(DSoupInfo.COMMAND, DSoupInfo.class);
		registry.put(DSoupNames.COMMAND, DSoupNames.class);
		registry.put(DStoreNames.COMMAND, DStoreNames.class);
		registry.put(DTest.COMMAND, DTest.class);
	}

	/**
	 * Register commands to Newton.
	 * 
	 * @param registry
	 *            the registry.
	 */
	protected void registerTo(Map<String, Class<? extends IDockCommandToNewton>> registry) {
		registry.put(DAddEntry.COMMAND, DAddEntry.class);
		registry.put(DBackupPackages.COMMAND, DBackupPackages.class);
		registry.put(DCreateSoup.COMMAND, DCreateSoup.class);
		registry.put(DDeleteAllPackages.COMMAND, DDeleteAllPackages.class);
		registry.put(DDeleteEntries.COMMAND, DDeleteEntries.class);
		registry.put(DDeletePkgDir.COMMAND, DDeletePkgDir.class);
		registry.put(DDeleteSoup.COMMAND, DDeleteSoup.class);
		registry.put(DDesktopInfo.COMMAND, DDesktopInfo.class);
		registry.put(DEmptySoup.COMMAND, DEmptySoup.class);
		registry.put(DGetChangedIDs.COMMAND, DGetChangedIDs.class);
		registry.put(DGetIndexDescription.COMMAND, DGetIndexDescription.class);
		registry.put(DGetInheritance.COMMAND, DGetInheritance.class);
		registry.put(DGetPackageIDs.COMMAND, DGetPackageIDs.class);
		registry.put(DGetPatches.COMMAND, DGetPatches.class);
		registry.put(DGetSoupIDs.COMMAND, DGetSoupIDs.class);
		registry.put(DGetSoupInfo.COMMAND, DGetSoupInfo.class);
		registry.put(DGetSoupNames.COMMAND, DGetSoupNames.class);
		registry.put(DGetStoreNames.COMMAND, DGetStoreNames.class);
		registry.put(DInitiateDocking.COMMAND, DInitiateDocking.class);
		registry.put(DLastSyncTime.COMMAND, DLastSyncTime.class);
		registry.put(DLoadPackage.COMMAND, DLoadPackage.class);
		registry.put(DReturnChangedEntry.COMMAND, DReturnChangedEntry.class);
		registry.put(DReturnEntry.COMMAND, DReturnEntry.class);
		registry.put(DSetCurrentSoup.COMMAND, DSetCurrentSoup.class);
		registry.put(DSetCurrentStore.COMMAND, DSetCurrentStore.class);
		registry.put(DTest.COMMAND, DTest.class);
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

	/**
	 * Create a new dock command.
	 * 
	 * @param cmdName
	 *            the command name.
	 * @return the command - {@code null} otherwise.
	 */
	public IDockCommand create(byte[] cmdName) {
		return create(cmdName, 0);
	}

	/**
	 * Create a new dock command.
	 * 
	 * @param cmdName
	 *            the command name.
	 * @param offset
	 *            the offset.
	 * @return the command - {@code null} otherwise.
	 */
	public IDockCommand create(byte[] cmdName, int offset) {
		return create(new String(cmdName, offset, DockCommand.COMMAND_NAME_LENGTH));
	}

	/**
	 * Create a new dock command.
	 * 
	 * @param cmdName
	 *            the command name.
	 * @return the command - {@code null} otherwise.
	 */
	public IDockCommand create(String cmdName) {
		Class<? extends IDockCommand> clazz = getRegistry().get(cmdName);

		if (clazz != null) {
			IDockCommand cmd = null;
			try {
				cmd = clazz.newInstance();
			} catch (InstantiationException ie) {
				ie.printStackTrace();
			} catch (IllegalAccessException iae) {
				iae.printStackTrace();
			}
			return cmd;
		}

		return null;
	}

	/**
	 * Create a new dock command.
	 * 
	 * @param in
	 *            the input that starts with the command name.
	 * @return the command - {@code null} otherwise.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public IDockCommand create(InputStream in) throws IOException {
		byte[] cmdName = new byte[DockCommand.COMMAND_NAME_LENGTH];
		int count = in.read(cmdName);
		if (count == -1) {
			throw new EOFException();
		}
		return create(cmdName);
	}
}
