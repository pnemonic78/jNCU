package net.sf.jncu.cdil.mnp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sf.jncu.cdil.PipeDisconnectedException;
import net.sf.jncu.cdil.mnp.MNPPipe.MNPState;
import net.sf.jncu.io.NullOutputStream;

public class EmptyPacketLayer extends MNPSerialPacketLayer {

	private final InputStream nullIn;
	private final OutputStream nullOut;

	public EmptyPacketLayer(MNPPipe pipe) {
		super(pipe, null);
		setName("EmptyPacketLayer-" + getId());
		setTimeout(Integer.MAX_VALUE);
		this.nullIn = new ByteArrayInputStream(new byte[] {});
		this.nullOut = new NullOutputStream();
	}

	@Override
	protected InputStream getInput() throws IOException {
		return nullIn;
	}

	@Override
	protected OutputStream getOutput() throws IOException {
		return nullOut;
	}

	@Override
	protected boolean allowAcknowledge(MNPPacket packet) {
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
