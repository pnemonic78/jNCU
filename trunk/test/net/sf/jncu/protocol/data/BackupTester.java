package net.sf.jncu.protocol.data;

import java.util.Collection;
import java.util.List;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.mnp.MNPPacket;
import net.sf.jncu.cdil.mnp.MNPPacketListener;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.cdil.mnp.PacketLogger;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.data.DEntry;
import net.sf.jncu.protocol.v1_0.data.DIndexDescription;
import net.sf.jncu.protocol.v1_0.data.DSetCurrentSoup;
import net.sf.jncu.protocol.v1_0.data.DSoupIDs;
import net.sf.jncu.protocol.v1_0.data.DSoupInfo;
import net.sf.jncu.protocol.v1_0.data.DSoupNames;
import net.sf.jncu.protocol.v1_0.io.DGetStoreNames;
import net.sf.jncu.protocol.v1_0.io.DStoreNames;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v1_0.sync.DLastSyncTime;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.IconModule.IconModuleListener;
import net.sf.jncu.protocol.v2_0.data.DSendSoup;
import net.sf.jncu.protocol.v2_0.data.DSoupNotDirty;
import net.sf.jncu.protocol.v2_0.io.DSetStoreGetNames;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled2;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.protocol.v2_0.sync.DBackupSoupDone;

/**
 * Test to backup from the Newton.
 * 
 * @author moshew
 */
public class BackupTester implements IconModuleListener, MNPPacketListener, DockCommandListener {

	private String portName;
	private boolean running = false;
	private CDLayer layer;
	private MNPPipe pipe;
	private PacketLogger logger;
	private Store store;
	private List<Soup> soups;
	private Soup soup;
	private Integer result;
	private boolean backupDone = false;

	public BackupTester() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		BackupTester tester = new BackupTester();
		if (args.length < 1) {
			System.out.println("args: port");
			System.exit(1);
			return;
		}
		tester.setPortName(args[0]);
		// tester.setPath(args[1]);

