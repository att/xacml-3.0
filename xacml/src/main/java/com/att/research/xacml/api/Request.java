/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api;

import java.util.Collection;
import java.util.Iterator;

/**
 * Provides the API for objects that represent XACML Request elements.  Requests are used to specify the contents of a XACML decision request.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public interface Request {

	/**
	 * Gets the {@link com.att.research.xacml.api.RequestDefaults} representing the XACML RequestDefaults for this <code>Request</code>.
	 * 
	 * @return the <code>RequestDefaults</code> representing the XACML RequestDefaults for this <code>Request</code>.
	 */
	public RequestDefaults getRequestDefaults();
	
	/**
	 * Returns true if the list of XACML PolicyIds should be returned for this <code>Request</code>.
	 * 
	 * @return true if XACML PolicyIds should be returned, otherwise false
	 */
	public boolean getReturnPolicyIdList();
	
	/**
	 * Returns true if the results from multiple individual decisions for this <code>Request</code> should
	 * be combined into a single XACML Result.
	 * 
	 * @return true if multiple results should be combined, otherwise false.
	 */
	public boolean getCombinedDecision();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.RequestAttributes} representing XACML Attributes elements for
	 * this <code>Request</code>.  The <code>Collection</code> should not be modified.  Implementations are free to use unmodifiable lists
	 * to enforce this.
	 * 
	 * @return the <code>Collection</code> of <code>RequestAttributes</code> representing XACML Attributes elements for
	 * this <code>Request</code>.
	 */
	public Collection<RequestAttributes> getRequestAttributes();
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.RequestAttributes} representing XACML Attributes elements for
	 * this <code>Request</code> that contain {@link com.att.research.xacml.api.Attribute}s where <code>getIncludeInResults</code> is true.
	 * 
	 * @return a <code>Collection</code> of <code>RequestAttributes</code> containing one or more <code>Attribute</code>s to include in results.
	 */
	public Collection<AttributeCategory> getRequestAttributesIncludedInResult();
	
	/**
	 * Gets an <code>Iterator</code> over all of the {@link com.att.research.xacml.api.RequestAttributes} objects
	 * found in this <code>Request</code> with the given {@link com.att.research.xacml.api.Identifier} representing a XACML Category.
	 * 
	 * @param category the <code>Identifier</code> representing the XACML Category of the <code>ReqestAttributes</code> to retrieve.
	 * @return an <code>Iterator</code> over all of the <code>RequestAttributes</code> whose Category matches the given <code>Identifier</code>
	 */
	public Iterator<RequestAttributes> getRequestAttributes(Identifier category);
	
	/**
	 * Gets a single matching <code>RequestAttributes</code> representing the XACML Attributes element with whose xml:Id matches the given <code>String</code>
	 * 
	 * @param xmlId the <code>String</code> representing the xml:Id of the <code>RequestAttributes</code> to retrieve
	 * @return the single matching <code>RequestAttributes</code> object or null if not found
	 */
	public RequestAttributes getRequestAttributesByXmlId(String xmlId);
	
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.RequestReference}s representing XACML MultiRequest elements in this <code>Request</code>.
	 * 
	 * @return the <code>Collection</code> of <code>RequestAttributes</code> representing XACML MultiRequest elements in this <code>Request</code>.
	 */
	public Collection<RequestReference> getMultiRequests();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.Status} representing the XACML Status element for the Request represented by this <code>Request</code>.
	 * 
	 * @return the <code>Status</code> representing the XACML Status element for the Request represented by this <code>Request</code>.
	 */
	public Status getStatus();
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of the <code>Request</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * 		Two <code>Requests</code> (<code>r1</code> and <code>r2</code>) are equals if:
	 * 			{@code r1.getRequestDefaults() == null && r2.getRequestDefaults() == null} OR {@code r1.getRequestDefaults().equals(r2.getRequestDefaults())} AND
	 * 			{@code r1.getReturnPolicyIdList() == r2.getReturnPolicyIdList()} AND
	 * 			{@code r1.getCombinedDecision() == r2.getCombinedDecision()} AND
	 * 			{@code r1.getRequestAttributes()} is pairwise equal to {@code r2.getRequestAttributes()} AND
	 * 			{@code r1.getMultiRequests()} is pairwise equal to {@code r2.getMultiRequests()}
	 */
	@Override
	public boolean equals(Object obj);
}
