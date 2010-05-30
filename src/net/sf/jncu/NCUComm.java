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
package net.sf.jncu;

import gnu.io.CommPortIdentifier;

import java.util.List;

import net.sf.jncu.cdil.mnp.CommPorts;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.cdil.mnp.NCUSerialPortEngine;

/**
 * Communications helper.
 * <p>
 * Must specify VM arguments for the Java library path variable
 * <tt>java.library.path</tt> at start-up:
 * <ul>
 * <li>for Linux i686:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Linux/i686</code>
 * <li>for Linux ia64:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Linux/ia64</code>
 * <li>for Linux x86_64 (x64):
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Linux/x86_64</code>
 * <li>for Mac OS X:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Mac_OS_X</code>
 * <li>for Solaris SPARC 32:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Solaris/sparc32</code>
 * <li>for Solaris SPARC 64:
 * <code>-Djava.library.path=<em>${user.dir}</em>/lib;<em>${user.dir}</em>/lib/Solaris/sparc64</code>
 * <li>for Windows: <code>-Djava.library.path="<em>${user.dir}</em>\lib";"<em>${user.dir}</em>\lib\Windows\x86"</code> <//ul>
 * 
 * @author moshew
 */
@Deprecated
public class NCUComm {

	private NCUSerialPortEngine talk;

	/**
	 * Constructs a new communications helper.
	 */
	public NCUComm() {
		super();
	}

	public void startListenForNewton(CommPortIdentifier portId, int baud) {
		if (talk == null) {
			talk = new NCUSerialPortEngine(this, portId, baud);
			talk.start();
		}
	}

	public void stopListenForNewton() {
		if (talk != null) {
			talk.close();
		}
		this.talk = null;
	}

	public static void main(String[] args) {
		NCUComm comm = new NCUComm();
		CommPorts commPorts = new CommPorts();
		try {
			List<CommPortIdentifier> ports = commPorts.getPortIdentifiers(CommPortIdentifier.PORT_SERIAL);
			CommPortIdentifier portId = ports.get(0);
			comm.startListenForNewton(portId, MNPSerialPort.BAUD_38400);
			// Thread.sleep(5000);
			// comm.stopListenForNewton();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
