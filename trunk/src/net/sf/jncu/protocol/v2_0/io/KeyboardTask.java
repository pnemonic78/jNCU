package net.sf.jncu.protocol.v2_0.io;

import java.util.TimerTask;

/**
 * @author Moshe
 */
public class KeyboardTask extends TimerTask {

	private final KeyboardInput input;

	/**
	 * Constructs a new task.
	 * 
	 * @param input
	 *            the owner input.
	 */
	public KeyboardTask(KeyboardInput input) {
		super();
		if (input == null)
			throw new IllegalArgumentException("(Keyboard input required");
		this.input = input;
	}

	@Override
	public void run() {
		// Write the buffer (either char or string) to sender.
		input.flush();
	}

}
