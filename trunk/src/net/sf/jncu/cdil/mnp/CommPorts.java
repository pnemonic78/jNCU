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
	@SuppressWarnings("unchecked")
	public List<CommPortIdentifier> getPortIdentifiers(int portType) throws NoSuchPortException {
		List<CommPortIdentifier> portIdentifiers = new ArrayList<CommPortIdentifier>();

		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = (CommPortIdentifier) portEnum.nextElement();
			if (portIdentifier.getPortType() == portType) {
				portIdentifiers.add(portIdentifier);
			}
		}
		if (portIdentifiers.size() == 0) {
			throw new NoSuchPortException();
		}

		return portIdentifiers;
	}

}
