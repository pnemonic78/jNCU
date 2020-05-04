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
package net.sf.jncu.protocol.v2_0.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.protocol.BaseDockCommandFromNewton;

/**
 * This command returns the names of the applications present on the Newton. It
 * also, optionally, returns the names of the soups associated with each
 * application. The array looks like this:
 * <code>[{name: "app name", soups: ["soup1", "soup2"]},<br/>
 * &nbsp;{name: "another app name", ...}, ...]</code>
 * <p>
 * Some built-in names are included. "System information" includes the system
 * and directory soups. If there are packages installed, a "Packages" item is
 * listed. If a card is present and has a backup there will be a "Card backup"
 * item. If there are soups that don't have an associated application (or whose
 * application I can't figure out) there's an "Other information" entry.
 * <p>
 * The soup names are optionally returned depending on the value received with
 * <tt>kDGetAppNames</tt>.
 * 
 * <pre>
 * 'appn'
 * length
 * result frames
 * </pre>
 * 
 * @author Moshe
 */
public class DAppNames extends BaseDockCommandFromNewton {

	/** <tt>kDAppNames</tt> */
	public static final String COMMAND = "appn";

	private final List<AppName> names = new ArrayList<AppName>();

	/**
	 * Creates a new command.
	 */
	public DAppNames() {
		super(COMMAND);
	}

	@Override
	protected void decodeCommandData(InputStream data) throws IOException {
		names.clear();

		NSOFDecoder decoder = new NSOFDecoder();
		NSOFObject o = decoder.inflate(data);
		if (!NSOFImmediate.isNil(o)) {
			NSOFArray arr = (NSOFArray) o;
			int size = arr.length();
			AppName name;
			for (int i = 0; i < size; i++) {
				name = new AppName((NSOFFrame) arr.get(i));
				names.add(name);
			}
		}
	}

	/**
	 * Get the application names.
	 * 
	 * @return the names.
	 */
	public List<AppName> getNames() {
		return names;
	}
}
