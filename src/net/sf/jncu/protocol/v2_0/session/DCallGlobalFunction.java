/**
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jncu.newton.stream.NSOFArray;
import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFSymbol;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDCallGlobalFunction</tt><br>
 * This command asks the Newton to call the specified function and return it's
 * result. This function must be a global function. The return value from the
 * function is sent to the desktop with a <tt>kDCallResult</tt> command.
 * 
 * <pre>
 * 'cgfn'
 * length
 * function name symbol
 * args array
 * </pre>
 * 
 * @author moshew
 */
public class DCallGlobalFunction extends DockCommandToNewton {

	public static final String COMMAND = "cgfn";

	private NSOFSymbol funcName;
	private NSOFArray args;

	/**
	 * Creates a new command.
	 */
	public DCallGlobalFunction() {
		super(COMMAND);
	}

	/**
	 * Get the function name.
	 * 
	 * @return the function name.
	 */
	public NSOFSymbol getFunctionName() {
		return funcName;
	}

	/**
	 * Set the function name.
	 * 
	 * @param funcName
	 *            the function name.
	 */
	public void setFunctionName(NSOFSymbol funcName) {
		this.funcName = funcName;
	}

	/**
	 * Set the function name.
	 * 
	 * @param funcName
	 *            the function name.
	 */
	public void setFunctionName(String funcName) {
		setFunctionName(new NSOFSymbol(funcName));
	}

	/**
	 * Get the array of arguments.
	 * 
	 * @return the arguments.
	 */
	public NSOFArray getArguments() {
		return args;
	}

	/**
	 * Set the array of arguments.
	 * 
	 * @param args
	 *            the arguments.
	 */
	public void setArguments(NSOFArray args) {
		this.args = args;
	}

	@Override
	protected ByteArrayOutputStream getCommandData() throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getFunctionName(), data);
		encoder.encode(getArguments(), data);
		return data;
	}}
