/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.IDateTime;
import com.att.research.xacml.std.datatypes.ISO8601Duration;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionDateTimeArithmetic implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML Date and Time Arithmetic predicates.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		dateTime-add-dayTimeDuration
 * 		dateTime-add-yearMonthDuration
 * 		dateTime-subtract-dayTimeDuration
 * 		dateTime-subtract-yearMonthDuration
 * 		date-add-yearMonthDuration
 * 		date-subtract-yearMonthDuration
 * 
 *      time-add-dayTimeDuration
 *      time-add-yearMonthDuration
 * 
 * @param <I> the java class for the data type of the function Input arguments;
 * 		SPECIAL CASE: this applies ONLY to the 2nd argument.
 * @param <O> the java class for the data type of the function Output;
 * 		SPECIAL CASE: this ALSO applies to the type of the 1st Input argument.
 */
public class FunctionDefinitionDateTimeArithmetic<O extends IDateTime<O>, I extends ISO8601Duration> extends FunctionDefinitionBase<O, I> {

  private static final Logger logger    = LoggerFactory.getLogger(FunctionDefinitionDateTimeArithmetic.class);
  
	public enum OPERATION {ADD, SUBTRACT}
	
	private final OPERATION operation;
	
	/**
	 * Constructor - need dataTypeArgs input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn Identifier
	 * @param dataTypeIn  Input DataType
	 * @param dataTypeArgsIn DataType arguments
	 * @param op Operation
	 */
	public FunctionDefinitionDateTimeArithmetic(Identifier idIn, DataType<O> dataTypeIn, DataType<I> dataTypeArgsIn, OPERATION op) {
		super(idIn, dataTypeIn, dataTypeArgsIn, false);
		this.operation = op;
	}

	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
		if (arguments == null ||  arguments.size() != 2) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
					" Expected 2 arguments, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}
		
		// first arg has same type as function output
		FunctionArgument functionArgument = arguments.get(0);
		ConvertedArgument<O> convertedArgument0 = new ConvertedArgument<>(functionArgument, this.getDataType(), false);
		if ( ! convertedArgument0.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedArgument0.getStatus()));
		}
		O idateOrig	= convertedArgument0.getValue();
		
		// second argument is of input type
		functionArgument = arguments.get(1);
		ConvertedArgument<I> convertedArgument1 = new ConvertedArgument<>(functionArgument, this.getDataTypeArgs(), false);
		if ( ! convertedArgument1.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedArgument1.getStatus()));
		}
		// get the Duration object from the argument which includes all fields, even if the incoming argument does not include them all
		ISO8601Duration duration = convertedArgument1.getValue();

        // add/subtract the duration to the input argument
		//
		O idateResult	= null;
		if (OPERATION.ADD.equals(this.operation)) {
			idateResult	= idateOrig.add(duration);
		} else if (OPERATION.SUBTRACT.equals(this.operation)) {
			idateResult	= idateOrig.sub(duration);
		}
        if (logger.isDebugEnabled()) {
          logger.debug("evaluating {} value is {} duration is {} result is {}", this.getShortFunctionId(), idateOrig, duration.stringValue(), idateResult);
        }
		try {
			return ExpressionResult.newSingle(this.getDataType().createAttributeValue(idateResult)); 
		} catch (DataTypeException e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message));			
		}
	}

}
