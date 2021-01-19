/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

/**
 * Defines the API for objects that represent XACML Status elements.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface Status {
	/**
	 * Gets the {@link com.att.research.xacml.api.StatusCode} for this <code>Status</code>.
	 * 
	 * @return the <code>StatusCode</code> for this <code>Status</code>.
	 */
	public StatusCode getStatusCode();
	
	/**
	 * Gets the <code>String</code> status message for this <code>Status</code>.
	 * 
	 * @return the <code>String</code> status message for this <code>Status</code>
	 */
	public String getStatusMessage();
	
	/**
	 * Gets the {@link com.att.research.xacml.api.StatusDetail} associated with this <code>Status</code> if there is any.
	 * 
	 * @return the <code>StatusDetail</code> for this <code>Status</code> if there is any.
	 */
	public StatusDetail getStatusDetail();
	
	/**
	 * Returns <code>true</code> if the <code>StatusCode</code> for this <code>Status</code> is the XACML OK value.
	 * 
	 * @return true if the <code>StatusCode</code> for this <code>Status</code> is the XACML OK value, else false
	 */
	public boolean isOk();
	
	/**
	 * Returns a <code>Status</code> with the same <code>StatusCode</code> and status message as this <code>Status</code>
	 * but whose <code>StatusDetail</code> is the merging of the <code>StatusDetail</code> in this <code>Status</code>
	 * and the <code>StatusDetail</code> in the given <code>Status</code>.
	 * 
	 * @param status the <code>Status</code> whose <code>StatusDetail</code> is to be merged in
	 * @return a <code>Status</code> with merged <code>StatusDetail</code>
	 */
	public Status merge(Status status);
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of the <code>Status</code> interface must override the <code>equals</code> method with the following semantics:
	 * 
	 * Two <code>Status</code> objects (<code>s1</code> and <code>s2</code>) are equal if:
	 * 		{@code s1.getStatusCode().equals(s2.getStatusCode())} AND
	 * 		{@code s1.getStatusMessage() == null && s2.getStatusMessage() == null} OR {@code s1.getStatusMessage().equals(s2.getStatusMessage())}
	 * 		{@code s1.getStatusDetail() == null && s2.getStatusDetail() == null} OR {@code s1.getStatusDetail().equals(s2.getStatusDetail())}
	 */
	@Override
	public boolean equals(Object obj);
}
