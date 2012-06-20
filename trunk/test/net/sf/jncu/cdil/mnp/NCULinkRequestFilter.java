package net.sf.jncu.cdil.mnp;

import java.io.OutputStream;

import net.sf.jncu.io.ReplaceOutputStream;

/**
 * NCU serial packet filter.
 * 
 * @author mwaisberg
 * 
 */
public class NCULinkRequestFilter extends ReplaceOutputStream {

	/** Bad LR from NCU to Newton. */
	protected static final byte[] LR_FROM_NCU_FIND = { 0x16, 0x10, 0x02, 0x1D, 0x01, 0x02, 0x01, 0x06, 0x01, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, 0x02, 0x01, 0x02, 0x03, 0x01,
			0x08, 0x04, 0x02, 0x40, 0x00, 0x08, 0x01, 0x03, 0x0E, 0x04, 0x03, 0x04, 0x00, (byte) 0xFA, 0x10, 0x03, (byte) 0x8A, 0x2E };

	/** Normal LR that should be sent to Newton. */
	protected static final byte[] LR_FROM_PC_REPLACE = { 22, 16, 2, 23, 1, 2, 1, 6, 1, 0, 0, 0, 0, -1, 2, 1, 2, 3, 1, 8, 4, 2, 0, 1, 8, 1, 3, 16, 3, -61, 75 };

	/**
	 * Creates a new filter.
	 * 
	 * @param out
	 *            the output.
	 */
	public NCULinkRequestFilter(OutputStream out) {
		super(out, LR_FROM_NCU_FIND, LR_FROM_PC_REPLACE);
	}
}
