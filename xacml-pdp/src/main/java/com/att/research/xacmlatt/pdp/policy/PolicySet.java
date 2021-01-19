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

import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.StatusCode;
import com.att.research.xacml.api.trace.Traceable;
import com.att.research.xacml.std.StdStatus;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.trace.StdTraceEvent;
import com.att.research.xacml.util.StringUtils;
import com.att.research.xacmlatt.pdp.eval.EvaluationContext;
import com.att.research.xacmlatt.pdp.eval.EvaluationException;
import com.att.research.xacmlatt.pdp.eval.EvaluationResult;
import com.att.research.xacmlatt.pdp.eval.MatchResult;

/**
 * PolicySet extends {@link com.att.research.xacmlatt.pdp.policy.PolicyDef} to represent a XACML PolicySet element.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public class PolicySet extends PolicyDef {
	private TargetedCombinerParameterMap<Identifier,PolicySetChild>		policyCombinerParameters	= new TargetedCombinerParameterMap<>();
	private List<PolicySetChild>										children;
	private List<CombiningElement<PolicySetChild>>						combiningPolicies;
	private CombiningAlgorithm<PolicySetChild>							combiningAlgorithm;
	
	private void ensureChildren() {
		if (this.children == null) {
			this.children	= new ArrayList<>();
		}
	}
	
	/**
	 * Performs lazy evaluation of the combining parameters from this <code>Policy</code>.
	 * 
	 * @return the <code>List</code> of <code>CombiningElement</code>s for all of the <code>Rule</code>s
	 */
	protected List<CombiningElement<PolicySetChild>> getCombiningPolicies() {
		if (this.combiningPolicies == null) {
			this.combiningPolicies			= new ArrayList<>();
			Iterator<PolicySetChild> iterPolicies	= this.getChildren();
			if (iterPolicies != null) {
				while (iterPolicies.hasNext()) {
					PolicySetChild policySetChild	= iterPolicies.next();
					this.combiningPolicies.add(new CombiningElement<>(policySetChild, this.policyCombinerParameters.getCombinerParameters(policySetChild)));
				}
			}
		}
		return this.combiningPolicies;
	}
	
	@Override
	protected boolean validateComponent() {
		if (super.validateComponent()) {
			if (this.getPolicyCombiningAlgorithm() == null) {
				this.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, "Missing policy combining algorithm");
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public PolicySet(StatusCode statusCodeIn, String statusMessageIn) {
		super(statusCodeIn, statusMessageIn);
	}

	public PolicySet(StatusCode statusCodeIn) {
		super(statusCodeIn);
	}
	
	public PolicySet(PolicySet policySetParent) {
		super(policySetParent);
	}

	public PolicySet() {
	}
	
	/**
	 * Gets an <code>Iterator</code> over the {@link com.att.research.xacmlatt.pdp.policy.TargetedCombinerParameter}s 
	 * for {@link com.att.research.xacmlatt.pdp.policy.Policy} elements in this
	 * <code>PolicySet</code>.
	 * 
	 * @return an <code>Iterator</code> over the <code>TargetedCombinerParameter</code>s for <code>Policy</code> elements in this
	 * <code>PolicySet</code>.
	 */
	public Iterator<TargetedCombinerParameter<Identifier,PolicySetChild>> getPolicyCombinerParameters() {
		return this.policyCombinerParameters.getTargetedCombinerParameters();
	}

	/**
	 * Sets the Policy combiner parameters for this <code>PolicySet</code> from the contents of the given <code>Collection</code>
	 * of <code>TargetedCombinerParameter</code>s.
	 * 
	 * @param policyCombinerParametersIn the <code>Collection</code> of <code>TargetedCombinerParameter</code>s.
	 */
	public void setPolicyCombinerParameters(Collection<TargetedCombinerParameter<Identifier,PolicySetChild>> policyCombinerParametersIn) {
		this.policyCombinerParameters.setCombinerParameters(policyCombinerParametersIn);
	}
	
	public void addPolicyCombinerParameter(TargetedCombinerParameter<Identifier,PolicySetChild> policyCombinerParameter) {
		this.policyCombinerParameters.addCombinerParameter(policyCombinerParameter);
	}
	
	public void addPolicyCombinerParameters(Collection<TargetedCombinerParameter<Identifier,PolicySetChild>> policyCombinerParametersIn) {
		this.policyCombinerParameters.addCombinerParameters(policyCombinerParametersIn);
	}
	
	/**
	 * Gets an <code>Iterator</code> over the <code>PolicySetChild</code> children of this <code>PolicySet</code>.
	 * 
	 * @return an <code>Iterator</code> over the <code>PolicySetChild</code> children of this <code>PolicySet</code> or null if there are none.
	 */
	public Iterator<PolicySetChild> getChildren() {
		return (this.children == null ? null : this.children.iterator());
	}
	
	public void setChildren(Collection<PolicySetChild> policySetChildren) {
		this.children	= null;
		if (policySetChildren != null) {
			this.addChildren(policySetChildren);
		}
	}
	
	public void addChild(PolicySetChild policySetChild) {
		this.ensureChildren();
		this.children.add(policySetChild);
	}
	
	public void addChildren(Collection<PolicySetChild> policySetChildren) {
		this.ensureChildren();
		this.children.addAll(policySetChildren);
	}
	
	/**
	 * Gets the {@link com.att.research.xacmlatt.pdp.policy.CombiningAlgorithm} for <code>PolicySetChild</code> children for this <code>PolicySet</code>.
	 * 
	 * @return the <code>CombiningAlgorithm</code> for <code>PolicySetChild</code> children for this <code>PolicySet</code>.
	 */
	public CombiningAlgorithm<PolicySetChild> getPolicyCombiningAlgorithm() {
		return this.combiningAlgorithm;
	}
	
	public void setPolicyCombiningAlgorithm(CombiningAlgorithm<PolicySetChild> combiningAlgorithmIn) {
		this.combiningAlgorithm	= combiningAlgorithmIn;
	}

	@Override
	public EvaluationResult evaluate(EvaluationContext evaluationContext) throws EvaluationException {
		/*
		 * First check to see if we are valid.  If not, return an error status immediately
		 */
		if (evaluationContext.isTracing()) {
			evaluationContext.trace(new StdTraceEvent<>("PolicySet", this, null));
		}
		if (!this.validate()) {
			return new EvaluationResult(new StdStatus(this.getStatusCode(), this.getStatusMessage()));
		}
		
		/*
		 * See if we match
		 */
		MatchResult thisMatchResult	= this.match(evaluationContext);
		assert(thisMatchResult != null);
		if (evaluationContext.isTracing()) {
			evaluationContext.trace(new StdTraceEvent<>("Match", this, thisMatchResult));
		}
		switch(thisMatchResult.getMatchCode()) {
		case INDETERMINATE:
			return new EvaluationResult(Decision.INDETERMINATE, thisMatchResult.getStatus());
		case MATCH:
			break;
		case NOMATCH:
			return new EvaluationResult(Decision.NOTAPPLICABLE);
		}
		
		/*
		 * Get the combining elements
		 */
		List<CombiningElement<PolicySetChild>> listCombiningElements	= this.getCombiningPolicies();
		assert(listCombiningElements != null);
		
		/*
		 * Run the PolicyCombiningAlgorithm
		 */
		assert(this.getPolicyCombiningAlgorithm() != null);
		EvaluationResult evaluationResultCombined	= this.getPolicyCombiningAlgorithm().combine(evaluationContext, listCombiningElements, getCombinerParameterList());
		assert(evaluationResultCombined != null);
		
		if (evaluationResultCombined.getDecision() == Decision.DENY || evaluationResultCombined.getDecision() == Decision.PERMIT) {
			this.updateResult(evaluationResultCombined, evaluationContext);
			
			/*
			 * Add my id to the policy set identifiers
			 */
			if (evaluationContext.getRequest().getReturnPolicyIdList()) {
				evaluationResultCombined.addPolicySetIdentifier(this.getIdReference());
			}
		}
		if (evaluationContext.isTracing()) {
			evaluationContext.trace(new StdTraceEvent<>("Result", this, evaluationResultCombined));
		}
		return evaluationResultCombined;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder	= new StringBuilder("{");
		stringBuilder.append("super=");
		stringBuilder.append(super.toString());
		
		String iteratorToDump;
		if ((iteratorToDump = StringUtils.toString(this.getPolicyCombinerParameters())) != null) {
			stringBuilder.append(",policyCombinerParameters=");
			stringBuilder.append(iteratorToDump);
		}
		if ((iteratorToDump = StringUtils.toString(this.getChildren())) != null) {
			stringBuilder.append(",children=");
			stringBuilder.append(iteratorToDump);
		}
		Object objectToDump;
		if ((objectToDump = this.getPolicyCombiningAlgorithm()) != null) {
			stringBuilder.append(",policyCombiningAlgorithm=");
			stringBuilder.append(objectToDump.toString());
		}
		
		stringBuilder.append('}');
		return stringBuilder.toString();
	}

	@Override
	public String getTraceId() {
		return this.getIdentifier().stringValue();
	}

	@Override
	public Traceable getCause() {
		return null;
	}
}
