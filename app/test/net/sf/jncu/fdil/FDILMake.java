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

import org.junit.Test;

import net.sf.junit.SFTestCase;

public class FDILMake extends SFTestCase {

    /**
     * Creates a new test case.
     */
    public FDILMake() {
        super();
    }

    @Test
    public void testMake() throws Exception {
        FDILibrary.startup();

        FDHandle obj;

        obj = FDILibrary.makeInt(0);
        assertNotNull(obj);
        assertTrue(FDILibrary.isInt(obj));
        assertEquals(0, FDILibrary.getInt(obj));

        obj = FDILibrary.makeInt(123);
        assertNotNull(obj);
        assertTrue(FDILibrary.isInt(obj));
        assertEquals(123, FDILibrary.getInt(obj));

        FDILibrary.shutdown();
    }
}
