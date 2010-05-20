/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

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

	/**
	 * Creates a new command.
	 */
	public DSetVBOCompression() {
		super(COMMAND);
	}

}
