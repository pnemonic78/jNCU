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

import net.sf.jncu.fdil.NSOFSymbol;

/**
 * Text translator.
 * <p>
 * <h2>Notes Soup Format</h2> 16 This section describes the format of entries in
 * the Notes soup. Each entry consists of a frame with the following slots:
 * <table>
 * <tr>
 * <th>Slot</th>
 * <th>Descriptions</th>
 * </tr>
 * <tr>
 * <td>{@code viewStationery}</td>
 * <td>This slot always contains the symbol 'paperroll.</td>
 * </tr>
 * <tr>
 * <td>{@code class}</td>
 * <td>The class symbol varies according to the type of stationery the user
 * creates on the New picker, which may be a note (class 'paperroll), an outline
 * (class 'list), or a checklist (class 'checkList).
 * <tr>
 * <td>height</td>
 * <td>This slot contains an immediate value that is the height, in pixels, of
 * the note.</td>
 * </tr>
 * <tr>
 * <td>timeStamp</td>
 * <td>Contains an immediate value: the date and time that this note was
 * created, in the number of minutes passed since midnight, January 1, 1904.
 * This slot must never contain the value nil.</td>
 * </tr>
 * <tr>
 * <td>labels</td>
 * <td>Optional. A symbol specified by the user as a label (file folder) for the
 * note.</td>
 * </tr>
 * <tr>
 * <td>title</td>
 * <td>Optional. A string or rich string displayed in the status bar of the
 * note. The user can change this by tapping the notes’s icon.</td>
 * </tr>
 * <tr>
 * <td>data</td>
 * <td>For notes (class 'paperroll), this slot holds an array of frames, which
 * contains either text, polygon, ink, or image objects. For outlines and
 * checklists (classes 'list and 'checkList), this slot is set to nil. There is
 * one frame for each text, polygon, ink, or image object in the note.
 * <p>
 * The text object frames have these slots:
 * <table>
 * <tr>
 * <td>viewStationery</td>
 * <td>Required. Always contains the symbol 'para.</td>
 * </tr>
 * <tr>
 * <td>viewBounds</td>
 * <td>Required. The bounds of the text object.
 * <tr>
 * <td>text</td>
 * <td>Required. A string that is the text contained in the paragraph.</td>
 * </tr>
 * <tr>
 * <td>tabs</td>
 * <td>Optional. An array of tab stops.</td>
 * </tr>
 * <tr>
 * <td>styles</td>
 * <td>Optional. An array holding font style information for the text.</td>
 * </tr>
 * </table>
 * For more information on the slots particular to paragraph views, see
 * “Paragraph Views” beginning on page 8-10 in Newton Programmer’s Guide.
 * <p>
 * The polygon object frames have these slots:
 * <table>
 * <tr>
 * <td>viewStationery</td>
 * <td>This slot always contains the symbol 'poly.</td>
 * </tr>
 * <tr>
 * <td>viewBounds</td>
 * <td>The bounds of the polygon.</td>
 * </tr>
 * <tr>
 * <td>points</td>
 * <td>Contains a binary data structure, which holds polygon data.</td>
 * </tr>
 * </table>
 * <p>
 * The ink object frames have these slots:
 * <table>
 * <tr>
 * <td>ink</td>
 * <td>This slot contains a binary data structure of the class 'ink that holds
 * the ink data.</td>
 * </tr>
 * <tr>
 * <td>viewBounds</td>
 * <td>The bounds of the ink object.</td>
 * </tr>
 * <tr>
 * <td>timeStamp</td>
 * <td>Contains an immediate value: the date and time that this note was
 * created, in the number of minutes passed since midnight, January 1, 1904.</td>
 * </tr>
 * </table>
 * <p>
 * The image object frames have these slots:
 * <table>
 * <tr>
 * <td>viewStationery</td>
 * <td>This slot always contains the symbol 'pict.</td>
 * </tr>
 * <tr>
 * <td>viewBounds</td>
 * <td>A bounds frame.</td>
 * </tr>
 * <tr>
 * <td>icon</td>
 * <td>A bitmap frame.</td>
 * </tr>
 * </table>
 * </td>
 * </tr>
 * <tr>
 * <td>topics</td>
 * <td>This slot is present only for outline and checklist entries (classes
 * 'list and 'checkList). This slot contains an array of frames with the
 * following slots:<br>
 * <table>
 * <tr>
 * <td>text</td>
 * <td>The text for this item.</td>
 * </tr>
 * <tr>
 * <td>styles</td>
 * <td>A styles frame for the text.</td>
 * </tr>
 * <tr>
 * <td>viewBounds</td>
 * <td>The bounds frame of the text object.</td>
 * </tr>
 * <tr>
 * <td>level</td>
 * <td>The indentation level. The default value, 1, specifies the left margin.
 * This slot will always be set to 1 for checklist entries.</td>
 * </tr>
 * </table>
 * </td>
 * </tr>
 * <tr>
 * <td>hideCount</td>
 * <td>Specifies how many items are hidden at level.</td>
 * </tr>
 * <tr>
 * <td>mtgDone</td>
 * <td>This slot appears only in checklist entries. A value of true indicates
 * that the topic has a check; nil indicates that it does not.</td>
 * </tr>
 * </table>
 * <br>
 * <em>Extracted from "Newton Programmer’s Reference (For Newton 2.0)</em>
 * 
 * @author Moshe
 */
public abstract class TextTranslator extends Translator {

	/**
	 * Get the file filter description.
	 * 
	 * @return the description.
	 */
	public static String getFilterDescription() {
		return "Text Document";
	}

	/** NSOFSymbol: 'paperroll 'list 'checkList */
	protected static final NSOFSymbol SLOT_CLASS = new NSOFSymbol("class");
	/** NSOFString. */
	protected static final NSOFSymbol SLOT_TITLE = new NSOFSymbol("title");
	/** NSOFInteger. */
	protected static final NSOFSymbol SLOT_TIME = new NSOFSymbol("timestamp");
	/**
	 * NSOFPlainArray of frames with SLOT_DATA_ slots. if (class == 'paperroll).
	 */
	protected static final NSOFSymbol SLOT_DATA = new NSOFSymbol("data");
	/** NSOFPlainArray of frames. if (class == 'checkList). */
	protected static final NSOFSymbol SLOT_TOPICS = new NSOFSymbol("topics");

	protected static final NSOFSymbol SLOT_DATA_INK = new NSOFSymbol("ink");
	/** NSOFString. */
	protected static final NSOFSymbol SLOT_DATA_TEXT = new NSOFSymbol("text");
	/** NSOFSmallRect. */
	protected static final NSOFSymbol SLOT_DATA_BOUNDS = new NSOFSymbol("viewBounds");
	/** NSOFSymbol: 'para */
	protected static final NSOFSymbol SLOT_DATA_STATIONERY = new NSOFSymbol("viewStationery");
	/** NSOFMagicPointer to a NSOFFrame. */
	protected static final NSOFSymbol SLOT_DATA_PROTO = new NSOFSymbol("_proto");

	/** "Outline". */
	protected static final NSOFSymbol CLASS_LIST = new NSOFSymbol("list");
	/** "Check list". */
	protected static final NSOFSymbol CLASS_CHECKLIST = new NSOFSymbol("checkList");
	/** "Note". */
	protected static final NSOFSymbol CLASS_PAPER = new NSOFSymbol("paperroll");

	/**
	 * Constructs a new translator.
	 */
	public TextTranslator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jncu.protocol.v2_0.data.Translator#getSoupName()
	 */
	@Override
	public String getSoupName() {
		return "Notes";
	}
}
