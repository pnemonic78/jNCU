package net.sf.jncu.cdil.mnp;

/**
 * NCU serial packet filter.
 * 
 * @author mwaisberg
 * 
 */
public class NCUPacketFilter implements MNPSerialPortFilter {

	/**
	 * Creates a new filter.
	 */
	public NCUPacketFilter() {
	}

	@Override
	public byte[] filterSerialPort(byte[] b) {
		return b;
	}
}