		try {
			try {
				tester.init();
				tester.run();
			} finally {
				tester.done();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(0);
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public void init() throws Exception {
		logger = new PacketLogger('P');
		layer = CDLayer.getInstance();
		// Initialize the library
		layer.startUp();
		// Create a connection object
		pipe = layer.createMNPSerial(portName, MNPSerialPort.BAUD_38400);
		pipe.setTimeout(30);
		pipe.startListening();
		pipe.addPacketListener(this);
		pipe.addCommandListener(this);
		// Wait for a connect request
		while (layer.getState() == CDState.LISTENING) {
			Thread.yield();
		}
		pipe.accept();
	}

	public void run() throws Exception {
		running = true;

		result = null;
		store = null;
		DGetStoreNames dGetStoreNames = new DGetStoreNames();
		pipe.write(dGetStoreNames);
		Thread.sleep(2000);
		while (running && (store == null))
			Thread.yield();

		result = null;
		DSetStoreGetNames dSetStoreGetNames = new DSetStoreGetNames();
		dSetStoreGetNames.setStore(store);
		pipe.write(dSetStoreGetNames);
		Thread.sleep(2000);
		while (running && ((soups == null) || soups.isEmpty()) && (result == null))
			Thread.yield();
		if (soups == null) {
			exit();
			return;
		}
		soup = soups.get(0);

		// DGetSyncOptions dGetSyncOptions = new DGetSyncOptions();
		// pipe.write(dGetSyncOptions);
		// Thread.sleep(2000);

		int size = Math.min(1, soups.size());
		for (int i = 0; i < size && running; i++) {
			soup = soups.get(i);
			System.out.println("@@@ soup=" + soup.getName());

			result = null;
			DSetCurrentSoup dSetCurrentSoup = new DSetCurrentSoup();
			dSetCurrentSoup.setSoup(soup);
			pipe.write(dSetCurrentSoup);
			Thread.sleep(1000);
			while (running && (result == null))
				Thread.yield();
			if ((result != null) && (result != 0)) {
				exit();
				return;
			}

			// result = null;
			// soup = null;
			// DSetSoupGetInfo dSetSoupGetInfo = new DSetSoupGetInfo();
			// dSetSoupGetInfo.setSoup(soup));
			// pipe.write(dSetSoupGetInfo);
			// Thread.sleep(2000);
			// while (running && (soup == null) && (result == null))
			// Thread.yield();
			// if (soup == null) {
			// exit();
			// return;
			// }

			// DONE
			// DGetIndexDescription dGetIndexDescription = new
			// DGetIndexDescription();
			// pipe.write(dGetIndexDescription);
			// Thread.sleep(2000);

			// DONE
			// DGetSoupIDs dGetSoupIDs = new DGetSoupIDs();
			// pipe.write(dGetSoupIDs);
			// Thread.sleep(2000);

			// DONE
			// DReturnEntry dReturnEntry = new DReturnEntry();
			// dReturnEntry.setId(840);
			// pipe.write(dReturnEntry);
			// Thread.sleep(2000);

			// DONE
			backupDone = false;
			DSendSoup dSendSoup = new DSendSoup();
			pipe.write(dSendSoup);
			Thread.sleep(1000);
			while (running && !backupDone)
				Thread.yield();

			// DONE
			// backupDone = false;
			// DBackupSoup dBackupSoup = new DBackupSoup();
			// dBackupSoup.setId(850);
			// pipe.write(dBackupSoup);
			// Thread.sleep(1000);
			// while (running && !backupDone)
			// Thread.yield();
		}

		// dLastSyncTime = new DLastSyncTime();
		// pipe.write(dLastSyncTime);
		// Thread.sleep(2000);
		//
		// DGetInheritance dGetInheritance = new DGetInheritance();
		// pipe.write(dGetInheritance);
		// Thread.sleep(2000);

		// DGetPatches dGetPatches = new DGetPatches();
		// pipe.write(dGetPatches);
		// Thread.sleep(2000);

		DLastSyncTime dLastSyncTime = new DLastSyncTime();
		pipe.write(dLastSyncTime);
		Thread.sleep(2000);

		Thread.sleep(10000);
		exit();

		while (running)
			Thread.yield();
	}

	private void exit() throws Exception {
		DOperationDone dOperationDone = new DOperationDone();
		pipe.write(dOperationDone);
		Thread.sleep(1000);
		DDisconnect dDisconnect = new DDisconnect();
		pipe.write(dDisconnect);
		Thread.sleep(1000);
	}

	private void done() throws Exception {
		if (pipe != null) {
			pipe.disconnect(); // Break the connection.
			pipe.dispose(); // Delete the pipe object
		}
		if (layer != null) {
			layer.shutDown(); // Close the library
		}
	}

	@Override
	public void successModule(IconModule module) {
		System.out.println("successModule");
		running = false;
	}

	@Override
	public void cancelModule(IconModule module) {
		System.out.println("cancelModule");
		running = false;
	}

	@Override
	public void packetAcknowledged(MNPPacket packet) {
		logger.log("a", packet, pipe.getDockingState());
	}

	@Override
	public void packetEOF() {
		logger.log("e", null, pipe.getDockingState());
	}

	@Override
	public void packetReceived(MNPPacket packet) {
		logger.log("r", packet, pipe.getDockingState());
	}

	@Override
	public void packetSent(MNPPacket packet) {
		logger.log("s", packet, pipe.getDockingState());
	}

	@Override
	public void commandReceiving(IDockCommandFromNewton command, int progress, int total) {
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		System.out.println("rcv: " + command);
		final String cmd = command.getCommand();

		if (DDisconnect.COMMAND.equals(cmd)) {
			running = false;
		} else if (DOperationCanceled.COMMAND.equals(cmd)) {
			running = false;
		} else if (DOperationCanceled2.COMMAND.equals(cmd)) {
			running = false;
		} else if (net.sf.jncu.protocol.v1_0.session.DOperationCanceled.COMMAND.equals(cmd)) {
			running = false;
		} else if (DResult.COMMAND.equals(cmd)) {
			DResult dResult = (DResult) command;
			result = dResult.getErrorCode();
		} else if (DStoreNames.COMMAND.equals(cmd)) {
			DStoreNames dStoreNames = (DStoreNames) command;
			store = dStoreNames.getDefaultStore();
		} else if (DSoupNames.COMMAND.equals(cmd)) {
			DSoupNames dSoupNames = (DSoupNames) command;
			soups = dSoupNames.getSoups();
		} else if (DSoupInfo.COMMAND.equals(cmd)) {
			DSoupInfo dSoupInfo = (DSoupInfo) command;
			soup = dSoupInfo.getSoup();
		} else if (DIndexDescription.COMMAND.equals(cmd)) {
			DIndexDescription dIndexDescription = (DIndexDescription) command;
			soup.setIndexes(dIndexDescription.getIndexes());
		} else if (DSoupIDs.COMMAND.equals(cmd)) {
			DSoupIDs dSoupIDs = (DSoupIDs) command;
			Collection<SoupEntry> entries = soup.getEntries();
			entries.clear();
			SoupEntry entry;
			for (Integer id : dSoupIDs.getIDs()) {
				entry = new SoupEntry();
				entry.setId(id);
				entries.add(entry);
			}
		} else if (DEntry.COMMAND.equals(cmd)) {
			DEntry dEntry = (DEntry) command;
			soup.addEntry(dEntry.getEntry());
		} else if (DBackupSoupDone.COMMAND.equals(cmd)) {
			backupDone = true;
		} else if (DSoupNotDirty.COMMAND.equals(cmd)) {
			backupDone = true;
		}
	}

	@Override
	public void commandSending(IDockCommandToNewton command, int progress, int total) {
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		System.out.println("snt: " + command);
		final String cmd = command.getCommand();

		if (DDisconnect.COMMAND.equals(cmd)) {
			running = false;
		}
	}

	@Override
	public void commandEOF() {
	}

}
