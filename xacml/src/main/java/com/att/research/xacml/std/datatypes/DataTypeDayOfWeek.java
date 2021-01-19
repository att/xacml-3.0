/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML3;

public class DataTypeDayOfWeek  extends DataTypeSemanticStringBase<ISODayOfWeek> {
  private static final DataTypeDayOfWeek singleInstance = new DataTypeDayOfWeek();

  public DataTypeDayOfWeek() {
    super(XACML3.ID_DATATYPE_DAYOFWEEK, ISODayOfWeek.class);
  }

  public static DataTypeDayOfWeek newInstance() {
    return singleInstance;
  }

  @Override
  public ISODayOfWeek convert(Object source) throws DataTypeException {
    if (source == null || (source instanceof ISODayOfWeek)) {
      return (ISODayOfWeek) source;
    }
    if (source instanceof DayOfWeek) {
      return new ISODayOfWeek((DayOfWeek) source, null);
    }
    if (source instanceof LocalDate) {
      LocalDate date = (LocalDate) source;
      return new ISODayOfWeek(date.getDayOfWeek(), null);
    }
    if (source instanceof LocalDateTime) {
      LocalDateTime date = (LocalDateTime) source;
      return new ISODayOfWeek(date.getDayOfWeek(), null);
    }
    if (source instanceof OffsetDateTime) {
      OffsetDateTime date = (OffsetDateTime) source;
      return new ISODayOfWeek(date.getDayOfWeek(), date.getOffset());
    }
    if (source instanceof ZonedDateTime) {
      ZonedDateTime date = (ZonedDateTime) source;
      return new ISODayOfWeek(date.getDayOfWeek(), date.getOffset());
    }
    //
    // Treat as a string representation
    //
    String stringValue = this.convertToString(source);//source.toString();
    try {
      //
      // Try to convert it
      //
      return ISODayOfWeek.parse(stringValue);
    } catch (ParseException e) {
      throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to Day Of Week", e);
    }
  }

}
