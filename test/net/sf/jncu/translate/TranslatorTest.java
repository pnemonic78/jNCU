package net.sf.jncu.translate;

import java.io.InputStream;
import java.util.Collection;

import org.junit.Test;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFNil;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.util.NewtonDateUtils;
import net.sf.junit.SFTestCase;

public class TranslatorTest extends SFTestCase {

	public TranslatorTest() {
		super("Translator");
	}

	/**
	 * Test the list of registered translators.
	 * 
	 * @throws Exception
	 */
	@Test
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

	/**
	 * Test translating a plain meeting.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMeetingPlain() throws Exception {
		TranslatorFactory factory = TranslatorFactory.getInstance();
		assertNotNull(factory);

		Collection<? extends Translator> translators = factory.getTranslatorsBySuffix("ics");
		assertNotNull(translators);
		assertFalse(0 == translators.size());
		Translator translator = translators.iterator().next();
		assertNotNull(translator);

		NSOFFrame meeting = new NSOFFrame();
		meeting.put(ICalendarTranslator.SLOT_CLASS, ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, new NSOFInteger(NewtonDateUtils.getMinutes(System.currentTimeMillis())));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		NSOFObject toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(meeting, toNewton);
	}
}
