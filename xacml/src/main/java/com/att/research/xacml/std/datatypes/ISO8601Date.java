/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.att.research.xacml.api.SemanticString;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * ISO8601Date is a representation of an ISO8601 format date without a time component. NOTE: This is
 * actually the extended specification for XACML3.0, not a strict ISO8601 date
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
@EqualsAndHashCode
public class ISO8601Date
    implements IDateTime<ISO8601Date>, Comparable<ISO8601Date>, SemanticString {
  private ISO8601DateTime dateTime;

  public ISO8601Date(ISO8601DateTime iso8601DateTime) {
    this.dateTime = iso8601DateTime;
  }

  public ISO8601Date(String timeZone, int yearIn, int monthIn, int dayIn) {
    this.dateTime = new ISO8601DateTime(timeZone, yearIn, monthIn, dayIn, 0, 0, 0, 0);
  }

  public ISO8601Date(ZoneOffset timeZone, int yearIn, int monthIn, int dayIn) {
    this.dateTime = new ISO8601DateTime(timeZone, yearIn, monthIn, dayIn, 0, 0, 0, 0);
  }

  public ISO8601Date(int yearIn, int monthIn, int dayIn) {
    this((String) null, yearIn, monthIn, dayIn);
  }
  
  public LocalDate getLocalDate() {
    return this.dateTime.getLocalDateTime().toLocalDate();
  }
  
  public boolean getHasTimeZone() {
    return this.dateTime.getHasTimeZone();
  }

  public String getTimeZone() {
    return this.dateTime.getTimeZone();
  }

  public int getYear() {
    return this.dateTime.getYear();
  }

  public int getMonth() {
    return this.dateTime.getMonth();
  }

  public int getDay() {
    return this.dateTime.getDay();
  }

  public ISO8601Date add(ISO8601Duration iso8601Duration) {
    return new ISO8601Date(this.dateTime.add(iso8601Duration));
  }

  public ISO8601Date sub(ISO8601Duration iso8601Duration) {
    return new ISO8601Date(this.dateTime.sub(iso8601Duration));
  }

  /**
   * Gets the <code>String</code> representation of the ISO8601 date represented by this
   * <code>ISO8601Date</code> object.
   * 
   * @param includeTimeZone <code>boolean</code> indicating whether the time zone information should
   *        be included
   * @return the <code>String</code> representation of the ISO8601 date represented by this
   *         <code>ISO8601Date</code> object.
   */
  public String stringValue(boolean includeTimeZone) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(String.format("%d-%02d-%02d", this.dateTime.getYear(),
        this.dateTime.getMonth(), this.dateTime.getDay()));
    if (includeTimeZone && this.dateTime.getHasTimeZone()) {
      stringBuilder.append(this.dateTime.getTimeZone());
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
  public int compareTo(ISO8601Date o) {
    return this.dateTime.compareTo(o.dateTime);
  }

  public static ISO8601Date fromLocalDate(LocalDate local) {
    return new ISO8601Date(local.getYear(), local.getMonthValue(), local.getDayOfMonth());
  }

  public static ISO8601Date fromOffsetDateTime(OffsetDateTime offset) {
    return new ISO8601Date(offset.getOffset(), offset.getYear(), offset.getMonthValue(),
        offset.getDayOfMonth());
  }

  public static ISO8601Date fromISO8601DateString(@NonNull String strDate) throws ParseException {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE;
      TemporalAccessor accessor = formatter.parse(strDate);
      LocalDate local = LocalDate.from(accessor);
      ZoneOffset offset = ZoneOffset.from(accessor);

      return new ISO8601Date(offset, local.getYear(), local.getMonthValue(),
          local.getDayOfMonth());
    } catch (DateTimeException e) {
    }
    try {
      LocalDate local = LocalDate.parse(strDate);
      return new ISO8601Date(local.getYear(), local.getMonthValue(), local.getDayOfMonth());
    } catch (DateTimeException e) {
      throw new ParseException(e.getLocalizedMessage(), 0);
    }
  }

}
