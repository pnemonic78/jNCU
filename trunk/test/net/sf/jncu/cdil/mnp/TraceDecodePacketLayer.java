package net.sf.jncu.cdil.mnp;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class TraceDecodePacketLayer extends EmptyPacketLayer {

	private final InputStream receivedFromNewton;
	private final InputStream sentToNewton;

	public TraceDecodePacketLayer(MNPPipe pipe, InputStream receivedFromNewton, InputStream sentToNewton) {
		super(pipe);
		setName("TraceDecodePacketLayer-" + getId());
		this.receivedFromNewton = receivedFromNewton;
		this.sentToNewton = new BufferedInputStream(sentToNewton, 1024);
	}

	@Override
	protected InputStream getInput() throws IOException {
		return receivedFromNewton;
	}

	public byte[] readSent() throws EOFException, IOException {
		return read(sentToNewton);
	}
}
