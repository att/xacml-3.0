/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.ArrayList;
import java.util.List;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypeString;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionStringNormalize extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML String normalization predicates as functions taking one <code>String</code> arg and returning a single value of the same type.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-normalize-space
 * 		string-normalize-to-lower-case
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 */
public class FunctionDefinitionStringNormalize extends FunctionDefinitionHomogeneousSimple<String, String> {
	
	/**
	 * List of string normalization operations.
	 * 
	 * @author glenngriffin
	 *
	 */
	public enum OPERATION {SPACE, LOWER_CASE }
	
	// operation to be used in this instance of the Arightmetic class
	private final OPERATION operation;
	
	
	// result variables used by all functions
	AttributeValue<String>	result;


	/**
	 * Constructor
	 * 
	 * @param idIn Identifier
	 * @param op Operation
	 */
	public FunctionDefinitionStringNormalize(Identifier idIn,  OPERATION op) {
		// for Arithmetic functions, the output type is the same as the input type (no mixing of Ints and Doubles!)
		super(idIn, DataTypes.DT_STRING, DataTypeString.newInstance(), 1);
		
		// save the operation and data type to be used in this instance
		operation = op;

	}


	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
		List<String> convertedArguments	= new ArrayList<>();
		Status status				= this.validateArguments(arguments, convertedArguments);
		
		/*
		 * If the function arguments are not correct, just return an error status immediately
		 */
		if (!status.getStatusCode().equals(StdStatusCode.STATUS_CODE_OK)) {
			return ExpressionResult.newError(getFunctionStatus(status));
		}
		
		/*
		 * Now perform the requested operation.
		 */
		ExpressionResult expressionResult = null;
		
		switch (operation) {
		case SPACE:
			result	= new StdAttributeValue<>(XACML.ID_DATATYPE_STRING, convertedArguments.get(0).trim() );
			break;
		case LOWER_CASE:
			result	= new StdAttributeValue<>(XACML.ID_DATATYPE_STRING, convertedArguments.get(0).toLowerCase() );
			break;
		}
		
		expressionResult = ExpressionResult.newSingle(result);

		return expressionResult;
	}

}
