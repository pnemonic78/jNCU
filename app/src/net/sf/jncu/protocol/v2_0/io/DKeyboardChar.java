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
package net.sf.jncu.protocol.v2_0.io;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.jncu.protocol.BaseDockCommandToNewton;

/**
 * This command sends 1 character to the Newton for processing. The char is a 2
 * byte Unicode character + a 2 byte state. The state is defined as follows:
 * <ol>
 * <li>Bit 1 = command key down
 * </ol>
 *
 * <pre>
 * 'kbdc'
 * length
 * char
 * state
 * </pre>
 *
 * @author moshew
 */
public class DKeyboardChar extends BaseDockCommandToNewton {

    /**
     * <tt>kDKeyboardChar</tt>
     */
    public static final String COMMAND = "kbdc";

    /**
     * Command key down.
     */
    public static final int STATE_COMMAND_DOWN = 1;
    /**
     * Shift key down.
     */
    public static final int STATE_SHIFT_DOWN = 2;
    /**
     * Caps Lock key down.
     */
    public static final int STATE_CAPS_DOWN = 4;
    /**
     * Option key down.
     */
    public static final int STATE_OPTION_DOWN = 8;
    /**
     * Control key down.
     */
    public static final int STATE_CONTROL_DOWN = 16;

    /**
     * <tt>kTabKey</tt>
     */
    public static final char TAB = '\u0009';
    /**
     * <tt>kBackspaceKey</tt>
     */
    public static final char BACK_SPACE = '\u0008';
    /**
     * <tt>kReturnKey</tt>
     */
    public static final char RETURN = '\r';
    /**
     * <tt>kEnterKey</tt>
     */
    public static final char ENTER = '\u0003';
    /**
     * <tt>kEscKey</tt>
     */
    public static final char ESCAPE = '\u001B';
    /**
     * <tt>kLeftArrowKey</tt>
     */
    public static final char LEFT = '\u001C';
    /**
     * <tt>kRightArrowKey</tt>
     */
    public static final char RIGHT = '\u001D';
    /**
     * <tt>kUpArrowKey</tt>
     */
    public static final char UP = '\u001E';
    /**
     * <tt>kDownArrowKey</tt>
     */
    public static final char DOWN = '\u001F';

    private char c;
    /**
     * Which modifier keys were pressed and other additional information.
     */
    private int state;

    /**
     * Creates a new command.
     */
    public DKeyboardChar() {
        super(COMMAND);
        setLength(4);
    }

    @Override
    protected void writeCommandData(OutputStream data) throws IOException {
        int c = getCharacter() & 0xFFFF;
        int state = getState() & 0xFFFF;
        data.write((c >> 8) & 0xFF);
        data.write((c >> 0) & 0xFF);
        data.write((state >> 8) & 0xFF);
        data.write((state >> 0) & 0xFF);
    }

    /**
     * Get the Unicode character.
     *
     * @return the character.
     */
    public char getCharacter() {
        return c;
    }

    /**
     * Set the Unicode character.
     *
     * @param c the character.
     */
    public void setCharacter(char c) {
        this.c = c;
    }

    /**
     * Get the state.
     *
     * @return the state.
     */
    public int getState() {
        return state;
    }

    /**
     * Set the state.
     *
     * @param state the state.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Convert the Java key code to the Newton key code.
     *
     * @param keyChar the key character.
     * @param keyCode the key code.
     * @return the Newton key code.
     */
    public static char toNewtonChar(char keyChar, int keyCode) {
        if (keyChar == KeyEvent.CHAR_UNDEFINED) {
            switch (keyCode) {
                case KeyEvent.VK_DOWN:
                    return DOWN;
                case KeyEvent.VK_LEFT:
                    return LEFT;
                case KeyEvent.VK_RIGHT:
                    return RIGHT;
                case KeyEvent.VK_UP:
                    return UP;
            }
            return 0;
        }

        switch (keyCode) {
            case KeyEvent.VK_BACK_SPACE:
                return BACK_SPACE;
            case KeyEvent.VK_ESCAPE:
                return ESCAPE;
            case '\r':
                return ENTER;
            case KeyEvent.VK_ENTER:
                return RETURN;
            case KeyEvent.VK_TAB:
                return TAB;
        }
        if (keyCode < 0x20)
            return 0;
        return keyChar;
    }

    /**
     * Convert the Java key modifiers to the Newton key state.
     *
     * @param vk the virtual key code.
     * @return the Newton key state.
     */
    public static int toNewtonState(int modifiers) {
        int flags = 0;
        if ((modifiers & InputEvent.ALT_MASK) != 0)
            flags |= DKeyboardChar.STATE_COMMAND_DOWN;
        if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0)
            flags |= DKeyboardChar.STATE_SHIFT_DOWN;
        if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0)
            flags |= DKeyboardChar.STATE_CONTROL_DOWN;
        if ((modifiers & InputEvent.META_DOWN_MASK) != 0)
            flags |= DKeyboardChar.STATE_OPTION_DOWN;
        return flags;
    }
}
