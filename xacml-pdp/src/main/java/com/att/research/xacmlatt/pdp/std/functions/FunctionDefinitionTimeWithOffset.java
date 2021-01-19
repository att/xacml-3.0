/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std.functions;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.ISO8601DateTime;
import com.att.research.xacml.std.datatypes.ISO8601Time;
import com.att.research.xacml.std.datatypes.ISOZoneOffset;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;

public class FunctionDefinitionTimeWithOffset implements FunctionDefinition {
  private static final Logger logger    = LoggerFactory.getLogger(FunctionDefinitionTimeWithOffset.class);
  private static final String SHORT_FUNCTION = "time-with-offset-same-instant";

  @Override
  public Identifier getId() {
    return XACML3.ID_FUNCTION_TIME_WITH_OFFSET;
  }

  @Override
  public Identifier getDataTypeId() {
    return XACML3.ID_DATATYPE_TIME;
  }

  @Override
  public boolean returnsBag() {
    return false;
  }

  @Override
  public ExpressionResult evaluate(EvaluationContext evaluationContext,
      List<FunctionArgument> arguments) {
    Status status = this.validateArguments(arguments);
    //
    // If the function arguments are not correct, just return an error status immediately
    //
    if (! status.getStatusCode().equals(StdStatusCode.STATUS_CODE_OK)) {
        return ExpressionResult.newError(status);
    }
    //
    // Get the first argument - we can accept multiple data types
    //
    Pair<Status, LocalTime> pairResultArg0 = extractFirstArgument(arguments.get(0));
    if (! pairResultArg0.getLeft().getStatusCode().equals(StdStatusCode.STATUS_CODE_OK)) {
      return ExpressionResult.newError(pairResultArg0.getLeft());
    }
    //
    // Get the second argument - which should be Zone Offset object
    //
    Pair<Status, ZoneOffset> pairResultArg1 = extractSecondArgument(arguments.get(1));
    if (! pairResultArg1.getLeft().getStatusCode().equals(StdStatusCode.STATUS_CODE_OK)) {
      return ExpressionResult.newError(pairResultArg1.getLeft());
    }
    //
    // Do the function
    //
    LocalTime arg0Time = pairResultArg0.getRight();
    ZoneOffset arg1Offset = pairResultArg1.getRight();
    OffsetTime offsetResult = OffsetTime.of(arg0Time, arg1Offset);
    logger.debug("{} time {} zone {} with instant {}", SHORT_FUNCTION, arg0Time, arg1Offset, offsetResult);
    
    ISO8601Time time = ISO8601Time.fromOffsetTime(offsetResult);
    AttributeValue<ISO8601Time> attributeValue = new StdAttributeValue<>(XACML3.ID_DATATYPE_TIME, time);

    return ExpressionResult.newSingle(attributeValue);
  }
  
  private Status validateArguments(List<FunctionArgument> listFunctionArguments) {
    if (listFunctionArguments == null || listFunctionArguments.size() != 2) {
      return new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, SHORT_FUNCTION + "Expecting 2 arguments");
    }
    
    return StdStatus.STATUS_OK;
  }
  
  private Pair<Status, LocalTime> extractFirstArgument(FunctionArgument argument) {
    LocalTime local = null;

    if (argument.isBag()) {
      return Pair.of(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, SHORT_FUNCTION + "Not expecting first arg to be a bag."), null);
    }
    
    AttributeValue<?> value = argument.getValue();
    if (DataTypes.DT_TIME.getId().equals(value.getDataTypeId())) {
      ISO8601Time time = (ISO8601Time) value.getValue();
      local = time.toLocalTime();
    } else if (DataTypes.DT_DATETIME.getId().equals(value.getDataTypeId())) {
      ISO8601DateTime date = (ISO8601DateTime) value.getValue();
      local = date.getLocalDateTime().toLocalTime();
    } else {
      return Pair.of(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, SHORT_FUNCTION + "First arg should be time or dateTime."), null);
    }
    
    return Pair.of(StdStatus.STATUS_OK, local);
  }
  
  private Pair<Status, ZoneOffset> extractSecondArgument(FunctionArgument argument) {
    ZoneOffset zone = null;
    
    if (argument.isBag()) {
      return Pair.of(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, SHORT_FUNCTION + "Not expecting second arg to be a bag."), null);
    }
    
    AttributeValue<?> value = argument.getValue();
    if (DataTypes.DT_ZONEOFFSET.getId().equals(value.getDataTypeId())) {
      ISOZoneOffset zoneValue = (ISOZoneOffset) value.getValue();
      zone = zoneValue.getZoneOffset();
    } else {
      return Pair.of(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, SHORT_FUNCTION + "Second arg should be zone offset."), null);
    }
    
    return Pair.of(StdStatus.STATUS_OK, zone);
  }
}
