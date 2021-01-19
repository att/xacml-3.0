/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import com.att.research.xacml.api.SemanticString;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * ISO8601Datetime is a combination of an {@link com.att.research.xacml.std.datatypes.ISO8601Date} and a {@link com.att.research.xacml.std.datatypes.ISO8601Time}
 * with a common ZoneOffset.
 * 
 * Note: This is a temporary implementation.  It appears Java 8 will have better classes for dealing with ISO8601 dates and times.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
@EqualsAndHashCode
public class ISO8601DateTime implements IDateTime<ISO8601DateTime>, Comparable<ISO8601DateTime>, SemanticString {
	private LocalDateTime dateTime;
	private ZoneOffset zone;
	
	/**
	 * Creates a new <code>ISO8601DateTime</code> using the supplied field values.
	 * 
	 * @param timeZone the <code>String</code> name of the time zone.  Use null for non-time-zoned instances
	 * @param yearIn the integer year (-9999 to 9999)
	 * @param monthIn the integer month (1-12)
	 * @param dayIn the integer day of the month (1-31)
	 * @param hourIn the integer hour of the day (0-23)
	 * @param minuteIn the integer minute of the hour (0-59)
	 * @param secondIn the integer second of the minute (0-59)
	 * @param millisecondIn the integer milliseconds (0-999)
	 */
	public ISO8601DateTime(ZoneOffset timeZone, int yearIn, int monthIn, int dayIn, int hourIn, int minuteIn, int secondIn, int millisecondIn) {
	  this.dateTime = LocalDateTime.of(yearIn, monthIn, dayIn, hourIn, minuteIn, secondIn, millisecondIn * 1000000);
	  this.zone = timeZone;
	}
	
	public ISO8601DateTime(String timeZone, int yearIn, int monthIn, int dayIn, int hourIn, int minuteIn, int secondIn, int millisecondIn) {
		this((timeZone == null ? null : ZoneOffset.of(timeZone)), yearIn, monthIn, dayIn, hourIn, minuteIn, secondIn, millisecondIn);
	}
	
	public ISO8601DateTime(ZoneOffset timeZone, ISO8601Date iso8601Date, ISO8601Time iso8601Time) {
		this(timeZone, iso8601Date.getYear(), iso8601Date.getMonth(), iso8601Date.getDay(), iso8601Time.getHour(), iso8601Time.getMinute(), iso8601Time.getSecond(), iso8601Time.getMillisecond());
	}
	
	public ISO8601DateTime(int yearIn, int monthIn, int dayIn, int hourIn, int minuteIn, int secondIn, int millisecondIn) {
		this((ZoneOffset)null, yearIn, monthIn, dayIn, hourIn, minuteIn, secondIn, millisecondIn);
	}
	
	private ISO8601DateTime(LocalDateTime local, ZoneOffset zone) {
	  this.dateTime = local;
	  this.zone = zone;
	}
	
	/**
	 * Gets the value indicating whether this <code>ISO8601DateTime</code> is time-zoned or non-time-zoned.
	 * 
	 * @return true if this <code>ISO8601DateTime</code> is time-zoned, else false
	 */
	public boolean getHasTimeZone() {
		return this.zone != null;
	}
	
	public String getTimeZone() {
		if (this.getHasTimeZone()) {
		  return this.zone.toString();
		} else {
			return null;
		}
	}
	
	public ZoneOffset getZoneOffset() {
	  return this.zone;
	}
	
	public LocalDateTime getLocalDateTime() {
	  return this.dateTime;
	}
	
	public int getYear() {
	  return this.dateTime.getYear();
	}
	public int getMonth() {
		return this.dateTime.getMonthValue();
	}
	public int getDay() {
		return this.dateTime.getDayOfMonth();
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
		return this.dateTime.getNano() / 1000000;
	}
		
	/**
	 * Gets a <code>ISO8601DateTime</code> equivalent to this <code>ISO8601DateTime</code> converted to
	 * the GMT time zone.  If this <code>ISO8601DateTime</code> is non-time-zoned, this method will throw
	 * an illegal state exception.
	 * 
	 * @return a <code>ISO8601DateTime</code> equivalent to this <code>ISO8601DateTime</code> in the GMT time zone.
	 * @throws IllegalStateException if this <code>ISO8601DateTime</code> is non-time-zoned.
	 */
	public ISO8601DateTime getGMTDateTime() {
		if (!this.getHasTimeZone()) {
			throw new IllegalStateException("Cannot convert non-time-zoned ISO8601DateTime to GMT");
		}
		OffsetDateTime offset = OffsetDateTime.of(this.dateTime, this.zone);
		OffsetDateTime offsetGMT = offset.withOffsetSameInstant(ZoneOffset.UTC);
		return new ISO8601DateTime(offsetGMT.toLocalDateTime(), offsetGMT.getOffset());
	}
	
	private ISO8601DateTime add(ISO8601Duration iso8601Duration, int sign) {
	  
	  if (this.zone != null) {
	    return addOffset(iso8601Duration, sign);
	  }
	  return addLocalTime(iso8601Duration, sign);
	}
	
