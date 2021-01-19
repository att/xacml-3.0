/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.eval;

import com.att.research.xacml.api.IdReferenceMatch;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.api.trace.TraceEngine;
import com.att.research.xacmlatt.pdp.policy.Policy;
import com.att.research.xacmlatt.pdp.policy.PolicyDef;
import com.att.research.xacmlatt.pdp.policy.PolicyFinderResult;
import com.att.research.xacmlatt.pdp.policy.PolicySet;

/**
 * EvaluationContext provides the interface that the PDP uses to evaluate its set of Policies and PolicySets against
 * a {@link com.att.research.xacml.api.Request}.
 * 
 * @author car
 * @version $Revision: 1.2 $
 */
public interface EvaluationContext extends PIPFinder, TraceEngine {
	/**
	 * Gets the original <code>Request</code> provided to the <code>ATTPDPEngine</code>'s <code>decide</code> method.
	 * 
	 * @return the <code>Request</code> provided to the <code>ATTPDPEngine</code>'s <code>decide</code> method.
	 */
	public Request getRequest();
	
	/**
	 * Gets the root {@link com.att.research.xacmlatt.pdp.policy.PolicyDef} from the policy store
	 * configured by the particular implementation of the <code>PolicyFinderFactory</code> class.
	 * 
	 * @return a <code>PolicyFinderResult</code> with the root <code>PolicyDef</code>
	 */
	public abstract PolicyFinderResult<PolicyDef> getRootPolicyDef();
	
	/**
	 * Gets the {@link com.att.research.xacmlatt.pdp.policy.Policy} that matches the given {@link com.att.research.xacml.api.IdReferenceMatch}.
	 * 
	 * @param idReferenceMatch the <code>IdReferenceMatch</code> to search for
	 * @return a <code>PolicyFinderResult</code> with the <code>Policy</code> matching the given <code>IdReferenceMatch</code>
	 */
	public abstract PolicyFinderResult<Policy> getPolicy(IdReferenceMatch idReferenceMatch);
	
	/**
	 * Gets the {@link com.att.research.xacmlatt.pdp.policy.PolicySet} that matches the given {@link com.att.research.xacml.api.IdReferenceMatch}.
	 * 
	 * @param idReferenceMatch the <code>IdReferenceMatch</code> to search for
	 * @return a <code>PolicyFinderResult</code> with the <code>PolicySet</code> matching the given <code>IdReferenceMatch</code>.
	 */
	public abstract PolicyFinderResult<PolicySet> getPolicySet(IdReferenceMatch idReferenceMatch);
	
	/**
	 * Gets the {@link com.att.research.xacml.api.pip.PIPResponse} containing {@link com.att.research.xacml.api.Attribute}s that
	 * match the given {@link com.att.research.xacml.api.pip.PIPRequest} from this <code>EvaluationContext</code>.
	 * 
	 * @param pipRequest the <code>PIPRequest</code> specifying which <code>Attribute</code>s to retrieve
	 * @return the <code>PIPResponse</code> containing the {@link com.att.research.xacml.api.Status} and <code>Attribute</code>s
	 * @throws PIPException PIP exception
	 */
	public PIPResponse getAttributes(PIPRequest pipRequest) throws PIPException;
}
