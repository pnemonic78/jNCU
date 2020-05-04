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
package net.sf.lang;

/**
 * ASCII control characters.
 * <table>
 * <tr>
 * <th></th>
 * <th></th>
 * <th></th>
 * <th></th>
 * <th>CTRL</th>
 * <th>(^D means to hold the CTRL key and hit d)
 * <tr>
 * <th>Oct</th>
 * <th>Dec</th>
 * <th>Char</th>
 * <th>Hex</th>
 * <th>Key</th>
 * <th>Comments
 * <tr>
 * <td align="right">\000</td>
 * <td align="right">0</td>
 * <td><tt>NUL</tt></td>
 * <td align="right">\x00</td>
 * <td>^@ \0</td>
 * <td>(Null byte)</td>
 * </tr>
 * <tr>
 * <td align="right">\001</td>
 * <td align="right">1</td>
 * <td><tt>SOH</tt></td>
 * <td align="right">\x01</td>
 * <td>^A</td>
 * <td>(Start of heading)</td>
 * </tr>
 * <tr>
 * <td align="right">\002</td>
 * <td align="right">2</td>
 * <td><tt>STX</tt></td>
 * <td align="right">\x02</td>
 * <td>^B</td>
 * <td>(Start of text)</td>
 * </tr>
 * <tr>
 * <td align="right">\003</td>
 * <td align="right">3</td>
 * <td><tt>ETX</tt></td>
 * <td align="right">\x03</td>
 * <td>^C</td>
 * <td>(End of text) (see: UNIX keyboard CTRL)</td>
 * </tr>
 * <tr>
 * <td align="right">\004</td>
 * <td align="right">4</td>
 * <td><tt>EOT</tt></td>
 * <td align="right">\x04</td>
 * <td>^D</td>
 * <td>(End of transmission) (see: UNIX keyboard CTRL)</td>
 * </tr>
 * <tr>
 * <td align="right">\005</td>
 * <td align="right">5</td>
 * <td><tt>ENQ</tt></td>
 * <td align="right">\x05</td>
 * <td>^E</td>
 * <td>(Enquiry)</td>
 * </tr>
 * <tr>
 * <td align="right">\006</td>
 * <td align="right">6</td>
 * <td><tt>ACK</tt></td>
 * <td align="right">\x06</td>
 * <td>^F</td>
 * <td>(Acknowledge)</td>
 * </tr>
 * <tr>
 * <td align="right">\007</td>
 * <td align="right">7</td>
 * <td><tt>BEL</tt></td>
 * <td align="right">\x07</td>
 * <td>^G</td>
 * <td>(Ring terminal bell)</td>
 * </tr>
 * <tr>
 * <td align="right">\010</td>
 * <td align="right">8</td>
 * <td><tt>BS</tt></td>
 * <td align="right">\x08</td>
 * <td>^H \b</td>
 * <td>(Backspace) (\b matches backspace inside [] only) (see: UNIX keyboard
 * CTRL)</td>
 * </tr>
 * <tr>
 * <td align="right">\011</td>
 * <td align="right">9</td>
 * <td><tt>HT</tt></td>
 * <td align="right">\x09</td>
 * <td>^I \t</td>
 * <td>(Horizontal tab)</td>
 * </tr>
 * <tr>
 * <td align="right">\012</td>
 * <td align="right">10</td>
 * <td><tt>LF</tt></td>
 * <td align="right">\x0A</td>
 * <td>^J \n</td>
 * <td>(Line feed) (Default UNIX NL) (see End of Line below)</td>
 * </tr>
 * <tr>
 * <td align="right">\013</td>
 * <td align="right">11</td>
 * <td><tt>VT</tt></td>
 * <td align="right">\x0B</td>
 * <td>^K</td>
 * <td>(Vertical tab)</td>
 * </tr>
 * <tr>
 * <td align="right">\014</td>
 * <td align="right">12</td>
 * <td><tt>FF</tt></td>
 * <td align="right">\x0C</td>
 * <td>^L \f</td>
 * <td>(Form feed)</td>
 * </tr>
 * <tr>
 * <td align="right">\015</td>
 * <td align="right">13</td>
 * <td><tt>CR</tt></td>
 * <td align="right">\x0D</td>
 * <td>^M \r</td>
 * <td>(Carriage return) (see: End of Line below)</td>
 * </tr>
 * <tr>
 * <td align="right">\016</td>
 * <td align="right">14</td>
 * <td><tt>SO</tt></td>
 * <td align="right">\x0E</td>
 * <td>^N</td>
 * <td>(Shift out)</td>
 * </tr>
 * <tr>
 * <td align="right">\017</td>
 * <td align="right">15</td>
 * <td><tt>SI</tt></td>
 * <td align="right">\x0F</td>
 * <td>^O</td>
 * <td>(Shift in)</td>
 * </tr>
 * <tr>
 * <td align="right">\020</td>
 * <td align="right">16</td>
 * <td><tt>DLE</tt></td>
 * <td align="right">\x10</td>
 * <td>^P</td>
 * <td>(Data link escape)</td>
 * </tr>
 * <tr>
 * <td align="right">\021</td>
 * <td align="right">17</td>
 * <td><tt>DC1</tt></td>
 * <td align="right">\x11</td>
 * <td>^Q</td>
 * <td>(Device control 1) (XON) (Default UNIX START char.)</td>
 * </tr>
 * <tr>
 * <td align="right">\022</td>
 * <td align="right">18</td>
 * <td><tt>DC2</tt></td>
 * <td align="right">\x12</td>
 * <td>^R</td>
 * <td>(Device control 2)</td>
 * </tr>
 * <tr>
 * <td align="right">\023</td>
 * <td align="right">19</td>
 * <td><tt>DC3</tt></td>
 * <td align="right">\x13</td>
 * <td>^S</td>
 * <td>(Device control 3) (XOFF) (Default UNIX STOP char.)</td>
 * </tr>
 * <tr>
 * <td align="right">\024</td>
 * <td align="right">20</td>
 * <td><tt>DC4</tt></td>
 * <td align="right">\x14</td>
 * <td>^T</td>
 * <td>(Device control 4)</td>
 * </tr>
 * <tr>
 * <td align="right">\025</td>
 * <td align="right">21</td>
 * <td><tt>NAK</tt></td>
 * <td align="right">\x15</td>
 * <td>^U</td>
 * <td>(Negative acknowledge) (see: UNIX keyboard CTRL)</td>
 * </tr>
 * <tr>
 * <td align="right">\026</td>
 * <td align="right">22</td>
 * <td><tt>SYN</tt></td>
 * <td align="right">\x16</td>
 * <td>^V</td>
 * <td>(Synchronous idle)</td>
 * </tr>
 * <tr>
 * <td align="right">\027</td>
 * <td align="right">23</td>
 * <td><tt>ETB</tt></td>
 * <td align="right">\x17</td>
 * <td>^W</td>
 * <td>(End of transmission block)</td>
 * </tr>
 * <tr>
 * <td align="right">\030</td>
 * <td align="right">24</td>
 * <td><tt>CAN</tt></td>
 * <td align="right">\x18</td>
 * <td>^X</td>
 * <td>(Cancel)</td>
 * </tr>
 * <tr>
 * <td align="right">\031</td>
 * <td align="right">25</td>
 * <td><tt>EM</tt></td>
 * <td align="right">\x19</td>
 * <td>^Y</td>
 * <td>(End of medium)</td>
 * </tr>
 * <tr>
 * <td align="right">\032</td>
 * <td align="right">26</td>
 * <td><tt>SUB</tt></td>
 * <td align="right">\x1A</td>
 * <td>^Z</td>
 * <td>(Substitute character)</td>
 * </tr>
 * <tr>
 * <td align="right">\033</td>
 * <td align="right">27</td>
 * <td><tt>ESC</tt></td>
 * <td align="right">\x1B</td>
 * <td>^[</td>
 * <td>(Escape)</td>
 * </tr>
 * <tr>
 * <td align="right">\034</td>
 * <td align="right">28</td>
 * <td><tt>FS</tt></td>
 * <td align="right">\x1C</td>
 * <td>^\</td>
 * <td>(File separator, Information separator four)</td>
 * </tr>
 * <tr>
 * <td align="right">\035</td>
 * <td align="right">29</td>
 * <td><tt>GS</tt></td>
 * <td align="right">\x1D</td>
 * <td>^]</td>
 * <td>(Group separator, Information separator three)</td>
 * </tr>
 * <tr>
 * <td align="right">\036</td>
 * <td align="right">30</td>
 * <td><tt>RS</tt></td>
 * <td align="right">\x1E</td>
 * <td>^^</td>
 * <td>(Record separator, Information separator two)</td>
 * </tr>
 * <tr>
 * <td align="right">\037</td>
 * <td align="right">31</td>
 * <td><tt>US</tt></td>
 * <td align="right">\x1F</td>
 * <td>^_</td>
 * <td>(Unit separator, Information separator one)</td>
 * </tr>
 * <tr>
 * <td align="right">\177</td>
 * <td align="right">127</td>
 * <td><tt>DEL</tt></td>
 * <td align="right">\x7F</td>
 * <td>^?</td>
 * <td>(Delete) (see: UNIX keyboard CTRL)</td>
 * </tr>
 * </table>
 *
 * @author moshew
 */
