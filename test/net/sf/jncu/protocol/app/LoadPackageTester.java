package net.sf.jncu.protocol.app;

import java.io.File;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.cdil.CDState;
import net.sf.jncu.cdil.mnp.MNPSerialPort;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.IconModule.IconModuleListener;
import net.sf.jncu.protocol.v2_0.app.LoadPackage;

/**
 * Test to load a package into the Newton.
 * 
 * @author moshew
 */
public class LoadPackageTester implements IconModuleListener {

	private String portName;
	private String pkgPath;
	private LoadPackage pkg;
	private boolean loading = false;

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
			tester.run();
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

	public void run() throws Exception {
		CDLayer layer = null;
		CDPipe<?> pipe = null;
		try {
			layer = CDLayer.getInstance();
			// Initialize the library
			layer.startUp();
			// Create a connection object
			pipe = layer.createMNPSerial(portName, MNPSerialPort.BAUD_38400);
			pipe.startListening();
			// Wait for a connect request
			while (layer.getState() == CDState.LISTENING) {
				Thread.yield();
			}
			if (layer.getState() == CDState.CONNECT_PENDING) {
				pipe.accept(); // Accept the connect request

				File file = new File(pkgPath);

				loading = true;
				pkg = new LoadPackage(pipe, false);
				pkg.loadPackage(file);
				while (loading)
					Thread.yield();
			}
		} finally {
			if (pipe != null) {
				pipe.disconnect(); // Break the connection.
				pipe.dispose(); // Delete the pipe object
			}
			if (layer != null) {
				layer.shutDown(); // Close the library
			}
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

}
