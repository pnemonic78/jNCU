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
import java.io.OutputStream;

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

/**
 * The package info for the specified package is returned. See the
 * <tt>kDPackageInfo</tt> command described below. Note that multiple packages
 * could be returned because there may be multiple packages with the same title
 * but different package ids. Note that this finds packages only in the current
 * store.
 * <p>
 * <em>"Package title" is not the same as "Application name".
 * 
 * <pre>
 * 'gpin'
 * length
 * title ref
 * </pre>
 * 
 * @author Moshe
 * @see DPackageInfo
 */
public class DGetPackageInfo extends BaseDockCommandToNewton {

	/** <tt>kDGetPackageInfo</tt> */
	public static final String COMMAND = "gpin";

	private String title;

	/**
	 * Constructs a new command.
	 */
	public DGetPackageInfo() {
		super(COMMAND);
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(new NSOFString(getTitle()), data);
	}

	/**
	 * Get the package title.
	 * 
	 * @return the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the package title.
	 * 
	 * @param title
	 *            the title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
