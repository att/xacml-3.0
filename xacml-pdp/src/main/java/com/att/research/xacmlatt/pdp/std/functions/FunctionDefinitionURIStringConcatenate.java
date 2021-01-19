/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.net.URI;
import java.util.List;

import com.att.research.xacml.api.DataTypeException;
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
 * FunctionDefinitionURIStringConcatenate extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML uri-string-concatenate predicate as a function taking one <code>URI</code> and one or more <code>String</code> arguments
 * and returning a single <code>URI</code> value.
 * 
 * THIS FUNCTION IS DEPRECATED IN 3.0 BUT NOT REMOVED.
 * To provide backward compatibility for previously built XACML policies we include it in our R3 version.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		uri-string-concatenate
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 */
@Deprecated
public class FunctionDefinitionURIStringConcatenate extends FunctionDefinitionBase<URI, URI> {
	

	/**
	 * Constructor
	 * 
	 * @param idIn Identifier
	 */
	public FunctionDefinitionURIStringConcatenate(Identifier idIn) {
		super(idIn, DataTypes.DT_ANYURI, DataTypes.DT_ANYURI, false);
	}


	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {
		
		if (arguments == null || arguments.size() < 2) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, getShortFunctionId() + " Expected 2 or more arguments, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}

		// get the string to search for
		ConvertedArgument<URI> uriArgument = new ConvertedArgument<>(arguments.get(0), DataTypes.DT_ANYURI, false);
		if ( ! uriArgument.isOk()) {
			Status decoratedStatus = new StdStatus(uriArgument.getStatus().getStatusCode(), uriArgument.getStatus().getStatusMessage() + " at arg index 0"  );
			return ExpressionResult.newError(getFunctionStatus(decoratedStatus));
		}
		StringBuilder uriString = new StringBuilder(uriArgument.getValue().toString());
		
		// remaining arguments are strings
		String[] stringValues = new String[arguments.size() - 1];
		
		for (int i = 1; i < arguments.size(); i++) {

			ConvertedArgument<String> stringArgument = new ConvertedArgument<>(arguments.get(i), DataTypes.DT_STRING, false);
			if ( ! stringArgument.isOk()) {
				Status decoratedStatus = new StdStatus(stringArgument.getStatus().getStatusCode(), stringArgument.getStatus().getStatusMessage() + " at arg index " + i );
				return ExpressionResult.newError(getFunctionStatus(decoratedStatus));
			}
			
			stringValues[i-1] = stringArgument.getValue();
		
		}
		
		
		// add each of the strings onto the URI
		for (int i = 0; i < stringValues.length; i++) {
			uriString.append(stringValues[i]);
		}
		
		URI resultURI = null;
		try {
			resultURI = DataTypes.DT_ANYURI.convert(uriString);
		} catch (DataTypeException e) {
			String message = e.getMessage();
			if (e.getCause() != null) {
				message = e.getCause().getMessage();
			}
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, getShortFunctionId() + 
					" Final string '" + uriString + "' not URI, " + message));
		}
		
		return ExpressionResult.newSingle(new StdAttributeValue<>(XACML.ID_DATATYPE_ANYURI, resultURI));

	}

}
