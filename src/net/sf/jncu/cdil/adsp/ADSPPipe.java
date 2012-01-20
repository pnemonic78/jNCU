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
package net.sf.jncu.cdil.adsp;

import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.ServiceNotSupportedException;

/**
 * AppleTalk pipe.
 * 
 * @author moshew
 */
public class ADSPPipe extends CDPipe<ADSPPacket> {

	protected final String name;
	protected final byte type;

	/**
	 * Creates a new ADSP pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @param name
	 *            the name of the ADSP connection. This string is what appears
	 *            in the Chooser list on the Newton OS device. If you pass
	 *            {@code null} for this parameter, the CDIL uses a default name
	 *            based on your desktop computer's preferences (for instance, on
	 *            a Macintosh, it will use the strings specified in the File
	 *            Sharing control panel).
	 * @param type
	 *            the connection type. This is searched for by the Chooser on
	 *            the Newton OS device. If you pass {@code null} for this
	 *            parameter, the CDIL uses the type specified by the
	 *            Connection/Dock application.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	public ADSPPipe(CDLayer layer, String name, byte type) throws ServiceNotSupportedException {
		super(layer);
		this.name = name;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.CDPipe#createCommandLayer()
	 */
	@Override
	protected CDCommandLayer<ADSPPacket> createCommandLayer() {
		// TODO Auto-generated method stub
		return null;
	}

}
