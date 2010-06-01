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

import net.sf.jncu.newton.stream.NSOFArray;
import net.sf.jncu.newton.stream.NSOFEncoder;
import net.sf.jncu.newton.stream.NSOFSymbol;
import net.sf.jncu.protocol.DockCommandToNewton;

/**
 * <tt>kDCallRootMethod</tt><br>
 * This command asks the Newton to call the specified root method. The return
 * value from the method is sent to the desktop with a <tt>kDCallResult</tt>
 * command.
 * 
 * <pre>
 * 'crmd'
 * length
 * method name symbol
 * args array
 * </pre>
 * 
 * @author moshew
 */
public class DCallRootMethod extends DockCommandToNewton {

	public static final String COMMAND = "crmd";

	private NSOFSymbol methodName;
	private NSOFArray args;

	/**
	 * Creates a new command.
	 */
	public DCallRootMethod() {
		super(COMMAND);
	}

	/**
	 * Get the method name.
	 * 
	 * @return the method name.
	 */
	public NSOFSymbol getMethodName() {
		return methodName;
	}

	/**
	 * Set the method name.
	 * 
	 * @param methodName
	 *            the method name.
	 */
	public void setMethodName(NSOFSymbol methodName) {
		this.methodName = methodName;
	}

	/**
	 * Set the method name.
	 * 
	 * @param methodName
	 *            the method name.
	 */
	public void setMethodName(String methodName) {
		setMethodName(new NSOFSymbol(methodName));
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
	protected void writeCommandData(OutputStream data) throws IOException {
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.encode(getMethodName(), data);
		encoder.encode(getArguments(), data);
	}
}