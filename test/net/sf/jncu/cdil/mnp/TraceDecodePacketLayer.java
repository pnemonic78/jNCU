package net.sf.jncu.cdil.mnp;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.mnp.MNPPipe.MNPState;

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

	@Override
	protected boolean allowAcknowledge() {
		return false;
	}

	@Override
	public void run() {
		try {
			((MNPPipe) pipe).setState(MNPState.MNP_HANDSHAKE_LR_LISTEN);
		} catch (PipeDisconnectedException pde) {
			pde.printStackTrace();
		}
		super.run();
	}
}
