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
package net.sf.jncu.newton.stream;

/**
 * Newton Streamed Object Format - Raw bitmap data.
 * <p>
 * <em>Warning</em>: Different formats may be used by images or functions in
 * future ROMs. This format will still be supported for displaying images. This
 * format does not describe images created by other applications nor any images
 * provided or found in the Newton ROM. You can use the following format
 * information to create and manipulate your own bitmaps -- preferably at
 * compile time:
 * <p>
 * Binary object <raw bitmap data> - class <tt>'bits</tt>
 * <table>
 * <tr>
 * <th>bytes</th>
 * <th>data-type</th>
 * <th>description</th>
 * </tr>
 * <tr>
 * <td>0-3</td>
 * <td>long</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td>4-5</td>
 * <td>word</td>
 * <td>#bytes per row of the bitmap data (must be a multiple of 4)</td>
 * </tr>
 * <tr>
 * <td>6-7</td>
 * <td>word</td>
 * <td>ignored</td>
 * </tr>
 * <tr>
 * <td>8-15</td>
 * <td>bitmap</td>
 * <td>rectangle - portion of bits to use--see IM I</td>
 * </tr>
 * <tr>
 * <td>&nbsp;&nbsp;8-9</td>
 * <td>word</td>
 * <td>top</td>
 * </tr>
 * <tr>
 * <td>&nbsp;&nbsp;10-11</td>
 * <td>word</td>
 * <td>left</td>
 * </tr>
 * <tr>
 * <td>&nbsp;&nbsp;12-13</td>
 * <td>word</td>
 * <td>bottom</td>
 * </tr>
 * <tr>
 * <td>&nbsp;&nbsp;14-15</td>
 * <td>word</td>
 * <td>right</td>
 * </tr>
 * <tr>
 * <td>16-*</td>
 * <td>bits</td>
 * <td>pixel data, 1 for "on" pixel, 0 for "off"</td>
 * </tr>
 * </table>
 * 
 * @author Moshe
 */
public class NSOFRawBitmap extends NSOFBinaryObject {

	public static final NSOFSymbol NS_CLASS = new NSOFSymbol("bits");

	/**
	 * Constructs a new bitmap.
	 */
	public NSOFRawBitmap() {
		super();
		setNSClass(NS_CLASS);
	}

}
