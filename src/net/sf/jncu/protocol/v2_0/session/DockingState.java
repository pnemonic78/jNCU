package net.sf.jncu.protocol.v2_0.session;

/** State for handshaking protocol. */
public enum DockingState {
	/** Listen for LR from Newton. */
	HANDSHAKE_LR_LISTEN,
	/** Newton sends LR. */
	HANDSHAKE_LR_RECEIVED,
	/** Send LR to Newton. */
	HANDSHAKE_LR_SENDING,
	/** Newton sends LA (for LR in previous step). */
	HANDSHAKE_LR_SENT,
	/** Listen for LT (kDRequestToDock) from Newton. */
	HANDSHAKE_RTDK_LISTEN,
	/**
	 * Newton sends LT (kDRequestToDock).<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_RTDK_RECEIVED,
	/** Send LT (kDInitiateDocking) to Newton. */
	HANDSHAKE_DOCK_SENDING,
	/** Newton sends LA (for LT in previous step). */
	HANDSHAKE_DOCK_SENT,
	/** List for LT (DNewtonName) from Newton. */
	HANDSHAKE_NAME_LISTEN,
	/**
	 * Newton sends LT (kDNewtonName).<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_NAME_RECEIVED,
	/** Send LT (kDDesktopInfo) to Newton. */
	HANDSHAKE_DINFO_SENDING,
	/** Newton sends LA (for LT in previous step). */
	HANDSHAKE_DINFO_SENT,
	/** List for LT (kDNewtonInfo) from Newton. */
	HANDSHAKE_NINFO_LISTEN,
	/**
	 * Newton sends LT (kDNewtonInfo).<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_NINFO_RECEIVED,
	/** Send LT (kDWhichIcons) to Newton (optional). */
	HANDSHAKE_ICONS_SENDING,
	/** Newton sends LA (for LT in previous step). */
	HANDSHAKE_ICONS_SENT,
	/** List for LT (kDResult) from Newton (for kDWhichIcons). */
	HANDSHAKE_ICONS_RESULT_LISTEN,
	/**
	 * Newton sends LT (kDResult) (for kDWhichIcons in previous step).<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_ICONS_RESULT_RECEIVED,
	/** Send LT (kDSetTimeout) to Newton. */
	HANDSHAKE_TIMEOUT_SENDING,
	/** Newton sends LA (for LT in previous step). */
	HANDSHAKE_TIMEOUT_SENT,
	/** List for LT (kDPassword) from Newton. */
	HANDSHAKE_PASS_LISTEN,
	/**
	 * Newton sends LT (kDPassword).<br>
	 * Send LA to Newton.
	 */
	HANDSHAKE_PASS_RECEIVED,
	/** Send LT (kDPassword) to Newton. */
	HANDSHAKE_PASS_SENDING,
	/** Newton sends LA (for LT in previous step). */
	HANDSHAKE_PASS_SENT,
	/** Finished handshaking. */
	HANDSHAKE_DONE,
	/** Connect request accepted. */
	ACCEPTED,
	/** Idle. */
	IDLE,
	/** Disconnecting. */
	DISCONNECTING,
	/** Disconnected. */
	DISCONNECTED
}
