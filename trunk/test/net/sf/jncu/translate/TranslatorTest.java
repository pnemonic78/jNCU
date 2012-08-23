package net.sf.jncu.translate;

import java.io.InputStream;
import java.util.Collection;

import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFNil;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.util.NewtonDateUtils;
import net.sf.junit.SFTestCase;

import org.junit.Test;

public class TranslatorTest extends SFTestCase {

	private static final long MINUTES_5 = 5 * 60 * 1000;

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
	 * Test translating a plain meeting for 30 minutes.
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

		final long now = System.currentTimeMillis();

		NSOFFrame meeting = new NSOFFrame();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, new NSOFInteger(NewtonDateUtils.getMinutes(now)));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		NSOFObject toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(meeting, toNewton);
	}

	/**
	 * Test translating a plain meeting for 150 minutes.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMeetingPlain150() throws Exception {
		TranslatorFactory factory = TranslatorFactory.getInstance();
		assertNotNull(factory);

		Collection<? extends Translator> translators = factory.getTranslatorsBySuffix("ics");
		assertNotNull(translators);
		assertFalse(0 == translators.size());
		Translator translator = translators.iterator().next();
		assertNotNull(translator);

		final long now = System.currentTimeMillis();

		NSOFFrame meeting = new NSOFFrame();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(150));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, new NSOFInteger(NewtonDateUtils.getMinutes(now)));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		NSOFObject toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(meeting, toNewton);
	}

	/**
	 * Test translating a plain meeting for 30 minutes with an alarm 5 minutes
	 * before.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMeetingAlarm() throws Exception {
		TranslatorFactory factory = TranslatorFactory.getInstance();
		assertNotNull(factory);

		Collection<? extends Translator> translators = factory.getTranslatorsBySuffix("ics");
		assertNotNull(translators);
		assertFalse(0 == translators.size());
		Translator translator = translators.iterator().next();
		assertNotNull(translator);

		final long now = System.currentTimeMillis();

		NSOFFrame meeting = new NSOFFrame();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, new NSOFInteger(NewtonDateUtils.getMinutes(now - MINUTES_5)));
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, new NSOFInteger(NewtonDateUtils.getMinutes(now)));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		NSOFObject toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(meeting, toNewton);
	}

	/**
	 * Test translating a plain meeting with notes.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMeetingNotes() throws Exception {
		TranslatorFactory factory = TranslatorFactory.getInstance();
		assertNotNull(factory);

		Collection<? extends Translator> translators = factory.getTranslatorsBySuffix("ics");
		assertNotNull(translators);
		assertFalse(0 == translators.size());
		Translator translator = translators.iterator().next();
		assertNotNull(translator);

		final long now = System.currentTimeMillis();
		// TODO NSOFArray mtgNotes = new NSOFPlainArray();

		NSOFFrame meeting = new NSOFFrame();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, new NSOFInteger(NewtonDateUtils.getMinutes(now)));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL/* TODO mtgNotes */);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		NSOFObject toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(meeting, toNewton);
	}
}
