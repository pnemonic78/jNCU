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
package net.sf.swing.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * document filter that has a maximum length to limit the number of characters.
 * 
 * @author Moshe
 * 
 */
public class DocumentLengthFilter extends DocumentFilter {

	private final int maxLength;

	/**
	 * Create a new filter.
	 * 
	 * @param length
	 *            the maximum length.
	 */
	public DocumentLengthFilter(int length) {
		this.maxLength = length;
	}

	/**
	 * Get the maximum length.
	 * 
	 * @return the maximum length.
	 */
	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String text,
			AttributeSet attr) throws BadLocationException {
		if (text == null)
			return;
		Document doc = fb.getDocument();
		int docLength = doc.getLength();
		if (docLength + text.length() > maxLength)
			return;
		super.insertString(fb, offset, text, attr);
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text,
			AttributeSet attrs) throws BadLocationException {
		if (text != null) {
			Document doc = fb.getDocument();
			int docLength = doc.getLength();
			if (docLength - length + text.length() > maxLength)
				return;
		}
		super.replace(fb, offset, length, text, attrs);
	}
}
