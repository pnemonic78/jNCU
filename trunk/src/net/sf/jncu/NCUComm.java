package net.sf.jncu;

import gnu.io.CommPortIdentifier;

import java.util.List;

import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.comm.CommPorts;
import net.sf.jncu.comm.NCUSerialPortEngine;

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
