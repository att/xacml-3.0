/*
 *
 *          Copyright (c) 2013,2019  AT&T Knowledge Ventures
 *                     SPDX-License-Identifier: MIT
 */

package com.att.research.xacml.api;

import java.net.URI;

/**
 * Identifier is the interface for objects that represent a XACML 3.0 identifier.  In most cases the {@link com.att.research.xacml.std.IdentifierImpl}
 * in this package will suffice as an implementation of this interface, but all use of identifiers will use the <code>Identifier</code>
 * interface to allow for extensions.
 * 
 * Classes that implement the <code>Identifier</code> interface should override the <code>equals</code> method to meet the following semantics:
 * 		Two <code>Identifier</code>s are <code>equal</code> if the values returned by the <code>getUri</code> method are <code>equal</code>.
 * 
 * @author Christopher A. Rath
 * @version $Revision: 1.2 $
 */
public interface Identifier extends SemanticString {
	/**
	 * Gets this <code>Identifier</code> as a <code>URI</code>.
	 * 
	 * @return the <code>URI</code> representation of this <code>Identifier</code>.
	 */
	public URI getUri();
	
	/**
	 * {@inheritDoc}
	 * 
	 * The implementation of the <code>Identifier</code> interface must override the <code>hashCode</code> method .
	 * 
	 */
	@Override
	public int hashCode();
	
	/**
	 * {@inheritDoc}
	 * 
	 * The implementation of the <code>Identifier</code> interface must override the <code>equals</code> method with the following
	 * semantics:
	 * 		Two <code>Identifier</code> objects (<code>i1</code> and <code>i2</code>) are equal if:
	 * 			{@code a1.getUri().equals(a2.getUri())}
	 */
	@Override
	public boolean equals(Object obj);
}
