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
		None, Initialised, TranslatorList, Importing, SetStoreToDefault, SetCurrentSoup, CreateDefaultSoup, AddEntry, Changed, Imported, Cancelled, Finished
	}

	protected static final String TITLE = "Import File";

	private State state = State.None;
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
		state = State.Initialised;
	}

	@Override
	public void commandReceived(IDockCommandFromNewton command) {
		if (state == State.Cancelled)
			return;
		if (state == State.Finished)
			return;

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
				case Initialised:
					state = State.Importing;
					break;
				case Importing:
					DSetStoreToDefault setStore = new DSetStoreToDefault();
					write(setStore);
					break;
				case SetStoreToDefault:
					DSetCurrentSoup setSoup = new DSetCurrentSoup();
					setSoup.setName(translator.getSoupName());
					write(setSoup);
					break;
				case SetCurrentSoup:
					importer = new Importer();
					importer.start();
					break;
				}
			} else {
				if (state == State.SetCurrentSoup) {
					if (code == DSetCurrentSoup.ERROR_NOT_FOUND) {

					}
				} else {
					commandEOF();
					state = State.Cancelled;
					showError(result.getError().getMessage() + "\nCode: " + code);
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
			SoupChanged soup = DSoupsChanged.createSoup(translator.getName(), cmdAdded.getId());

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
		if (state == State.Cancelled)
			return;
		if (state == State.Finished)
			return;

		String cmd = command.getCommand();

		if (DImporting.COMMAND.equals(cmd)) {
			state = State.Importing;
		} else if (DTranslatorList.COMMAND.equals(cmd)) {
			state = State.TranslatorList;
		} else if (DSetStoreToDefault.COMMAND.equals(cmd)) {
			state = State.SetStoreToDefault;
		} else if (DSetCurrentSoup.COMMAND.equals(cmd)) {
			state = State.SetCurrentSoup;
		} else if (DCreateDefaultSoup.COMMAND.equals(cmd)) {
			state = State.CreateDefaultSoup;
		} else if (DAddEntry.COMMAND.equals(cmd)) {
			state = State.AddEntry;
		} else if (DSoupsChanged.COMMAND.equals(cmd)) {
			state = State.Changed;
			DOperationDone done = new DOperationDone();
			write(done);
		} else if (DOperationDone.COMMAND.equals(cmd)) {
			commandEOF();
			state = State.Finished;
		} else if (DOperationCanceledAck.COMMAND.equals(cmd)) {
			commandEOF();
			state = State.Cancelled;
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

		if (state == State.Initialised) {
			translators = TranslatorFactory.getInstance().getTranslatorsByFile(file);
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
			try {
				in = new FileInputStream(file);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			NSOFFrame entry = (NSOFFrame) translator.translateToNewton(in);

			DAddEntry add = new DAddEntry();
			add.setObject(entry);
			write(add);
		}
	}
}
