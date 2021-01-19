/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std.functions;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypeBoolean;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.ISO8601DateTime;
import com.att.research.xacml.std.datatypes.ISODayOfWeek;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;

public class FunctionDefinitionDateTimeInDayOfWeekRange implements FunctionDefinition  {
  private static final Logger logger    = LoggerFactory.getLogger(FunctionDefinitionDateTimeInDayOfWeekRange.class);
  private static final String SHORT_FUNCTION = "dateTime-in-dayOfWeek-range";

  @Override
  public Identifier getId() {
    return XACML3.ID_FUNCTION_DATETIME_IN_DAYOFWEEK_RANGE;
  }

  @Override
  public Identifier getDataTypeId() {
    return XACML3.ID_DATATYPE_BOOLEAN;
  }

  @Override
  public boolean returnsBag() {
    return false;
  }

  @Override
  public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
    //
    // Validate and retrieve all the arguments
    //
    Triple<ISO8601DateTime, ISODayOfWeek, ISODayOfWeek> args = null;
    try {
      args = this.validateArguments(arguments);
    } catch (IllegalArgumentException e) {
      logger.error("Illegal arguments", e);
      return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, SHORT_FUNCTION + e.getLocalizedMessage()));
    }
    //
    // Do the calculation
    //
    return this.calculateInRange(args.getLeft(), args.getMiddle(), args.getRight());
  }
  
  private Triple<ISO8601DateTime, ISODayOfWeek, ISODayOfWeek> validateArguments(List<FunctionArgument> arguments) {
    //
    // Sanity check of the arguments
    //
    if (arguments == null || arguments.size() != 3) {
      throw new IllegalArgumentException("Expecting 3 arguments");
    }
    //
    // Get the first argument
    //
    FunctionArgument arg0 = arguments.get(0);
    //
    // Should not be a bag
    //
    if (arg0.isBag()) {
      throw new IllegalArgumentException("1st argument should not be a bag.");
    }
    //
    // Definitely should be a date time
    //
    if (arg0.getValue() == null) {
      throw new NullPointerException();
    }
    if (! DataTypes.DT_DATETIME.getId().equals(arg0.getValue().getDataTypeId())) {
      throw new IllegalArgumentException("1st argument should be dateTime.");
    }
    ISO8601DateTime dateTime = (ISO8601DateTime) arg0.getValue().getValue();
    logger.debug("original dateTime {}", dateTime);
    //
    // Determine the default time zone, if it hasn't been specified.
    //
    ZoneId zoneId = ZoneId.systemDefault();
    ZoneOffset defaultZone = zoneId.getRules().getOffset(Instant.now());
    logger.debug("Default zoneId is {}", defaultZone);
    if (dateTime.getHasTimeZone()) {
      defaultZone = dateTime.getZoneOffset();
      logger.debug("using dateTime default zone {}", defaultZone);
    } else {
      //
      // Ensure 1st argument is using this zone
      //
      OffsetDateTime offset = OffsetDateTime.of(dateTime.getLocalDateTime(), defaultZone);
      dateTime = ISO8601DateTime.fromOffsetDateTime(offset);
    }
    logger.debug("final dateTime {}", dateTime);
    //
    // Get the other 2 arguments
    //
    ISODayOfWeek dowStart = validateDowArgument("2nd", arguments.get(1), defaultZone);
    ISODayOfWeek dowEnd = validateDowArgument("3rd", arguments.get(2), defaultZone);
    logger.debug("range {} and {}", dowStart, dowEnd);
    //
    // So far good arguments
    //
    return Triple.of(dateTime, dowStart, dowEnd);
  }
  
  private ISODayOfWeek validateDowArgument(String strIndex, FunctionArgument arg, ZoneOffset defaultOffset) {
    //
    // Get the first argument, which should not be a bag.
    //
    if (arg.isBag()) {
      throw new IllegalArgumentException(strIndex + " argument should not be a bag.");
    }
    //
    // Should be DOW
    //
    if (arg.getValue() == null) {
      throw new NullPointerException();
    }
    if (! DataTypes.DT_DAYOFWEEK.getId().equals(arg.getValue().getDataTypeId())) {
      throw new IllegalArgumentException(strIndex + " argument should be dayOfWeek.");
    }
    //
    // Get the value
    //
    ISODayOfWeek dow = (ISODayOfWeek) arg.getValue().getValue();
    //
    // If we are missing a time zone, use the default
    //
    if (dow.getZone() == null) {
      logger.debug("dow {} arg had no zone {}, using {}", strIndex, dow, defaultOffset);
      return new ISODayOfWeek(dow.getDay(), defaultOffset);
    }
    return dow;
  }

  private ExpressionResult calculateInRange(ISO8601DateTime dateTime, ISODayOfWeek dowStart, ISODayOfWeek dowEnd) {
    //
    // Section 7.6.3 implementation - step 1 already done in validate arguments
    //
    //
    // Step 2 - normalize the 1st argument
    //
    OffsetDateTime value = OffsetDateTime.of(dateTime.getLocalDateTime(), dateTime.getZoneOffset());
    value = value.withOffsetSameInstant(ZoneOffset.UTC);
    logger.debug("normalized {} to {} which is a {}", dateTime, value, value.getDayOfWeek());
    //
    // Step 3 - Move to previous Sunday (even if its a Sunday)
    //
    OffsetDateTime referenceDate = value;
    do {
      referenceDate = referenceDate.minusDays(1);
    } while (! DayOfWeek.SUNDAY.equals(referenceDate.getDayOfWeek()));
    logger.debug("previous Sunday is {}", referenceDate);
    OffsetDateTime referencePoint = OffsetDateTime.of(referenceDate.getYear(), referenceDate.getMonthValue(), referenceDate.getDayOfMonth(), 0, 0, 0, 0, referenceDate.getOffset());
    logger.debug("reference point {}", referencePoint);
    //
    // Step 4 - Create series of inclusive start points
    //
    OffsetDateTime dateStart = OffsetDateTime.of(referencePoint.getYear(), referencePoint.getMonthValue(), referencePoint.getDayOfMonth(), 0, 0, 0, 0, dowStart.getZone());
    dateStart = dateStart.plusDays(dowStart.getDay());
    dateStart = dateStart.withOffsetSameInstant(ZoneOffset.UTC);
    logger.debug("dateStart normalized to {}", dateStart);
    //
    // Check if difference less than a day
    //
    if (ChronoUnit.DAYS.between(referencePoint, dateStart) < 1) {
      dateStart = dateStart.plusDays(7);
      logger.debug("dateStart added 7 days to {}", dateStart);
    }
    //
    // Step 5 - Create series of exclusive end points (eg add one)
    //
    OffsetDateTime dateEnd = OffsetDateTime.of(referencePoint.getYear(), referencePoint.getMonthValue(), referencePoint.getDayOfMonth(), 0, 0, 0, 0, dowEnd.getZone());
    dateEnd = dateEnd.plusDays(dowEnd.getDay() + 1L);
    dateEnd = dateEnd.withOffsetSameInstant(ZoneOffset.UTC);
    logger.debug("dateEnd normalized to {}", dateEnd);
    //
    // Check if difference great than 8 days
    //
    if (ChronoUnit.DAYS.between(referencePoint, dateEnd) > 8) {
      dateEnd = dateStart.minusDays(7);
      logger.debug("dateEnd subtracted 7 days to {}", dateEnd);
    }
    logger.debug("comparing {} in range of {} and {}", value, dateStart, dateEnd);
    //
    // Step 6
    //
    if (dateEnd.compareTo(dateStart) > 0 && 
        (value.compareTo(dateStart) >= 0 && value.compareTo(dateEnd) < 0)) {
      logger.debug("step 6 is true");
      return ExpressionResult.newSingle(DataTypeBoolean.AV_TRUE);
    }
    //
    // Step 7
    //
    if (dateEnd.compareTo(dateStart) <= 0 && 
        (value.compareTo(dateEnd) < 0 || value.compareTo(dateStart) >= 0)) {
      logger.debug("step 7 is true");
      return ExpressionResult.newSingle(DataTypeBoolean.AV_TRUE);
    }
    //
    // Step 8 - Otherwise it is false
    //
    return ExpressionResult.newSingle(DataTypeBoolean.AV_FALSE);
  }
}
