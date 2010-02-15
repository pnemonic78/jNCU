package net.sf.jncu.cdil;

/**
 * NCU Connection state.
 * 
 * @author moshew
 */
public enum CDILState {

	/** 0 CDIL is uninitialised. */
	Uninitialized,
	/** 1 tried to bring up connection, but it failed. */
	InvalidConnection,
	/** 2 is not connected. */
	Disconnected,
	/** 3 is listening for a connection. */
	Listening,
	/** 4 A connection is pending. */
	ConnectPending,
	/** 5 is connected. */
	Connected,
	/** 6 is either reading or writing. */
	Busy,
	/** 7 is currently aborting. */
	Aborting,
	/** 8 is currently starting up. */
	Startup,
	/** 50 Users can add states after this point. */
	Userstate;

}
