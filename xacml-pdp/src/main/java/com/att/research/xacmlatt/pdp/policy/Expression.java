/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.StatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;

/**
 * Expression extends {@link com.att.research.xacmlatt.pdp.policy.PolicyComponent} to represent a XACML ExpressionType element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public abstract class Expression extends PolicyComponent {

	public Expression(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public Expression(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}

	public Expression() {
	}
	
	/**
	 * Evaluates this <code>Expression</code> in the given {@link com.att.research.xacmlatt.pdp.eval.EvaluationContext}.
	 * 
	 * @param evaluationContext the <code>EvaluationContext</code> in which to evaluate this <code>Expression</code>
	 * @param policyDefaults the {@link com.att.research.xacmlatt.pdp.policy.PolicyDefaults} to use in evaluating this <code>Expression</code>
	 * @return a {@link com.att.research.xacmlatt.pdp.policy.ExpressionResult}
	 * @throws EvaluationException EvaluationException
	 */
	public abstract ExpressionResult evaluate(EvaluationContext evaluationContext, PolicyDefaults policyDefaults) throws EvaluationException;
}
