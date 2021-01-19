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
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionComparison implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML comparison predicates as functions taking two arguments of the same type
 * and returning a <code>Boolean</code>.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		integer-greater-than
 * 		integer-greater-than-or-equal
 * 		integer-less-than
 * 		integer-less-than-or-equal	
 * 		double-greater-than
 * 		double-greater-than-or-equal
 * 		double-less-than
 * 		double-less-than-or-equal 
 * 
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <I> the java class for the data type of the function Input arguments
 */
public class FunctionDefinitionComparison<I extends Comparable<I>> extends FunctionDefinitionHomogeneousSimple<Boolean, I> {

	/**
	 * List of comparison operations.
	 * 
	 * @author glenngriffin
	 *
	 */
	public enum OPERATION {GREATER_THAN, GREATER_THAN_EQUAL, LESS_THAN, LESS_THAN_EQUAL }
	
	// the operation for this instance of the class
	private OPERATION operation;
	
	
	/**
	 * Constructor - need dataType input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn IDentifier
	 * @param dataTypeArgsIn DataTYpe arguments
	 * @param opIn Operation
	 */
	public FunctionDefinitionComparison(Identifier idIn, DataType<I> dataTypeArgsIn, OPERATION opIn) {
		super(idIn, DataTypes.DT_BOOLEAN, dataTypeArgsIn, 2);
		operation = opIn;
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
		
		int compareResult;
		try {
			compareResult = ((I)convertedArguments.get(0)).compareTo((I)convertedArguments.get(1));
		} catch (Exception e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message));
		}

		switch (operation) {
		case GREATER_THAN:
			if (compareResult > 0) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}
			
		case GREATER_THAN_EQUAL:
			if (compareResult > -1) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}
			
		case LESS_THAN:
			if (compareResult < 0) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}
			
		case LESS_THAN_EQUAL:
			if (compareResult < 1) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}
		}
	
		// switch on enum should handle everything - should never get here
		return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " ENUM did not cover case of " + operation));

	}



}
