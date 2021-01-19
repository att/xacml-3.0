/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

import com.att.research.xacml.api.SemanticString;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * ISO8601Time represents a time of day with an optional timezone indication using ISO8601 standard representations
 * for time, ordering, and operations.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
@EqualsAndHashCode
public class ISO8601Time implements IDateTime<ISO8601Time>, Comparable<ISO8601Time>, SemanticString {
	private static final int ARBITRARY_YEAR		= 1970;
	private static final int ARBITRARY_MONTH	= 11;
	private static final int ARBITRARY_DAY		= 15;
	
	private ISO8601DateTime dateTime;
	
	public ISO8601Time(ISO8601DateTime iso8601DateTime) {
		this.dateTime	= iso8601DateTime;
	}
	
	/**
	 * Creates a <code>ISO8601Time</code> object for the given instant of time in the given <code>ISO8601TimeZone</code>.
	 * 
	 * @param timeZoneIn String time zone
	 * @param hourIn the hour input Hour
	 * @param minuteIn the minute input Minute
	 * @param secondIn the second input Second
	 * @param millisecondIn the millisecond
	 */
	public ISO8601Time(String timeZoneIn, int hourIn, int minuteIn, int secondIn, int millisecondIn) {
		this.dateTime	= new ISO8601DateTime(timeZoneIn, ARBITRARY_YEAR, ARBITRARY_MONTH, ARBITRARY_DAY, hourIn, minuteIn, secondIn, millisecondIn);
	}
	
	public ISO8601Time(int hourIn, int minuteIn, int secondIn, int millisecondIn) {
		this((String)null, hourIn, minuteIn, secondIn, millisecondIn);
	}
	
	public ISO8601Time(ZoneOffset timeZoneIn, int hourIn, int minuteIn, int secondIn, int millisecondIn) {
      this.dateTime   = new ISO8601DateTime(timeZoneIn, ARBITRARY_YEAR, ARBITRARY_MONTH, ARBITRARY_DAY, hourIn, minuteIn, secondIn, millisecondIn);
	}
	
	public boolean getHasTimeZone() {
		return this.dateTime.getHasTimeZone();
	}
	public String getTimeZone() {
		return this.dateTime.getTimeZone();
	}
	public int getHour() {
		return this.dateTime.getHour();
	}
	public int getMinute() {
		return this.dateTime.getMinute();
	}
	public int getSecond() {
		return this.dateTime.getSecond();
	}
	public int getMillisecond() {
		return this.dateTime.getMillisecond();
	}
	
	public LocalTime toLocalTime() {
	  return this.dateTime.getLocalDateTime().toLocalTime();
	}
	
	public ZoneOffset getZoneOffset() {
	  return this.dateTime.getZoneOffset();
	}
	
	public ISO8601Time add(ISO8601Duration iso8601Duration) {
		return new ISO8601Time(this.dateTime.add(iso8601Duration));
	}
	
	public ISO8601Time sub(ISO8601Duration iso8601Duration) {
		return new ISO8601Time(this.dateTime.sub(iso8601Duration));
	}
	
	/**
	 * Gets the <code>String</code> ISO8601 time representation of this <code>ISO8601Time</code>.
	 * 
	 * @param includeTimeZone <code>boolean</code> indicating whether the time zone information should be included
	 * @return the <code>String</code> ISO8601 time representation of this <code>ISO8601Time</code>.
	 */
	public String stringValue(boolean includeTimeZone) {
		StringBuilder stringBuilder	= new StringBuilder();
		stringBuilder.append(String.format("%02d", this.getHour()));
		stringBuilder.append(':');
		stringBuilder.append(String.format("%02d", this.getMinute()));
		stringBuilder.append(':');
		stringBuilder.append(String.format("%02d", this.getSecond()));
		int ms	= this.getMillisecond();
		if (ms > 0) {
			stringBuilder.append('.');
			stringBuilder.append(String.format("%03d", ms));
		}
		if (this.getHasTimeZone() && includeTimeZone) {
			stringBuilder.append(this.getTimeZone());
		}
		return stringBuilder.toString();		
	}
	
	@Override
	public String stringValue() {
		return this.stringValue(true);
	}
	
	@Override
	public String toString() {
		return this.stringValue(true);
	}
		
	@Override
	public int compareTo(ISO8601Time o) {
		return this.dateTime.compareTo(o.dateTime);
	}
	
	public static ISO8601Time fromOffsetTime(OffsetTime offset) {
      return new ISO8601Time(offset.getOffset(), offset.getHour(), offset.getMinute(), offset.getSecond(), offset.get(ChronoField.MILLI_OF_SECOND));
	}
	
	public static ISO8601Time fromLocalTime(LocalTime local) {
	  return new ISO8601Time(local.getHour(), local.getMinute(), local.getSecond(), local.get(ChronoField.MILLI_OF_SECOND));
	}
	/**
	 * Creates a new <code>Time</code> object by parsing the <code>String</code> supplied which must conform to the
	 * ISO8601 standard for time formats @see <a href="http://www.w3.org/TR/NOTE-datetime"></a>
	 * 
	 * @param timeString the timeString to parse
	 * @return a new <code>Time</code> representing the given <code>String</code>
	 * @throws ParseException if the string cannot be interpreted as an ISO8601 time string
	 */
	public static ISO8601Time fromISO8601TimeString(@NonNull String timeString) throws ParseException {
	  //
	  // See if there is a time zone
	  //
	  try {
	    OffsetTime offset = OffsetTime.parse(timeString);
	    //
	    // If we get to here, then there was a time zone
	    //
	    return new ISO8601Time(offset.getOffset(), offset.getHour(), offset.getMinute(), offset.getSecond(), offset.get(ChronoField.MILLI_OF_SECOND));
	  } catch (DateTimeException e) {
	    // Ignore the exception
	  }
	  //
	  // Check for no time zone
	  //
	  try {
	    LocalTime local = LocalTime.parse(timeString);
	    //
	    // If we get to here, then the parse worked
	    //
        return new ISO8601Time(local.getHour(), local.getMinute(), local.getSecond(), local.get(ChronoField.MILLI_OF_SECOND));
	  } catch (DateTimeException e) {
	    throw new ParseException(e.getLocalizedMessage(), 0);
	  }
	}	
}
