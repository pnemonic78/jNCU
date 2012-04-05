package net.sf.jncu.protocol.data;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.mnp.MNPPacket;
import net.sf.jncu.cdil.mnp.MNPPacketListener;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.cdil.mnp.PacketLogger;
import net.sf.jncu.protocol.DockCommandListener;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.io.DGetStoreNames;
import net.sf.jncu.protocol.v1_0.io.DStoreNames;
import net.sf.jncu.protocol.v1_0.io.Store;
import net.sf.jncu.protocol.v1_0.query.DGetInheritance;
import net.sf.jncu.protocol.v1_0.session.DDisconnect;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.IconModule.IconModuleListener;
import net.sf.jncu.protocol.v2_0.io.DSetStoreGetNames;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled;

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
		pipe.setTimeout(Integer.MAX_VALUE);
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
		store = null;

		DGetStoreNames dGetStoreNames = new DGetStoreNames();
		pipe.write(dGetStoreNames);
		Thread.sleep(1000);

		System.out.println(store);
		while (running && (store == null))
			Thread.yield();
		DSetStoreGetNames dSetStoreGetNames = new DSetStoreGetNames();
		dSetStoreGetNames.setStore(store);
		pipe.write(dSetStoreGetNames);
		Thread.sleep(1000);

		DGetInheritance dGetInheritance = new DGetInheritance();
		pipe.write(dGetInheritance);
		Thread.sleep(1000);

		// DSetSoupGetInfo dSetSoupGetInfo = new DSetSoupGetInfo();
		// pipe.write(dSetSoupGetInfo);
		// Thread.sleep(1000);
		//
		// DLastSyncTime dLastSyncTime = new DLastSyncTime();
		// pipe.write(dLastSyncTime);
		// Thread.sleep(1000);

		DDisconnect dDisconnect = new DDisconnect();
		pipe.write(dDisconnect);

		while (running)
			Thread.yield();
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
		} else if (net.sf.jncu.protocol.v1_0.session.DOperationCanceled.COMMAND.equals(cmd)) {
			running = false;
		} else if (DStoreNames.COMMAND.equals(cmd)) {
			DStoreNames dStoreNames = (DStoreNames) command;
			store = dStoreNames.getDefaultStore();
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
