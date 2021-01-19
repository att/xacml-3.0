/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.ArrayList;
import java.util.List;

import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.ISO8601Time;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionTimeInRange implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML time-in-range predicates as a function taking three arguments of type <code>Time</code>
 * and returning a <code>Boolean</code>.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		time-in-range
 * 
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <I> the java class for the data type of the function Input arguments.  
 */
public class FunctionDefinitionTimeInRange<I> extends FunctionDefinitionHomogeneousSimple<Boolean, I> {


	
	
	/**
	 * Constructor - need dataType input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn Identifier
	 * @param dataTypeArgsIn DataType arguments
	 */
	public FunctionDefinitionTimeInRange(Identifier idIn, DataType<I> dataTypeArgsIn) {
		super(idIn, DataTypes.DT_BOOLEAN, dataTypeArgsIn, 3);
	}


	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {

		List<I> convertedArguments	= new ArrayList<>();
		Status status				= this.validateArguments(arguments, convertedArguments);
		
		/*
		 * If the function arguments are not correct, just return an error status immediately
		 */
		if (!status.getStatusCode().equals(StdStatusCode.STATUS_CODE_OK)) {
			return ExpressionResult.newError(getFunctionStatus(status));
		}
		
		int compareResultLow;
		int compareResultHigh;
		try {

			compareResultLow = ((ISO8601Time) convertedArguments.get(1)).compareTo((ISO8601Time)convertedArguments.get(0));
			compareResultHigh = ((ISO8601Time)convertedArguments.get(2)).compareTo((ISO8601Time)convertedArguments.get(0));
		} catch (Exception e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message));
		}

		// is arg 0 within the inclusive range of the other two?
		if (compareResultLow <=0 && compareResultHigh >= 0) {
			return ER_TRUE;
		} else {
			return ER_FALSE;
		}
	}



}
