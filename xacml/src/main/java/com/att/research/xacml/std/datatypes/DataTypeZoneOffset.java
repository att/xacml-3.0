/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std.datatypes;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.XACML3;

public class DataTypeZoneOffset extends DataTypeSemanticStringBase<ISOZoneOffset> {
  private static final DataTypeZoneOffset singleInstance = new DataTypeZoneOffset();

  public DataTypeZoneOffset() {
    super(XACML3.ID_DATATYPE_ZONE_OFFSET, ISOZoneOffset.class);
  }
  
  public static DataTypeZoneOffset newInstance() {
    return singleInstance;
  }

  @Override
  public ISOZoneOffset convert(Object source) throws DataTypeException {
    if (source == null || (source instanceof ISOZoneOffset)) {
      return (ISOZoneOffset) source;
    } else if (source instanceof ZoneOffset) {
      return ISOZoneOffset.fromZoneOffset((ZoneOffset) source);
    } else if (source instanceof OffsetTime) {
      return ISOZoneOffset.fromZoneOffset(((OffsetTime) source).getOffset());
    } else if (source instanceof OffsetDateTime) {
      return ISOZoneOffset.fromZoneOffset(((OffsetDateTime) source).getOffset());
    } else if (source instanceof ZonedDateTime) {
      return ISOZoneOffset.fromZoneOffset(((ZonedDateTime) source).getOffset());
    }
    //
    // Treat as a string representation
    //
    String stringValue = source.toString();
    try {
      //
      // Try to convert it
      //
      return ISOZoneOffset.newInstance(stringValue);
    } catch (ParseException e) {
      throw new DataTypeException(this, "Failed to convert \"" + source.getClass().getCanonicalName() + "\" with value \"" + stringValue + "\" to Zone Offset", e);
    }
  }

}
