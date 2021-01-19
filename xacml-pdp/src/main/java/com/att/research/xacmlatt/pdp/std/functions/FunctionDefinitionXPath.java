/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.std.functions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPathExpression;

import org.w3c.dom.NodeList;

import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.api.XACML;
import com.att.research.xacml.std.StdAttributeValue;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.XPathExpressionWrapper;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.policy.ExpressionResult;
import com.att.research.xacmlatt.pdp.policy.FunctionArgument;


/**
 * FunctionDefinitionXPath extends {@link com.att.research.xacmlatt.pdp.std.functions.FunctionDefinitionHomogeneousSimple} to
 * implement the XACML XPath predicates as functions taking one or two <code>XPathExpression</code> arguments and returning 
 * either an <code>Integer</code> or a <code>Boolean</code>.
 * 
 * XACML version 1.0 and 2.0 used <code>String</code> data type as input.
 * We do NOT support those functions because of ambiguity in the meaning of those Strings.
 * The root of the XPath changed from the Request level in 2.0 to the Content level in 3.0.
 * Also the 2.0 Requests contain only one Content, located in the resources category, while 3.0 allows Requests to contain Content in multiple categories.
 * 
 * In the first implementation of XACML we had separate files for each XACML Function.
 * This release combines multiple Functions in fewer files to minimize code duplication.
 * This file supports the following XACML codes:
 * 		xpath-node-count
 * 		xpath-node-equals
 * 		xpath-node-match
 * 
 * @author glenngriffin
 * @version $Revision: 1.1 $
 * 
 * @param <O> the java class for the data type of the function Output

 * 
 */
public class FunctionDefinitionXPath<O> extends FunctionDefinitionHomogeneousSimple<O, XPathExpressionWrapper> {
	
	/**
	 * List of string normalization operations.
	 * 
	 * @author glenngriffin
	 *
	 */
	public enum OPERATION {COUNT, EQUAL, MATCH }
	
	// operation to be used in this instance of the Arightmetic class
	private final OPERATION operation;
	
	
	// result variables used by all functions
	AttributeValue<String>	result;


	/**
	 * Constructor
	 * 
	 * @param idIn Identifier
	 * @param dataTypeIn  DataType in
	 * @param op operation
	 */
	public FunctionDefinitionXPath(Identifier idIn, DataType<O> dataTypeIn, OPERATION op) {
		super(idIn, dataTypeIn, DataTypes.DT_XPATHEXPRESSION, ( (op == OPERATION.COUNT) ? 1 : 2 ) );
		// save the operation and data type to be used in this instance
		operation = op;

	}

