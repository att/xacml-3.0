/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.IdReferenceMatch;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;
import com.att.research.xacmlatt.pdp.eval.MatchResult;

/**
 * PolicyIdReferenceBase extends {@link com.att.research.xacmlatt.pdp.policy.PolicySetChild} to implement a XACML PolicyIdReference element.
 * 
 * @author car
 * @version $Revision: 1.1 $
 * 
 * @param <T> PolicyDef
 */
public abstract class PolicyIdReferenceBase<T extends PolicyDef> extends PolicySetChild {
	private IdReferenceMatch	idReferenceMatch;
	private T					referencee;
	
	@Override
	protected boolean validateComponent() {
		if (super.validateComponent()) {
			if (this.getIdReferenceMatch() == null) {
				this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing reference id");
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * If the <code>T</code> referencee has not been set, this method will try and find it
	 * in the given <code>EvaluationContext</code> and return it.
	 * 
	 * @param evaluationContext the <code>EvaluationContext</code> to search for the referencee
	 * @return the <code>T</code> referencee if found, else null
	 * @throws EvaluationException if there is an error attempting to locate the referenced <code>T</code>.
	 */
	protected abstract T ensureReferencee(EvaluationContext evaluationContext) throws EvaluationException;
	
	public PolicyIdReferenceBase(PolicySet policySetParent, StatusCode statusCodeIn, String statusMessageIn) {
		super(policySetParent, statusCodeIn, statusMessageIn);
	}
	
	public PolicyIdReferenceBase(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public PolicyIdReferenceBase(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}
	
	public PolicyIdReferenceBase(PolicySet policySetParent) {
		super(policySetParent);
	}

	public PolicyIdReferenceBase() {
	}
	
	/**
	 * Gets the {@link com.att.research.xacml.api.IdReferenceMatch} for this <code>PolicyIdReferenceBase</code>.
	 * 
	 * @return the <code>IdReferenceMatch</code> for this <code>PolicyIdReference</code>.
	 */
	public IdReferenceMatch getIdReferenceMatch() {
		return this.idReferenceMatch;
	}
	
	public void setIdReferenceMatch(IdReferenceMatch idReferenceMatchIn) {
		this.idReferenceMatch	= idReferenceMatchIn;
	}
	
	/**
	 * Sets the <code>PolicyDef</code> object referred to by this <code>PolicyIdReferenceBase</code>.
	 * 
	 * @return the <code>PolicyDef</code> object referred to by this <code>PolicyIdReferenceBase</code>
	 */
	public T getReferencee() {
		return this.referencee;
	}
	
	public void setReferencee(T referenceeIn) {
		this.referencee	= referenceeIn;
	}
	
	@Override
	public EvaluationResult evaluate(EvaluationContext evaluationContext) throws EvaluationException {
		T thisReferencee	= this.ensureReferencee(evaluationContext);
		if (thisReferencee == null) {
			return new EvaluationResult(Decision.INDETERMINATE, new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Could not find referencee for " + this.getIdReferenceMatch().toString()));
		} else {
			return thisReferencee.evaluate(evaluationContext);
		}
	}

	@Override
	public MatchResult match(EvaluationContext evaluationContext) throws EvaluationException {
		T thisReferencee	= this.ensureReferencee(evaluationContext);
		if (thisReferencee == null) {
			return new MatchResult(MatchResult.MatchCode.INDETERMINATE, new StdStatus(StdStatusCode.STATUS_CODE_PROCESSING_ERROR, "Could not find referencee for " + this.getIdReferenceMatch().toString()));
		} else {
			return thisReferencee.match(evaluationContext);
		}
	}

}
