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
package net.sf.jncu.fdil;

/**
 * Newton Streamed Object Format - Character.
 * <p>
 * Character objects are immediate objects which contain a 16 bit Unicode
 * character.
 *
 * @author Moshe
 */
public class NSOFCharacter extends NSOFImmediate {

    /**
     * Default character class.<br>
     * <tt>kFD_SymChar</tt>
     */
    public static final NSOFSymbol CLASS_CHARACTER = new NSOFSymbol("character");

    protected static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * Constructs a new character.<br>
     * <em>Reserved for use by decoder!</em>
     */
    protected NSOFCharacter() {
        super(IMMEDIATE_CHARACTER);
        setObjectClass(CLASS_CHARACTER);
    }

    /**
     * Constructs a new character.
     *
     * @param value the character.
     */
    public NSOFCharacter(char value) {
        super(value, IMMEDIATE_CHARACTER);
        setObjectClass(CLASS_CHARACTER);
    }

    /**
     * Get the character.
     *
     * @return the character.
     */
    public char getChar() {
        return (char) getValue();
    }

    @Override
    public String toString() {
        final int value = getValue();
        char hex0 = HEX[value & 0x000F];
        char hex1 = HEX[(value >> 4) & 0x000F];
        char hex2 = HEX[(value >> 8) & 0x000F];
        char hex3 = HEX[(value >> 12) & 0x000F];
        return "$\\" + hex3 + hex2 + hex1 + hex0;
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFCharacter(this.getChar());
    }
}
