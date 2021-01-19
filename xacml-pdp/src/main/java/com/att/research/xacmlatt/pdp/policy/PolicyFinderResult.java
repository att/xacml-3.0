/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacmlatt.pdp.policy;

import com.att.research.xacml.api.Status;

/**
 * PolicyFinderResult is the interface for return values of the methods in the {@link com.att.research.xacmlatt.pdp.policy.PolicyFinderFactory} interface.
 * 
 * @author car
 * @version $Revision: 1.1 $
 * @param <T> the class extending {@link com.att.research.xacmlatt.pdp.policy.PolicyDef} contained as a result in this <code>PolicyFinderResult</code>
 */
public interface PolicyFinderResult<T extends PolicyDef> {
	/**
	 * Gets the {@link com.att.research.xacml.api.Status} of the method call.
	 * 
	 * @return the <code>Status</code> of the method call
	 */
	public Status getStatus();
	
	/**
	 * Gets the {@link com.att.research.xacmlatt.pdp.policy.PolicyDef} returned by the method if the status is OK.
	 * 
	 * @return the <code>T</code> returned by the method if the status is OK.
	 */
	public T getPolicyDef();
}
