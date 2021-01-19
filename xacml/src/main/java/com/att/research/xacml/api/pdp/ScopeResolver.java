/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */
package com.att.research.xacml.api.pdp;

import com.att.research.xacml.api.Attribute;

/**
 * ScopeResolver is the interface that objects implement that can be used to resolve attribute scopes to individual
 * identifiers.
 * 
 * @author car
 * @version $Revision$
 */
public interface ScopeResolver {
	/**
	 * Examines the given {@link com.att.research.xacml.api.Attribute} representing a resource identifier for a hierarchical
	 * resource and returns an <code>Iterator</code> over a set of <code>Attribute</code>s representing individual resource identifiers
	 * that are part of the requested hierarchy as specified by the {@link com.att.research.xacml.api.pdp.ScopeQualifier}.
	 * 
	 * @param attributeResourceId the <code>Attribute</code> for the resource identifier
	 * @param scopeQualifier the <code>ScopeQualifier</code> determining which nodes are returned
	 * @return a {@link com.att.research.xacml.api.pdp.ScopeResolverResult} with the results of the request
	 * @throws ScopeResolverException if there is an error resolving the resource identifier to a scope.
	 */
	public ScopeResolverResult resolveScope(Attribute attributeResourceId, ScopeQualifier scopeQualifier) throws ScopeResolverException;
}
