/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.ZoneOffset;
import com.att.research.xacml.api.SemanticString;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
public class ISODayOfWeek implements SemanticString {
    
  private DayOfWeek day;
  private ZoneOffset zone;
  
  public ISODayOfWeek(DayOfWeek day, ZoneOffset zone) {
    this.day = day;
    this.zone = zone;
  }
  public ISODayOfWeek(int day, ZoneOffset zone) {
    this(DayOfWeek.of(day), zone);
  }
  public ISODayOfWeek(String day, ZoneOffset zone) {
    this(DayOfWeek.valueOf(day), zone);
  }
  
  public int getDay() {
    return this.day.getValue();
  }
  
  public DayOfWeek getDayEnum() {
    return this.day;
  }
  
  public ZoneOffset getZone() {
    return this.zone;
  }
  
  public static ISODayOfWeek parse(@NonNull String str) throws ParseException {
    str = str.strip();
    try {
      if (str.length() < 1) {
        throw new IllegalArgumentException("zero length string");
      }
      //
      // Mandatory first digit is the day of week
      //
      String strDigit = str.substring(0, 1);
      DayOfWeek dayOfWeek = DayOfWeek.of(Integer.parseInt(strDigit));
      ZoneOffset zone = null;
      //
      // Optional rest is time zone
      //
      str = str.substring(1);
      if (str.length() > 0) {
        zone = ZoneOffset.of(str);
      }
      //
      // Ok great - create it
      //
      return new ISODayOfWeek(dayOfWeek, zone);
    } catch (Exception e) {
      throw new ParseException(e.getLocalizedMessage(), 0);
    }
  }

  @Override
  public String stringValue() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.day.getValue());
    if (this.zone != null) {
      builder.append(this.zone.toString());
    }
    return builder.toString();
  }
  
  @Override
  public String toString() {
    return this.stringValue();
  }

}
