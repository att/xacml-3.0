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

public class FunctionDefinitionRecurringTimeEqual extends FunctionDefinitionHomogeneousSimple<Boolean, ISO8601Time> {
  private static final Logger logger    = LoggerFactory.getLogger(FunctionDefinitionRecurringTimeEqual.class);
  
  public FunctionDefinitionRecurringTimeEqual(Identifier idIn, DataType<ISO8601Time> dataTypeArgsIn) {
    super(idIn, DataTypes.DT_BOOLEAN, dataTypeArgsIn, 2);
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
    ZonedDateTime arg1 = FunctionTimeUtils.normalizeTimeArgument(arguments.get(0), ZoneId.systemDefault());
    logger.debug("normalized argument {}", arg1);
    //
    // Get argument 2
    //
    ZonedDateTime arg2 = FunctionTimeUtils.normalizeTimeArgument(arguments.get(1), ZoneId.systemDefault());
    logger.debug("normalized argument {}", arg1);
    //
    // Evaluate just local time piece
    //
    LocalTime localArg1 = arg1.toLocalTime();
    LocalTime localArg2 = arg2.toLocalTime();
    //
    // Equals?
    //
    if (localArg1.equals(localArg2)) {
      return ER_TRUE;
    }
    return ER_FALSE;
  }
}
