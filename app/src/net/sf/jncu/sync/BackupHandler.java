/*
 * Copyright 2010, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jncu.sync;

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
     * @throws BackupException if a backup error occurs.
     */
    void startBackup() throws BackupException;

    /**
     * Receive notification of the end of the backup archive.
     *
     * @throws BackupException if a backup error occurs.
     */
    void endBackup() throws BackupException;

    /**
     * Receive notification of modified time stamp.
     *
     * @param time the time in milliseconds.
     * @throws BackupException if a backup error occurs.
     */
    void modified(long time) throws BackupException;

    /**
     * Receive notification of device information.
     *
     * @param info the Newton device information.
     * @throws BackupException if a backup error occurs.
     */
    void deviceInformation(NewtonInfo info) throws BackupException;

    /**
     * Receive notification of the start of a store.
     *
     * @param storeName the store name.
     * @throws BackupException if a backup error occurs.
     */
    void startStore(String storeName) throws BackupException;

    /**
     * Receive notification of the store definition.
     *
     * @param store the store.
     * @throws BackupException if a backup error occurs.
     */
    void storeDefinition(Store store) throws BackupException;

    /**
     * Receive notification of the end of a store.
     *
     * @param store the store.
     * @throws BackupException if a backup error occurs.
     */
    void endStore(Store store) throws BackupException;

    /**
     * Receive notification of the start of a package.
     *
     * @param storeName the owner store name.
     * @param pkgName   the package name.
     * @throws BackupException if a backup error occurs.
     */
    void startPackage(String storeName, String pkgName) throws BackupException;

    /**
     * Receive notification of the end of a package.
     *
     * @param storeName the owner store name.
     * @param pkg       the package.
     * @throws BackupException if a backup error occurs.
     */
    void endPackage(String storeName, ApplicationPackage pkg) throws BackupException;

    /**
     * Receive notification of the start of a soup.
     *
     * @param storeName the owner store name.
     * @param soupName  the soup name.
     * @throws BackupException if a backup error occurs.
     */
    void startSoup(String storeName, String soupName) throws BackupException;

    /**
     * Receive notification of the soup definition.
     *
     * @param storeName the owner store name.
     * @param soup      the soup.
     * @throws BackupException if a backup error occurs.
     */
    void soupDefinition(String storeName, Soup soup) throws BackupException;

    /**
     * Receive notification of the end of a soup.
     *
     * @param storeName the owner store name.
     * @param soup      the soup.
     * @throws BackupException if a backup error occurs.
     */
    void endSoup(String storeName, Soup soup) throws BackupException;

    /**
     * Receive notification of a soup entry.
     *
     * @param storeName the owner store name.
     * @param soup      the owner soup.
     * @param entry     the entry.
     * @throws BackupException if a backup error occurs.
     */
    void soupEntry(String storeName, Soup soup, SoupEntry entry) throws BackupException;

}
