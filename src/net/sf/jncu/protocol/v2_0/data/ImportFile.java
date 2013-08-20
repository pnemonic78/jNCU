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
package net.sf.jncu.protocol.v2_0.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jncu.cdil.CDPacket;
import net.sf.jncu.cdil.CDPipe;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.protocol.IDockCommandFromNewton;
import net.sf.jncu.protocol.IDockCommandToNewton;
import net.sf.jncu.protocol.v1_0.data.DAddEntry;
import net.sf.jncu.protocol.v1_0.data.DAddedID;
import net.sf.jncu.protocol.v1_0.data.DSetCurrentSoup;
import net.sf.jncu.protocol.v1_0.query.DResult;
import net.sf.jncu.protocol.v2_0.IconModule;
import net.sf.jncu.protocol.v2_0.io.DSetStoreToDefault;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceled2;
import net.sf.jncu.protocol.v2_0.session.DOperationCanceledAck;
import net.sf.jncu.protocol.v2_0.session.DOperationDone;
import net.sf.jncu.protocol.v2_0.sync.DSoupsChanged;
import net.sf.jncu.protocol.v2_0.sync.DSoupsChanged.SoupChanged;
import net.sf.jncu.translate.Translator;
import net.sf.jncu.translate.TranslatorFactory;

/**
 * Import file module.
 * 
 * @author Moshe
 */
public class ImportFile extends IconModule {

	protected enum State {
		/** None. */
		NONE,
		/** Initialised. */
		INITIALISED,
		/** Translator list. */
		TRANSLATOR_LIST,
		/** Importing. */
		IMPORTING,
		/** Set store to default. */
		SET_STORE_DEFAULT,
		/** Set current soup. */
		SET_CURRENT_SOUP,
		/** Create default soup. */
		CREATE_DEFAULT_SOUP,
		/** Add entry. */
		ADD_ENTRY,
		/** Changed. */
		CHANGED,
		/** Imported. */
		IMPORTED,
		/** Cancelled. */
		CANCELLED,
		/** Finished. */
		FINISHED
	}

	protected static final String TITLE = "Import File";

	private State state = State.NONE;
	private File file;
	private Importer importer;
	private List<? extends Translator> translators;
	private Translator translator;
	private DSoupsChanged changed;

	/**
	 * Constructs a new import module.
	 * 
	 * @param pipe
	 *            the pipe.
	 */
	public ImportFile(CDPipe<? extends CDPacket> pipe) {
		super(TITLE, pipe);
		setName("ImportFile-" + getId());
		state = State.INITIALISED;
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (!isEnabled())
			return;

		super.commandReceived(command);

		String cmd = command.getCommand();

		if (DSetTranslator.COMMAND.equals(cmd)) {
			DSetTranslator cmdSet = (DSetTranslator) command;
			translator = translators.get(cmdSet.getTranslatorIndex());
			startImport();
		} else if (DResult.COMMAND.equals(cmd)) {
			DResult result = (DResult) command;
			int code = result.getErrorCode();

			if (code == DResult.OK) {
				switch (state) {
				case INITIALISED:
					state = State.IMPORTING;
					break;
				case IMPORTING:
					DSetStoreToDefault setStore = new DSetStoreToDefault();
					write(setStore);
					break;
				case SET_STORE_DEFAULT:
					DSetCurrentSoup setSoup = new DSetCurrentSoup();
					setSoup.setName(translator.getApplicationName());
					write(setSoup);
					break;
				case SET_CURRENT_SOUP:
					importer = new Importer();
					importer.start();
					break;
				}
			} else {
				if (state == State.SET_CURRENT_SOUP) {
					if (code == DSetCurrentSoup.ERROR_NOT_FOUND) {

					}
				} else {
					done();
					state = State.CANCELLED;
					showError(result.getError().getMessage() + "\nCode: "
							+ code);
				}
			}
		} else if (DOperationCanceled2.COMMAND.equals(cmd)) {
			// TODO Stop sending the file.
			// pipe.cancel(importer);
			// importer.kill();
			DOperationCanceledAck ack = new DOperationCanceledAck();
			write(ack);
		} else if (DAddedID.COMMAND.equals(cmd)) {
			DAddedID cmdAdded = (DAddedID) command;
			SoupChanged soup = DSoupsChanged.createSoup(
					translator.getApplicationName(), cmdAdded.getId());

			if (changed == null)
				changed = new DSoupsChanged();
			changed.addSoup(soup);
			// TODO Ask translator if anything more to add, otherwise we can
			// finish.
			write(changed);
		}
	}

	@Override
	public void commandSent(IDockCommandToNewton command) {
		if (!isEnabled())
			return;

		super.commandSent(command);

		String cmd = command.getCommand();

		if (DImporting.COMMAND.equals(cmd)) {
			state = State.IMPORTING;
		} else if (DTranslatorList.COMMAND.equals(cmd)) {
			state = State.TRANSLATOR_LIST;
		} else if (DSetStoreToDefault.COMMAND.equals(cmd)) {
			state = State.SET_STORE_DEFAULT;
		} else if (DSetCurrentSoup.COMMAND.equals(cmd)) {
			state = State.SET_CURRENT_SOUP;
		} else if (DCreateDefaultSoup.COMMAND.equals(cmd)) {
			state = State.CREATE_DEFAULT_SOUP;
		} else if (DAddEntry.COMMAND.equals(cmd)) {
			state = State.ADD_ENTRY;
		} else if (DSoupsChanged.COMMAND.equals(cmd)) {
			state = State.CHANGED;
			DOperationDone done = new DOperationDone();
			write(done);
		} else if (DOperationDone.COMMAND.equals(cmd)) {
			state = State.FINISHED;
		} else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
			state = State.CANCELLED;
		}
	}

	/**
	 * Start the importing process.
	 * 
	 * @param file
	 *            the file.
	 */
	public void importFile(File file) {
		this.file = file;
		this.start();
	}

	@Override
	public void run() {
		if (state == State.INITIALISED) {
			translators = TranslatorFactory.getInstance().getTranslatorsByFile(
					file);
			if ((translators == null) || translators.isEmpty()) {
				showError("No translator found");
				return;
			}
			translator = translators.get(0);
			if (translators.size() == 1) {
				startImport();
			} else {
				DTranslatorList list = new DTranslatorList();
				for (Translator translator : translators) {
					list.addTranslator(translator.getName());
				}
				write(list);
			}
		}
	}

	/**
	 * Start importing.
	 */
	private void startImport() {
		DImporting importing = new DImporting();
		write(importing);
	}

	/**
	 * Send the file contents in a non-blocking thread so that the command
	 * receivers can continue to function.
	 */
	private class Importer extends Thread {

		public Importer() {
			super();
			setName("Importer-" + getId());
		}

		@Override
		public void run() {
			InputStream in;
			NSOFFrame entry;
			try {
				in = new FileInputStream(file);
				entry = (NSOFFrame) translator.translateToNewton(in);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			DAddEntry add = new DAddEntry();
			add.setObject(entry);
			write(add);
		}
	}
}
