/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.cdil;

/**
 * NCU Connection state.
 * 
 * @author moshew
 */
public enum CDState {

	/**
	 * Is uninitialised.<br>
	 * <tt>kCD_Uninitialized</tt>
	 */
	UNINITIALIZED,
	// /**
	// * Tried to bring up connection, but it failed.<br>
	// * <tt></tt>
	// */
	// INVALID_CONNECTION,
	/**
	 * Is not connected.<br>
	 * <tt>kCD_Disconnected</tt>
	 */
	DISCONNECTED,
	/**
	 * Is listening for a connection.<br>
	 * <tt>kCD_Listening</tt>
	 */
	LISTENING,
	/**
	 * A connection is pending.<br>
	 * <tt>kCD_ConnectPending</tt>
	 */
	CONNECT_PENDING,
	/**
	 * Is connected.<br>
	 * <tt>kCD_Connected</tt>
	 */
	CONNECTED,
	/**
	 * A disconnection is pending.<br>
	 * <tt>kCD_DisconnectPending</tt>
	 */
	DISCONNECT_PENDING;
	// /**
	// * Is either reading or writing.<br>
	// * <tt></tt>
	// */
	// BUSY,
	// /**
	// * Is currently aborting.<br>
	// * <tt></tt>
	// */
	// ABORTING,
	// /**
	// * Is currently starting up.<br>
	// * <tt></tt>
	// */
	// STARTUP,
	// /**
	// * 50 Users can add states after this point.<br>
	// * <tt></tt>
	// */
	// USER;

}
