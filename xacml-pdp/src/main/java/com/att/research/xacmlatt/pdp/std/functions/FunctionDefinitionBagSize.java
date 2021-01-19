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
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;

/**
 * FunctionDefinitionBagSize implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML 'type'-bag-size predicates as functions taking one <code>Bag</code> argument and returning an <code>Integer</code> 
 * representing the number of elements in the bag.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		string-bag-size
 * 		boolean-bag-size
 * 		integer-bag-size
 * 		double-bag-size
 * 		time-bag-size
 * 		date-bag-size
 * 		dateTime-bag-size
 * 		anyURI-bag-size
 * 		hexBinary-bag-size
 * 		base64Binary-bag-size
 * 		dayTimeDuration-bag-size (version 1 and3)
 * 		yearMonthDuration-bag-size (version 1 and 3)
 * 		x500Name-bag-size
 * 		rfc822Name-bag-size
 * 		ipAddress-bag-size
 * 		dnsName-bag-size
 * 
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <I> the java class for the data type of the function Input arguments
 */
public class FunctionDefinitionBagSize<I> extends FunctionDefinitionBase<BigInteger, I> {


	/**
	 * Constructor - need dataType input because of java Generic type-erasure during compilation.
	 * 
     * @param idIn Identifier
     * @param dataTypeArgsIn DataType arguments
	 */
	public FunctionDefinitionBagSize(Identifier idIn, DataType<I> dataTypeArgsIn) {
		super(idIn, DataTypes.DT_INTEGER, dataTypeArgsIn, false);
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
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Expected 1 argument, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}
		
		FunctionArgument argument = arguments.get(0);
		ConvertedArgument<Bag> convertedArgument = new ConvertedArgument<>(argument, null, true);

		if ( ! convertedArgument.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedArgument.getStatus()));
		}
		
		Bag bag = convertedArgument.getBag();
		
		if (bag == null) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Bag is null" ));

		}


		// type is correct, so create a wrapper and return it
		AttributeValue<BigInteger> resultAttributeValue = new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER, BigInteger.valueOf(bag.size()));
		return ExpressionResult.newSingle(resultAttributeValue);
	}

	

}
