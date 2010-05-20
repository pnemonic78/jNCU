package net.sf.jncu.protocol.v2_0;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFObject;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * Command to the Newton with a single NewtonScript object request.
 * 
 * @author moshew
 */
public abstract class DockCommandToNewtonScript extends DockCommandToNewton {

	private NSOFObject object;

	/**
	 * Creates a new command.
	 * 
	 * @param cmd
	 *            the command.
	 */
	public DockCommandToNewtonScript(String cmd) {
		super(cmd);
	}

	/**
	 * Set the object.
	 * 
	 * @param object
	 *            the object.
	 */
	public void setObject(NSOFObject object) {
		this.object = object;
	}

	/**
	 * Get the object.
	 * 
	 * @return the object.
	 */
	public NSOFObject getObject() {
		return object;
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getObject(), data);
		return data;
	}
}