	private ISO8601DateTime addOffset(ISO8601Duration iso8601Duration, int sign) {
      OffsetDateTime addedOffset = OffsetDateTime.of(this.dateTime, this.zone);
      
      long value;
      if ((value = iso8601Duration.getYears()) != 0) {
        addedOffset = addedOffset.plusYears(sign * value);
      }
      if ((value = iso8601Duration.getMonths()) != 0) {
        addedOffset = addedOffset.plusMonths(sign * value);
      }
      if ((value = iso8601Duration.getDays()) != 0) {
        addedOffset = addedOffset.plusDays(sign * value);
      }
      if ((value = iso8601Duration.getHours()) != 0) {
        addedOffset = addedOffset.plusHours(sign * value);
      }
      if ((value = iso8601Duration.getMinutes()) != 0) {
        addedOffset = addedOffset.plusMinutes(sign * value);
      }
      long seconds    = iso8601Duration.getSeconds();
      if (seconds >= 1) {
        addedOffset = addedOffset.plusSeconds(sign * seconds);
      }
      long millis = iso8601Duration.getMillis();
      if (millis != 0) {
        addedOffset = addedOffset.plusNanos(sign * millis * 1000000);
      }

      return new ISO8601DateTime(addedOffset.toLocalDateTime(), addedOffset.getOffset());
	}
	
	private ISO8601DateTime addLocalTime(ISO8601Duration iso8601Duration, int sign) {
      LocalDateTime addedLocal = this.dateTime;
      long value;
      if ((value = iso8601Duration.getYears()) != 0) {
        addedLocal = addedLocal.plusYears(sign * value);
      }
      if ((value = iso8601Duration.getMonths()) != 0) {
        addedLocal = addedLocal.plusMonths(sign * value);
      }
      if ((value = iso8601Duration.getDays()) != 0) {
        addedLocal = addedLocal.plusDays(sign * value);
      }
      if ((value = iso8601Duration.getHours()) != 0) {
        addedLocal = addedLocal.plusHours(sign * value);
      }
      if ((value = iso8601Duration.getMinutes()) != 0) {
        addedLocal = addedLocal.plusMinutes(sign * value);
      }
      long seconds    = iso8601Duration.getSeconds();
      if (seconds >= 1) {
        addedLocal = addedLocal.plusSeconds(sign * seconds);
      }
      long millis = iso8601Duration.getMillis();
      if (millis != 0) {
        addedLocal = addedLocal.plusNanos(sign * millis * 1000000);
      }
      return new ISO8601DateTime(addedLocal, null);
	}
	
	/**
	 * Adds the given <code>ISO8601Duration</code> to this <code>ISO8601DateTime</code> and returns
	 * a new <code>ISO8601DateTime</code> with the result.
	 * 
	 * @param iso8601Duration the <code>ISO8601Duration</code> to add
	 * @return a new <code>ISO8601DateTime</code> with the result of the addition
	 */
	public ISO8601DateTime add(ISO8601Duration iso8601Duration) {
		return this.add(iso8601Duration, iso8601Duration.getDurationSign());
	}
	
	public ISO8601DateTime sub(ISO8601Duration iso8601Duration) {
		return this.add(iso8601Duration, -iso8601Duration.getDurationSign());
	}
		
	public static ISO8601DateTime fromOffsetDateTime(OffsetDateTime offset) {
	  return new ISO8601DateTime(offset.toLocalDateTime(), offset.getOffset());
	}
	
	public static ISO8601DateTime fromLocalDateTime(LocalDateTime local) {
	  return new ISO8601DateTime(local, null);
	}
	
	/**
	 * Creates a new <code>ISO8601DateTime</code> by parsing the given <code>String</code> in the extended ISO8601 
	 * format defined for XML.
	 * 
	 * @param strDateTime the <code>String</code> in ISO8601 date-time format.
	 * @return a new <code>ISO8601DateTime</code>.
	 * @throws ParseException if there is an error parsing the <code>String</code>
	 */
	public static ISO8601DateTime fromISO8601DateTimeString(@NonNull String strDateTime) throws ParseException {
	  try {
	    OffsetDateTime date = OffsetDateTime.parse(strDateTime);
	    return new ISO8601DateTime(date.toLocalDateTime(), date.getOffset());
	  } catch (DateTimeException e) {
	  }
	  try {
	    LocalDateTime date = LocalDateTime.parse(strDateTime);
	    return new ISO8601DateTime(date, null);
	  } catch (DateTimeException e) {
	    throw new ParseException(e.getLocalizedMessage(), 0);
	  }
	}
	
	@Override
	public String stringValue() {
	  if (this.zone != null) {
	    OffsetDateTime offset = OffsetDateTime.of(this.dateTime, this.zone);
	    return offset.toString();
	  }
	  return this.dateTime.toString();
	}
	
	@Override
	public String toString() {
		return this.stringValue();
	}

	@Override
	public int compareTo(ISO8601DateTime o) {
	  if (this.zone != null) {
        OffsetDateTime offset = OffsetDateTime.of(this.dateTime, this.zone);
        OffsetDateTime objectOffset = OffsetDateTime.of(o.dateTime, o.zone);
        return offset.compareTo(objectOffset);
	  }
	  return this.dateTime.compareTo(o.dateTime);
	}
	
}
