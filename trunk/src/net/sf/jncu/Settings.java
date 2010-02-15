package net.sf.jncu;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.jncu.comm.CommPorts;
import net.sf.jncu.comm.NCUSerialPort;

/**
 * Settings.
 * 
 * @author moshew
 */
public class Settings {

	private final Collection<CommPortIdentifier> portIds = new ArrayList<CommPortIdentifier>();
	private CommPortIdentifier portId;
	private int baud = NCUSerialPort.BAUD_38400;
	private boolean listen = true;
	private File backupFolder = new File(".");

	/**
	 * Constructs new settings.
	 */
	public Settings() {
		super();
		rescanPorts();
	}

	public Collection<CommPortIdentifier> getPorts() {
		return portIds;
	}

	public void rescanPorts() {
		portIds.clear();

		CommPorts discoverer = new CommPorts();
		try {
			portIds.addAll(discoverer.getPortIdentifiers(CommPortIdentifier.PORT_SERIAL));
		} catch (NoSuchPortException nspe) {
			nspe.printStackTrace();
		}
	}

	/**
	 * @return the portSelected
	 */
	public CommPortIdentifier getPortIdentifier() {
		return portId;
	}

	/**
	 * @param portId
	 *            the portSelected to set
	 */
	public void setPortIdentifier(CommPortIdentifier portId) {
		this.portId = portId;
	}

	/**
	 * @param portName
	 *            the portSelected to set
	 */
	public void setPortIdentifier(String portName) {
		this.portId = null;
		for (CommPortIdentifier portId : portIds) {
			if (portId.getName().equals(portName)) {
				this.portId = portId;
				break;
			}
		}
	}

	/**
	 * @return the portSpeed
	 */
	public int getPortSpeed() {
		return baud;
	}

	/**
	 * @param portSpeed
	 *            the portSpeed to set
	 */
	public void setPortSpeed(int portSpeed) {
		this.baud = portSpeed;
	}

	/**
	 * @return the listen
	 */
	public boolean isListen() {
		return listen;
	}

	/**
	 * @param listen
	 *            the listen to set
	 */
	public void setListen(boolean listen) {
		this.listen = listen;
	}

	/**
	 * @return the backupFolder
	 */
	public File getBackupFolder() {
		return backupFolder;
	}

	/**
	 * @param backupFolder
	 *            the backupFolder to set
	 */
	public void setBackupFolder(File backupFolder) {
		this.backupFolder = backupFolder;
	}

	/**
	 * @param backupFolder
	 *            the backupFolder to set
	 */
	public void setBackupFolder(String backupFolder) {
		this.backupFolder = new File(backupFolder);
	}
}
