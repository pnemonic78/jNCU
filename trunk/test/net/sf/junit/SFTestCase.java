package net.sf.junit;

import junit.framework.TestCase;

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
	 * @param name
	 *            the name.
	 */
	public SFTestCase(String name) {
		super(name);
	}

	/**
	 * Asserts that two arrays are equal.
	 * 
	 * @param expected
	 *            the expected value.
	 * @param actual
	 *            the actual value.
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
			return;
		}
		if (expected.length != actual.length) {
			fail("expected length: " + expected.length + " but was " + actual.length);
		}

		for (int i = 0; i < expected.length; i++) {
			if (expected[i] != actual[i]) {
				fail("at index " + i + ", expected: <" + expected[i] + "> but was " + actual[i] + ">");
			}
		}
	}

	/**
	 * Asserts that two arrays are equal.
	 * 
	 * @param expected
	 *            the expected value.
	 * @param actual
	 *            the actual value.
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
			return;
		}
		if (expected.length != actual.length) {
			fail("expected length: " + expected.length + " but was " + actual.length);
		}

		for (int i = 0; i < expected.length; i++) {
			if (expected[i] != actual[i]) {
				fail("at index " + i + ", expected: <" + expected[i] + "> but was " + actual[i] + ">");
			}
		}
	}

}
