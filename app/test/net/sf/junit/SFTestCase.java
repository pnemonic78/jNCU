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
package net.sf.junit;

import junit.framework.TestCase;

import java.nio.CharBuffer;

/**
 * SourceForge test case.
 *
 * @author moshew
 */
public class SFTestCase extends TestCase {

    /**
     * Constructs a test case.
     */
    public SFTestCase() {
        super();
    }

    /**
     * Constructs a test case with the given name.
     *
     * @param name the name.
     */
    public SFTestCase(String name) {
        super(name);
    }

    /**
     * Asserts that two arrays are equal.
     *
     * @param expected the expected value.
     * @param actual   the actual value.
     */
    public void assertEquals(byte[] expected, byte[] actual) {
        if (expected == actual) {
            return;
        }
        if (expected == null) {
            if (actual != null) {
                fail("expected: <null> but was <" + actual + ">");
            }
            return;
        }
        if (actual == null) {
            fail("expected: <" + expected + "> but was <null>");
            return; // redundant, but here for compiler warnings later.
        }
        if (expected.length != actual.length) {
            fail("expected length: " + expected.length + " but was " + actual.length);
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                fail("at index " + i + ", expected: <" + expected[i] + "> but was <" + actual[i] + ">");
            }
        }
    }

    /**
     * Asserts that two arrays are equal.
     *
     * @param expected the expected value.
     * @param actual   the actual value.
     */
    public void assertEquals(int[] expected, int[] actual) {
        if (expected == actual) {
            return;
        }
        if (expected == null) {
            if (actual != null) {
                fail("expected: <null> but was <" + actual + ">");
            }
            return;
        }
        if (actual == null) {
            fail("expected: <" + expected + "> but was <null>");
            return; // redundant, but here for compiler warnings later.
        }
        if (expected.length != actual.length) {
            fail("expected length: " + expected.length + " but was " + actual.length);
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                fail("at index " + i + ", expected: <" + expected[i] + "> but was <" + actual[i] + ">");
            }
        }
    }

    /**
     * Asserts that two arrays are equal.
     *
     * @param expected the expected value.
     * @param actual   the actual value.
     */
    public void assertEquals(char[] expected, char[] actual) {
        if (expected == actual) {
            return;
        }
        if (expected == null) {
            if (actual != null) {
                fail("expected: <null> but was <" + actual.toString() + ">");
            }
            return;
        }
        if (actual == null) {
            fail("expected: <" + expected.toString() + "> but was <null>");
            return; // redundant, but here for compiler warnings later.
        }
        if (expected.length != actual.length) {
            fail("expected length: " + expected.length + " but was " + actual.length);
        }

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                fail("at index " + i + ", expected: <" + expected[i] + "> but was <" + actual[i] + ">");
            }
        }
    }

    public void assertEquals(CharBuffer expected, CharBuffer actual) {
        if (expected.remaining() != actual.remaining()) {
            fail("expected remaining: " + expected.remaining() + " but was " + actual.remaining());
        }
        int p = expected.position();
        for (int i = expected.limit() - 1, j = actual.limit() - 1; i >= p; i--, j--) {
            char v1 = expected.get(i);
            char v2 = actual.get(j);
            if (v1 != v2) {
                fail("at index " + i + ", expected: <" + v1 + ", " + Integer.toHexString(v1) + "> but was <" + v2 + ", " + Integer.toHexString(v2) + ">");
            }
        }
    }
}
