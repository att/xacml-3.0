/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api;

import java.util.Collection;

/**
 * Defines the API for objects that represent XACML StatusDetail elements.
 *  
 * @author Christopher A. Rath
 * @version $Revision: 1.1 $
 */
public interface StatusDetail {
	/**
	 * Gets the <code>Collection</code> of {@link com.att.research.xacml.api.MissingAttributeDetail} objects for this <code>StatusDetail</code>.
	 * If there are no <code>MissingAttributeDetail</code>s an empty <code>Collection</code> must be returned.
	 * 
	 * @return the <code>MissingAttributeDetail</code> objects for this <code>StatusDetail</code> or null if none.
	 */
	public Collection<MissingAttributeDetail>	getMissingAttributeDetails();
	
	/**
	 * Gets a <code>StatusDetail</code> object that is the result of merging this <code>StatusDetail</code> with the
	 * given <code>StatusDetail</code>.
	 * 
	 * @param statusDetail the <code>StatusDetail</code> to merge in
	 * @return a <code>StatusDetail</code> merging this <code>StatusDetail</code> with the given <code>StatusDetail</code>.
	 */
	public StatusDetail merge(StatusDetail statusDetail);
	
	/**
	 * {@inheritDoc}
	 * 
	 * Implementations of the <code>StatusDetail</code> interface must override the <code>equals</code> method as follows:
	 * 
	 * 		Two <code>StatusDetail</code>s (<code>s1</code> and <code>s2</code>) are equal if:
	 * 			{@code s1.getMissingAttributeDetails()} is pair-wise equal to {@code s2.getMissingAttributeDetails()}
	 */
	@Override
	public boolean equals(Object obj);
}
