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
package net.sf.jncu.translate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Translator factory.
 * 
 * @author Moshe
 */
public class TranslatorFactory {

	private static TranslatorFactory instance;
	private static final Map<Class<? extends Translator>, TranslatorItem> registry = new HashMap<Class<? extends Translator>, TranslatorItem>();

	/**
	 * Translator registry item.
	 */
	protected static class TranslatorItem {

		private final Class<? extends Translator> clazz;
		private String description;
		private final Set<String> extensions = new TreeSet<String>();

		/**
		 * Constructs a new item.
		 * 
		 * @param clazz
		 *            the translator class.
		 */
		public TranslatorItem(Class<? extends Translator> clazz) {
			super();
			this.clazz = clazz;
		}

		@Override
		public int hashCode() {
			return clazz.hashCode();
		}
	}

	/**
	 * Constructs a new translator factory.
	 */
	protected TranslatorFactory() {
		super();
	}

	/**
	 * Get the factory instance.
	 * 
	 * @return the factory.
	 */
	public static TranslatorFactory getInstance() {
		if (instance == null) {
			instance = new TranslatorFactory();
		}
		return instance;
	}

	/**
	 * Get the command registry.
	 * 
	 * @return the registry.
	 */
	protected Map<Class<? extends Translator>, TranslatorItem> getRegistry() {
		if (registry.isEmpty()) {
			register(registry);
		}
		return registry;
	}

	/**
	 * Register the translator.
	 * 
	 * @param registry
	 *            the target registry.
	 * @param clazz
	 *            the translator class.
	 * @param description
	 *            the file description.
	 * @param extensions
	 *            the file extensions.
	 */
	protected synchronized void register(Map<Class<? extends Translator>, TranslatorItem> registry, Class<? extends Translator> clazz, String description, String[] extensions) {
		TranslatorItem item = registry.get(clazz);
		if (item == null) {
			item = new TranslatorItem(clazz);
			registry.put(clazz, item);
		}
		item.description = description;
		item.extensions.clear();
		for (String ext : extensions)
			item.extensions.add(ext);
	}

	/**
	 * Register dock commands.
	 * <p>
	 * It's only really necessary to register "from Newton" commands.
	 * 
	 * @param registry
	 *            the registry.
	 */
	protected synchronized void register(Map<Class<? extends Translator>, TranslatorItem> registry) {
		register(registry, DelimitedTextTranslator.class, TextTranslator.getFilterDescription(), DelimitedTextTranslator.getFilterExtensions());
		register(registry, ICalendarTranslator.class, CalendarTranslator.getFilterDescription(), ICalendarTranslator.getFilterExtensions());
		register(registry, PlainTextTranslator.class, TextTranslator.getFilterDescription(), PlainTextTranslator.getFilterExtensions());
		register(registry, RichTextTranslator.class, RichTextTranslator.getFilterDescription(), RichTextTranslator.getFilterExtensions());
		register(registry, VCalendarTranslator.class, CalendarTranslator.getFilterDescription(), VCalendarTranslator.getFilterExtensions());
		register(registry, VCardTranslator.class, NamesTranslator.getFilterDescription(), VCardTranslator.getFilterExtensions());
		register(registry, WindowsMetaFileTranslator.class, ImageTranslator.getFilterDescription(), WindowsMetaFileTranslator.getFilterExtensions());
	}

	/**
	 * Get the list of translators.
	 * 
	 * @param file
	 *            the file.
	 * @return the list of translators.
	 */
	public List<? extends Translator> getTranslatorsByFile(File file) {
		String name = file.getName();
		int indexExt = name.lastIndexOf('.');
		String ext = name;
		if (indexExt > 0)
			ext = name.substring(indexExt + 1);
		return getTranslatorsBySuffix(ext);
	}

	/**
	 * Get the list of translators.
	 * 
	 * @param fileSuffix
	 *            the file suffix.
	 * @return the list of translators.
	 */
	public List<Translator> getTranslatorsBySuffix(String fileSuffix) {
		if ((fileSuffix == null) || (fileSuffix.length() == 0))
			return null;
		if (getRegistry().isEmpty())
			return null;

		fileSuffix = fileSuffix.toLowerCase();
		final List<Translator> result = new ArrayList<Translator>();
		Class<? extends Translator> clazz;
		Translator translator;

		for (TranslatorItem item : getRegistry().values()) {
			if (item.extensions.contains(fileSuffix)) {
				clazz = item.clazz;
				try {
					translator = clazz.newInstance();
					result.add(translator);
				} catch (InstantiationException ie) {
					ie.printStackTrace();
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
				}
			}
		}

		if (result.isEmpty())
			return null;

		return result;
	}

	/**
	 * Get the list of registered file filters.
	 * 
	 * @return the the list of file filters.
	 */
	public List<FileFilter> getFileFilters() {
		final List<FileFilter> filters = new ArrayList<FileFilter>();

		if (getRegistry().isEmpty())
			return filters;

		Map<String, Set<String>> byDescription = new HashMap<String, Set<String>>();
		Set<String> extensions;
		for (TranslatorItem item : getRegistry().values()) {
			extensions = byDescription.get(item.description);
			if (extensions == null) {
				extensions = new TreeSet<String>();
				byDescription.put(item.description, extensions);
			}
			extensions.addAll(item.extensions);
		}

		FileFilter filter;
		String[] extensionsArr;
		for (String description : byDescription.keySet()) {
			extensions = byDescription.get(description);
			extensionsArr = new String[extensions.size()];
			extensionsArr = extensions.toArray(extensionsArr);
			filter = new FileNameExtensionFilter(description, extensionsArr);
			filters.add(filter);
		}

		return filters;
	}
}
