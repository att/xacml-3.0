/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;


import java.math.BigInteger;
import java.util.List;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionStringFunctions implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML String Functions predicates except for the conversions between <code>String</code> and <code>DataType</code>
 * which are contained in <code>FunctionDefinitionStringConversion</code>.
 * The functions in this file do not have a lot in common except that the return data type is known and the input argument types are
 * either known or of the generic type.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-concatenate
 * 		string-starts-with
 * 		anyURI-starts-with
 * 		string-ends-with
 * 		anyURI-ends-with
 * 		string-contains
 * 		anyURI-contains
 * 		string-substring
 * 		anyURI-substring
 * 
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <I> the java class for the data type of the function Input arguments
 * @param <O> the java class for the data type of the function Output -
 * 		needed because different functions within this class have different output types
 */
public class FunctionDefinitionStringFunctions<O, I> extends FunctionDefinitionBase<O, I> {

	/**
	 * List of String operations.
	 * 
	 * @author glenngriffin
	 *
	 */
	public enum OPERATION {CONCATENATE, STARTS_WITH, ENDS_WITH, CONTAINS, SUBSTRING }
	
	// operation to be used in this instance of the StringFunctions class
	private final OPERATION operation;
	
	
	/**
	 * Constructor - need dataTypeArgs input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn Identifier
	 * @param dataTypeIn Input DataType
	 * @param dataTypeArgsIn DataType arguments
	 * @param op Operation
	 */
	public FunctionDefinitionStringFunctions(Identifier idIn, DataType<O> dataTypeIn, DataType<I> dataTypeArgsIn, OPERATION op) {
		super(idIn, dataTypeIn, dataTypeArgsIn, false);
		this.operation = op;
	}

	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {

		if (arguments == null || 
				(operation == OPERATION.CONCATENATE && arguments.size() < 2) ||
				(operation == OPERATION.SUBSTRING && arguments.size() != 3) ||
				(operation != OPERATION.SUBSTRING && operation != OPERATION.CONCATENATE && arguments.size() != 2) ) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Expected " +
				((operation == OPERATION.SUBSTRING) ? 3 : (operation == OPERATION.CONCATENATE ? "2 or more " : 2)) + " arguments, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}
		

		
		ExpressionResult expressionResult = null;
		
		String firstArgumentAsString = null;
		String secondArgumentAsString = null;
		
		Integer secondArgumentAsInteger = null;
		Integer thirdArgumentAsInteger = null;
		
		// most of the functions take 2  args, but SUBSTRING takes 3 AND concatenate takes 2 or more
		if (operation == OPERATION.CONCATENATE) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < arguments.size(); i++) {
				FunctionArgument functionArgument = arguments.get(i);
				ConvertedArgument<I> convertedArgument = new ConvertedArgument<>(functionArgument, this.getDataTypeArgs(), false);
				if ( ! convertedArgument.isOk()) {
					return ExpressionResult.newError(getFunctionStatus(convertedArgument.getStatus()));
				}
				try {
					String argumentAsString = this.getDataTypeArgs().toStringValue( convertedArgument.getValue());
					builder.append(argumentAsString);
				} catch (DataTypeException e) {
					String message = e.getMessage();
					if (e.getCause() != null) {
						message = e.getCause().getMessage();
					}
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message ));
				}
			}
			AttributeValue<String> stringResult =  new StdAttributeValue<>(XACML.ID_DATATYPE_STRING, 
					builder.toString() );
			expressionResult = ExpressionResult.newSingle(stringResult);
			return expressionResult;
			
		} else if (operation == OPERATION.SUBSTRING) {
			// first arg is of generic type
			FunctionArgument functionArgument = arguments.get(0);
			ConvertedArgument<I> convertedArgument0 = new ConvertedArgument<>(functionArgument, this.getDataTypeArgs(), false);
			if ( ! convertedArgument0.isOk()) {
				return ExpressionResult.newError(getFunctionStatus(convertedArgument0.getStatus()));
			}
			try {
				firstArgumentAsString = this.getDataTypeArgs().toStringValue( convertedArgument0.getValue());
			} catch (DataTypeException e) {
				String message = e.getMessage();
				if (e.getCause() != null) {
					message = e.getCause().getMessage();
				}
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message ));
			}
			
			functionArgument = arguments.get(1);
			ConvertedArgument<BigInteger> convertedArgumentInt = new ConvertedArgument<>(functionArgument, DataTypes.DT_INTEGER, false);
			if ( ! convertedArgumentInt.isOk()) {
				return ExpressionResult.newError(getFunctionStatus(convertedArgumentInt.getStatus()));
			}
			secondArgumentAsInteger = convertedArgumentInt.getValue().intValue();
			if (secondArgumentAsInteger < 0 || secondArgumentAsInteger > firstArgumentAsString.length()) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " +
						"Start point '" + secondArgumentAsInteger + "' out of range 0-" + firstArgumentAsString.length() +
						" for string='" + firstArgumentAsString + "'"));
			}
			
			
			functionArgument = arguments.get(2);
			convertedArgumentInt = new ConvertedArgument<>(functionArgument, DataTypes.DT_INTEGER, false);
			if ( ! convertedArgumentInt.isOk()) {
				return ExpressionResult.newError(getFunctionStatus(convertedArgumentInt.getStatus()));
			}
			thirdArgumentAsInteger = convertedArgumentInt.getValue().intValue();
			// special case: -1 means "to end of string"
			if (thirdArgumentAsInteger < -1 || thirdArgumentAsInteger > firstArgumentAsString.length()) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " +
						"End point '" + thirdArgumentAsInteger + "' out of range 0-" + firstArgumentAsString.length() +
						" for string='" + firstArgumentAsString + "'"));
			}
			if (thirdArgumentAsInteger != -1 && thirdArgumentAsInteger < secondArgumentAsInteger) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " +
						"End point '" + thirdArgumentAsInteger + "' less than start point '" + secondArgumentAsString + "'" +
						" for string='" + firstArgumentAsString + "'"));
			}
			
		} else {
			// expect 2 args, one String and one of Generic type
			FunctionArgument functionArgument = arguments.get(0);
			ConvertedArgument<String> convertedArgument0 = new ConvertedArgument<>(functionArgument, DataTypes.DT_STRING, false);
			if ( ! convertedArgument0.isOk()) {
				return ExpressionResult.newError(getFunctionStatus(convertedArgument0.getStatus()));
			}
			firstArgumentAsString = convertedArgument0.getValue();
			
			
			functionArgument = arguments.get(1);
			ConvertedArgument<I> convertedArgument1 = new ConvertedArgument<>(functionArgument, this.getDataTypeArgs(), false);
			if ( ! convertedArgument1.isOk()) {
				return ExpressionResult.newError(getFunctionStatus(convertedArgument1.getStatus()));
			}
			try {
				secondArgumentAsString = this.getDataTypeArgs().toStringValue( convertedArgument1.getValue());
			} catch (DataTypeException e) {
				String message = e.getMessage();
				if (e.getCause() != null) {
					message = e.getCause().getMessage();
				}
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message +
						" " + message ));
			}
		
			
		}
		
		// arguments are ready - do the operation
		
		switch (operation) {
		case STARTS_WITH:
			if (secondArgumentAsString.startsWith(firstArgumentAsString)) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}
			
			
		case ENDS_WITH:
			if (secondArgumentAsString.endsWith(firstArgumentAsString)) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}
			
		case CONTAINS:
			if (secondArgumentAsString.contains(firstArgumentAsString)) {
				return ER_TRUE;
			} else {
				return ER_FALSE;
			}
			
		case SUBSTRING:
			String substring = null;
			if (thirdArgumentAsInteger == -1) {
				// from start point to end of string
				substring = firstArgumentAsString.substring(secondArgumentAsInteger);
			} else {
				substring = firstArgumentAsString.substring(secondArgumentAsInteger, thirdArgumentAsInteger);
			}
			AttributeValue<String> stringResult =  new StdAttributeValue<>(XACML.ID_DATATYPE_STRING, substring);
			expressionResult = ExpressionResult.newSingle(stringResult);
			break;
		
		default:
		    break;
		}
		
		
		return expressionResult;

	}


	
	

}
