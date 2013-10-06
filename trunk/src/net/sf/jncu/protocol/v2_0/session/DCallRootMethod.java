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

import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.protocol.BaseDockCommandToNewton;

/**
 * This command asks the Newton to call the specified root method. The return
 * value from the method is sent to the desktop with a <tt>kDCallResult</tt>
 * command.
 * 
 * <pre>
 * 'crmd' ('crmf')
 * length
 * method name symbol
 * args array
 * </pre>
 * 
 * @author moshew
 */
public class DCallRootMethod extends BaseDockCommandToNewton {

	/** <tt>kDCallRootMethod</tt> */
	public static final String COMMAND = "crmf";

	private String methodName;
	private Object[] args;

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
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Set the method name.
	 * 
	 * @param methodName
	 *            the method name.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
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
		encoder.flatten(new NSOFSymbol(getMethodName()), data);
		encoder.flatten(NSOFEncoder.toNS(getArguments()), data);
	}
}
