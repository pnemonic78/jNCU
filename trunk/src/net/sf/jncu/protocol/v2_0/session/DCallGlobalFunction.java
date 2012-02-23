/*
 * Source file of the jNCU project.
 * Copyright (c) 2010. All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 * 
 * http://sourceforge.net/projects/jncu
 * 
 * http://jncu.sourceforge.net/
 *
 * Contributor(s):
 *   Moshe Waisberg
 * 
 */
package net.sf.jncu.protocol.v2_0.session;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.newton.stream.*;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
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

	/** <tt>kDCallGlobalFunction</tt> */
	public static final String COMMAND = "cgfn";

	private String funcName;
	private Object[] args;

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
	public String getFunctionName() {
		return funcName;
	}

	/**
	 * Set the function name.
	 * 
	 * @param funcName
	 *            the function name.
	 */
	public void setFunctionName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * Get the array of arguments.
	 * 
	 * @return the arguments.
	 */
	public Object[] getArguments() {
		return args;
	}

	/**
	 * Set the array of arguments.
	 * 
	 * @param args
	 *            the arguments.
	 */
	public void setArguments(Object[] args) {
		this.args = args;
	}

	@Override
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(new NSOFSymbol(getFunctionName()), data);
		encoder.encode(NSOFEncoder.toNS(getArguments()), data);
	}
}
