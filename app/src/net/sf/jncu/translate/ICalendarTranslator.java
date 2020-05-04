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
package net.sf.jncu.translate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.CuType;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Trigger;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;
import net.sf.jncu.fdil.NSOFArray;
import net.sf.jncu.fdil.NSOFFrame;
import net.sf.jncu.fdil.NSOFImmediate;
import net.sf.jncu.fdil.NSOFInteger;
import net.sf.jncu.fdil.NSOFNil;
import net.sf.jncu.fdil.NSOFObject;
import net.sf.jncu.fdil.NSOFPlainArray;
import net.sf.jncu.fdil.NSOFString;
import net.sf.jncu.fdil.NSOFSymbol;
import net.sf.jncu.newton.os.Soup;
import net.sf.jncu.newton.os.SoupEntry;
import net.sf.jncu.util.NewtonDateUtils;

/**
 * iCalendar translator.
 * <p>
 * See <a href="http://tools.ietf.org/html/rfc5545">RFC 5545</a> for the
 * iCalendar specification.
 * <p>
 * Here is a simple example from <a
 * href="http://en.wikipedia.org/wiki/ICalendar">Wikipedia</a>:<br>
 * <br>
 * <tt>BEGIN:VCALENDAR<br>
 * VERSION:2.0<br>
 * PRODID:-//hacksw/handcal//NONSGML v1.0//EN<br>
 * BEGIN:VEVENT<br>
 * UID:uid1@example.com<br>
 * DTSTAMP:19970714T170000Z<br>
 * ORGANIZER;CN=John Doe:MAILTO:john.doe@example.com<br>
 * DTSTART:19970714T170000Z<br>
 * DTEND:19970715T035959Z<br>
 * SUMMARY:Bastille Day Party<br>
 * END:VEVENT<br>
 * END:VCALENDAR<br></tt>
 * 
 * @author Moshe
 */
public class ICalendarTranslator extends CalendarTranslator {

	private static final String[] EXT = { "ics", "ifb" };

	public static final NSOFSymbol SLOT_CLASS = new NSOFSymbol("class");
	public static final NSOFSymbol SLOT_ALARM = new NSOFSymbol("mtgAlarm");
	public static final NSOFSymbol SLOT_DURATION = new NSOFSymbol("mtgDuration");
	public static final NSOFSymbol SLOT_ICON_TYPE = new NSOFSymbol("mtgIconType");
	public static final NSOFSymbol SLOT_INVITEES = new NSOFSymbol("mtgInvitees");
	public static final NSOFSymbol SLOT_LOCATION = new NSOFSymbol("mtgLocation");
	public static final NSOFSymbol SLOT_START_DATE = new NSOFSymbol("mtgStartDate");
	public static final NSOFSymbol SLOT_TEXT = new NSOFSymbol("mtgText");
	public static final NSOFSymbol SLOT_NOTES = new NSOFSymbol("NotesData");
	public static final NSOFSymbol SLOT_BOUNDS = new NSOFSymbol("viewBounds");
	public static final NSOFSymbol SLOT_STATIONERY = new NSOFSymbol("viewStationery");
	public static final NSOFSymbol SLOT_COMPANY = new NSOFSymbol("company");
	public static final NSOFSymbol SLOT_LABELS = new NSOFSymbol("labels");
	public static final NSOFSymbol SLOT_ALIAS = new NSOFSymbol("_alias");
	public static final NSOFSymbol SLOT_ENTRY_CLASS = new NSOFSymbol("_entryClass");
	public static final NSOFSymbol SLOT_FAKE_ID = new NSOFSymbol("_fakeID");
	public static final NSOFSymbol SLOT_UNSELECTED = new NSOFSymbol("_unselected");
	public static final NSOFSymbol SLOT_NAME = new NSOFSymbol("name");
	public static final NSOFSymbol SLOT_NAME_FIRST = new NSOFSymbol("first");
	public static final NSOFSymbol SLOT_NAME_LAST = new NSOFSymbol("last");
	public static final NSOFSymbol SLOT_GROUP = new NSOFSymbol("group");

