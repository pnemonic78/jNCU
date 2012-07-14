package net.sf.jncu.protocol.v2_0.io;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.cdil.mnp.EmptyPipe;
import net.sf.jncu.cdil.mnp.MNPPipe;

public class KeyboardInputTester {

	private CDLayer layer;
	private MNPPipe pipe;
	private KeyboardInput input;

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		KeyboardInputTester tester = new KeyboardInputTester();
		tester.run();
	}

	public KeyboardInputTester() throws PlatformException, ServiceNotSupportedException {
		super();
		this.layer = CDLayer.getInstance();
		this.pipe = new EmptyPipe(layer);
		this.input = new KeyboardInput(pipe);
	}

	public void run() {
		input.start();
	}
}
