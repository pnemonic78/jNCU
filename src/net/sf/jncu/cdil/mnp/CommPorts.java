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
package net.sf.jncu.cdil.mnp;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * NCU communication ports.
 * 
 * @author moshew
 * @see CommTrace CommTrace for setting library paths.
 */
public class CommPorts {

	/**
	 * Creates a new port helper.
	 */
	public CommPorts() {
		super();
	}

	/**
	 * Get the list of communication port identifiers.
	 * 
	 * @param portType
	 *            the port type.
	 * @return the list of ports.
	 * @throws NoSuchPortException
	 *             if no ports are found.
	 */
	public List<CommPortIdentifier> getPortIdentifiers(int portType)
			throws NoSuchPortException {
		List<CommPortIdentifier> portIdentifiers = new ArrayList<CommPortIdentifier>();

		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = (CommPortIdentifier) portEnum
					.nextElement();
			if (portIdentifier.getPortType() == portType) {
				portIdentifiers.add(portIdentifier);
			}
		}
		if (portIdentifiers.size() == 0) {
			throw new NoSuchPortException();
		}

		return portIdentifiers;
	}

	public static void main(String[] args) {
		CommPorts ports = new CommPorts();
		try {
			for (CommPortIdentifier id : ports
					.getPortIdentifiers(CommPortIdentifier.PORT_SERIAL)) {
				System.out.println(id.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
