package net.sf.jncu.protocol.v2_0.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDKeyboardChar</tt><br>
 * This command sends 1 character to the Newton for processing. The char is a 2
 * byte Unicode character + a 2 byte state. The state is defined as follows:
 * <ol>
 * <li>Bit 1 = command key down
 * </ol>
 * 
 * <pre>
 * 'kbdc'
 * length
 * char
 * state
 * </pre>
 * 
 * @author moshew
 */
public class DKeyboardChar extends DockCommandToNewton {

	public static final String COMMAND = "kbdc";

	/** Command key down. */
	public static final int STATE_COMMAND_DOWN = 1;

	private char c;
	private int state;

	/**
	 * Creates a new command.
	 */
	public DKeyboardChar() {
		super(COMMAND);
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ntohl(((getCharacter() & 0xFFFF) << 16) | (getState() & 0xFFFF), data);
		return data;
	}

	/**
	 * Get the Unicode character.
	 * 
	 * @return the character.
	 */
	public char getCharacter() {
		return c;
	}

	/**
	 * Set the Unicode character.
	 * 
	 * @param c
	 *            the character.
	 */
	protected void setCharacter(char c) {
		this.c = c;
	}

	/**
	 * Get the state.
	 * 
	 * @return the state.
	 */
	public int getState() {
		return state;
	}

	/**
	 * Set the state.
	 * 
	 * @param state
	 *            the state.
	 */
	public void setState(int state) {
		this.state = state;
	}

}
