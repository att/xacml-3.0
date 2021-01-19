/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacmlatt.pdp.eval;

import java.util.Collection;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.IdReference;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.std.StdMutableResult;

/**
 * EvaluationResult extends {@link com.att.research.xacml.std.StdMutableResult} with methods useful within a PDP implementation
 * 
 * @author car
 * @version $Revision: 1.1 $
 */
public class EvaluationResult extends StdMutableResult {
	public EvaluationResult() {
		super();
	}
	
	public EvaluationResult(Decision decisionIn, Status statusIn) {
		super(decisionIn, statusIn);
	}
	
	public EvaluationResult(Status statusIn) {
		super(statusIn);
	}
	
	public EvaluationResult(Decision decisionIn) {
		super(decisionIn);
	}
	
	public EvaluationResult(Decision decisionIn, 
			Collection<Obligation> obligationsIn, 
			Collection<Advice> adviceIn, 
			Collection<AttributeCategory> attributesIn, 
			Collection<IdReference> policyIdentifiersIn, 
			Collection<IdReference> policySetIdentifiersIn) {
		super(decisionIn, obligationsIn, adviceIn, attributesIn, policyIdentifiersIn, policySetIdentifiersIn);
	}
	
	/**
	 * Creates an <code>EvaluationResult</code> generally from a {@link com.att.research.xacmlatt.pdp.policy.Rule} <code>evaluation</code>
	 * call.
	 * 
	 * @param decisionIn the <code>Decision</code>
	 * @param obligationsIn the <code>Collection</code> of <code>Obligation</code>s
	 * @param adviceIn the <code>Collection</code> of <code>Advice</code> objects
	 */
	public EvaluationResult(Decision decisionIn, Collection<Obligation> obligationsIn, Collection<Advice> adviceIn) {
		super(decisionIn, obligationsIn, adviceIn, null, null, null);
	}
	
	public void merge(EvaluationResult evaluationResult) {
		if (this.getStatus() == null) {
			this.setStatus(evaluationResult.getStatus());
		} else {
			this.getStatus().merge(evaluationResult.getStatus());
		}
		this.addObligations(evaluationResult.getObligations());
		this.addAdvice(evaluationResult.getAssociatedAdvice());
		this.addAttributeCategories(evaluationResult.getAttributes());
		this.addPolicyIdentifiers(evaluationResult.getPolicyIdentifiers());
		this.addPolicySetIdentifiers(evaluationResult.getPolicySetIdentifiers());
	}
}
