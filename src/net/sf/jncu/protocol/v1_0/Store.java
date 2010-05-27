/**
 * 
 */
package net.sf.jncu.protocol.v1_0;

import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.newton.stream.NSOFImmediate;
import net.sf.jncu.newton.stream.NSOFInteger;
import net.sf.jncu.newton.stream.NSOFNil;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.newton.stream.NSOFString;
import net.sf.jncu.newton.stream.NSOFSymbol;
import net.sf.jncu.newton.stream.NSOFTrue;

/**
 * Store information.
 * <p>
 * Each array slot contains the following information about a store:<br>
 * <code>
 * {<br>
 * &nbsp;&nbsp;name: "",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;totalsize: 1234,<br>
 * &nbsp;&nbsp;usedsize: 1234,<br>
 * &nbsp;&nbsp;kind: "",<br>
 * &nbsp;&nbsp;info: {store info frame},<br>
 * &nbsp;&nbsp;readOnly: true,<br>
 * &nbsp;&nbsp;defaultStore: true,		// only for the default store<br>
 * &nbsp;&nbsp;storePassword: password  // only if a store password has been set<br>
 * }</code>
 * 
 * @author moshew
 */
public class Store {

	protected static final NSOFSymbol ENTRY_NAME = new NSOFSymbol("name");
	protected static final NSOFSymbol ENTRY_SIGNATURE = new NSOFSymbol("signature");
	protected static final NSOFSymbol ENTRY_TOTALSIZE = new NSOFSymbol("totalsize");
	protected static final NSOFSymbol ENTRY_USEDSIZE = new NSOFSymbol("usedsize");
	protected static final NSOFSymbol ENTRY_KIND = new NSOFSymbol("kind");
	protected static final NSOFSymbol ENTRY_INFO = new NSOFSymbol("info");
	protected static final NSOFSymbol ENTRY_READONLY = new NSOFSymbol("readOnly");
	protected static final NSOFSymbol ENTRY_DEFAULT = new NSOFSymbol("defaultStore");
	protected static final NSOFSymbol ENTRY_PASSWORD = new NSOFSymbol("storePassword");

	private String name;
	private int signature;
	private int totalSize;
	private int usedSize;
	private String kind;
	private NSOFFrame info;
	private boolean readOnly;
	private boolean defaultStore;
	private int password;

	/**
	 * Creates a new store.
	 */
	public Store() {
		super();
	}

	/**
	 * Get the frame.
	 * 
	 * @return the frame.
	 */
	public NSOFFrame toFrame() {
		NSOFFrame frame;
		frame = new NSOFFrame();
		frame.put(ENTRY_NAME, new NSOFString(getName()));
		frame.put(ENTRY_SIGNATURE, new NSOFInteger(getSignature()));
		frame.put(ENTRY_TOTALSIZE, new NSOFInteger(getTotalSize()));
		frame.put(ENTRY_USEDSIZE, new NSOFInteger(getUsedSize()));
		frame.put(ENTRY_KIND, new NSOFString(getKind()));
		frame.put(ENTRY_INFO, getInfo());
		frame.put(ENTRY_READONLY, isReadOnly() ? new NSOFTrue() : new NSOFNil());
		frame.put(ENTRY_DEFAULT, isDefaultStore() ? new NSOFTrue() : new NSOFNil());
		if (getPassword() != 0) {
			frame.put(ENTRY_PASSWORD, new NSOFInteger(getPassword()));
		}
		return frame;
	}

	/**
	 * Decode the frame.
	 * 
	 * @param frame
	 *            the frame.
	 */
	public void decodeFrame(NSOFFrame frame) {
		NSOFObject value;

		value = frame.get(ENTRY_DEFAULT);
		setDefaultStore(false);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setDefaultStore(imm.isTrue());
		}

		value = frame.get(ENTRY_INFO);
		setInfo((NSOFFrame) value);

		value = frame.get(ENTRY_KIND);
		setKind(null);
		if (value != null) {
			NSOFString s = (NSOFString) value;
			setKind(s.getValue());
		}

		value = frame.get(ENTRY_NAME);
		setName(null);
		if (value != null) {
			NSOFString s = (NSOFString) value;
			setName(s.getValue());
		}

		value = frame.get(ENTRY_PASSWORD);
		setPassword(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setPassword(imm.getValue());
		}

		value = frame.get(ENTRY_READONLY);
		setReadOnly(false);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setReadOnly(imm.isTrue());
		}

		value = frame.get(ENTRY_SIGNATURE);
		setSignature(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setSignature(imm.getValue());
		}

		value = frame.get(ENTRY_TOTALSIZE);
		setTotalSize(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setTotalSize(imm.getValue());
		}

		value = frame.get(ENTRY_USEDSIZE);
		setUsedSize(0);
		if (value != null) {
			NSOFImmediate imm = (NSOFImmediate) value;
			setUsedSize(imm.getValue());
		}
	}

	/**
	 * Get the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the signature.
	 * 
	 * @return the signature.
	 */
	public int getSignature() {
		return signature;
	}

	/**
	 * Set the signature.
	 * 
	 * @param signature
	 *            the signature.
	 */
	public void setSignature(int signature) {
		this.signature = signature;
	}

	/**
	 * Get the total size.
	 * 
	 * @return the size.
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * Set the total size.
	 * 
	 * @param totalSize
	 *            the size.
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * Get the used size.
	 * 
	 * @return the size.
	 */
	public int getUsedSize() {
		return usedSize;
	}

	/**
	 * Set the used size.
	 * 
	 * @param usedSize
	 *            the size.
	 */
	public void setUsedSize(int usedSize) {
		this.usedSize = usedSize;
	}

	/**
	 * Get the kind.
	 * 
	 * @return the kind.
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * Set the kind.
	 * 
	 * @param kind
	 *            the kind.
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * Get the information.
	 * 
	 * @return the information.
	 */
	public NSOFFrame getInfo() {
		return info;
	}

	/**
	 * Set the information.
	 * 
	 * @param info
	 *            the information.
	 */
	public void setInfo(NSOFFrame info) {
		this.info = info;
	}

	/**
	 * Is read-only?
	 * 
	 * @return true if read-only.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Set read-only.
	 * 
	 * @param readOnly
	 *            true if read-only.
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * Is default store?
	 * 
	 * @return true if the default store.
	 */
	public boolean isDefaultStore() {
		return defaultStore;
	}

	/**
	 * Set as the default store.
	 * 
	 * @param defaultStore
	 *            true if the default store.
	 */
	public void setDefaultStore(boolean defaultStore) {
		this.defaultStore = defaultStore;
	}

	/**
	 * Get the password.
	 * 
	 * @return the password.
	 */
	public int getPassword() {
		return password;
	}

	/**
	 * Set the password.
	 * 
	 * @param password
	 *            the password.
	 */
	public void setPassword(int password) {
		this.password = password;
	}

}