	@Override
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments) {

		List<NodeList> nodeListList = new ArrayList<>();

		List<XPathExpressionWrapper> convertedArguments	= new ArrayList<>();
		Status status				= this.validateArguments(arguments, convertedArguments);

		/*
		 * If the function arguments are not correct, just return an error status immediately
		 */
		if (!status.getStatusCode().equals(StdStatusCode.STATUS_CODE_OK)) {
			return ExpressionResult.newError(getFunctionStatus(status));
		}
		
		// check the evaluationContext and Request for null
		if (evaluationContext == null) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +
					" Got null EvaluationContext"));
		}
		if (evaluationContext.getRequest() == null) {
			return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +
					" Got null Request in EvaluationContext"));
		}
		
		
		// each argument is an XPath that needs to be evaluated against the Content part of some Category (specified in the argument)
		for (int i = 0; i < arguments.size(); i++) {
			FunctionArgument functionArgument = arguments.get(i);
			if (functionArgument.isBag()) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Got bag at index " + i));
			}
			AttributeValue<?> attributeValueFunctionArgument	= functionArgument.getValue();
			if (attributeValueFunctionArgument == null) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() + " Got null value at index " + i));
			}
			Identifier xpathCategory = attributeValueFunctionArgument.getXPathCategory();
			if (xpathCategory == null) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, this.getShortFunctionId() +
						" Got null Category at index " + i));
			}
			
			Iterator<RequestAttributes> it = evaluationContext.getRequest().getRequestAttributes(xpathCategory);
			if (it == null) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, this.getShortFunctionId() +
						" Got null Iterator at index " + i));
			}
			
			NodeList nodeList = null;

			while (it.hasNext()) {
				if (nodeList != null) {
					// the request has more than one Content entry for the same Category - error
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, this.getShortFunctionId() +
							" More than one Content section for id '" + xpathCategory + "'" ));
				}
				RequestAttributes requestAttributes = it.next();
				
				// if there is no Content section then we return either 0 or FALSE
				if (requestAttributes.getContentRoot() == null) {
					if (operation == OPERATION.COUNT){
						return ExpressionResult.newSingle(new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER, BigInteger.valueOf(0) ));
					} else {
						return ER_FALSE;
					}
				}

				try {
					XPathExpression xPathExpression = convertedArguments.get(i).getXpathExpressionWrapped();
					nodeList    = requestAttributes.getContentNodeListByXpathExpression(xPathExpression);
				} catch (Exception e) {
					return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, this.getShortFunctionId() +
							" XPath produces null result at '" + convertedArguments.get(i).getPath() + "' at index " + i ));
				}


			}
			
			if (nodeList == null) {
				return ExpressionResult.newError(new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, this.getShortFunctionId() +
						" XPathExpression returned null at index " + i));
			}
			
			// add this nodeList to the list of lists
			nodeListList.add(nodeList);
		}
		
		
		/*
		 * Now perform the requested operation.
		 */
		ExpressionResult expressionResult = null;
		
		switch (operation) {
		case COUNT:
			Integer listLength = Integer.valueOf((nodeListList.get(0).getLength()));
			expressionResult = ExpressionResult.newSingle(new StdAttributeValue<>(XACML.ID_DATATYPE_INTEGER,
					new BigInteger(listLength.toString()) ));
			return expressionResult;
			
			
		case EQUAL:
			// true if any node in first list equals any node in second set.
			// The spec says: "Two nodes are considered equal if they have the same identity."
			// we use the isSameNode method in Node to determine that.
			for (int index0 = 0; index0 < nodeListList.get(0).getLength(); index0++) {
				for (int index1 = 0; index1 < nodeListList.get(1).getLength(); index1++)  {
					if (nodeListList.get(0).item(index0).isSameNode(nodeListList.get(1).item(index1))) {
						return ER_TRUE;
					}
				}
			}
			// none from the first list found in the second
			return ER_FALSE;
			
			
		case MATCH:
			// this is looking to see if any of the nodes in the second set are children of (or equal to) the nodes in the first set
			// Call recursive check for that.
			expressionResult = nodeListMatch(nodeListList.get(0), nodeListList.get(1));
			return expressionResult;
		}
		
		expressionResult = ExpressionResult.newSingle(result);

		return expressionResult;
	}
	
	/**
	 * Recursive method checking to see if anything in list 2 equals anything in list 1 OR list 1's child nodes
	 * @param list1
	 * @param list2
	 * @return
	 */
	private ExpressionResult nodeListMatch(NodeList list1, NodeList list2) {
		// look for match with current contents of list 1
		for (int index1 = 0; index1 < list1.getLength(); index1++) {
			for (int index2 = 0; index2 < list2.getLength(); index2++)  {
				if (list1.item(index1).isSameNode(list2.item(index2))) {
					return ER_TRUE;
				}
			}
		}
		// look for match with children of list 1
		for (int index1 = 0; index1 < list1.getLength(); index1++) {
			if (nodeListMatch(list1.item(index1).getChildNodes(), list2) == ER_TRUE) {
				return ER_TRUE;
			}
			// this one had no children that matched, so check the next element in list1
		}
		
		// no match anywhere
		return ER_FALSE;
	}

}
