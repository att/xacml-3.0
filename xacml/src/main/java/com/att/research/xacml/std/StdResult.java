/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.Collection;

import com.att.research.xacml.api.Advice;
import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.IdReference;
import com.att.research.xacml.api.Obligation;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.Result} interface.
 * 
 * @author Christopher A. Rath
 * @version $Revision$
 */
public class StdResult extends Wrapper<Result> implements Result {
	/**
	 * Creates an immutable <code>StdResult</code> by wrapping another {@link com.att.research.xacml.api.Result}.  By creating
	 * this wrapper, the caller is stating they will not modify the wrapped <code>Result</code> any further.
	 * 
	 * @param result the <code>Result</code> to wrap.
	 */
	public StdResult(Result result) {
		super(result);
	}
	
	/**
	 * Creates an immutable <code>StdResult</code> with the given {@link com.att.research.xacml.api.Decision} and {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param decision the <code>Decision</code> for the new <code>StdResult</code>
	 * @param status the <code>Status</code> for the new <code>StdResult</code>
	 */
	public StdResult(Decision decision, Status status) {
		this(new StdMutableResult(decision, status));
	}
	
	/**
	 * Creates an immutable <code>StdResult</code> with the given {@link com.att.research.xacml.api.Decision} and an OK {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param decision the <code>Decision</code> for the new <code>StdResult</code>
	 */
	public StdResult(Decision decision) {
		this(new StdMutableResult(decision));
	}
	
	/**
	 * Creates an immutable <code>StdResult</code> with an <code>INDETERMINATE</code> {@link com.att.research.xacml.api.Decision} and
	 * the given {@link com.att.research.xacml.api.Status}.
	 * 
	 * @param status the <code>Status</code> for the new <code>StdResult</code>
	 */
	public StdResult(Status status) {
		this(new StdMutableResult(status));
	}
	
	/**
	 * Creates an immutable <code>StdResult</code> with the given {@link com.att.research.xacml.api.Decision} and the given set of <code>Collection</code>s
	 * with the details of the result.
	 * 
	 * @param decisionIn the <code>Decision</code> for the new <code>StdMutableResult</code>
	 * @param obligationsIn a <code>Collection</code> of {@link com.att.research.xacml.api.Obligation}s for the new <code>StdMutableResult</code>
	 * @param adviceIn a <code>Collection</code> of {@link com.att.research.xacml.api.Advice} objects for the new <code>StdMutableResult</code>
	 * @param attributesIn a <code>Collection</code> of {@link com.att.research.xacml.api.AttributeCategory} objects for the new <code>StdMutableResult</code>
	 * @param policyIdentifiersIn a <code>Collection</code> of {@link com.att.research.xacml.api.IdReference} objects for the Policy identifiers
	 * @param policySetIdentifiersIn a <code>Collection</code> of {@link com.att.research.xacml.api.IdReference} objects for the PolicySet identifiers
	 */
	public StdResult(Decision decisionIn, 
			Collection<Obligation> obligationsIn, 
			Collection<Advice> adviceIn, 
			Collection<AttributeCategory> attributesIn, 
			Collection<IdReference> policyIdentifiersIn, 
			Collection<IdReference> policySetIdentifiersIn) {
		this(new StdMutableResult(decisionIn, obligationsIn, adviceIn, attributesIn, policyIdentifiersIn, policySetIdentifiersIn));
	}
	
	@Override
	public Decision getDecision() {
		return this.getWrappedObject().getDecision();
	}

	@Override
	public Status getStatus() {
		return this.getWrappedObject().getStatus();
	}

	@Override
	public Collection<Obligation> getObligations() {
		return this.getWrappedObject().getObligations();
	}

	@Override
	public Collection<Advice> getAssociatedAdvice() {
		return this.getWrappedObject().getAssociatedAdvice();
	}

	@Override
	public Collection<AttributeCategory> getAttributes() {
		return this.getWrappedObject().getAttributes();
	}

	@Override
	public Collection<IdReference> getPolicyIdentifiers() {
		return this.getWrappedObject().getPolicyIdentifiers();
	}

	@Override
	public Collection<IdReference> getPolicySetIdentifiers() {
		return this.getWrappedObject().getPolicySetIdentifiers();
	}

}
