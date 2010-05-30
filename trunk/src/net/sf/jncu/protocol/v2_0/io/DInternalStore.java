/**
 * 
 */
package net.sf.jncu.protocol.v2_0.io;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jncu.newton.stream.NSOFDecoder;
import net.sf.jncu.newton.stream.NSOFFrame;
import net.sf.jncu.protocol.DockCommandFromNewton;
import net.sf.jncu.protocol.v1_0.Store;

/**
 * <tt>kDInternalStore</tt><br>
 * This command returns information about the internal store. The info is in the
 * form of a frame that looks like this: <br>
 * <code>{<br>
 * &nbsp;&nbsp;name: "Internal",<br>
 * &nbsp;&nbsp;signature: 1234,<br>
 * &nbsp;&nbsp;totalsize: 1234,<br>
 * &nbsp;&nbsp;usedsize: 1234,<br>
 * &nbsp;&nbsp;kind: "Internal",<br>
 * }</code><br>
 * This is the same frame returned by <tt>kDStoreNames</tt> except that the
 * store info isn't returned.
 * 
 * <pre>
 * 'isto'
 * length
 * store frame
 * </pre>
 * 
 * @author moshew
 */
public class DInternalStore extends DockCommandFromNewton {

	public static final String COMMAND = "isto";

	private Store store;

	public DInternalStore() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.jncu.protocol.DockCommandFromNewton#decodeData(java.io.InputStream
	 * )
	 */
	@Override
	protected void decodeData(InputStream data) throws IOException {
		NSOFDecoder decoder = new NSOFDecoder();
		NSOFFrame frame = (NSOFFrame) decoder.decode(data);
		setStore(frame);
	}

	/**
	 * Get the store information.
	 * 
	 * @return the store.
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * Set the store information.
	 * 
	 * @param store
	 *            the store.
	 */
	protected void setStore(Store store) {
		this.store = store;
	}

	/**
	 * Set the store information.
	 * 
	 * @param frame
	 *            the store frame.
	 */
	protected void setStore(NSOFFrame frame) {
		Store store = new Store();
		store.decodeFrame(frame);
		setStore(store);
	}

}
