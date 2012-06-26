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
