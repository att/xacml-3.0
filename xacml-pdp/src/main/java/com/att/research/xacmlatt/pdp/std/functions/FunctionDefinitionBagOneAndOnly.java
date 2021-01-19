/*
 *
 *          Copyright (c) 2013,2019-2020  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.List;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionBagOneAndOnly implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML 'type'-one-and-only predicates as functions taking one <code>Bag</code> argument and returning the single element in that bag of the 'type'.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-one-and-only
 * 		boolean-one-and-only
 * 		integer-one-and-only
 * 		double-one-and-only
 * 		time-one-and-only
 * 		date-one-and-only
 * 		dateTime-one-and-only
 * 		anyURI-one-and-only
 * 		hexBinary-one-and-only
 * 		base64Binary-one-and-only
 * 		dayTimeDuration-one-and-only (version 1 and3)
 * 		yearMonthDuration-one-and-only (version 1 and 3)
 * 		x500Name-one-and-only
 * 		rfc822Name-one-and-only
 * 		ipAddress-one-and-only
 * 		dnsName-one-and-only
 *      time-zone-one-and-only
 * 
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <I> the java class for the data type of the elements in the bag handed to this as the Input argument, 
 * 	which is also the type of the return value
 * 
 */
public class FunctionDefinitionBagOneAndOnly<I> extends FunctionDefinitionBase<I,I> {

	
	/**
	 * Constructor - need dataType input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn Identifier
	 * @param dataTypeArgsIn DataType arguments
	 */
	public FunctionDefinitionBagOneAndOnly(Identifier idIn, DataType<I> dataTypeArgsIn) {
		super(idIn, dataTypeArgsIn, dataTypeArgsIn, false);
	}

	/**
	 * Evaluates this <code>FunctionDefinition</code> on the given <code>List</code> of{@link com.att.research.xacmlatt.pdp.policy.FunctionArgument}s.
	 * 
	 * @param evaluationContext the {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext} to use in the evaluation
	 * @param arguments the <code>List</code> of <code>FunctionArgument</code>s for the evaluation
	 * @return an {@link com.att.research.xacmlatt.pdp.policy.ExpressionResult} with the results of the call
	 */
	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {

		if (arguments == null || arguments.size() != 1) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, getShortFunctionId() + " Expected 1 argument, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}
		
		FunctionArgument argument = arguments.get(0);
		ConvertedArgument<Bag> convertedArgument = new ConvertedArgument<>(argument, null, true);

		if ( ! convertedArgument.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedArgument.getStatus()));
		}
		
		Bag bag = convertedArgument.getBag();
		
		if (bag.size() != 1) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, getShortFunctionId() + 
					" Expected 1 but Bag has " + bag.size() + " elements"));
		}

		// get the single value from the bag
		AttributeValue<?> attributeValueOneAndOnly	= bag.getAttributeValues().next();
		assert(attributeValueOneAndOnly != null);
		
		// make sure it has the right type
		//
		if (!this.getDataTypeId().equals(attributeValueOneAndOnly.getDataTypeId())) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, getShortFunctionId() + 
					" Element in bag of wrong type. Expected " + 
					this.getShortDataTypeId(this.getDataTypeId()) + " got " + this.getShortDataTypeId(attributeValueOneAndOnly.getDataTypeId())));			
		}
		return ExpressionResult.newSingle(attributeValueOneAndOnly);
	}

}