	public static final NSOFSymbol CLASS_MEETING = new NSOFSymbol("meeting");
	public static final NSOFSymbol CLASS_NAMEREF_LOCATION = new NSOFSymbol("nameRef.meetingPlace");
	public static final NSOFSymbol CLASS_COMPANY = new NSOFSymbol("company");
	public static final NSOFSymbol CLASS_NAMEREF_PEOPLE = new NSOFSymbol("nameRef.people");
	public static final NSOFSymbol CLASS_PERSON = new NSOFSymbol("person");
	public static final NSOFSymbol CLASS_GROUP = new NSOFSymbol("group");

	public static final NSOFSymbol STATIONERY_MEETING = new NSOFSymbol("meeting");

	public static final String CATEGORY_MEETING = "MEETING";

	protected static final String SOUP_CALENDAR = "Calendar";
	protected static final String SOUP_CALENDAR_NOTES = "Calendar Notes";
	protected static final String SOUP_REPEAT = "Repeat Meetings";
	protected static final String SOUP_REPEAT_NOTES = "Repeat Notes";

	private static UidGenerator uidGenerator;

	/**
	 * Get the file filter extensions.
	 * 
	 * @return the array of extensions.
	 */
	public static String[] getFilterExtensions() {
		return EXT;
	}

	/**
	 * Constructs a new translator.
	 */
	public ICalendarTranslator() {
		super();
	}

	@Override
	public String getName() {
		return "iCalendar";
	}

	@Override
	public String getApplicationName() {
		return "Dates";
	}

	@Override
	public Collection<Soup> translateToNewton(InputStream in) throws TranslationException {
		CalendarBuilder builder = new CalendarBuilder();
		Calendar cal;
		try {
			cal = builder.build(in);
		} catch (IOException ioe) {
			throw new TranslationException(ioe);
		} catch (ParserException pe) {
			throw new TranslationException(pe);
		}

		if (isMeeting(cal))
			return translateToMeeting(cal);
		if (isWeeklyMeeting(cal))
			return translateToMeeting(cal);
		if (isEvent(cal))
			return translateToMeeting(cal);
		if (isMultiDayEvent(cal))
			return translateToMeeting(cal);
		if (isAnnualEvent(cal))
			return translateToMeeting(cal);
		return null;
	}

	/**
	 * Is the calendar item a plain "Meeting"?
	 * 
	 * @param cal
	 *            the iCalendar.
	 * @return {@code true} if a plain meeting.
	 */
	protected boolean isMeeting(Calendar cal) {
		return true;
	}

	/**
	 * Is the calendar item a "Weekly Meeting"?
	 * 
	 * @param cal
	 *            the iCalendar.
	 * @return {@code true} if a weekly meeting.
	 */
	protected boolean isWeeklyMeeting(Calendar cal) {
		return false;
	}

	/**
	 * Is the calendar item an "Event"?
	 * 
	 * @param cal
	 *            the iCalendar.
	 * @return {@code true} if an event.
	 */
	protected boolean isEvent(Calendar cal) {
		return false;
	}

	/**
	 * Is the calendar item an "Multi-Day Event"?
	 * 
	 * @param cal
	 *            the iCalendar.
	 * @return {@code true} if a multi-day event.
	 */
	protected boolean isMultiDayEvent(Calendar cal) {
		return false;
	}

	/**
	 * Is the calendar item an "Annual Event"?
	 * 
	 * @param cal
	 *            the iCalendar.
	 * @return {@code true} if an annual event.
	 */
	protected boolean isAnnualEvent(Calendar cal) {
		return false;
	}

