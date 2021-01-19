/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.std;

import java.util.Collection;
import java.util.Iterator;

import com.att.research.xacml.api.AttributeCategory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.RequestAttributes;
import com.att.research.xacml.api.RequestDefaults;
import com.att.research.xacml.api.RequestReference;
import com.att.research.xacml.api.Status;
import com.att.research.xacml.util.Wrapper;

/**
 * Immutable implementation of the {@link com.att.research.xacml.api.Request} interface.
 * 
 * @author car
 * @version $Revison$
 */
public class StdRequest extends Wrapper<Request> implements Request {
	/**
	 * Creates a new <code>StdRequest</code> that is a copy of the given {@link com.att.research.xacml.api.Request}.
	 * 
	 * @param request the <code>Request</code> to copy
	 */
	public StdRequest(Request request) {
		super(request);
	}
	
	/**
	 * Creates a new <code>StdMutableRequest</code> with the given parameters.
	 * 
	 * @param statusIn the {@link com.att.research.xacml.api.Status} of the <code>StdMutableRequest</code> representing its validity
	 * @param requestDefaultsIn the {@link com.att.research.xacml.api.RequestDefaults} representing the XACML RequestDefaults
	 * @param returnPolicyIdListIn a boolean indicating whether XACML PolicyId and PolicySetIds should be returned with the Results
	 * @param combinedDecisionIn a boolean indicating whether multiple Decision Request Results should be combined into a single Result
	 * @param listRequestAttributes a <code>Collection</code> of {@link com.att.research.xacml.api.RequestAttributes} defining the parameters of the Request
	 * @param listRequestReferences a <code>Collection</code> of {@link com.att.research.xacml.api.RequestReference}s for multiple decision requests
	 */
	public StdRequest(Status statusIn, 
					  RequestDefaults requestDefaultsIn, 
					  boolean returnPolicyIdListIn, 
					  boolean combinedDecisionIn, 
					  Collection<RequestAttributes> listRequestAttributes, 
					  Collection<RequestReference> listRequestReferences) {
		this(new StdMutableRequest(statusIn, requestDefaultsIn, returnPolicyIdListIn, combinedDecisionIn, listRequestAttributes, listRequestReferences));
	}
	
	/**
	 * Creates a new <code>StdMutableRequest</code> with the given parameters and a default {@link com.att.research.xacml.api.Status} of OK.
	 * 
	 * @param requestDefaultsIn the {@link com.att.research.xacml.api.RequestDefaults} representing the XACML RequestDefaults
	 * @param returnPolicyIdListIn a boolean indicating whether XACML PolicyId and PolicySetIds should be returned with the Results
	 * @param combinedDecisionIn a boolean indicating whether multiple Decision Request Results should be combined into a single Result
	 * @param listRequestAttributes a <code>Collection</code> of {@link com.att.research.xacml.api.RequestAttributes} defining the parameters of the Request
	 * @param listRequestReferences a <code>Collection</code> of {@link com.att.research.xacml.api.RequestReference}s for multiple decision requests
	 */
	public StdRequest(RequestDefaults requestDefaultsIn, 
					  boolean returnPolicyIdListIn, 
					  boolean combinedDecisionIn, 
					  Collection<RequestAttributes> listRequestAttributes, 
					  Collection<RequestReference> listRequestReferences) {
		this(new StdMutableRequest(requestDefaultsIn, returnPolicyIdListIn, combinedDecisionIn, listRequestAttributes, listRequestReferences));
	}
	
	/**
	 * Creates a new <code>StdRequest</code> with the given {@link com.att.research.xacml.api.Status} and defaults for all other attributes.
	 * 
	 * @param statusIn the <code>Status</code> for the new <code>StdRequest</code>.
	 */
	public StdRequest(Status statusIn) {
		this(new StdMutableRequest(statusIn));
	}
	
	@Override
	public RequestDefaults getRequestDefaults() {
		return this.getWrappedObject().getRequestDefaults();
	}

	@Override
	public boolean getReturnPolicyIdList() {
		return this.getWrappedObject().getReturnPolicyIdList();
	}

	@Override
	public boolean getCombinedDecision() {
		return this.getWrappedObject().getCombinedDecision();
	}

	@Override
	public Collection<RequestAttributes> getRequestAttributes() {
		return this.getWrappedObject().getRequestAttributes();
	}
	
	@Override
	public Collection<AttributeCategory> getRequestAttributesIncludedInResult() {
		return this.getWrappedObject().getRequestAttributesIncludedInResult();
	}

	@Override
	public Iterator<RequestAttributes> getRequestAttributes(Identifier category) {
		return this.getWrappedObject().getRequestAttributes(category);
	}

	@Override
	public RequestAttributes getRequestAttributesByXmlId(String xmlId) {
		return this.getWrappedObject().getRequestAttributesByXmlId(xmlId);
	}

	@Override
	public Collection<RequestReference> getMultiRequests() {
		return this.getWrappedObject().getMultiRequests();
	}

	@Override
	public Status getStatus() {
		return this.getWrappedObject().getStatus();
	}

}
