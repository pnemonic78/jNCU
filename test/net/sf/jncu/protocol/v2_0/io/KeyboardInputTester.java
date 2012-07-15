package net.sf.jncu.protocol.v2_0.io;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.cdil.mnp.EmptyPipe;
import net.sf.jncu.cdil.mnp.MNPPipe;

public class KeyboardInputTester implements WindowListener {

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
		input.getDialog().addWindowListener(this);
	}

	public void run() {
		input.start();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
