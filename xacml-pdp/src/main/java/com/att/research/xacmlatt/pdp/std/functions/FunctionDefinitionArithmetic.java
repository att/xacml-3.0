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
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionArithmetic extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML Arithmetic predicates as functions taking one or two arguments of the same data type and returning a single value of the same type.
 * 
 * In Java there is no way to do arithmetic operations generically, so we need to have individual code for each operation on each class within this class.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		integer-add
 * 		double-add
 * 		integer-subtract
 * 		double-subtract
 * 		integer-multiply
 * 		double-multiply
 * 		integer-divide
 * 		double-divide
 * 		integer-mod
 * 		integer-abs
 * 		double-abs
 * 		round
 * 		floor
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <T> the java class for the data type of the function arguments
 */
public class FunctionDefinitionArithmetic<T extends Number> extends FunctionDefinitionHomogeneousSimple<T,T> {
	
	/**
	 * List of arithmetic operations.
	 * 
	 * @author glenngriffin
	 *
	 */
	public enum OPERATION {ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD, ABS, ROUND, FLOOR }
	
	// operation to be used in this instance of the Arightmetic class
	private final OPERATION operation;
	
	// result variables used by all functions, one for each type
	private AttributeValue<BigInteger>	integerResult;
	private AttributeValue<Double>	doubleResult;

	/**
	 * Constructor
	 * 
	 * @param idIn Identifier
	 * @param dataTypeArgsIn DataType
	 * @param op Operation
	 * @param nArgs  number of arguments
	 */
	public FunctionDefinitionArithmetic(Identifier idIn, DataType<T> dataTypeArgsIn, OPERATION op, int nArgs) {
		// for Arithmetic functions, the output type is the same as the input type (no mixing of Ints and Doubles!)
		super(idIn, dataTypeArgsIn, dataTypeArgsIn, nArgs);
		
		// save the operation to be used in this instance
		operation = op;
	}


	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
		List<T> convertedArguments	= new ArrayList<>();
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
		
		try {
			switch (operation) {
			case ADD:
				if (this.getDataType() == DataTypes.DT_INTEGER) {
					integerResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER, ((BigInteger)convertedArguments.get(0)).add( (BigInteger)convertedArguments.get(1)) );
					expressionResult = ExpressionResult.newSingle(integerResult);
				} else {
					doubleResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_DOUBLE, (Double)convertedArguments.get(0) + (Double)convertedArguments.get(1));
					expressionResult = ExpressionResult.newSingle(doubleResult);
				}
				break;
			case SUBTRACT:
				if (this.getDataType() == DataTypes.DT_INTEGER) {
					integerResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER, ((BigInteger)convertedArguments.get(0)).subtract( (BigInteger)convertedArguments.get(1)) );
					expressionResult = ExpressionResult.newSingle(integerResult);
				} else {
					doubleResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_DOUBLE, (Double)convertedArguments.get(0) - (Double)convertedArguments.get(1));
					expressionResult = ExpressionResult.newSingle(doubleResult);
				}
				break;
			case MULTIPLY:
				if (this.getDataType() == DataTypes.DT_INTEGER) {
					integerResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER, ((BigInteger)convertedArguments.get(0)).multiply((BigInteger)convertedArguments.get(1)) );
					expressionResult = ExpressionResult.newSingle(integerResult);
				} else {
					doubleResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_DOUBLE, (Double)convertedArguments.get(0) * (Double)convertedArguments.get(1));
					expressionResult = ExpressionResult.newSingle(doubleResult);
				}
				break;
			case DIVIDE:
				if (this.getDataType() == DataTypes.DT_INTEGER) {
					if ( ((BigInteger)convertedArguments.get(1)).equals(BigInteger.valueOf(0)) ) {
						return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +" Divide by 0 error: "+
								arguments.get(0).getValue().getValue().toString() + ", " + arguments.get(1).getValue().getValue().toString()));
					}
					integerResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER, ((BigInteger)convertedArguments.get(0)).divide((BigInteger)convertedArguments.get(1)) );
					expressionResult = ExpressionResult.newSingle(integerResult);
				} else {
					if ((Double)convertedArguments.get(1) == 0) {
						return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +" Divide by 0 error: "+
								arguments.get(0).getValue().getValue().toString() + ", " + arguments.get(1).getValue().getValue().toString()));
					}
					doubleResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_DOUBLE, (Double)convertedArguments.get(0) / (Double)convertedArguments.get(1));
					expressionResult = ExpressionResult.newSingle(doubleResult);
				}
				break;
			case MOD:
				if ( ((BigInteger)convertedArguments.get(1)).equals(BigInteger.valueOf(0)) ) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +" Divide by 0 error: "+
							arguments.get(0).getValue().getValue().toString() + ", " + arguments.get(1).getValue().getValue().toString()));
				}
				integerResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER, ((BigInteger)convertedArguments.get(0)).remainder((BigInteger)convertedArguments.get(1)) );
				expressionResult = ExpressionResult.newSingle(integerResult);
				break;
			case ABS:
				if (this.getDataType() == DataTypes.DT_INTEGER) {
					integerResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER, ((BigInteger)convertedArguments.get(0)).abs() );
					expressionResult = ExpressionResult.newSingle(integerResult);
				} else {
					doubleResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_DOUBLE, Math.abs((Double)convertedArguments.get(0)));
					expressionResult = ExpressionResult.newSingle(doubleResult);
				}
				break;
			case ROUND:
				doubleResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_DOUBLE, (double)(Math.round((Double)convertedArguments.get(0))) );
				expressionResult = ExpressionResult.newSingle(doubleResult);
				break;
			case FLOOR:
				doubleResult	= new StdAttributeValue<>(XACML.ID_DATATYPE_DOUBLE, Math.floor((Double)convertedArguments.get(0)));
				expressionResult = ExpressionResult.newSingle(doubleResult);
				break;
			}
		} catch (Exception e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			String args = arguments.get(0).getValue().toString();
			if (arguments.size() > 1) {
				args += ", " + arguments.get(1).getValue().toString();
			}
			expressionResult = ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message +
					" args: " + args + " " + e.getMessage() ));
		}
		
		return expressionResult;
	}

}
