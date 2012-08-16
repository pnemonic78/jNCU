package net.sf.jncu.protocol.sync;

import java.io.File;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.mnp.MNPPacket;
import net.sf.jncu.cdil.mnp.MNPPacketListener;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.cdil.mnp.PacketLogger;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.Store;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v1_0.session.DOperationCanceled;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.app.AppName;
import net.sf.jncu.protocol.v2_0.sync.BackupModule;
import net.sf.jncu.protocol.v2_0.sync.BackupModule.BackupListener;
import net.sf.jncu.protocol.v2_0.sync.DGetSyncOptions;
import net.sf.jncu.protocol.v2_0.sync.DRequestToSync;
import net.sf.jncu.protocol.v2_0.sync.DSynchronize;

/**
 * Test to backup from the Newton.
 * 
 * @author moshew
 */
public class BackupTester implements BackupListener, MNPPacketListener, DockCommandListener {

	private String portName;
	private boolean running = false;
	private CDLayer layer;
	private MNPPipe pipe;
	private PacketLogger logger;
	// private Store store;
	// private List<Soup> soups;
	// private Soup soup;
	// private Integer result;
	private BackupModule backup;

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

		File userFolder = new File(System.getProperty("user.home"));
		File jncuFolder = new File(userFolder, "jNCU");
		jncuFolder.mkdirs();
		File f = new File(jncuFolder, "Backups/backup.zip");
		backup = new BackupModule(pipe, false);
		backup.addListener(this);
		backup.backup(f);

		// TODO - this is probably only for 1.x
		// DGetPatches dGetPatches = new DGetPatches();
		// pipe.write(dGetPatches);
		// Thread.sleep(2000);

		// Thread.sleep(30000);
		// backup.cancel();
		// exit();

		while (running)
			Thread.sleep(5000);
	}

	private void exit(boolean cancel) {
		running = false;
		try {
			if (cancel && pipe.isConnected()) {
				DOperationCanceled dOperationCanceled = new DOperationCanceled();
				pipe.write(dOperationCanceled);
				Thread.sleep(1000);
			}
			if (pipe.isConnected()) {
				DDisconnect dDisconnect = new DDisconnect();
				pipe.write(dDisconnect);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		System.out.println("BT successModule module=" + module);
		exit(false);
	}

	@Override
	public void cancelModule(IconModule module) {
		System.out.println("BT cancelModule module=" + module);
		exit(false);
	}

	@Override
	public void backupStore(BackupModule module, Store store) {
		System.out.println("BT backupStore module=" + module + " store=" + store);
	}

	@Override
	public void backupApplication(BackupModule module, Store store, AppName appName) {
		System.out.println("BT backupApplication module=" + module + " appName=" + appName);
	}

	@Override
	public void backupSoup(BackupModule module, Store store, AppName appName, Soup soup) {
		System.out.println("BT backupSoup module=" + module + " soup=" + soup);
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
		} else if (DRequestToSync.COMMAND.equals(cmd)) {
			DGetSyncOptions dGetSyncOptions = new DGetSyncOptions();
			try {
				pipe.write(dGetSyncOptions);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// TODO backup = new BackupModule(pipe, true);
		} else if (DSynchronize.COMMAND.equals(cmd)) {
			DGetSyncOptions dGetSyncOptions = new DGetSyncOptions();
			try {
				pipe.write(dGetSyncOptions);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// TODO backup = new BackupModule(pipe, true);
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
