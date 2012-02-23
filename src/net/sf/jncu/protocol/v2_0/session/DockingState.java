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
package net.sf.jncu.protocol.v2_0.session;

/** State for handshaking protocol. */
public enum DockingState {
	/** Initial state. */
	NONE,
	/** Link request from Newton. */
	HANDSHAKE_LR,
	/**
	 * Listen for <tt>kDRequestToDock</tt> from Newton.<br>
	 * Newton sent <tt>kDRequestToDock</tt>.<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_RTDK,
	/** Send <tt>kDInitiateDocking</tt> to Newton. */
	HANDSHAKE_DOCK,
	/**
	 * Listen for <tt>kDNewtonName</tt> from Newton.<br>
	 * Newton sent <tt>kDNewtonName</tt>.<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_NNAME,
	/** Send <tt>kDDesktopInfo</tt> to Newton. */
	HANDSHAKE_DINFO,
	/**
	 * Listen for <tt>kDNewtonInfo</tt> from Newton.<br>
	 * Newton sent <tt>kDNewtonInfo</tt>.<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_NINFO,
	/** Send <tt>kDWhichIcons</tt> to Newton (optional). */
	HANDSHAKE_ICONS,
	/**
	 * Listen for <tt>kDResult</tt> from Newton (for <tt>kDWhichIcons</tt>).<br>
	 * Newton sent <tt>kDResult</tt> (for <tt>kDWhichIcons</tt> in previous
	 * step).<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_ICONS_RESULT,
	/** Send <tt>kDSetTimeout</tt> to Newton. */
	HANDSHAKE_TIMEOUT,
	/**
	 * Listen for <tt>kDPassword</tt> from Newton.<br>
	 * Newton sent <tt>kDPassword</tt>.<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_PASS,
	/** Send <tt>kDPassword</tt> to Newton. */
	HANDSHAKE_PASS_REPLY,
	/** Finished handshaking. */
	HANDSHAKE_DONE,
	/** Disconnecting. */
	DISCONNECTING,
	/** Disconnected. */
	DISCONNECTED
}
