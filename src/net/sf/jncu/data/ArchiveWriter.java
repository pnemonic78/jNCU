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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFEncoder;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.newton.os.ApplicationPackage;
import net.sf.jncu.newton.os.NewtonInfo;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.newton.os.Store;

/**
 * Backup from the Newton device to an archive file.
 * 
 * @author mwaisberg
 * 
 */
public class ArchiveWriter {

	private final File file;

	/**
	 * Creates a new archive writer.
	 * 
	 * @param file
	 *            the destination file.
	 */
	public ArchiveWriter(File file) {
		this.file = file;
	}

	/**
	 * Write the archive to the file.
	 * 
	 * @param archive
	 *            the archive to write.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(Archive archive) throws IOException {
		File parent = file.getParentFile();
		File tmp;
		OutputStream fout = null;
		ZipOutputStream out = null;

		try {
			parent.mkdirs();
			tmp = File.createTempFile("jncu", null, parent);
			tmp.deleteOnExit();
			fout = new BufferedOutputStream(new FileOutputStream(tmp));
			out = new ZipOutputStream(fout);

			writeDevice(archive, out);
			writeStores(archive, out);

			out.finish();
			out.close();
			fout = null;
			out = null;

			tmp.renameTo(file);
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Write the device information.
	 * 
	 * @param archive
	 *            the archive to write.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeDevice(Archive archive, ZipOutputStream out) throws IOException {
		NewtonInfo info = archive.getDeviceInfo();
		if (info == null)
			return;
		ZipEntry entry = new ZipEntry(Archive.ENTRY_DEVICE);
		out.putNextEntry(entry);
		NSOFFrame frame = info.toFrame();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(frame, out);
	}

	/**
	 * Write the stores.
	 * 
	 * @param archive
	 *            the archive to write.
	 * @param out
	 *            the output.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeStores(Archive archive, ZipOutputStream out) throws IOException {
		ZipEntry entry = new ZipEntry(Archive.ENTRY_STORES + Archive.DIRECTORY);
		out.putNextEntry(entry);

		for (Store store : archive.getStores()) {
			writeStore(archive, out, entry, store);
		}
	}

	/**
	 * Write a store.
	 * 
	 * @param archive
	 *            the archive to write.
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param store
	 *            the store.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeStore(Archive archive, ZipOutputStream out, ZipEntry parent, Store store) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + store.getName() + Archive.DIRECTORY);
		out.putNextEntry(entry);

		ZipEntry entryStore = new ZipEntry(entry.getName() + Archive.ENTRY_STORE);
		out.putNextEntry(entryStore);
		NSOFFrame frame = store.toFrame();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(frame, out);

		writePackages(archive, out, entry, store);
		writeSoups(archive, out, entry, store);
	}

	/**
	 * Write the packages.
	 * 
	 * @param archive
	 *            the archive to write.
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param store
	 *            the store.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writePackages(Archive archive, ZipOutputStream out, ZipEntry parent, Store store) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + Archive.ENTRY_PACKAGES + Archive.DIRECTORY);
		out.putNextEntry(entry);

		for (ApplicationPackage pkg : store.getPackages()) {
			writePackage(archive, out, entry, pkg);
		}
	}

	/**
	 * Write a package.
	 * 
	 * @param archive
	 *            the archive to write.
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param pkg
	 *            the package.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writePackage(Archive archive, ZipOutputStream out, ZipEntry parent, ApplicationPackage pkg) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + pkg.toString());
		out.putNextEntry(entry);
		// TODO implement me!
	}

	/**
	 * Write the soups.
	 * 
	 * @param archive
	 *            the archive to write.
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param store
	 *            the store.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeSoups(Archive archive, ZipOutputStream out, ZipEntry parent, Store store) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + Archive.ENTRY_SOUPS + Archive.DIRECTORY);
		out.putNextEntry(entry);

		for (Soup soup : store.getSoups()) {
			writeSoup(archive, out, entry, soup);
		}
	}

	/**
	 * Write a soup.
	 * 
	 * @param archive
	 *            the archive to write.
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param soup
	 *            the soup.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeSoup(Archive archive, ZipOutputStream out, ZipEntry parent, Soup soup) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + soup.getName() + Archive.DIRECTORY);
		out.putNextEntry(entry);

		ZipEntry entryStore = new ZipEntry(entry.getName() + Archive.ENTRY_SOUP);
		out.putNextEntry(entryStore);
		NSOFFrame frame = soup.toFrame();
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(frame, out);

		writeSoupEntries(archive, out, entry, soup);
	}

	/**
	 * Write soup entries.
	 * 
	 * @param archive
	 *            the archive to write.
	 * @param out
	 *            the output.
	 * @param parent
	 *            the parent entry.
	 * @param soup
	 *            the soup.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void writeSoupEntries(Archive archive, ZipOutputStream out, ZipEntry parent, Soup soup) throws IOException {
		ZipEntry entry = new ZipEntry(parent.getName() + Archive.ENTRY_ENTRIES);
		out.putNextEntry(entry);

		Collection<SoupEntry> entries = soup.getEntries();
		NSOFArray arr = new NSOFPlainArray(entries.size());
		int i = 0;
		for (SoupEntry item : entries) {
			arr.set(i++, item);
		}
		NSOFEncoder encoder = new NSOFEncoder();
		encoder.flatten(arr, out);
	}
}
