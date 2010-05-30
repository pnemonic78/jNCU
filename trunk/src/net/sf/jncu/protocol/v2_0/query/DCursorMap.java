/**
 * 
 */
package net.sf.jncu.protocol.v2_0.query;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFObject;

/**
 * <tt>kDCursorMap</tt><br>
 * Applies the specified function to each of the cursor's entries in turn and
 * returns an array of the results. A <tt>kDRefResult</tt> is returned. See
 * MapCursor in NPG.
 * 
 * <pre>
 * 'cmap'
 * length
 * cursor id
 * function
 * </pre>
 * 
 * @author moshew
 */
public class DCursorMap extends DCursor {

	public static final String COMMAND = "cmap";

	private NSOFObject function;

	/**
	 * Creates a new command.
	 */
	public DCursorMap() {
		super(COMMAND);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.DockCommandToNewton#getCommandData()
	 */
	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getFunction(), data);
	}

	/**
	 * Get the function.
	 * 
	 * @return the function.
	 */
	public NSOFObject getFunction() {
		return function;
	}

	/**
	 * Set the function.
	 * 
	 * @param function
	 *            the function.
	 */
	public void setFunction(NSOFObject function) {
		this.function = function;
	}

}
