package net.sf.jncu.protocol;

import net.sf.lang.ControlCharacter;

/**
 * Docking frame constants.
 * 
 * @author moshew
 */
public class DockingFrame {

	/** Frame start. */
	public static final byte[] FRAME_START = { ControlCharacter.SYN, ControlCharacter.DLE, ControlCharacter.STX };
	/** Frame end. */
	public static final byte[] FRAME_END = { ControlCharacter.DLE, ControlCharacter.ETX };

}
