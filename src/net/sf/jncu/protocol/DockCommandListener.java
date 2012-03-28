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
package net.sf.jncu.protocol;

/**
 * Docking command listener interface.
 * 
 * @author moshew
 */
public interface DockCommandListener {

	/**
	 * Notification that a command was received.
	 * 
	 * @param command
	 *            the command.
	 */
	public void commandReceived(IDockCommandFromNewton command);

	/**
	 * Notification that a command is being received.<br>
	 * Used mainly to show a progress bar.
	 * <p>
	 * <em>If the command is small then this method might never be called.</em>
	 * 
	 * @param command
	 *            the command.
	 * @param progress
	 *            the number of bytes received.
	 * @param total
	 *            the total number of bytes to receive.
	 */
	public void commandReceiving(IDockCommandFromNewton command, int progress, int total);

	/**
	 * Notification that a command was sent.
	 * 
	 * @param command
	 *            the command.
	 */
	public void commandSent(IDockCommandToNewton command);

	/**
	 * Notification that a command is being received.<br>
	 * Used mainly to show a progress bar.
	 * <p>
	 * <em>If the command is small then this method might never be called.</em>
	 * 
	 * @param command
	 *            the command.
	 * @param progress
	 *            the number of bytes sent.
	 * @param total
	 *            the total number of bytes to send.
	 */
	public void commandSending(IDockCommandToNewton command, int progress, int total);

	/**
	 * Notification that no more commands will be available.
	 */
	public void commandEOF();

}
