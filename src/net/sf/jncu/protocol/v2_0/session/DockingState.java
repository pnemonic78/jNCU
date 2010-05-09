package net.sf.jncu.protocol.v2_0.session;

/** State for handshaking protocol. */
public enum DockingState {
	/** Listen for link request from Newton. */
	HANDSHAKE_LR_LISTEN,
	/** Newton sends LR. */
	HANDSHAKE_LR_RECEIVED,
	/** Send LR to Newton. */
	HANDSHAKE_LR_SENDING,
	/** Newton sends LA (for LR in previous step). */
	HANDSHAKE_LR_SENT,
	/** Listen for <tt>kDRequestToDock</tt> from Newton. */
	HANDSHAKE_RTDK_LISTEN,
	/**
	 * Newton sends <tt>kDRequestToDock</tt>.<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_RTDK_RECEIVED,
	/** Send <tt>kDInitiateDocking</tt> to Newton. */
	HANDSHAKE_DOCK_SENDING,
	/** Newton sends LA. */
	HANDSHAKE_DOCK_SENT,
	/** List for <tt>kDNewtonName</tt> from Newton. */
	HANDSHAKE_NAME_LISTEN,
	/**
	 * Newton sends <tt>kDNewtonName</tt>.<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_NAME_RECEIVED,
	/** Send LT (kDDesktopInfo) to Newton. */
	HANDSHAKE_DINFO_SENDING,
	/** Newton sends LA. */
	HANDSHAKE_DINFO_SENT,
	/** List for <tt>kDNewtonInfo</tt> from Newton. */
	HANDSHAKE_NINFO_LISTEN,
	/**
	 * Newton sends <tt>kDNewtonInfo</tt>.<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_NINFO_RECEIVED,
	/** Send <tt>kDWhichIcons</tt> to Newton (optional). */
	HANDSHAKE_ICONS_SENDING,
	/** Newton sends LA. */
	HANDSHAKE_ICONS_SENT,
	/** List for <tt>kDResult</tt> from Newton (for kDWhichIcons). */
	HANDSHAKE_ICONS_RESULT_LISTEN,
	/**
	 * Newton sends <tt>kDResult</tt> (for kDWhichIcons in previous step).<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_ICONS_RESULT_RECEIVED,
	/** Send <tt>kDSetTimeout</tt> to Newton. */
	HANDSHAKE_TIMEOUT_SENDING,
	/** Newton sends LA. */
	HANDSHAKE_TIMEOUT_SENT,
	/** List for <tt>kDPassword</tt> from Newton. */
	HANDSHAKE_PASS_LISTEN,
	/**
	 * Newton sends <tt>kDPassword</tt>.<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_PASS_RECEIVED,
	/** Send <tt>kDPassword</tt> to Newton. */
	HANDSHAKE_PASS_SENDING,
	/** Newton sends LA. */
	HANDSHAKE_PASS_SENT,
	/** Password mismatch. */
	HANDSHAKE_PASS_FAILED,
	/** Finished handshaking. */
	HANDSHAKE_DONE,
	/** Connect request accepted. */
	@Deprecated
	ACCEPTED,
	/** Idle. */
	@Deprecated
	IDLE,
	/** Disconnecting. */
	DISCONNECTING,
	/** Disconnected. */
	DISCONNECTED
}