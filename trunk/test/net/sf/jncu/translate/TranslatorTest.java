package net.sf.jncu.translate;

import java.util.Collection;

import net.sf.jncu.translate.Translator;
import net.sf.jncu.translate.TranslatorFactory;
import net.sf.junit.SFTestCase;

public class TranslatorTest extends SFTestCase {

	public TranslatorTest() {
		super("Translator");
	}

	public void testList() throws Exception {
		TranslatorFactory factory = TranslatorFactory.getInstance();
		assertNotNull(factory);

		Collection<? extends Translator> translators = factory.getTranslatorsBySuffix(null);
		assertNull(translators);

		translators = factory.getTranslatorsBySuffix("zzz");
		assertNull(translators);

		translators = factory.getTranslatorsBySuffix("WMF");
		assertNotNull(translators);
		assertEquals(1, translators.size());

		translators = factory.getTranslatorsBySuffix("txt");
		assertNotNull(translators);
		assertEquals(2, translators.size());
	}
}
