/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionNumberTypeConversion extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML predicates for converting <code>String</code> to <code>{@literal DataType<?>}</code> and vice versa.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		boolean-from-string
 * 		string-from-boolean
 * 		integer-from-string
 * 		string-from-integer
 * 		double-from-string
 * 		string-from-double
 * 		time-from-string
 * 		string-from-time
 * 		date-from-string
 * 		string-from-date
 * 		dateTime-from-string
 * 		string-from-dateTime
 * 		anyURI-from-string
 * 		string-from-anyURI
 * 		dayTimeDuration-from-string
 * 		string-from-dayTimeDuration
 * 		yearMonthDuration-from-string
 * 		string-from-yearMonthDuration
 * 		x500Name-from-string
 * 		string-from-x500Name
 * 		rfc822Name-from-string
 * 		string-from-rfc822Name
 * 		ipAddress-from-string
 * 		string-from-ipAddress
 * 		dnsName-from-string
 * 		string-from-dnsName
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <O> the java class for the data type of the function Output
 * @param <I> the java class for the data type of the function Input argument
 */
public class FunctionDefinitionStringConversion<O,I> extends FunctionDefinitionHomogeneousSimple<O, I> {
  private static final Logger logger    = LoggerFactory.getLogger(FunctionDefinitionStringConversion.class);

	public FunctionDefinitionStringConversion(Identifier idIn, DataType<O> outputType, DataType<I> argType) {
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
		 * Do different conversion depending on which way we are going (to/from String)
		 */
		if (this.getDataTypeId().equals(DataTypes.DT_STRING.getId())) {
			// converting TO String
			try {
				String output = this.getDataTypeArgs().toStringValue(convertedArguments.get(0));
				ExpressionResult result = ExpressionResult.newSingle(new StdAttributeValue<>(this.getDataTypeId(), output));
				logger.debug("Converted {} to a string {}", convertedArguments.get(0), output);
	            return result;
			} catch (Exception e) {
				String message = e.getMessage();
				if (e.getCause() != null) {
					message = e.getCause().getMessage();
				}
				// untested - not clear how this could happen
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +  " " + message));
			}
		} else {
			// converting FROM String to object of DataType
			try {
				O output = this.getDataType().convert(convertedArguments.get(0));
                logger.debug("Converted string {} to {}", convertedArguments.get(0), output);
				return ExpressionResult.newSingle(new StdAttributeValue<>(this.getDataTypeId(), output));
			} catch (Exception e) {
				String message = e.getMessage();
				if (e.getCause() != null) {
					message = e.getCause().getMessage();
				}
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, this.getShortFunctionId() + " " + message ));
			}
		}
		
	}
}
