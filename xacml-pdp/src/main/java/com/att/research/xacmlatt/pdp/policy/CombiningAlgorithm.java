/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.List;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacmlatt.pdp.eval.Evaluatable;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;

/**
 * CombiningAlgorithm is the interface for objects that implement XACML combining algorithms for rules, policies, and policy sets.
 * 
 * @author car
 * @version $Revision: 1.1 $
 *
 * @param <T> the type of object to be combined
 */
public interface CombiningAlgorithm<T extends Evaluatable> {
	/**
	 * Gets the {@link com.att.research.xacml.api.Identifier} for this <code>CombiningAlgorithm</code>.
	 * 
	 * @return the <code>Identifier</code> for this <code>CombiningAlgorithm</code>
	 */
	public Identifier getId();
	
	/**
	 * Evaluates as many of the <code>CombiningElement</code>s supplied with the given <code>CombinerParameter</code>s based on
	 * the particular combining algorithm and combines their <code>EvaluationResult</code>s into a single <code>EvaluationResult</code>.
	 * 
	 * @param evaluationContext the <code>EvaluationContext</code> in which to evaluate each of the <code>CombiningElement</code>s
	 * @param elements the <code>List</code> of <code>CombiningElement</code>s to evaluate
	 * @param combinerParameters the <code>List</code> of <code>CombinerParameter</code>s to apply to the combining algorithm
	 * @return the combined <code>EvaluationResult</code>
	 * @throws EvaluationException if there is an error in the <code>evaluate</code> method of any of the <code>CombiningElement</code>s
	 */
	public EvaluationResult combine(EvaluationContext evaluationContext, List<CombiningElement<T>> elements, List<CombinerParameter> combinerParameters) throws EvaluationException;
}