public interface ControlCharacter {

    /**
     * Null byte.
     */
    char NUL = 0x0000;
    /**
     * Start of heading.
     */
    char SOH = 0x0001;
    /**
     * Start of text.
     */
    char STX = 0x0002;
    /**
     * End of text.
     */
    char ETX = 0x0003;
    /**
     * End of transmission.
     */
    char EOT = 0x0004;
    /**
     * Enquiry.
     */
    char ENQ = 0x0005;
    /**
     * Acknowledge.
     */
    char ACK = 0x0006;
    /**
     * Ring terminal bell.
     */
    char BEL = 0x0007;
    /**
     * Backspace.
     */
    char BS = 0x0008;
    /**
     * Horizontal tabulation.
     */
    char HT = 0x0009;
    /**
     * Line feed.
     */
    char LF = 0x000A;
    /**
     * Vertical tabulation.
     */
    char VT = 0x000B;
    /**
     * Form feed.
     */
    char FF = 0x000C;
    /**
     * Carriage return.
     */
    char CR = 0x000D;
    /**
     * Shift out.
     */
    char SO = 0x000E;
    /**
     * Shift in.
     */
    char SI = 0x000F;
    /**
     * Data link escape.
     */
    char DLE = 0x0010;
    /**
     * Device control 1 (XON).
     */
    char DC1 = 0x0011;
    /**
     * Device control 2.
     */
    char DC2 = 0x0012;
    /**
     * Device control 3 (XOFF).
     */
    char DC3 = 0x0013;
    /**
     * Device control 4.
     */
    char DC4 = 0x0014;
    /**
     * Negative acknowledge.
     */
    char NAK = 0x0015;
    /**
     * Synchronous idle.
     */
    char SYN = 0x0016;
    /**
     * End of transmission block.
     */
    char ETB = 0x0017;
    /**
     * Cancel.
     */
    char CAN = 0x0018;
    /**
     * End of medium.
     */
    char EM = 0x0019;
    /**
     * Substitute character.
     */
    char SUB = 0x001A;
    /**
     * Escape.
     */
    char ESC = 0x001B;
    /**
     * File separator, Information separator four.
     */
    char FS = 0x001C;
    /**
     * Group separator, Information separator three.
     */
    char GS = 0x001D;
    /**
     * Record separator, Information separator two.
     */
    char RS = 0x001E;
    /**
     * Unit separator, Information separator one.
     */
    char US = 0x001F;
    /**
     * Delete.
     */
    char DEL = 0x007F;

}
