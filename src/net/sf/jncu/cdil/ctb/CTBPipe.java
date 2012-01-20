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
package net.sf.jncu.cdil.ctb;

import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.ServiceNotSupportedException;

/**
 * Macintosh Communication Tool Serial pipe.
 * 
 * @author moshew
 */
public class CTBPipe extends CDPipe<CTBPacket> {

	protected final String toolName;
	protected final String configString;

	/**
	 * Creates a new CTB pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @param toolName
	 *            the name of the communication tool.
	 * @param configString
	 *            a tool-dependent configuration string.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	public CTBPipe(CDLayer layer, String toolName, String configString) throws ServiceNotSupportedException {
		super(layer);
		this.toolName = toolName;
		this.configString = configString;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.CDPipe#createCommandLayer()
	 */
	@Override
	protected CDCommandLayer<CTBPacket> createCommandLayer() {
		// TODO Auto-generated method stub
		return null;
	}

}
