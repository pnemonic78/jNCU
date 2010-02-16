package net.sf.jncu.cdil;

/**
 * NCU Connection state.
 * 
 * @author moshew
 */
public enum CDState {

	/** 0 CDIL is uninitialised. */
	UNINITIALIZED,
	/** 1 tried to bring up connection, but it failed. */
	INVALID_CONNECTION,
	/** 2 is not connected. */
	DISCONNECTED,
	/** 3 is listening for a connection. */
	LISTENING,
	/** 4 A connection is pending. */
	CONNECT_PENDING,
	/** 5 is connected. */
	CONNECTED,
	/** 6 is either reading or writing. */
	BUSY,
	/** 7 is currently aborting. */
	ABORTING,
	/** 8 is currently starting up. */
	STARTUP,
	/** 50 Users can add states after this point. */
	USER;

}
