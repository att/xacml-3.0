/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std.functions;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.ISO8601Time;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

public class FunctionTimeUtils {
  private static final LocalDate REFERENCE_DATE = LocalDate.of(2017, 1, 15);

  protected FunctionTimeUtils() {
    // This class only holds static methods
  }

  public static ZonedDateTime normalizeTimeArgument(FunctionArgument argument, ZoneId defaultTimeZone) {
    ConvertedArgument<ISO8601Time> arg = new ConvertedArgument<>(argument, DataTypes.DT_TIME, false);
    ISO8601Time value = arg.getValue();
    ZonedDateTime normalized;
    if (! value.getHasTimeZone()) {
      normalized = ZonedDateTime.of(REFERENCE_DATE, value.toLocalTime(), defaultTimeZone);
    } else {
      normalized = ZonedDateTime.of(REFERENCE_DATE, value.toLocalTime(), value.getZoneOffset());
    }
    return normalized.withZoneSameInstant(ZoneId.of("Z"));
  }
  
}
