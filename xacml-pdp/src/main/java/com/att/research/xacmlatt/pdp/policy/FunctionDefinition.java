/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.List;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;

/**
 * FunctionDefinition is the interface that objects representing XACML functions found in Match and Apply elements in Policies, PolicySets
 * and Rules.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public interface FunctionDefinition {
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for this <code>FunctionDefinition</code>.
	 * 
	 * @return the <code>Identifier</code> for this <code>FunctionDefinition</code>.
	 */
	public Identifier getId();
	
	/**
	 * Returns the <code>Identifier</code> for the data type returned by this function if <code>returnsBag()</code> is false or
	 * if this <code>FunctionDefinition</code> returns a bag containing a single data type.  Otherwise it returns null.
	 * 
	 * @return the <code>Identifier</code> for the XACML data type this <code>FunctionDefinition</code> returns
	 */
	public Identifier getDataTypeId();
	
	/**
	 * Determines if this <code>FunctionDefinition</code> returns a bag of values or a single value.
	 * 
	 * @return true if this <code>FunctionDefinition</code> returns a bag, else false
	 */
	public boolean returnsBag();
	
	/**
	 * Evaluates this <code>FunctionDefinition</code> on the given <code>List</code> of{@link com.att.research.xacmlatt.pdp.policy.FunctionArgument}s.
	 * 
	 * @param evaluationContext the {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext} to use in the evaluation
	 * @param arguments the <code>List</code> of <code>FunctionArgument</code>s for the evaluation
	 * @return an {@link com.att.research.xacmlatt.pdp.policy.ExpressionResult} with the results of the call
	 */
	public ExpressionResult evaluate(EvaluationContext evaluationContext, List<FunctionArgument> arguments);
}
