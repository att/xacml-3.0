/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionNumberTypeConversion extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML predicates foc converting <code>Double</code> to <code>Integer</code> and vice versa.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		double-to-integer
 * 		integer-to-double
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <O> the java class for the data type of the function Output
 * @param <I> the java class for the data type of the function Input argument
 */
public class FunctionDefinitionNumberTypeConversion<O extends Number, I extends Number> extends FunctionDefinitionHomogeneousSimple<O, I> {


	public FunctionDefinitionNumberTypeConversion(Identifier idIn, DataType<O> outputType, DataType<I> argType) {
		super(idIn, outputType, argType, 1);
		
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
		 * Numeric operations cannot be operated on generically in java, so we have to check the types and handle separately.
		 * Whichever type the argument is, convert it to the other
		 */
		ExpressionResult expressionResult;
		try {
			if (convertedArguments.get(0).getClass() == java.math.BigInteger.class) {
				AttributeValue<Double>	doubleResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_DOUBLE,
						Double.valueOf(  ((BigInteger)convertedArguments.get(0)).toString()  ) );
				expressionResult = ExpressionResult.newSingle(doubleResult);
			} else {
				AttributeValue<BigInteger>	integerResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER, BigInteger.valueOf(((Double)convertedArguments.get(0)).intValue()) );
				expressionResult = ExpressionResult.newSingle(integerResult);
			}
		} catch (Exception e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message));
		}
		
		return expressionResult;
	}

}
