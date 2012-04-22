package net.sf.jncu.cdil.mnp;

public class PacketLogger {

	private final char direction;
	private int id;

	public PacketLogger(char direction) {
		this.direction = direction;
	}

	public void log(String method, MNPPacket packet, Object cookie) {
		if (packet == null) {
			processNull(direction, method, cookie);
			return;
		}
		if (packet instanceof MNPLinkAcknowledgementPacket) {
			processLA(direction, method, (MNPLinkAcknowledgementPacket) packet, cookie);
		} else if (packet instanceof MNPLinkDisconnectPacket) {
			processLD(direction, method, (MNPLinkDisconnectPacket) packet, cookie);
		} else if (packet instanceof MNPLinkRequestPacket) {
			processLR(direction, method, (MNPLinkRequestPacket) packet, cookie);
		} else if (packet instanceof MNPLinkTransferPacket) {
			processLT(direction, method, (MNPLinkTransferPacket) packet, cookie);
		} else {
			throw new ClassCastException("unknown packet type");
		}
	}

	private void processNull(char direction, String method, Object cookie) {
		System.out.println(System.currentTimeMillis() + "\t" + nextId() + "\t" + direction + " m:" + method + " cookie:" + cookie);
	}

	private void processLA(char direction, String method, MNPLinkAcknowledgementPacket packet, Object cookie) {
		System.out.println(System.currentTimeMillis() + "\t" + nextId() + "\t" + direction + " m:" + method + " type:LA seq:" + packet.getSequence() + " cookie:" + cookie);
	}

	private void processLD(char direction, String method, MNPLinkDisconnectPacket packet, Object cookie) {
		System.out.println(System.currentTimeMillis() + "\t" + nextId() + "\t" + direction + " m:" + method + " type:LD reason:" + packet.getReasonCode() + " user:"
				+ packet.getUserCode() + " cookie:" + cookie);
	}

	private void processLR(char direction, String method, MNPLinkRequestPacket packet, Object cookie) {
		System.out.println(System.currentTimeMillis() + "\t" + nextId() + "\t" + direction + " m:" + method + " type:LR dpo:" + packet.getDataPhaseOpt() + " framing:"
				+ packet.getFramingMode() + " info:" + packet.getMaxInfoLength() + " outstanding:" + packet.getMaxOutstanding() + " cookie:" + cookie);
	}

	private void processLT(char direction, String method, MNPLinkTransferPacket packet, Object cookie) {
		System.out.println(System.currentTimeMillis() + "\t" + nextId() + "\t" + direction + " m:" + method + " type:LT seq:" + packet.getSequence() + " data:"
				+ dataToString(packet.getData()) + " cookie:" + cookie);
	}

	private synchronized String nextId() {
		StringBuffer s = new StringBuffer(2);
		if (id < 10)
			s.append('0');
		s.append(id);
		id++;
		return s.toString();
	}

	private String dataToString(byte[] data) {
		StringBuffer buf = new StringBuffer();
		buf.append('[');
		int b;
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				buf.append(',');
			}
			b = data[i] & 0xFF;
			buf.append("0x");
			if (b < 0x10) {
				buf.append('0');
			}
			buf.append(Integer.toHexString(b));
			if ((b >= 0x020) && (b <= 0x7E)) {
				buf.append(' ');
				buf.appendCodePoint(b);
			}
		}
		buf.append(']');
		return buf.toString();
	}

}
