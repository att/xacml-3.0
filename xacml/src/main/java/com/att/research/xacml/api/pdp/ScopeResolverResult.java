/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pdp;

import java.util.Iterator;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.Status;

/**
 * ScopeResolverResult is the interface for objects returned by the {@link com.att.research.xacml.api.pdp.ScopeResolver}'s 
 * <code>resolveScope</code> method.
 * 
 * @author car
 * @version $Revision$
 */
public interface ScopeResolverResult {
	/*
	 * Gets the {@link com.att.research.xacml.api.Status} for the scope resolution request.
	 * 
	 * @return the <code>Status</code> of the scope resolution request
	 */
	public Status getStatus();
	
	/*
	 * Gets an <code>Iterator</code> over {@link com.att.research.xacml.api.Attribute}s resolved from a scope resolution request.
	 * 
	 * @return an <code>Iterator</code> over the <code>Attribute</code>s resolved from a scope resolution request.
	 */
	public Iterator<Attribute> getAttributes();
}
