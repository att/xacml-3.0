/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.List;

import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionBag implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML 'type'-bag predicates as functions taking 0, 1 or multiple arguments of the same data type and returning a <code>Bag</code>.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-bag
 * 		boolean-bag
 * 		integer-bag
 * 		double-bag
 * 		time-bag
 * 		date-bag
 * 		dateTime-bag
 * 		anyURI-bag
 * 		hexBinary-bag
 * 		base64Binary-bag
 * 		dayTimeDuration-bag (version 1 and3)
 * 		yearMonthDuration-bag (version 1 and 3)
 * 		x500Name-bag
 * 		rfc822Name-bag
 * 		ipAddress-bag
 * 		dnsName-bag
 * 
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <I> the java class for the data type of the function Input arguments,
 * 		which is also the "type" of the returned bag
 */
public class FunctionDefinitionBag<I> extends FunctionDefinitionBase<I, I> {

	
	/**
	 * Constructor - need dataType input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn Identifier
	 * @param dataTypeArgsIn DataType arguments
	 */
	public FunctionDefinitionBag(Identifier idIn, DataType<I> dataTypeArgsIn) {
		super(idIn, dataTypeArgsIn, dataTypeArgsIn, true);
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

		// create a list to put the values into
		Bag elementBag	= new Bag();
		
		// see if we have arguments
		if (arguments != null && ! arguments.isEmpty()) {
	
			// for each arg, evaluate it, check type, and put on the list
			for (FunctionArgument argument : arguments) {
				// get the argument, evaluate it and check status
				ConvertedArgument<I> convertedArgument = new ConvertedArgument<>(argument, this.getDataTypeArgs(), false);
				
				// check the status
				if ( ! convertedArgument.isOk()) {
					return ExpressionResult.newError(getFunctionStatus(convertedArgument.getStatus()));
				}
				
				// Special case: Most methods want the value contained in the AttributeValue object inside the FunctionArgument.
				// This one wants the AttributeValue itself.
				// We use the ConvertedArgument constructor to validate that the argument is ok, then use the AttributeValue
				// from the FunctionArgument.
				elementBag.add(argument.getValue());
			}
		}
		
		// return it
		return ExpressionResult.newBag(elementBag);
	}

	
	

}
