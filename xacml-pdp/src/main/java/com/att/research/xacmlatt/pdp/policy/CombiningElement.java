/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.att.research.xacmlatt.pdp.eval.Evaluatable;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;

/**
 * CombiningElement wraps an {@link com.att.research.xacmlatt.pdp.eval.Evaluatable} with a set of
 * {@link com.att.research.xacmlatt.pdp.policy.TargetedCombinerParameter}s for use with a 
 * {@link com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm} to get a combined {@link com.att.research.xacmlatt.pdp.eval.EvaluationResult}
 * 
 * @author car
 * @version $Revision: 1.1 $
 * 
 * @param <T> the java class extending <code>Evaluatable</code> of the objects to be combined
 */
public class CombiningElement<T extends Evaluatable> {
	private T										evaluatable;
	private List<CombinerParameter>	targetedCombinerParameters;
	
	/**
	 * Creates a new <code>CombiningElement</code> with the given <code>Evaluatable</code> and <code>List</code> of
	 * <code>TargetedCombinerParameter</code>.
	 * 
	 * @param evaluatableIn the <code>Evaluatable</code>
	 * @param targetedCombinerParametersIn the <code>List</code> of <code>TargetedCombinerParameter</code>s.
	 */
	public CombiningElement(T evaluatableIn, Collection<CombinerParameter> targetedCombinerParametersIn) {
		this.evaluatable	= evaluatableIn;
		if (targetedCombinerParametersIn != null) {
			this.targetedCombinerParameters	= new ArrayList<>();
			this.targetedCombinerParameters.addAll(targetedCombinerParametersIn);
		}
	}
	
	/**
	 * Gets the <code>Evaluatable</code> for this <code>CombiningElement</code>.
	 * 
	 * @return the <code>Evaluatable</code> for this <code>CombiningElement</code>
	 */
	public T getEvaluatable() {
		return this.evaluatable;
	}
	
	/**
	 * Gets an <code>Iterator</code> over the <code>TargetedCombinerParameters</code> for this
	 * <code>CombiningElement</code>.
	 * 
	 * @return an <code>Iterator</code> over the <code>TargetedCombinerParameters</code> for this <code>CombiningElement</code>
	 */
	public Iterator<CombinerParameter> getTargetedCombinerParameters() {
		return (this.targetedCombinerParameters == null ? null : this.targetedCombinerParameters.iterator());
	}
	
	/**
	 * Evaluates this <code>CombiningElement</code> in the given {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext}.
	 * 
	 * @param evaluationContext the <code>EvaluationContext</code>
	 * @return the {@link com.att.research.xacmlatt.pdp.eval.EvaluationResult} from the <code>Evaluatable</code>
	 * @throws EvaluationException if there is an error in the <code>evaluate</code> method of the <code>Evaluatable</code>
	 */
	public EvaluationResult evaluate(EvaluationContext evaluationContext) throws EvaluationException {
		return this.getEvaluatable().evaluate(evaluationContext);
	}

}
