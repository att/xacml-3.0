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
 * PolicyIdReference extends {@link com.att.research.xacmlatt.pdp.policy.PolicyIdReferenceBase} for
 * {@link com.att.research.xacmlatt.pdp.policy.Policy} objects with an implementation of the <code>ensureReferencee</code>
 * method to find a <code>Policy</code>.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class PolicyIdReference extends PolicyIdReferenceBase<Policy> {

	public PolicyIdReference(PolicySet policySetParent, StatusCode statusCodeIn, String statusMessageIn) {
		super(policySetParent, statusCodeIn, statusMessageIn);
	}
	
	public PolicyIdReference(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public PolicyIdReference(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}
	
	public PolicyIdReference(PolicySet policySetParent) {
		super(policySetParent);
	}

	public PolicyIdReference() {
	}

	@Override
	protected Policy ensureReferencee(EvaluationContext evaluationContext) throws EvaluationException {
		if (this.getReferencee() == null) {
			PolicyFinderResult<Policy> policyFactoryResult	= evaluationContext.getPolicy(this.getIdReferenceMatch());
			if (policyFactoryResult.getStatus() == null || policyFactoryResult.getStatus().isOk()) {
				this.setReferencee(policyFactoryResult.getPolicyDef());
			}
		}
		return this.getReferencee();
	}

}
