/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.Bag;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;
import com.att.research.xacmlatt.pdp.policy.FunctionArgumentAttributeValue;
import com.att.research.xacmlatt.pdp.policy.FunctionDefinition;
import com.att.research.xacmlatt.pdp.std.StdFunctionDefinitionFactory;

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
public class FunctionDefinitionHigherOrderBag<O,I> extends FunctionDefinitionBase<O, I> {

	/**
	 * List of comparison operations.
	 * 
	 * @author glenngriffin
	 *
	 */
	public enum OPERATION {ANY_OF, ALL_OF, ANY_OF_ANY, ALL_OF_ANY, ANY_OF_ALL, ALL_OF_ALL, MAP  }
	
	// the operation for this instance of the class
	private OPERATION operation;
	
	
	/**
	 * Constructor - need dataType input because of java Generic type-erasure during compilation.
	 * 
	 * @param idIn Identifier
	 * @param dataTypeIn DataType input
	 * @param dataTypeArgsIn DataType arguments
	 * @param opIn Operation
	 */
	public FunctionDefinitionHigherOrderBag(Identifier idIn, DataType<O> dataTypeIn, DataType<I> dataTypeArgsIn, OPERATION opIn) {
		super(idIn, dataTypeIn, dataTypeArgsIn, opIn == OPERATION.MAP );
		operation = opIn;
	}


	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {

		// simple argument check
		if (arguments == null || arguments.size() < 2) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
					" Expected at least 2 arguments, got " + 
					((arguments == null) ? "null" : arguments.size()) ));
		}
		
		// three functions have some things known about the arguments
		if (operation == OPERATION.ALL_OF_ANY || operation == OPERATION.ANY_OF_ALL || operation == OPERATION.ALL_OF_ALL) {
			if (arguments.size() != 3) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
					" Expected 3 arguments, got " + arguments.size()) );
			}
			// the 2nd & 3rd arguments must both be bags
			if ( arguments.get(1) == null || ! arguments.get(1).isBag() ) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
						" 2nd argument must be bag, got '" + ((arguments.get(1) == null) ? "null" : this.getShortDataTypeId(arguments.get(1).getValue().getDataTypeId())) + "'" ));
			}
			if (arguments.get(2) == null || ! arguments.get(2).isBag() ) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
						" 3rd argument must be bag, got '" + ((arguments.get(2) == null) ? "null" : this.getShortDataTypeId(arguments.get(2).getValue().getDataTypeId())) + "'" ));
			}
		}
		
		// first argument is supposed to be a Function ID passed to us as an AnyURI
		FunctionArgument functionIdArgument = arguments.get(0);
		if (functionIdArgument == null || functionIdArgument.getValue() == null) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
					" Predicate Function (first argument) was null"));
		}
		if ( ! functionIdArgument.getValue().getDataTypeId().equals(DataTypes.DT_ANYURI.getId())) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
					" First argument expected URI, got " + functionIdArgument.getValue().getDataTypeId() ) );
		}
		Identifier functionId = new IdentifierImpl((URI) functionIdArgument.getValue().getValue());
		
		// look up the actual function definition based on that ID
		StdFunctionDefinitionFactory fdf = new StdFunctionDefinitionFactory();
		
		FunctionDefinition predicate = fdf.getFunctionDefinition(functionId);
		
		if (predicate == null) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
					" First argument was not URI of a function, got '" + functionId + "'") );
		}
		// in all cases except MAP, the predicate must return True/False
		if (operation != OPERATION.MAP) {
			if ( ! predicate.getDataTypeId().equals(DataTypes.DT_BOOLEAN.getId())) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
						" Predicate Function must return boolean, but '" + predicate.getId() + "' returns '" + this.getShortDataTypeId(predicate.getDataTypeId()) ));
			}
		}
		
		
		
		// The remaining arguments may be either bags or primitive types.
		// We do not know what the primitive types will be, and do not concern ourselves about that here 
		// (the predicate function we just got and will call later will complain if they do not match its expectations).
		// The predicate function will want things as FunctionAttributes, so we do not need to unwrap anything.
		boolean bagSeen = false;
		for (int i = 1; i < arguments.size(); i++) {
			FunctionArgument argument = arguments.get(i);
			if (argument == null) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
						" Got null argument at index " + i) );
			}
			// force evaluation and check status
			if ( ! argument.getStatus().isOk()) {
				return ExpressionResult.newError(getFunctionStatus(argument.getStatus()));
			}

			// for bags, remember that we saw one; for non-bag primitives, check that the primitive value is not null
			if (argument.isBag()) {
				bagSeen = true;
			} else {
				if (argument.getValue() == null || argument.getValue().getValue() == null) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
							" Got null attribute at index " + i) );
				}
			}
		}

		// all functions require at least one bag
		if ( ! bagSeen && operation != OPERATION.ANY_OF_ANY) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
					" Did not get any Bag argument; must have at least 1") );
		}
		
		
		// arguments are ready for use
		
		// list of arguments for passing to the predicate
		List<FunctionArgument> predicateArguments = new ArrayList<>();

		// for functions that take a single bag, which index is that bag at
		int indexOfBagInOriginalArgs = -1;
		
		// bag iterator
		Iterator<AttributeValue<?>> bagIterator1;
		Iterator<AttributeValue<?>> bagIterator2;

		
		
		
		switch (operation) {
		
		case ANY_OF:
			// Copy the primitive arguments to the list for passing to the predicate,
			// putting a place-holder in for the value from the (single) bag
			for (int i = 1; i < arguments.size(); i++) {
				predicateArguments.add(arguments.get(i));
				if (arguments.get(i).isBag()) {
					if (indexOfBagInOriginalArgs == -1) {
						indexOfBagInOriginalArgs = i ;
					} else {
						// bag already found - we should have only one
						return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
								" must have only 1 bag; found one at index " + indexOfBagInOriginalArgs + " and another at " + i) );
					}
				}
			}
			
			// get each primitive value in turn
			bagIterator1 = arguments.get(indexOfBagInOriginalArgs).getBag().getAttributeValues();
			while (bagIterator1.hasNext()) {
				// all of the predicate arguments have been created except that the one from the bag needs to replace the place-holder in the list
				predicateArguments.set(indexOfBagInOriginalArgs - 1, new FunctionArgumentAttributeValue(bagIterator1.next()));
				ExpressionResult res = predicate.evaluate(evaluationContext, predicateArguments);
				if ( ! res.isOk()) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
							" Predicate error: " + res.getStatus().getStatusMessage()) );
				}
				if ( (Boolean)(res.getValue().getValue()) ) {
					return ER_TRUE;
				}
			}

			return ER_FALSE;
			
			
			
		case ALL_OF:
			// Copy the primitive arguments to the list for passing to the predicate,
			// putting a place-holder in for the value from the (single) bag
			for (int i = 1; i < arguments.size(); i++) {
				predicateArguments.add(arguments.get(i));
				if (arguments.get(i).isBag()) {
					if (indexOfBagInOriginalArgs == -1) {
						indexOfBagInOriginalArgs = i ;
					} else {
						// bag already found - we should have only one
						return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
								" must have only 1 bag; found one at index " + indexOfBagInOriginalArgs + " and another at " + i) );
					}
				}
			}
			
			// get each primitive value in turn
			bagIterator1 = arguments.get(indexOfBagInOriginalArgs).getBag().getAttributeValues();
			while (bagIterator1.hasNext()) {
				// all of the predicate arguments have been created except that the one from the bag needs to replace the place-holder in the list
				predicateArguments.set(indexOfBagInOriginalArgs - 1, new FunctionArgumentAttributeValue(bagIterator1.next()));
				ExpressionResult res = predicate.evaluate(evaluationContext, predicateArguments);
				if ( ! res.isOk()) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
							" Predicate error: " + res.getStatus().getStatusMessage()) );
				}
				if ( ! (Boolean)(res.getValue().getValue())) {
					return ER_FALSE;
				}
			}
			return ER_TRUE;
		
			
		case ANY_OF_ANY:
			// empty bags can give odd error messages, so check here and return something that makes more sense
			for (int i = 1; i < arguments.size(); i++) {
				if (arguments.get(i).isBag() && arguments.get(i).getBag().size() == 0) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
							" Bag is empty at index " + i ));
				}
			}
			// This is different from all the other Higher-order bag functions because it can take an unbounded number of arguments any/all of which may be bags.
			// (The others take either an unbounded number of args of which exactly 1 is a bag, or they take exactly 2 bags)
			// To handle the possibility of multiple bags without knowing a priori how many there might be,
			// we first create all possible lists of arguments to be passed to the predicate.
			// This is done using a depth-first search of the total argument space.
			List<List<FunctionArgument>> listOfPredicateLists = new ArrayList<>();		
			
			/*
			 * Start the recursive append process
			 */
			appendCrossProduct(new ArrayList<>(), arguments.subList(1, arguments.size()), 0, listOfPredicateLists);
			
			// we now have all possible argument lists for the predicate to work on, so do the ANY operation now
			for (List<FunctionArgument> predicateArgumentList : listOfPredicateLists) {
				// all of the predicate arguments have been created except that the one from the bag needs to replace the place-holder in the list
				ExpressionResult res = predicate.evaluate(evaluationContext, predicateArgumentList);
				if ( ! res.isOk()) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
							" Predicate error: " + res.getStatus().getStatusMessage()) );
				}
				if ( (Boolean)(res.getValue().getValue()) ) {
					return ER_TRUE;
				}
			}
			
			// if we get here then none of the combinations gave a TRUE result
			return ER_FALSE;
		
			
			
		case ALL_OF_ANY:
//TODO - it might be more efficient to extract all the attributes from the first bag and convert them to FunctionArguments just once, then use that list each time
			
			// get the element from the 2nd bag that we want to check all elements from the 1st bag against
			bagIterator2 = arguments.get(2).getBag().getAttributeValues();
			while (bagIterator2.hasNext()) {
				FunctionArgument predicateArgument2 = new FunctionArgumentAttributeValue(bagIterator2.next());
				boolean allMatch = true;
				
				// now look at every value of the first bag operating with the selected value from the 2nd
				bagIterator1 = arguments.get(1).getBag().getAttributeValues();
				while (bagIterator1.hasNext()) {

					predicateArguments.clear();
					predicateArguments.add(new FunctionArgumentAttributeValue(bagIterator1.next()));
					predicateArguments.add(predicateArgument2);
					
					ExpressionResult res = predicate.evaluate(evaluationContext, predicateArguments);
					if ( ! res.isOk()) {
						return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
								" Predicate error: " + res.getStatus().getStatusMessage()) );
					}
					if ( ! (Boolean)(res.getValue().getValue())) {
						allMatch = false;
						break;
					}
				}
				if (allMatch) {
					// wee found one value in bag2 that works (is TRUE) for all values in bag1
					return ER_TRUE;
				}
				// this value from bag2 did not work, so get the next one
			}
			
			// no value in bag2 worked for all values of bag1
			return ER_FALSE;

			
			
		case ANY_OF_ALL:
//TODO - it might be more efficient to extract all the attributes from the 2nd bag and convert them to FunctionArguments just once, then use that list each time
			
			// get the element from the 1st bag that we want to check all elements from the 1st bag against
			bagIterator1 = arguments.get(1).getBag().getAttributeValues();
			while (bagIterator1.hasNext()) {
				FunctionArgument predicateArgument1 = new FunctionArgumentAttributeValue(bagIterator1.next());
				boolean allMatch = true;
				
				// now look at every value of the 2nd bag operating with the selected value from the first
				bagIterator2 = arguments.get(2).getBag().getAttributeValues();
				while (bagIterator2.hasNext()) {
					predicateArguments.clear();
					predicateArguments.add(predicateArgument1);
					predicateArguments.add(new FunctionArgumentAttributeValue(bagIterator2.next()));
					
					ExpressionResult res = predicate.evaluate(evaluationContext, predicateArguments);
					if ( ! res.isOk()) {
						return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
								" Predicate error: " + res.getStatus().getStatusMessage()) );
					}
					if ( ! (Boolean)(res.getValue().getValue())) {
						allMatch = false;
						break;
					}
				}
				if (allMatch) {
					// wee found one value in bag1 that works (is TRUE) for all values in bag2
					return ER_TRUE;
				}
				// this value from bag1 did not work, so get the next one
			}
			
			// no value in bag1 worked for all values of bag2
			return ER_FALSE;
			
			
			
		case ALL_OF_ALL:
//TODO - it might be more efficient to extract all the attributes from the 2nd bag and convert them to FunctionArguments just once, then use that list each time

			// get the element from the 1st bag that we want to check all elements from the 1st bag against
			bagIterator1 = arguments.get(1).getBag().getAttributeValues();
			while (bagIterator1.hasNext()) {
				FunctionArgument predicateArgument1 = new FunctionArgumentAttributeValue(bagIterator1.next());

				// now look at every value of the 2nd bag operating with the selected value from the first
				bagIterator2 = arguments.get(2).getBag().getAttributeValues();
				while (bagIterator2.hasNext()) {
					predicateArguments.clear();
					predicateArguments.add(predicateArgument1);
					predicateArguments.add(new FunctionArgumentAttributeValue(bagIterator2.next()));

					ExpressionResult res = predicate.evaluate(evaluationContext, predicateArguments);
					if ( ! res.isOk()) {
						return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
								" Predicate error: " + res.getStatus().getStatusMessage()) );
					}

					if ( ! (Boolean)(res.getValue().getValue())) {
						return ER_FALSE;
					}
				}
				// this value did not fail, so try the next
			}
			
			// everything in bag1 worked (was true) for everything in bag 2
			return ER_TRUE;
			
			
			
		case MAP:
			// Copy the primitive arguments to the list for passing to the predicate,
			// putting a place-holder in for the value from the (single) bag
			for (int i = 1; i < arguments.size(); i++) {
				predicateArguments.add(arguments.get(i));
				if (arguments.get(i).isBag()) {
					if (indexOfBagInOriginalArgs == -1) {
						indexOfBagInOriginalArgs = i ;
					} else {
						// bag already found - we should have only one
						return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
								" must have only 1 bag; found one at index " + indexOfBagInOriginalArgs + " and another at " + i) );
					}
				}
			}
			
			Bag outputBag = new Bag();
			
			// get each primitive value in turn
			bagIterator1 = arguments.get(indexOfBagInOriginalArgs).getBag().getAttributeValues();
			while (bagIterator1.hasNext()) {
				// all of the predicate arguments have been created except that the one from the bag needs to replace the place-holder in the list
				predicateArguments.set(indexOfBagInOriginalArgs - 1, new FunctionArgumentAttributeValue(bagIterator1.next()));
				ExpressionResult res = predicate.evaluate(evaluationContext, predicateArguments);
				if ( ! res.isOk()) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
							" Predicate error: " + res.getStatus().getStatusMessage()) );
				}
				if (res.isBag()) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + 
							" Cannot put bag inside bag; predicate was '" + predicate.getId() + "'"));
				}
				outputBag.add(res.getValue());
			}
			
			
			return ExpressionResult.newBag(outputBag);
			
		}
	
		// all cases should have been covered by above - should never get here
		return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Could not evaluate Higher-Order Bag function " + operation));

	}


	
	
	
	
	
	
	
	
	
	
	/**
	 * Performs the depth-first walk to generate argument lists.  Needed by any-of-any because of the variable number of bags it might get.
	 * 
	 * This code was salvaged from the R2 version of the product and adjusted to fit the new way of doing business.
	 * 
	 * @param argListInProgress the current argument list being generated in this pass
	 * @param valueList the list of expression result values 
	 * @param nPosition the position within the expression result values to use to append to the base argument list
	 * @param listArgLists the <code>List</code> where final argument lists are appended
	 */
	private static void appendCrossProduct(List<FunctionArgument> argListInProgress, List<FunctionArgument> valueList, int nPosition, List<List<FunctionArgument>> listArgLists) {
		/*
		 * Have we hit a leaf?
		 */
		if (nPosition >= valueList.size()) {
			List<FunctionArgument>	copy	= new ArrayList<>();
			copy.addAll(argListInProgress);
			listArgLists.add(copy);
			return;
		}
		
		/*
		 * Check to see if the value at the current position is a primitive or a bag
		 */
		FunctionArgument	FunctionArgument	= valueList.get(nPosition);
		if (FunctionArgument.isBag() && FunctionArgument.getBag().getAttributeValues() != null && FunctionArgument.getBag().size() > 0) {
			Iterator<AttributeValue<?>>	iterBagValues	= FunctionArgument.getBag().getAttributeValues();
			while (iterBagValues.hasNext()) {
				AttributeValue<?>	attributeValue	= iterBagValues.next();
				FunctionArgument	functionArgument	= new FunctionArgumentAttributeValue(attributeValue);
				argListInProgress.add(functionArgument);
				appendCrossProduct(argListInProgress, valueList, nPosition+1, listArgLists);
				argListInProgress.remove(argListInProgress.size()-1);
			}
		} else {
			/*
			 * This is a simple value, so we can just append to the argListInProgress and continue the recursion
			 */
			argListInProgress.add(FunctionArgument);
			appendCrossProduct(argListInProgress, valueList, nPosition+1, listArgLists);
			argListInProgress.remove(argListInProgress.size()-1);
		}
	}
	
	
	
	
	
	
	

}
