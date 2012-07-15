package net.sf.jncu.protocol.v2_0.io;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import net.sf.jncu.cdil.CDILNotInitializedException;
import net.sf.jncu.cdil.CDLayer;
import net.sf.jncu.cdil.PlatformException;
import net.sf.jncu.cdil.ServiceNotSupportedException;
import net.sf.jncu.cdil.mnp.EmptyPipe;
import net.sf.jncu.cdil.mnp.MNPPipe;
import net.sf.jncu.cdil.mnp.MNPSerialPort;

public class KeyboardInputTester implements WindowListener, KeyboardInputListener {

	private String portName;
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
		if (args.length > 0) {
			tester.setPortName(args[0]);
		}
		tester.run();
	}

	public KeyboardInputTester() throws PlatformException, ServiceNotSupportedException, CDILNotInitializedException {
		super();
		this.layer = CDLayer.getInstance();
		if (portName == null)
			this.pipe = new EmptyPipe(layer);
		else
			this.pipe = layer.createMNPSerial(portName, MNPSerialPort.BAUD_38400);
		this.input = new KeyboardInput(pipe);
		input.getDialog().addWindowListener(this);
		input.getDialog().addInputListener(this);
	}

	public void setPortName(String portName) {
		this.portName = portName;
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

	@Override
	public void charTyped(KeyEvent ke) {
		if (ke.getID() != KeyEvent.KEY_PRESSED)
			return;
		char keyChar = DKeyboardChar.toNewtonChar(ke.getKeyChar(), ke.getKeyCode());
		// Ignore unknown characters.
		if (keyChar == 0)
			return;
		int keyFlags = DKeyboardChar.toNewtonState(ke.getModifiers());
		System.out.println("charTyped keyChar=" + keyChar + " keyFlags=" + keyFlags);
	}

	@Override
	public void stringTyped(String text) {
		System.out.println("stringTyped [" + text + "]");
	}
}