	/**
	 * Translate to "Meeting".
	 * 
	 * @param cal
	 *            the iCalendar.
	 * @return the soup entries.
	 * @throws TranslationException
	 *             if a translation error occurs.
	 */
	protected Collection<Soup> translateToMeeting(Calendar cal) throws TranslationException {
		Collection<Soup> soups = new ArrayList<Soup>();
		VEvent event = (VEvent) cal.getComponent(Component.VEVENT);
		DtStart start = event.getStartDate();
		if (start == null)
			throw new TranslationException("start date required");
		Dur duration = event.getDuration().getDuration();
		if (duration == null)
			throw new TranslationException("duration required");
		Summary summary = event.getSummary();
		VAlarm alarm = (VAlarm) event.getAlarms().getComponent(Component.VALARM);
		Location location = event.getLocation();
		PropertyList attendees = event.getProperties(Property.ATTENDEE);

		NSOFInteger mtgStartDate = NewtonDateUtils.toMinutes(start.getDate().getTime());
		NSOFInteger mtgDuration = new NSOFInteger(toMinutes(duration));
		NSOFString mtgText = new NSOFString(summary.getValue());
		NSOFInteger mtgAlarm = null;
		if (alarm != null) {
			Trigger alarmTrigger = alarm.getTrigger();
			if (alarmTrigger != null) {
				Date alarmDate = alarmTrigger.getDate();
				if (alarmDate != null) {
					mtgAlarm = NewtonDateUtils.toMinutes(alarmDate.getTime());
				}
			}
		}
		NSOFFrame mtgLocation = null;
		if (location != null) {
			mtgLocation = new NSOFFrame();
			mtgLocation.setObjectClass(ICalendarTranslator.CLASS_NAMEREF_LOCATION);
			mtgLocation.put(ICalendarTranslator.SLOT_COMPANY, new NSOFString(location.getValue()));
			mtgLocation.put(ICalendarTranslator.SLOT_ENTRY_CLASS, CLASS_COMPANY);
			mtgLocation.put(ICalendarTranslator.SLOT_FAKE_ID, NSOFNil.NIL);
			mtgLocation.put(ICalendarTranslator.SLOT_UNSELECTED, NSOFNil.NIL);
		}
		NSOFArray mtgInvitees = null;
		if (!attendees.isEmpty()) {
			mtgInvitees = new NSOFPlainArray();
			NSOFFrame invitee;
			Attendee attendee;
			Cn cn;
			CuType cuType;
			NSOFFrame person;
			NSOFString company, group;
			String name, first, last;
			int indexSpace;

			for (Object item : attendees) {
				attendee = (Attendee) item;
				cn = (Cn) attendee.getParameter(Parameter.CN);
				cuType = (CuType) attendee.getParameter(Parameter.CUTYPE);

				invitee = new NSOFFrame();
				invitee.setObjectClass(ICalendarTranslator.CLASS_NAMEREF_PEOPLE);
				invitee.put(ICalendarTranslator.SLOT_FAKE_ID, NSOFNil.NIL);
				invitee.put(ICalendarTranslator.SLOT_UNSELECTED, NSOFNil.NIL);
				mtgInvitees.add(invitee);

				if (cuType == CuType.GROUP) {
					group = (cn == null) ? null : new NSOFString(cn.getValue());

					invitee.put(ICalendarTranslator.SLOT_ENTRY_CLASS, ICalendarTranslator.CLASS_GROUP);
					invitee.put(ICalendarTranslator.SLOT_GROUP, group);
				} else if ((cuType == CuType.ROOM) || (cuType == CuType.RESOURCE)) {
					company = (cn == null) ? null : new NSOFString(cn.getValue());

					invitee.put(ICalendarTranslator.SLOT_ENTRY_CLASS, ICalendarTranslator.CLASS_COMPANY);
					invitee.put(ICalendarTranslator.SLOT_COMPANY, company);
				} else {
					name = (cn == null) ? "" : cn.getValue();
					indexSpace = name.lastIndexOf(' ');
					if (indexSpace >= 0) {
						first = name.substring(0, indexSpace);
						last = name.substring(indexSpace + 1);
					} else {
						first = name;
						last = null;
					}

					person = new NSOFFrame();
					person.setObjectClass(ICalendarTranslator.CLASS_PERSON);
					person.put(ICalendarTranslator.SLOT_NAME_FIRST, new NSOFString(first));
					person.put(ICalendarTranslator.SLOT_NAME_LAST, new NSOFString(last));

					invitee.put(ICalendarTranslator.SLOT_ENTRY_CLASS, ICalendarTranslator.CLASS_PERSON);
					invitee.put(ICalendarTranslator.SLOT_NAME, person);
				}
			}
		}

		SoupEntry meeting = new SoupEntry();
		meeting.put(SLOT_CLASS, CLASS_MEETING);
		meeting.put(SLOT_ALARM, mtgAlarm);
		meeting.put(SLOT_DURATION, mtgDuration);
		meeting.put(SLOT_ICON_TYPE, NSOFNil.NIL);
		meeting.put(SLOT_INVITEES, mtgInvitees);
		meeting.put(SLOT_LOCATION, mtgLocation);
		meeting.put(SLOT_START_DATE, mtgStartDate);
		meeting.put(SLOT_TEXT, mtgText);
		meeting.put(SLOT_NOTES, NSOFNil.NIL);
		meeting.put(SLOT_BOUNDS, NSOFNil.NIL);
		meeting.put(SLOT_STATIONERY, STATIONERY_MEETING);

		Soup soup = new Soup(SOUP_CALENDAR);
		soup.addEntry(meeting);
		soups.add(soup);

		return soups;
	}

