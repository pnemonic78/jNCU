/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDSetVBOCompression</tt><br>
 * This command controls which VBOs are sent compressed to the desktop. VBO can
 * always be sent compressed, never compressed or only package VBOs sent
 * compressed.
 * 
 * <pre>
 * 'cvbo'
 * length
 * what
 * </pre>
 * 
 * @see eUncompressedVBOs
 * @see eCompressedPackagesOnly
 * @see eCompressedVBOs
 * @author moshew
 */
public class DSetVBOCompression extends DockCommandToNewton {

	public static final String COMMAND = "cvbo";
	/**
	 * VBO sent uncompressed.
	 */
	public static final int eUncompressedVBOs = 0;
	/**
	 * Only package VBOs sent compressed.
	 */
	public static final int eCompressedPackagesOnly = 1;
	/**
	 * VBO sent compressed.
	 */
	public static final int eCompressedVBOs = 2;

	private int compression;

	/**
	 * Creates a new command.
	 */
	public DSetVBOCompression() {
		super(COMMAND);
	}

	/**
	 * Get the compression.
	 * 
	 * @return the compression.
	 */
	public int getCompression() {
		return compression;
	}

	/**
	 * Set the compression.
	 * 
	 * @param compression
	 *            the compression.
	 */
	public void setCompression(int compression) {
		this.compression = compression;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		ntohl(getCompression(), data);
	}

}
