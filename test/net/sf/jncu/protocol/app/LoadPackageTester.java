package net.sf.jncu.protocol.app;

import java.io.File;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.mnp.MNPPacket;
import net.sf.jncu.cdil.mnp.MNPPacketListener;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.cdil.mnp.PacketLogger;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.IconModule.IconModuleListener;
import net.sf.jncu.protocol.v2_0.app.LoadPackage;

/**
 * Test to load a package into the Newton.
 * 
 * @author moshew
 */
public class LoadPackageTester implements IconModuleListener, MNPPacketListener {

	private String portName;
	private String pkgPath;
	private LoadPackage pkg;
	private boolean loading = false;
	private CDLayer layer;
	private MNPPipe pipe;
	private PacketLogger logger;

	public LoadPackageTester() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		LoadPackageTester tester = new LoadPackageTester();
		if (args.length < 2) {
			System.out.println("args: port file");
			System.exit(1);
			return;
		}
		tester.setPortName(args[0]);
		tester.setPath(args[1]);
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
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public void setPath(String path) {
		this.pkgPath = path;
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
		// Wait for a connect request
		while (layer.getState() == CDState.LISTENING) {
			Thread.yield();
		}
	}

	public void run() throws Exception {
		// if (layer.getState() == CDState.CONNECT_PENDING) {
		pipe.accept();

		File file = new File(pkgPath);

		loading = true;
		pkg = new LoadPackage(pipe, false);
		pkg.loadPackage(file);
		while (loading)
			Thread.yield();
		// }
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
		loading = false;
	}

	@Override
	public void cancelModule(IconModule module) {
		System.out.println("cancelModule");
		loading = false;
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

}