	@Override
	public InputStream translateFromNewton(SoupEntry entry) throws TranslationException {
		NSOFFrame meeting = entry;
		NSOFImmediate mtgAlarm = (NSOFImmediate) meeting.get(SLOT_ALARM);
		NSOFImmediate mtgStartDate = (NSOFImmediate) meeting.get(SLOT_START_DATE);
		if (NSOFImmediate.isNil(mtgStartDate))
			throw new TranslationException("slot " + SLOT_START_DATE + " required");
		NSOFImmediate mtgDuration = (NSOFImmediate) meeting.get(SLOT_DURATION);
		if (NSOFImmediate.isNil(mtgDuration))
			throw new TranslationException("slot " + SLOT_DURATION + " required");
		NSOFObject mtgText = meeting.get(SLOT_TEXT);
		NSOFObject mtgLocation = meeting.get(SLOT_LOCATION);
		NSOFObject mtgInvitees = meeting.get(SLOT_INVITEES);

		Date start = new DateTime(NewtonDateUtils.fromMinutes(mtgStartDate.getValue()));
		Dur duration = new Dur(0, 0, mtgDuration.getValue(), 0);
		String summary = NSOFImmediate.isNil(mtgText) ? "" : ((NSOFString) mtgText).getValue();
		VEvent event = new VEvent(start, duration, summary);
		Uid uid = getUidGenerator().generateUid();
		event.getProperties().add(uid);
		event.getProperties().add(new Categories(CATEGORY_MEETING));
		if (!NSOFImmediate.isNil(mtgAlarm)) {
			VAlarm alarm = new VAlarm(new DateTime(NewtonDateUtils.fromMinutes(mtgAlarm.getValue())));
			alarm.getProperties().add(Action.DISPLAY);
			alarm.getProperties().add(new Description(summary));
			event.getAlarms().add(alarm);
		}
		if (!NSOFImmediate.isNil(mtgLocation) && CLASS_NAMEREF_LOCATION.equals(mtgLocation.getObjectClass())) {
			NSOFFrame mtgLocationFrame = (NSOFFrame) mtgLocation;
			NSOFString company = (NSOFString) mtgLocationFrame.get(SLOT_COMPANY);
			Location location = new Location(company.getValue());
			event.getProperties().add(location);
		}
		if (!NSOFImmediate.isNil(mtgInvitees)) {
			NSOFArray mtgInviteesArr = (NSOFArray) mtgInvitees;
			int size = mtgInviteesArr.length();
			NSOFFrame invitee;
			NSOFFrame person;
			NSOFString first;
			NSOFString last;
			NSOFObject value;
			String name;
			Attendee attendee;
			NSOFSymbol entryClass;
			NSOFString company, group;

			for (int i = 0; i < size; i++) {
				invitee = (NSOFFrame) mtgInviteesArr.get(i);
				if (!CLASS_NAMEREF_PEOPLE.equals(invitee.getObjectClass()))
					continue;
				attendee = new Attendee();
				try {
					attendee.setValue("mailto:jncu@sourceforge.net");
				} catch (URISyntaxException e) {
					throw new TranslationException(e);
				}
				name = null;
				entryClass = (NSOFSymbol) invitee.get(SLOT_ENTRY_CLASS);
				if (CLASS_PERSON.equals(entryClass)) {
					person = (NSOFFrame) invitee.get(SLOT_NAME);
					attendee.getParameters().add(CuType.INDIVIDUAL);

					first = null;
					value = person.get(SLOT_NAME_FIRST);
					if (!NSOFImmediate.isNil(value)) {
						first = (NSOFString) value;
						name = first.getValue();
					}

					last = null;
					value = person.get(SLOT_NAME_LAST);
					if (!NSOFImmediate.isNil(value)) {
						last = (NSOFString) value;
						if (name == null)
							name = last.getValue();
						else
							name += " " + last.getValue();
					}
				} else if (CLASS_GROUP.equals(entryClass)) {
					group = (NSOFString) invitee.get(SLOT_GROUP);
					attendee.getParameters().add(CuType.GROUP);
					name = group.getValue();
				} else if (CLASS_COMPANY.equals(entryClass)) {
					company = (NSOFString) invitee.get(SLOT_COMPANY);
					attendee.getParameters().add(CuType.ROOM);
					name = company.getValue();
				}
				if (name != null) {
					name = name.trim();
					attendee.getParameters().add(new Cn(name));
					event.getProperties().add(attendee);
				}
			}
		}
		Calendar cal = new Calendar();
		cal.getProperties().add(new ProdId("-//jNCU//iCal4j 1.0//EN"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getProperties().add(CalScale.GREGORIAN);
		cal.getComponents().add(event);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CalendarOutputter outputter = new CalendarOutputter();
		try {
			outputter.output(cal, out);
		} catch (ValidationException ve) {
			throw new TranslationException(ve);
		} catch (IOException ioe) {
			throw new TranslationException(ioe);
		}

		byte[] b = out.toByteArray();
		return new ByteArrayInputStream(b);
	}

	/**
	 * Get the UID generator instance.
	 * 
	 * @return the generator.
	 * @throws TranslationException
	 *             if a socket error occurs.
	 */
	protected UidGenerator getUidGenerator() throws TranslationException {
		if (uidGenerator == null) {
			try {
				uidGenerator = new UidGenerator("jNCU");
			} catch (SocketException se) {
				throw new TranslationException(se);
			}
		}
		return uidGenerator;
	}

	/**
	 * Calculate the duration as minutes.
	 * 
	 * @param duration
	 *            the duration.
	 * @return the number of minutes.
	 */
	protected static int toMinutes(Dur duration) {
		int durationWeeks = duration.getWeeks();
		int durationDays = duration.getDays();
		int durationHours = duration.getHours();
		int durationMinutes = duration.getMinutes();
		int durationSeconds = duration.getSeconds();

		int weeksToDays = durationWeeks * 7;
		int daysToHours = (weeksToDays + durationDays) * 24;
		int hoursToMinutes = (daysToHours + durationHours) * 60;
		int secondsToMinutes = durationSeconds / 60;

		return hoursToMinutes + durationMinutes + secondsToMinutes;
	}
}
