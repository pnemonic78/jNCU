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

import net.sf.jncu.fdil.NSOFDecoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.v1_0.app.PackageInfo;

/**
 * This command is sent in response to a <tt>kDGetPackageInfo</tt> command. An
 * array is returned that contains a frame for each package with the specified
 * name (there may be more than one package with the same name but different
 * package id). The returned frame looks like this:<br>
 * <code>
 * {<br>
 * &nbsp;&nbsp;name: "The name passed in",<br>
 * &nbsp;&nbsp;packagesize: 123,<br>
 * &nbsp;&nbsp;packageid: 123,<br>
 * &nbsp;&nbsp;packageversion: 1,<br>
 * &nbsp;&nbsp;format: 1,<br>
 * &nbsp;&nbsp;devicekind: 1,<br>
 * &nbsp;&nbsp;devicenumber: 1,<br>
 * &nbsp;&nbsp;deviceid: 1,<br>
 * &nbsp;&nbsp;modtime: 123213213,<br>
 * &nbsp;&nbsp;iscopyprotected: true,<br>
 * &nbsp;&nbsp;length: 123,<br>
 * &nbsp;&nbsp;safetoremove: true<br>
 * }</code>
 * 
 * <pre>
 * 'pinf'
 * length
 * info ref
 * </pre>
 * 
 * @author Moshe
 */
public class DPackageInfo extends DockCommandFromNewton {

	/** <tt>kDPackageInfo</tt> */
	public static final String COMMAND = "pinf";

	private PackageInfo info;

	/**
	 * Creates a new command.
	 */
	public DPackageInfo() {
		super(COMMAND);
	}

	@Override
	protected void decodeData(InputStream data) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		PackageInfo pkg = new PackageInfo();
		pkg.decode((NSOFFrame)decoder.decode(data));
		setInfo(pkg);
	}

	/**
	 * @return the info.
	 */
	public PackageInfo getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info.
	 */
	protected void setInfo(PackageInfo info) {
		this.info = info;
	}

}
