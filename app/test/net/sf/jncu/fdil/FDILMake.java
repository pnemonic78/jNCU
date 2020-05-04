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
package net.sf.jncu.fdil;

import net.sf.junit.SFTestCase;

import org.junit.Test;

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
