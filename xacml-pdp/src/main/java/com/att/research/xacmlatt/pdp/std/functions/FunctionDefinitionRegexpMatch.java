/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;


import java.util.List;

import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionRegexMatch implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML 'type'-regex-match predicates as functions taking two arguments, the first of <code>String</code>,
 * representing a regular expression, and the second of the type for that specific predicate,
 * and returning a <code>Boolean</code> for whether the regular expression matches the string representation of the second argument.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-regexp-match
 * 		anyURI-regexp-match
 * 		x500Name-regexp-match
 * 		rfc822Name-regexp-match (in sub-class {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionRFC822NameMatch} )
 * 		ipAddress-regexp-match
 * 		dnsName-regexp-match
 * 
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <I> the java class for the data type of the function Input arguments
 */
public class FunctionDefinitionRegexpMatch<I> extends FunctionDefinitionBase<Boolean, I> {

	
	/**
	 * Constructor - need dataTypeArgs input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn Identifier
	 * @param dataTypeArgsIn DataType arguments
	 */
	public FunctionDefinitionRegexpMatch(Identifier idIn, DataType<I> dataTypeArgsIn) {
		super(idIn, DataTypes.DT_BOOLEAN, dataTypeArgsIn, false);
	}


	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {

		if (arguments == null || arguments.size() != 2) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Expected 2 arguments, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}
		
		// get the regular expression
		FunctionArgument regexpArgument = arguments.get(0);

		ConvertedArgument<String> convertedArgument = new ConvertedArgument<>(regexpArgument, DataTypes.DT_STRING, false);
		if ( ! convertedArgument.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedArgument.getStatus()));
		}
		
		// String regexpValue = (String)regexpArgument.getValue().getValue();
		String regexpValue	= convertedArgument.getValue();

		
		// now get the element to match
		FunctionArgument elementArgument = arguments.get(1);
		
		ConvertedArgument<I> convertedElement = new ConvertedArgument<>(elementArgument, this.getDataTypeArgs(), false);
		if ( ! convertedElement.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedElement.getStatus()));
		}
		
		I elementValueObject = convertedElement.getValue();

		String elementValueString;
		try {
			elementValueString = this.getDataTypeArgs().toStringValue(elementValueObject);
		} catch (DataTypeException e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " " + message));
		}
		
		// ConvertedArgument checks for null value, so do not need to do again here

		if (elementValueString.matches(regexpValue)) {
			return ER_TRUE;
		} else {
			return ER_FALSE;
		}

	}


	
	

}
