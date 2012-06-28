package net.sf.jncu.protocol.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jncu.cdil.mnp.TraceDecode;
import net.sf.jncu.data.Archive;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.data.DSoupInfo;
import net.sf.jncu.protocol.v1_0.data.DSoupNames;
import net.sf.jncu.protocol.v1_0.io.DStoreNames;
import net.sf.jncu.protocol.v2_0.session.DNewtonName;

public class BackupDecode extends TraceDecode implements DockCommandListener {

	private Store store;
	private List<Soup> soups;
	private Soup soup;
	private Archive archive;
	private boolean done;

	public BackupDecode() throws IOException {
		this.archive = new Archive();
	}

	@Override
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
			if (!Boolean.getBoolean("debug")) {
				Thread.sleep(1000L);
				System.exit(0);// Kill all threads.
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		final String cmd = command.getCommand();

		if (DNewtonName.COMMAND.equals(cmd)) {
			DNewtonName dNewtonName = (DNewtonName) command;
			archive.setDeviceInfo(dNewtonName.getInformation());
		} else if (DStoreNames.COMMAND.equals(cmd)) {
			DStoreNames dStoreNames = (DStoreNames) command;
			store = dStoreNames.getDefaultStore();
			archive.setStores(dStoreNames.getStores());
		} else if (DSoupNames.COMMAND.equals(cmd)) {
			DSoupNames dSoupNames = (DSoupNames) command;
			soups = dSoupNames.getSoups();
			store.setSoups(soups);
		} else if (DSoupInfo.COMMAND.equals(cmd)) {
			DSoupInfo dSoupInfo = (DSoupInfo) command;
			soup = dSoupInfo.getSoup();
			Soup soupFound = store.findSoup(soup.getName(), soup.getSignature());
			if (soupFound != null) {
				soupFound.getInformation().putAll(soup.getInformation());
			}
		}
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
		if (done)
			return;
		String dir = System.getProperty("user.dir");
		File f = new File(dir, "Backups/backup.zip");
		try {
			archive.save(f);
			done = true;
			System.out.println("archive saved to " + f);
			System.exit(0);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
