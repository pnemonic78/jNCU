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

import java.io.InputStream;

import net.sf.jncu.fdil.NSOFObject;

/**
 * vCard translator.
 * 
 * @author Moshe
 */
public class VCardTranslator extends NamesTranslator {

	private static final String[] EXT = { "vcf", "xml" };

	/**
	 * Get the file filter extensions.
	 * 
	 * @return the array of extensions.
	 */
	public static String[] getFilterExtensions() {
		return EXT;
	}

	/**
	 * Constructs a new translator.
	 */
	public VCardTranslator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.translate.Translator#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSoupName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NSOFObject translateToNewton(InputStream in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream translateFromNewton(NSOFObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

}