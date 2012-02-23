package net.sf.jncu.cdil.mnp;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.mnp.MNPPipe.MNPState;
import net.sf.jncu.io.NullOutputStream;

public class TraceDecodePacketLayer extends MNPSerialPacketLayer {

	private final InputStream receivedFromNewton;
	private final OutputStream nullPort;
	private final InputStream sentToNewton;

	public TraceDecodePacketLayer(MNPPipe pipe, InputStream receivedFromNewton, InputStream sentToNewton) {
		super(pipe, null);
		setName("TraceDecodePacketLayer-" + getId());
		this.receivedFromNewton = receivedFromNewton;
		this.nullPort = new NullOutputStream();
		this.sentToNewton = sentToNewton;
		setTimeout(Integer.MAX_VALUE);
	}

	@Override
	protected InputStream getInput() throws IOException {
		return receivedFromNewton;
	}

	@Override
	protected OutputStream getOutput() throws IOException {
		return nullPort;
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
