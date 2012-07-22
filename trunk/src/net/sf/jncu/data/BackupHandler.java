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
package net.sf.jncu.data;

import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;

/**
 * Backup event listener.<br>
 * Archive parts are written via events, analogous to
 * {@link org.xml.sax.helpers.DefaultHandler}.
 * 
 * @author Moshe
 */
public interface BackupHandler {

	/**
	 * Receive notification of the beginning of the backup archive.
	 * 
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void startBackup() throws BackupException;

	/**
	 * Receive notification of the end of the backup archive.
	 * 
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void endBackup() throws BackupException;

	/**
	 * Receive notification of device information.
	 * 
	 * @param info
	 *            the Newton device information.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void deviceInformation(NewtonInfo info) throws BackupException;

	/**
	 * Receive notification of the start of a store.
	 * 
	 * @param store
	 *            the store.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void startStore(Store store) throws BackupException;

	/**
	 * Receive notification of the end of a store.
	 * 
	 * @param store
	 *            the store.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void endStore(Store store) throws BackupException;

	/**
	 * Receive notification of the start of a package.
	 * 
	 * @param store
	 *            the owner store.
	 * @param pkg
	 *            the package.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void startPackage(Store store, ApplicationPackage pkg) throws BackupException;

	/**
	 * Receive notification of the end of a package.
	 * 
	 * @param store
	 *            the owner store.
	 * @param pkg
	 *            the package.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void endPackage(Store store, ApplicationPackage pkg) throws BackupException;

	/**
	 * Receive notification of the start of a soup.
	 * 
	 * @param store
	 *            the owner store.
	 * @param soup
	 *            the soup.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void startSoup(Store store, Soup soup) throws BackupException;

	/**
	 * Receive notification of the end of a soup.
	 * 
	 * @param store
	 *            the owner store.
	 * @param soup
	 *            the soup.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void endSoup(Store store, Soup soup) throws BackupException;

	/**
	 * Receive notification of the start of a soup entry.
	 * 
	 * @param store
	 *            the owner store.
	 * @param soup
	 *            the owner soup.
	 * @param entry
	 *            the entry.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void startSoupEntry(Store store, Soup soup, SoupEntry entry) throws BackupException;

	/**
	 * Receive notification of the end of a soup entry.
	 * 
	 * @param store
	 *            the owner store.
	 * @param soup
	 *            the owner soup.
	 * @param entry
	 *            the entry.
	 * @throws BackupException
	 *             if a backup error occurs.
	 */
	public void endSoupEntry(Store store, Soup soup, SoupEntry entry) throws BackupException;
}
