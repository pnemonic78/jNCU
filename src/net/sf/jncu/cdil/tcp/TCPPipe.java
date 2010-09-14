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
package net.sf.jncu.cdil.tcp;

import net.sf.jncu.cdil.CDCommandLayer;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.ServiceNotSupportedException;

/**
 * TCP pipe.
 * 
 * @author moshew
 */
public class TCPPipe extends CDPipe {

	protected final int port;

	/**
	 * Creates a new TCP pipe.
	 * 
	 * @param layer
	 *            the owner layer.
	 * @param port
	 *            the TCP port to listen on. Note that once the connection is
	 *            made, data transfer actually occurs on a different, randomly
	 *            chosen, port. This frees up the port specified in this
	 *            parameter for future connections.
	 * @throws ServiceNotSupportedException
	 *             if the service is not supported.
	 */
	public TCPPipe(CDLayer layer, int port) throws ServiceNotSupportedException {
		super(layer);
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.cdil.CDPipe#createCommandLayer()
	 */
	@Override
	protected CDCommandLayer createCommandLayer() {
		// TODO Auto-generated method stub
		return null;
	}

}
