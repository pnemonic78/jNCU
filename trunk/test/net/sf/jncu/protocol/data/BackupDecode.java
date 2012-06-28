package net.sf.jncu.protocol.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.cdil.mnp.TraceDecode;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;

public class BackupDecode extends TraceDecode implements DockCommandListener {

	public BackupDecode() throws IOException {
	}

	protected DecodePayload createDecodePayload(InputStream receivedFromNewton, InputStream sentToNewton) throws Exception {
		DecodePayload dp = super.createDecodePayload(receivedFromNewton, sentToNewton);
		dp.getPipe().addCommandListener(this);
		return dp;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		File f = new File(args[0]);
		BackupDecode decoder;
		try {
			decoder = new BackupDecode();
			decoder.setFile(f);
			decoder.run();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (!Boolean.getBoolean("debug"))
			System.exit(0);// Kill all threads.
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
	}

	@Override
	public void commandReceiving(IDockCommandFromNewton command, int progress, int total) {
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
	}

	@Override
	public void commandSending(IDockCommandToNewton command, int progress, int total) {
	}

	@Override
	public void commandEOF() {
	}

}
