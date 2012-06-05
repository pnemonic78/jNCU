package net.sf.jncu.cdil.mnp;

import net.sf.jncu.cdil.CDPacketFilter;

/**
 * @author mwaisberg
 *
 */
public class TraceDecodePacketFilter implements CDPacketFilter<MNPPacket> {

	/**
	 * Creates a new filter.
	 */
	public TraceDecodePacketFilter() {
	}

	@Override
	public MNPPacket filterPacket(MNPPacket packet) {
		return packet;
	}

}
