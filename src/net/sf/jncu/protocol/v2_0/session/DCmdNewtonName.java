package net.sf.jncu.protocol.v2_0.session;

import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * Command to detail Newton's name.
 * 
 * @author moshew
 */
public class DCmdNewtonName extends DockCommandFromNewton {

	private String name;
	private Object version;

	// INFO_SYMBOLS = [
	// :newton_unique_id,
	// :manufacturer_id,
	// :machine_type,
	// :rom_version,
	// :rom_stage,
	// :ram_size,
	// :screen_height,
	// :screen_width,
	// :system_update_version,
	// :object_system_version,
	// :internal_store_signature,
	// :vertical_screen_resolution,
	// :horizontal_screen_resolution,
	// :screen_depth
	// ]

	/**
	 * Creates a new command.
	 */
	public DCmdNewtonName() {
		super(DockCommandSession.NewtonToDesktop.kDNewtonName);
	}

	@Override
	protected void decodeData(byte[] data, int offset, int length) {
		// def from_binary(b)
		// super(b)
		// c = b.unpack("A4A4A4N N NNNNNNNNNNNNNN")
		// version_info_length = c[4]
		// (0 ... INFO_SYMBOLS.length).each do |i|
		// @version_info[INFO_SYMBOLS[i]] = c[i + 5]
		// end
		// @name = String.from_utf16be(b[0x5c..-1])
		// end
	}
}
