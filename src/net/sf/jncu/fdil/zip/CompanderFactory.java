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
package net.sf.jncu.fdil.zip;

import java.util.HashMap;
import java.util.Map;

import net.sf.jncu.fdil.NSOFLargeBinary;

/**
 * Compander and decompressor object factory.
 * 
 * @author mwaisberg
 * 
 */
public class CompanderFactory {

	/**
	 * Specifies the use of the Lempel-Ziv compressor-expander.
	 */
	public static final String COMPANDER_LZ = "TLZStoreCompander";
	/**
	 * Specifies the use of a compander specialized for pixel map data. (A
	 * bitmap is a pixel map having a bit depth of 1.) This compander assumes
	 * that the data in the VBO is a pixel map and that the pixel map data is
	 * 32-bit aligned; that is, the length of the rows in the pixel map is an
	 * even multiple of 4 bytes.
	 */
	public static final String COMPANDER_PIXELMAP = "TPixelMapCompander";

	private static CompanderFactory instance;
	private static final Map<String, Class<? extends TCompander>> registryCompanders = new HashMap<String, Class<? extends TCompander>>();
	private static final Map<String, Class<? extends TDecompressor>> registryDecompressors = new HashMap<String, Class<? extends TDecompressor>>();

	/**
	 * Creates a new factory.
	 */
	private CompanderFactory() {
		register();
	}

	/**
	 * Register the classes.
	 */
	private void register() {
		registerCompanders();
		registerDecompressors();

	}

	/**
	 * Register the companders.
	 */
	private void registerCompanders() {
		registryCompanders.put(COMPANDER_LZ, TLZStoreCompander.class);
		registryCompanders.put(COMPANDER_PIXELMAP, TPixelMapCompander.class);
		registryCompanders.put("TSimpleStoreCompander", TSimpleStoreCompander.class);
		registryCompanders.put("TStoreCompanderWrapper", TStoreCompanderWrapper.class);
	}

	/**
	 * Register the decompressors.
	 */
	private void registerDecompressors() {
		registryDecompressors.put("TLZRelocStoreDecompressor", TLZRelocStoreDecompressor.class);
		registryDecompressors.put("TLZStoreDecompressor", TLZStoreDecompressor.class);
		registryDecompressors.put("TSimpleRelocStoreDecompressor", TSimpleRelocStoreDecompressor.class);
		registryDecompressors.put("TSimpleStoreDecompressor", TSimpleStoreDecompressor.class);
		registryDecompressors.put("TZippyRelocStoreDecompressor", TZippyRelocStoreDecompressor.class);
		registryDecompressors.put("TZippyStoreDecompressor", TZippyStoreDecompressor.class);
	}

	/**
	 * Get the factory instance.
	 * 
	 * @return the factory.
	 */
	public static CompanderFactory getInstance() {
		if (instance == null) {
			instance = new CompanderFactory();
		}
		return instance;
	}

	/**
	 * Creates a compander.
	 * 
	 * @param name
	 *            the compander name.
	 * @return the new compander - {@code null} otherwise.
	 */
	public TCompander createCompander(String name) {
		Class<? extends TCompander> clazz = registryCompanders.get(name);
		if (clazz == null)
			return null;
		try {
			return clazz.newInstance();
		} catch (InstantiationException ie) {
			ie.printStackTrace();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a compander.
	 * 
	 * @param blob
	 *            the BLOB.
	 * @return the new compander.
	 */
	public TCompander createCompander(NSOFLargeBinary blob) {
		return createCompander(blob.getCompanderName());
	}

	/**
	 * Creates a decompressor.
	 * 
	 * @param name
	 *            the decompressor name.
	 * @return the new decompressor - {@code null} otherwise.
	 */
	public TDecompressor createDecompressor(String name) {
		Class<? extends TDecompressor> clazz = registryDecompressors.get(name);
		if (clazz == null)
			return null;
		try {
			return clazz.newInstance();
		} catch (InstantiationException ie) {
			ie.printStackTrace();
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a decompressor.
	 * 
	 * @param blob
	 *            the BLOB.
	 * @return the new decompressor.
	 */
	public TDecompressor createDecompressor(NSOFLargeBinary blob) {
		return createDecompressor(blob.getCompanderName());
	}
}
