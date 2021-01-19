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
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionEquality extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML Equality predicates as functions taking two arguments of the same data type and returning a <code>Boolean</code>.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-equal
 * 		boolean-equal
 * 		integer-equal
 * 		double-equal
 * 		date-equal
 * 		time-equal
 * 		dateTime-equal
 * 		dayTimeDuration-equal
 * 		yearMonthDuration-equal
 * 		anyURI-equal
 * 
 * @author car
 * @version $Revision: 1.2 $
 * 
 * @param <I> the java class for the data type of the function Input arguments
 */
public class FunctionDefinitionEquality<I> extends FunctionDefinitionHomogeneousSimple<Boolean, I> {
	
	/**
	 * Determines if the two <code>T</code> values are equal using the java <code>equals</code> method.  Derived classes
	 * may override this if the <code>equals</code> method is not sufficient.
	 * 
	 * @param v1 the first object to compare
	 * @param v2 the second object to compare
	 * @return true if the two objects are the same, else false
	 */
	protected boolean isEqual(I v1, I v2) {
		return v1.equals(v2);
	}
	
	public FunctionDefinitionEquality(Identifier idIn, DataType<I> dataTypeArgsIn) {
		super(idIn, DataTypes.DT_BOOLEAN, dataTypeArgsIn, 2);
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
		
		/*
		 * Now just perform the equality operation.
		 */
		if (this.isEqual(convertedArguments.get(0), convertedArguments.get(1))) {
			return ER_TRUE;
		} else {
			return ER_FALSE;
		}
	}

}
