package net.sf.jncu.translate;

import java.io.InputStream;
import java.util.Collection;

import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFNil;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
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
	public void testRegistry() throws Exception {
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

		SoupEntry meeting = new SoupEntry();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, NewtonDateUtils.toMinutes(now));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		Collection<Soup> toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(1, toNewton.size());
		Soup soup = toNewton.iterator().next();
		assertNotNull(soup);
		assertEquals("Calendar", soup.getName());
		SoupEntry entry = soup.getEntries().iterator().next();
		assertNotNull(entry);
		assertEquals(meeting, entry);
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

		SoupEntry meeting = new SoupEntry();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(150));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, NewtonDateUtils.toMinutes(now));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		Collection<Soup> toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(1, toNewton.size());
		Soup soup = toNewton.iterator().next();
		assertNotNull(soup);
		assertEquals("Calendar", soup.getName());
		SoupEntry entry = soup.getEntries().iterator().next();
		assertNotNull(entry);
		assertEquals(meeting, entry);
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

		SoupEntry meeting = new SoupEntry();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NewtonDateUtils.toMinutes(now - MINUTES_5));
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, NewtonDateUtils.toMinutes(now));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		Collection<Soup> toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(1, toNewton.size());
		Soup soup = toNewton.iterator().next();
		assertNotNull(soup);
		assertEquals("Calendar", soup.getName());
		SoupEntry entry = soup.getEntries().iterator().next();
		assertNotNull(entry);
		assertEquals(meeting, entry);
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

		SoupEntry meeting = new SoupEntry();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, NewtonDateUtils.toMinutes(now));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL/* TODO mtgNotes */);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		Collection<Soup> toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(1, toNewton.size());
		Soup soup = toNewton.iterator().next();
		assertNotNull(soup);
		assertEquals("Calendar", soup.getName());
		SoupEntry entry = soup.getEntries().iterator().next();
		assertNotNull(entry);
		assertEquals(meeting, entry);
	}

	/**
	 * Test translating a plain meeting at a location.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMeetingLocation() throws Exception {
		TranslatorFactory factory = TranslatorFactory.getInstance();
		assertNotNull(factory);

		Collection<? extends Translator> translators = factory.getTranslatorsBySuffix("ics");
		assertNotNull(translators);
		assertFalse(0 == translators.size());
		Translator translator = translators.iterator().next();
		assertNotNull(translator);

		final long now = System.currentTimeMillis();
		NSOFFrame mtgLocation = new NSOFFrame();
		mtgLocation.setObjectClass(ICalendarTranslator.CLASS_NAMEREF_LOCATION);
		mtgLocation.put(ICalendarTranslator.SLOT_COMPANY, new NSOFString("Shopping Mall"));
		mtgLocation.put(ICalendarTranslator.SLOT_ENTRY_CLASS, ICalendarTranslator.CLASS_COMPANY);
		mtgLocation.put(ICalendarTranslator.SLOT_FAKE_ID, NSOFNil.NIL);
		mtgLocation.put(ICalendarTranslator.SLOT_UNSELECTED, NSOFNil.NIL);

		SoupEntry meeting = new SoupEntry();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, mtgLocation);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, NewtonDateUtils.toMinutes(now));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		Collection<Soup> toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(1, toNewton.size());
		Soup soup = toNewton.iterator().next();
		assertNotNull(soup);
		assertEquals("Calendar", soup.getName());
		SoupEntry entry = soup.getEntries().iterator().next();
		assertNotNull(entry);
		assertEquals(meeting, entry);
	}

	/**
	 * Test translating a meeting with an invitee.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMeetingInvitee() throws Exception {
		TranslatorFactory factory = TranslatorFactory.getInstance();
		assertNotNull(factory);

		Collection<? extends Translator> translators = factory.getTranslatorsBySuffix("ics");
		assertNotNull(translators);
		assertFalse(0 == translators.size());
		Translator translator = translators.iterator().next();
		assertNotNull(translator);

		final long now = System.currentTimeMillis();
		NSOFArray mtgInvitees = new NSOFPlainArray();
		NSOFFrame person = new NSOFFrame();
		person.setObjectClass(ICalendarTranslator.CLASS_PERSON);
		person.put(ICalendarTranslator.SLOT_NAME_FIRST, new NSOFString("John"));
		person.put(ICalendarTranslator.SLOT_NAME_LAST, new NSOFString("Doe"));
		NSOFFrame invitee = new NSOFFrame();
		invitee.setObjectClass(ICalendarTranslator.CLASS_NAMEREF_PEOPLE);
		invitee.put(ICalendarTranslator.SLOT_NAME, person);
		invitee.put(ICalendarTranslator.SLOT_ENTRY_CLASS, ICalendarTranslator.CLASS_PERSON);
		invitee.put(ICalendarTranslator.SLOT_FAKE_ID, NSOFNil.NIL);
		invitee.put(ICalendarTranslator.SLOT_UNSELECTED, NSOFNil.NIL);
		mtgInvitees.add(invitee);

		SoupEntry meeting = new SoupEntry();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, mtgInvitees);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, NewtonDateUtils.toMinutes(now));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		Collection<Soup> toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(1, toNewton.size());
		Soup soup = toNewton.iterator().next();
		assertNotNull(soup);
		assertEquals("Calendar", soup.getName());
		SoupEntry entry = soup.getEntries().iterator().next();
		assertNotNull(entry);
		assertEquals(meeting, entry);
	}

	/**
	 * Test translating a meeting with invitees.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMeetingInvitees() throws Exception {
		TranslatorFactory factory = TranslatorFactory.getInstance();
		assertNotNull(factory);

		Collection<? extends Translator> translators = factory.getTranslatorsBySuffix("ics");
		assertNotNull(translators);
		assertFalse(0 == translators.size());
		Translator translator = translators.iterator().next();
		assertNotNull(translator);

		final long now = System.currentTimeMillis();
		NSOFArray mtgInvitees = new NSOFPlainArray();
		NSOFFrame person = new NSOFFrame();
		person.setObjectClass(ICalendarTranslator.CLASS_PERSON);
		person.put(ICalendarTranslator.SLOT_NAME_FIRST, new NSOFString("John"));
		person.put(ICalendarTranslator.SLOT_NAME_LAST, new NSOFString("Doe"));
		NSOFFrame invitee = new NSOFFrame();
		invitee.setObjectClass(ICalendarTranslator.CLASS_NAMEREF_PEOPLE);
		invitee.put(ICalendarTranslator.SLOT_NAME, person);
		invitee.put(ICalendarTranslator.SLOT_ENTRY_CLASS, ICalendarTranslator.CLASS_PERSON);
		invitee.put(ICalendarTranslator.SLOT_FAKE_ID, NSOFNil.NIL);
		invitee.put(ICalendarTranslator.SLOT_UNSELECTED, NSOFNil.NIL);
		mtgInvitees.add(invitee);

		person = new NSOFFrame();
		person.setObjectClass(ICalendarTranslator.CLASS_PERSON);
		person.put(ICalendarTranslator.SLOT_NAME_FIRST, new NSOFString("Jane"));
		person.put(ICalendarTranslator.SLOT_NAME_LAST, new NSOFString("Smith"));
		invitee = new NSOFFrame();
		invitee.setObjectClass(ICalendarTranslator.CLASS_NAMEREF_PEOPLE);
		invitee.put(ICalendarTranslator.SLOT_NAME, person);
		invitee.put(ICalendarTranslator.SLOT_ENTRY_CLASS, ICalendarTranslator.CLASS_PERSON);
		invitee.put(ICalendarTranslator.SLOT_FAKE_ID, NSOFNil.NIL);
		invitee.put(ICalendarTranslator.SLOT_UNSELECTED, NSOFNil.NIL);
		mtgInvitees.add(invitee);

		SoupEntry meeting = new SoupEntry();
		meeting.setObjectClass(ICalendarTranslator.CLASS_MEETING);
		meeting.put(ICalendarTranslator.SLOT_ALARM, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_DURATION, new NSOFInteger(30));
		meeting.put(ICalendarTranslator.SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_INVITEES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_LOCATION, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_START_DATE, NewtonDateUtils.toMinutes(now));
		meeting.put(ICalendarTranslator.SLOT_TEXT, new NSOFString("buy groceries"));
		meeting.put(ICalendarTranslator.SLOT_NOTES, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(ICalendarTranslator.SLOT_STATIONERY, ICalendarTranslator.STATIONERY_MEETING);

		InputStream fromNewton = translator.translateFromNewton(meeting);
		assertNotNull(fromNewton);
		Collection<Soup> toNewton = translator.translateToNewton(fromNewton);
		assertNotNull(toNewton);
		assertEquals(1, toNewton.size());
		Soup soup = toNewton.iterator().next();
		assertNotNull(soup);
		assertEquals("Calendar", soup.getName());
		SoupEntry entry = soup.getEntries().iterator().next();
		assertNotNull(entry);
		assertEquals(meeting, entry);
	}
}
