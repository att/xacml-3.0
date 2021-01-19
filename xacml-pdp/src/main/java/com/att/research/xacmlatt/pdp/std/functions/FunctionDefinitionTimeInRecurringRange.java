/*
 *
 *          Copyright (c) 2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.std.functions;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.ISO8601Time;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

public class FunctionDefinitionTimeInRecurringRange extends FunctionDefinitionHomogeneousSimple<Boolean, ISO8601Time> {
  private static final Logger logger    = LoggerFactory.getLogger(FunctionDefinitionTimeInRecurringRange.class);

  public FunctionDefinitionTimeInRecurringRange(Identifier idIn, DataType<ISO8601Time> dataTypeArgsIn) {
    super(idIn, DataTypes.DT_BOOLEAN, dataTypeArgsIn, 3);
  }

  @Override
  public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
    List<ISO8601Time> convertedArguments  = new ArrayList<>();
    Status status = this.validateArguments(arguments, convertedArguments);
    
    //
    // If the function arguments are not correct, just return an error status immediately
    //
    if (!status.getStatusCode().equals(StdStatusCode.STATUS_CODE_OK)) {
        return ExpressionResult.newError(getFunctionStatus(status));
    }
    //
    // Get argument 1
    //
    ZonedDateTime valueTime = FunctionTimeUtils.normalizeTimeArgument(arguments.get(0), ZoneId.systemDefault());
    logger.debug("normalized argument {}", valueTime);
    //
    // Get argument 2
    //
    ZonedDateTime startTime = FunctionTimeUtils.normalizeTimeArgument(arguments.get(1), valueTime.getZone());
    logger.debug("normalized start {}", startTime);
    //
    // Get argument 3
    //
    ZonedDateTime endTime = FunctionTimeUtils.normalizeTimeArgument(arguments.get(2), valueTime.getZone());
    logger.debug("normalized end {}", endTime);
    //
    // Do the evaluation
    //
    return this.doEvaluation(valueTime, startTime, endTime);
  }
   
  private ExpressionResult doEvaluation(ZonedDateTime value, ZonedDateTime startDate, ZonedDateTime endDate) {
    LocalTime valueLocal = value.toLocalTime();    
    LocalTime startLocal = startDate.toLocalTime();
    LocalTime endLocal = endDate.toLocalTime();
    
    logger.debug("valueLocal {} startLocal {} endLocal {}", valueLocal, startLocal, endLocal);
    
    logger.debug("endLocal compare to startLocal {}", endLocal.compareTo(startLocal));
    logger.debug("valueLocal compare to starLocalt {}", valueLocal.compareTo(startLocal));
    logger.debug("valueLocal compare to endLocal {}", valueLocal.compareTo(endLocal));
    
    //
    // Refer to section 3.2.3 Implementation
    // This is step 4
    //
    if (endLocal.compareTo(startLocal) >= 0 &&
        valueLocal.compareTo(startLocal) >= 0 &&
        valueLocal.compareTo(endLocal) <= 0) {
      logger.debug("returning true from step 4");
      return ER_TRUE;
    }
    //
    // This is step 5
    //
    if (endLocal.compareTo(startLocal) < 0 &&
        (valueLocal.compareTo(endLocal) <=0 ||
        valueLocal.compareTo(startLocal) >=0)
        ) {
      logger.debug("returning true from step 5");
      return ER_TRUE;
    }
    //
    // This is step 6
    //
    return ER_FALSE;
  }
  
}
