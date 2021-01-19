/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.util.ArrayList;
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
 * FunctionDefinitionSet implements {@link com.att.research.xacmlatt.pdp.policy.FunctionDefinition} to
 * implement the XACML Set predicates as functions taking two arguments of <code>Bag</code> the same primitive type
 * and returning either a <code>Boolean</code> or a <code>Bag</code> of the same primitive type.
 * <P>
 * The ipAddress, dnsName and xPathExpression do not have set functions defined for them in section 10.2.8 of the Release 3 XACML spec.
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
 * 
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <I> the java class for the data type of the function Input arguments
 * @param <O> the java class for the data type of the function Output
 */
public class FunctionDefinitionSet<O,I> extends FunctionDefinitionBase<O, I> {

	/**
	 * List of comparison operations.
	 * 
	 * @author glenngriffin
	 *
	 */
	public enum OPERATION {INTERSECTION, AT_LEAST_ONE_MEMBER_OF, UNION, SUBSET, SET_EQUALS }
	
	// the operation for this instance of the class
	private OPERATION operation;
	
	
	/**
	 * Constructor - need dataType input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn Identifier
	 * @param dataTypeIn DataType in
	 * @param dataTypeArgsIn DataType arguments
	 * @param opIn Operation
	 */
	public FunctionDefinitionSet(Identifier idIn, DataType<O> dataTypeIn, DataType<I> dataTypeArgsIn, OPERATION opIn) {
		super(idIn, dataTypeIn, dataTypeArgsIn, (opIn == OPERATION.INTERSECTION || opIn == OPERATION.UNION) );
		operation = opIn;
	}


	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {

		if (arguments == null || arguments.size() != 2) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Expected 2 arguments, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}
		
		// get first bag
		FunctionArgument bagArgument = arguments.get(0);
		ConvertedArgument<Bag> convertedBagArgument = new ConvertedArgument<>(bagArgument, null, true);

		if ( ! convertedBagArgument.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedBagArgument.getStatus()));
		}
	
		Bag bag1 = convertedBagArgument.getBag();
		List<AttributeValue<?>> list1 = bag1.getAttributeValueList();
		
		// get second bag
		bagArgument = arguments.get(1);
		convertedBagArgument = new ConvertedArgument<>(bagArgument, null, true);

		if ( ! convertedBagArgument.isOk()) {
			return ExpressionResult.newError(getFunctionStatus(convertedBagArgument.getStatus()));
		}
	
		Bag bag2 = convertedBagArgument.getBag();
		List<AttributeValue<?>> list2 = bag2.getAttributeValueList();

		// arguments are ready BUT they have NOT had duplicates removed
		
		ExpressionResult expressionResult = null;
		
		// some functions return a bag rather than boolean
		Bag outBag;
		List<AttributeValue<?>> outList;
		
		
		switch (operation) {
		case INTERSECTION:
			outList = new ArrayList<>();
			
			for (AttributeValue<?> element : list1) {
				if (outList.contains(element)) {
					continue;
				}
				if (list2.contains(element)) {
					outList.add(element);
				}
			}
			
			// now have the intersection; put it in a bag
			
			outBag = new Bag();
			
			for (AttributeValue<?> element : outList) {
					outBag.add(element);
			}

			expressionResult = ExpressionResult.newBag(outBag);
			return expressionResult;
			
			
		case AT_LEAST_ONE_MEMBER_OF:
			// look for elements from the first list in the second.
			// duplicates do not matter because if the element is not there it does not matter that we look for it again,
			// and if it is there we stop the first time we see it.
			// If the first bag is empty, this should fail because no element from the first set can be found in the second set 
			// (because there IS no element in first set).
			for (AttributeValue<?> element : list1) {
				if (list2.contains(element)) {
					return ER_TRUE;
				}
			}
			// did not find any element from list 1 in list 2
			return ER_FALSE;
			
		case UNION:
			outList = new ArrayList<>();
			
			for (AttributeValue<?> element : list1) {
				if (outList.contains(element)) {
					continue;
				}
				outList.add((AttributeValue<?>) element);
			}
			for (AttributeValue<?> element : list2) {
				if (outList.contains(element)) {
					continue;
				}
				outList.add((AttributeValue<?>) element);
			}
			
			// now have the intersection; put it in a bag
			
			outBag = new Bag();
			
			for (AttributeValue<?> element : outList) {
				outBag.add(element);
			}

			expressionResult = ExpressionResult.newBag(outBag);
			return expressionResult;
			
			
		case SUBSET:
			// all elements from list 1 must exist in list 2.
			// duplicates do not matter because if an element is not found the first time we stop immediately,
			// and if it is found the first time it will also be found for the duplicate.
			// If the first set is empty we return TRUE because all elements (i.e. none) in the first set are in the second.
			for (AttributeValue<?> element : list1) {
				if ( ! list2.contains(element)) {
					return ER_FALSE;
				}
			}
			// all elements in list1 were found
			return ER_TRUE;
			
			
		case SET_EQUALS:
			// we cannot do a direct one-to-one compare because the lists may contain duplicates.  Also they may not be ordered the same.
			// So we ask:
			//		are all elements in list 1 in list 2 (ignoring duplicates)
			//		are all elements in list 2 in list 1 (ignoring duplicates)
			for (AttributeValue<?> element : list1) {
				if ( ! list2.contains(element)) {
					return ER_FALSE;
				}
			}
			for (AttributeValue<?> element : list2) {
				if ( ! list1.contains(element)) {
					return ER_FALSE;
				}
			}
			// all elements in each are part of the other
			return ER_TRUE;
		}
	
		// all cases should have been covered by above - should never get here
		return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Could not evaluate Set function " + operation));

	}



}
