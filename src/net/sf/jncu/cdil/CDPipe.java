package net.sf.jncu.cdil;

/**
 * CDIL pipe.
 * 
 * @author moshew
 */
public abstract class CDPipe {

	private CDState state = CDState.DISCONNECTED;

	/**
	 * Creates a new pipe.
	 */
	public CDPipe() {
		super();
	}

	/**
	 * Get the state.
	 * 
	 * @return the state.
	 */
	public CDState getState() {
		return state;
	}

	/**
	 * Set the state.
	 * 
	 * @param state
	 *            the state.
	 */
	void setState(CDState state) {
		this.state = state;
	}
}
