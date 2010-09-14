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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jncu.protocol.DockCommand;
import net.sf.jncu.protocol.v1_0.app.DPackage;
import net.sf.jncu.protocol.v1_0.app.DPackageIDList;
import net.sf.jncu.protocol.v1_0.data.DAddedID;
import net.sf.jncu.protocol.v1_0.data.DEntry;
import net.sf.jncu.protocol.v1_0.data.DIndexDescription;
import net.sf.jncu.protocol.v1_0.data.DSoupIDs;
import net.sf.jncu.protocol.v1_0.data.DSoupInfo;
import net.sf.jncu.protocol.v1_0.data.DSoupNames;
import net.sf.jncu.protocol.v1_0.io.DStoreNames;
import net.sf.jncu.protocol.v1_0.query.DInheritance;
import net.sf.jncu.protocol.v1_0.query.DPatches;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v1_0.session.DHello;
import net.sf.jncu.protocol.v1_0.session.DNewtonName;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v1_0.session.DRequestToDock;
import net.sf.jncu.protocol.v1_0.session.DTest;
import net.sf.jncu.protocol.v1_0.sync.DChangedEntry;
import net.sf.jncu.protocol.v1_0.sync.DChangedIDs;
import net.sf.jncu.protocol.v1_0.sync.DCurrentTime;

/**
 * Docking command factory.
 * 
 * @author moshew
 */
public class DockCommandFactory {

	private static DockCommandFactory instance;
	private static final Map<String, Class<? extends DockCommand>> registry = new HashMap<String, Class<? extends DockCommand>>();

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
	protected Map<String, Class<? extends DockCommand>> getRegistry() {
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
	protected void register(Map<String, Class<? extends DockCommand>> registry) {
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
	 * @return the command - <tt>null</tt> otherwise.
	 */
	public DockCommand create(byte[] cmdName) {
		return create(cmdName, 0);
	}

	/**
	 * Create a new dock command.
	 * 
	 * @param cmdName
	 *            the command name.
	 * @param offset
	 *            the offset.
	 * @return the command - <tt>null</tt> otherwise.
	 */
	public DockCommand create(byte[] cmdName, int offset) {
		return create(new String(cmdName, offset, DockCommand.COMMAND_NAME_LENGTH));
	}

	/**
	 * Create a new dock command.
	 * 
	 * @param cmdName
	 *            the command name.
	 * @return the command - <tt>null</tt> otherwise.
	 */
	public DockCommand create(String cmdName) {
		Class<? extends DockCommand> clazz = getRegistry().get(cmdName);

		if (clazz != null) {
			DockCommand cmd = null;
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
	 * @return the command - <tt>null</tt> otherwise.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public DockCommand create(InputStream in) throws IOException {
		byte[] cmdName = new byte[DockCommand.COMMAND_NAME_LENGTH];
		in.read(cmdName);
		return create(cmdName);
	}
}
