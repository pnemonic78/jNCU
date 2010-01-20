package net.sf.jncu;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

/**
 * Communications helper.
 * 
 * @author moshew
 */
public class NCUComm {

	public static final int BAUD_2400 = 2400;
	public static final int BAUD_4800 = 4800;
	public static final int BAUD_9600 = 9600;
	public static final int BAUD_38400 = 38400;
	public static final int BAUD_57600 = 57600;

	private NCUCommTalk talk;

	/**
	 * Constructs a new communications helper.
	 */
	public NCUComm() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Collection<CommPortIdentifier> getPorts(int portType) {
		Collection<CommPortIdentifier> ports = new ArrayList<CommPortIdentifier>();

		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier
				.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			if (portIdentifier.getPortType() == portType) {
				ports.add(portIdentifier);
			}
		}

		return ports;
	}

	public void startListenForNewton(CommPortIdentifier portId, int baud) {
		if (talk == null) {
			talk = new NCUCommTalk(this, portId, baud);
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
		try {
			Collection<CommPortIdentifier> ports = comm
					.getPorts(CommPortIdentifier.PORT_SERIAL);
			if (ports.size() == 0) {
				throw new NoSuchPortException();
			}
			CommPortIdentifier portId = ports.iterator().next();
			comm.startListenForNewton(portId, BAUD_38400);
			// Thread.sleep(5000);
			// comm.stopListenForNewton();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
